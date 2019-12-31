import cv2
import math
import grip_gen_one
import numpy as np
from bbox_tools import *
def sbb_key(x):
    '''
    The key to sort BoundingBoxes by leftmosts.
    '''
    if x.topleft[0] <= x.bottomleft[0]:
        return x.topleft[0]
    else:
        return x.bottomleft[0]
    
def draw_text(image, text, pos):
    """
    Draws text 'text' to display 'image' at position 'pos'.
    Text is drawn from bottom left at provided coordinates.

    Arguments:
        image: The cv2 image to draw to.
        text: (str) The string to write to screen
        pos: (tuple(int, int)) The position to write text to.
    """
    font = cv2.FONT_HERSHEY_SIMPLEX
    fontscale = 0.41
    fontcolor = (0, 0, 255)
    linetype = 1
    cv2.putText(image, text, pos, font, fontscale, fontcolor, linetype)

def make_pairs(bboxes, y_threshold=20):
    '''
    Makes pairs out of boxes using tilt directions.
    TODO: implement max Y range so that we don't make pairs out of things that aren't even close on the Y axis.
    
    Arguments:
        bboxes: (list(BoundingBox)) A *sorted* list of bounding boxes (BoundingBox) to be paired.
    
    Kwargs:
        y_threshold: (int) Max amount of y pixels in either direction that are allowed for bounding boxes to become a pair.

    Returns:
        ret: (list(Pair)) A list of pairs (Pair).
    '''
    # TODO: if we need to do multiple passes to get everything (because we might skip over some if we're reading left to right)
    #       then take out the ones that are paired with another (bboxes.remove(box)) and keep passing until no bboxes remain.
    lastbox = None
    searching = False
    pairs = []

    def strip_bboxes(box):
        for i, b in enumerate(bboxes):
            h = hash(b)
            if h == hash(box):
                bboxes.pop(i)
                return

    lastlen = -1
    while lastlen != len(bboxes):  # while there are still bounding boxes left to possibly pair
        lastlen = len(bboxes)
        for box in bboxes:
            if searching and box.tiltdirection == 'right':  # if we just got two rights in a row
                # replace the old box with the new one to initiate a new search
                lastbox = box
            elif searching and box.tiltdirection == 'left':  # if we're searching and we just got a left
                # it's a match, pair it up
                # but first, let's talk about parallel universes
                # if lastbox.bottomright[1] + y_threshold < box.topright[1]:  # if the bottommost part of the right isn't close enough to the topmost part of the left
                if lastbox.farthest_y + y_threshold < box.topright[1]:  # if the bottommost part of the right isn't close enough to the topmost part of the left
                    continue  # TODO: *don't* do the things with the taking out of the list thing here
                if lastbox.topleft[1] - y_threshold > box.farthest_y:  # if the topmost part of the right isn't close enough to the bottommost part of the left
                    continue  # TODO: *don't* do the things with the taking out of the list thing here
                pairs.append(lastbox.pair_with(box))
                # reset the search
                searching = False
                # remove both paired boxes from the bboxes list
                strip_bboxes(box)
                strip_bboxes(lastbox)
                lastbox = None
            elif box.tiltdirection == 'right':  # if this is our first right when we're not yet searching
                # start searching for a left
                searching = True
                lastbox = box
    
    return pairs

def create_boundingboxes(contours):
    '''
    Creates BoundingBoxes from the given contours.

    Arguments:
        contours: Contours to form BoundingBoxes from.
    
    Returns:
        ret: (list(BoundingBox)) An x-sorted list of bounding boxes (BoundingBox).
    '''
    ret = [BoundingBox(np.int0(cv2.boxPoints(cv2.minAreaRect(contour)))) for contour in contours]
    ret = list(sorted(ret, key=sbb_key))
    return ret

