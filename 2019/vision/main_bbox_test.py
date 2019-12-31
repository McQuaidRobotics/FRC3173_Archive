import grip_gen_one
import cv2
import time
import math
import numpy as np
from bbox_tools import *  # import every function and class from bbox_tools

cn = -1
pipeline = grip_gen_one.ImageProcessor()
for x in range(10):  # finds the last camera plugged in
    try:
        pipeline.process(cv2.VideoCapture(x).read()[1])
        cn += 1
    except Exception as e:
        pass
# cam = cv2.VideoCapture(cn)
cam = cv2.VideoCapture(0)
frame_width = cam.get(cv2.CAP_PROP_FRAME_WIDTH)   # float
frame_height = cam.get(cv2.CAP_PROP_FRAME_HEIGHT) # float
frame_center = (frame_height/2, frame_width/2)

#frame_height is synonymous with focal length here
camera_matrix = np.array(
    [[frame_height, 0, frame_center[0]],
    [0, frame_height, frame_center[1]],
    [0, 0, 1]], dtype = "double"
)
print("Camera Matrix:\n {0}".format(camera_matrix))

font = cv2.FONT_HERSHEY_SIMPLEX
fontscale = 0.41
fontcolor = (0, 0, 255)
linetype = 1

diagonalView = math.radians(68.5)
horizontalAspect = 4
verticalAspect = 3
diagonalAspect = math.hypot(horizontalAspect, verticalAspect)
horizontalView = math.atan(math.tan(diagonalView/2) * (horizontalAspect / diagonalAspect)) * 2
verticalView = math.atan(math.tan(diagonalView/2) * (verticalAspect / diagonalAspect)) * 2

#model points are in inches and are displaced from the reference point
model_points = np.array([
    (-5.9319, 0.5092, 0.0), #top left of left rectangle 1
    (-4.00, 0.0, 0.0), #top right of left rectangle 2
    (4.00, 0, 0.0), #top left of right rectangle 3
    (5.9319, 0.5092, 0.0), #top right of right rectangle 4
    (-7.3554, -4.8095, 0.0), #bottom left of left rectangle 5
    (-5.4235, -5.3126, 0.0), #bottom right of left rectangle 6
    (5.4235, -5.3126, 0.0), #bottom left of right rectangle 7
    (7.3554, -4.8095, 0.0) #bottom right of right rectangle 8
])
hFocalLength = frame_width / (2 * math.tan((horizontalView/2)))
vFocalLength = frame_height / (2*math.tan((verticalView/2)))
print(hFocalLength, vFocalLength)

debugprint = ""

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

def set_relative_exposure(exposure):
    """
    Adds 'exposure' to the exposure value for the camera at constant 'cam'.

    Arumgnets:
        exposure: (int) The value to add onto the exposure value of the camera.
    """
    p = cv2.CAP_PROP_EXPOSURE
    cam.set(p, cam.get(p) + exposure)

def get_exposure():
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

def get_target_angle(img, pair):
    '''(pair.left.topleft[0], -pair.left.topleft[1]),
    (pair.left.topright[0], -pair.left.topright[1]),
    (pair.right.topleft[0], -pair.right.topleft[1]),
    (pair.right.topright[0], -pair.right.topright[1]),
    (pair.left.bottomleft[0], -pair.left.bottomleft[1]),
    (pair.left.bottomright[0], -pair.left.bottomright[1]),
    (pair.right.bottomleft[0], -pair.right.bottomleft[1]),
    (pair.right.bottomright[0], -pair.right.bottomright[1])'''
    image_points = np.array([
        pair.left.topleft,
        pair.left.topright,
        pair.right.topleft,
        pair.right.topright,
        pair.left.bottomleft,
        pair.left.bottomright,
        pair.right.bottomleft,
        pair.right.bottomright
    ], dtype="double")

    dist_coeffs = np.zeros((4,1))
    (success, rotation_vector, translation_vector) = cv2.solvePnP(model_points, image_points, camera_matrix, dist_coeffs, flags=cv2.SOLVEPNP_ITERATIVE)
    (target_end_point2D, jacobian) = cv2.projectPoints(np.array([(0.0, 0.0, 1.0)]), rotation_vector, translation_vector, camera_matrix, dist_coeffs)

    p1y = (pair.left.topright[1] + pair.right.topleft[1])//2
    p1 = (get_centeroid_x(pair), p1y)
    p2 = (int(target_end_point2D[0][0][0]), int(target_end_point2D[0][0][1]))

    cv2.line(img, p1, p2, (255, 0, 0), 2)
    print("Rotation Vector:\n {0}".format(rotation_vector))
    print("Translation Vector:\n {0}".format(translation_vector))

    return 0  # debug for now


def get_centeroid_x(pair):
    sum = 0
    sum += pair.left.topleft[0] + pair.left.topright[0] + pair.left.bottomleft[0] + pair.left.bottomright[0]
    sum += pair.right.topleft[0] + pair.right.topright[0] + pair.right.bottomleft[0] + pair.right.bottomright[0]
    avgX = sum // 8
    return avgX

def get_heading_differential(img, pair):
    avgX = get_centeroid_x(pair)
    yaw = math.degrees(math.atan((avgX - frame_width/2) / hFocalLength))
    return round(yaw)

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
            draw_text(img, "(" + str(pairid) + ") " + str(get_heading_differential(img, pair)) + " heading deg", (idpos[0] - 20, idpos[1]))
            draw_text(img, str(get_target_angle(img, pair)) + " target deg", (idpos[0] - 10, idpos[1] + 12))

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

set_exposure(-8.0)

def mouse_callback(event, x, y, flag, param, img):  # callback for mouse events
    # print color of pixel clicked
    if event == cv2.EVENT_LBUTTONDOWN:
        color = img[x,y]
        print(color)

framesrc = None

cv2.namedWindow('camimg')
cv2.setMouseCallback('camimg', lambda a,b,c,d,e: mouse_callback(a,b,c,d,e,framesrc))

while True:
    _, framesrc = cam.read()
    pipeline.process(framesrc)
    frame = pipeline.cv_dilate_output
    camimg = framesrc
    size = framesrc.shape
    contours = make_contours(frame)
    bounding_boxes = autogen_bboxes(frame)
    pairs = make_pairs(bounding_boxes)
    # commented below due to testing drawing pairs
    # draw_contours(contours, camimg)

    for pair in pairs:
        left = pair.left
        right = pair.right
        # print("PAIR FOUND", left, right)
    #draw_pairs(pairs, frame)
    draw_pairs(pairs, camimg)
    
    #cv2.namedWindow("frame")
    # cv2.namedWindow("camimg")
    # cv2.putText(camimg, debugprint, (50, 0), font, fontscale, fontcolor, linetype)
    # cv2.imshow('frame', frame)
    cv2.imshow('camimg', camimg)
    k = cv2.waitKey(5) & 0xFF
    if k == 27:  # escape key
        break
    elif k == 119:
        set_relative_exposure(1)
        print(get_exposure())
    elif k == 115:
        set_relative_exposure(-1)
        print(get_exposure())
    # time.sleep(0.25)
    # breakpoint

cv2.destroyAllWindows()