def make_contours(img):
    '''
    Uses a cv2 image 'img' to create contours and form bounding boxes using them.

    Arguments:
        img: A cv2 image to create contours out of.
    
    Returns:
        contours: Contours for the given image.
    '''
    contours, hierarchy = cv2.findContours(img, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
    return contours

def autogen_bboxes(img):
    '''
    Automatically creates bounding boxes (BoundingBox) using the given image.

    Arguments:
        img: A cv2 image to create BoundingBoxes out of.

    Returns:
        bboxes: (list(BoundingBox)) A list of bounding boxes (BoundingBox).
    '''
    bboxes = create_boundingboxes(make_contours(img))
    return bboxes

def draw_contours(contours, img, color=(0, 255, 255)):
    '''
    Draws the given contours 'contours' onto the image 'img'.

    Arguments:
        contours: Contours to draw to the image.
        img: A cv2 image to draw contours to.
    
    Kwargs:
        color: (tuple(int, int, int)) The color to draw the line as.
    '''

    for contour in contours:
        rect = cv2.minAreaRect(contour)
        box = cv2.boxPoints(rect)
        box = np.int0(box)
        cv2.drawContours(img, [box], 0, color, 1)

def set_exposure(exposure):
    """
    Sets the exposure value 'exposure' for the camera at constant 'cam'.

    Arguments:
        exposure: (int) The value to set the exposure of the camera to.
    """
    cam.set(cv2.CAP_PROP_EXPOSURE, exposure)

def set_relative_exposure(cam, exposure):
    """
    Adds 'exposure' to the exposure value for the camera at 'cam'.

    Arumgnets:
        exposure: (int) The value to add onto the exposure value of the camera.
    """
    p = cv2.CAP_PROP_EXPOSURE
    cam.set(p, cam.get(p) + exposure)

def get_exposure(cam):
    """
    Gets the exposure value from the camera constant 'cam'.

    Returns:
        exposure: (int) Current exposure value from the camera.
    """
    return cam.get(cv2.CAP_PROP_EXPOSURE)

def draw_lines(points, img, color=(0, 255, 0), terminate=False, thickness=3, line_type=8):
    """
    Draws lines through points 'points' onto the image 'img'.

    Arguments:
        points: (list(int)) The points for the lines to meet.
        img: The cv2 image to draw lines onto.

    Kwargs:
        color: (tuple(int, int, int)) RGB color value to color the lines.
        terminate: (bool) Whether or not to enclose the points in a complete shape.
        thickness: (int) Thickness of the lines drawn.
        line_type: (int) Line type to draw.
    """
    finlist = points
    if not terminate:
        finlist = [points[-1]] + points + [points[0]]  # include the first point at the end and last point at the beginning to complete a square


    for i in range(len(finlist) - 2):
        point = tuple(finlist[i])
        point2 = tuple(finlist[i + 1])
        try:
            cv2.line(img, point, point2, color, thickness=thickness, lineType=line_type)  # draw line
        except:  # VERY dirty error handling
            print("error: ", point, point2)
            print("error2: ", finlist)

def get_angle_from_camera(img, bounds):
    return 0  # debug for now

def draw_pairs(pairs, img, color=(0, 255, 0), thickness=2, line_type=8, draw_with_ids=True, debug=True):
    """
    Draws Pair objects onto the image 'img'.

    Arguments:
        pairs: (list(Pair)) List of pairs to draw onto the image.
        img: cv2 image to draw onto.

    Kwargs:
        color: (list(int, int, int)) RGB color value to color the lines.
        thickness: (int) Thickness of the lines drawn.
        line_type: (int) Line type to draw.
        draw_with_ids: (bool) Whether or not to ID each pair for easy identification.
        debug: (bool) Various debug utilities.
    """

    for pairid, pair in enumerate(pairs):
        boxes = [pair.left, pair.right]
        topleft = pair.left.topleft
        topright = pair.right.topright
        bottomleft = pair.left.bottomleft
        bottomright = pair.right.bottomright

        """
        bounding_topleft = (pair.left.topleft[0] - (pair.left.bottomleft[0] - pair.left.topleft[0]), pair.left.topleft[1])
        bounding_topright = (pair.right.topright[0] + (pair.right.bottomright[0] - pair.right.topright[0]), pair.right.topright[1])
        bounding_bottomleft = pair.left.bottomleft
        bounding_bottomright = pair.right.bottomright
        bounds = [bounding_topleft, bounding_topright, bounding_bottomleft, bounding_bottomright]
        draw_bounds = [bounding_topleft, bounding_topright, bounding_bottomright, bounding_bottomleft]
        """
        draw_bounds = [pair.left.topright, pair.right.topleft, (pair.right.topleft[0], pair.right.bottomleft[1]), (pair.left.topright[0], pair.left.bottomright[1])]
        bounds = draw_bounds
        if debug:
            draw_lines(draw_bounds, img, (255, 0, 0), False, thickness, line_type)
        for box in boxes:
            positions = [box.topleft, box.topright, box.bottomright, box.bottomleft]
            draw_lines(positions, img, color, False, thickness, line_type)
        if draw_with_ids:
            idpos = (topleft[0] + ((topright[0] - topleft[0]) // 2), topleft[1] + ((bottomleft[1] - topleft[1]) // 2))
            draw_text(img, "(" + str(pairid) + ") " + str(get_angle_from_camera(img, bounds)) + "deg", (idpos[0] - 20, idpos[1]))

def angle_between_points(pos1, pos2):
    """
    Returns the angle from pos1 to pos2.

    Arguments:
        pos1: (tuple(int, int)) Initial position.
        pos2: (tuple(int, int)) "Goto" position.

    Returns:
        angle: (float) Angle between pos1 and pos2.
    """
    return (90 - (math.atan2(pos1[0] - pos2[0], pos1[1], pos2[1]) / math.pi * 180 + 180)) % 360

def get_last_plugged_in():
    pipeline = grip_gen_one.ImageProcessor()
    cn = -1
    if pipeline:
        for x in range(10):  # finds the last camera plugged in
            try:
                pipeline.process(cv2.VideoCapture(x).read()[1])
                cn += 1
            except Exception as e:
                pass
    return cv2.VideoCapture(cn)

def get_cam_img(cam, inc_original=True):
    _, framesrc = cam.read()
    pipeline = grip_gen_one.ImageProcessor()
    pipeline.process(framesrc)
    frame = pipeline.cv_dilate_output
    camimg = framesrc
    return frame, camimg if inc_original else frame

def draw_point(point, img):
    draw_text(img, str(point), tuple(point))

def get_image_points_from_pair(pair, imgDEBUG=None):
    # l.topleft, l.topright, l.bottomright, l.bottomleft, r.topleft, r.topright, r.bottomright, r.bottomleft
    # get the first point to be the anchor
    l = pair.left
    r = pair.right
    points = [l.bottomleft, l.bottomright, l.topright, l.topleft, r.bottomleft, r.bottomright, r.topright, r.topleft]
    # points = []
    if imgDEBUG is not None:
        draw_point(l.bottomleft, imgDEBUG)  # debug
        draw_point(l.bottomright, imgDEBUG) 
        draw_point(l.topright, imgDEBUG)
        draw_point(l.topleft, imgDEBUG)
    #print(points)
    finpoints = []
    #anchor = points[0]
    for p in points:
        x, y = map(float, p)
    #    ax, ay = anchor
    #    diffx = x - ax
    #    diffy = y - ay
        finpoints.append((x, y)) #(diffx, -diffy, 0))#changed from 3 dim to 2 dim
    return np.array(finpoints)

def get_cam_matrix(img):
    size = img.shape
    focal_length = size[1]
    center = (size[1]/2, size[0]/2)
    camera_matrix = np.array(
                             [[focal_length, 0, center[0]],
                             [0, focal_length, center[1]],
                             [0, 0, 1]], dtype = "double"
                             )
    return camera_matrix

def solvepnp_pair(pair, model_points, img, custom_algo=cv2.solvePnP):
    image_points = get_image_points_from_pair(pair)
    size = img.shape
    #print(three_dpoints)
    #print(image_points)
    camera_matrix = get_cam_matrix(img)
    dist_coeffs = np.zeros((4,1)) # Assuming no lens distortion
    # success, rotation_vector, translation_vector = cv2.solvePnP(3dpoints, image_points, camera_matrix, dist_coeffs, flags=cv2.SOLVEPNP_ITERATIVE)
    # return cv2.solvePnPRansac(model_points, image_points, camera_matrix, dist_coeffs, flags=cv2.SOLVEPNP_ITERATIVE)  # return only rot vec and trans vec
    return custom_algo(model_points, image_points, camera_matrix, dist_coeffs, flags=cv2.SOLVEPNP_ITERATIVE)  # return only rot vec and trans vec

def get_avg_distance(rvec, model_points, transmax):
    Z = np.zeros((8))
    for i in range(0, 8):
        point = np.dot(model_points[i], rvec) + np.matrix.transpose(transmax)
        Z[i] = point[0,2]

    avg = lambda x: sum(x) / len(x)
    depthdif = (Z.max() - Z.min()) / 2
    # return Z.min() + depthdif
    return avg(Z)
    #print(Z.max() - Z.min())
    #print(Z)
