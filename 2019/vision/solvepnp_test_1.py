from vision_tools_1 import *
import numpy as np
import math

# cam = get_last_plugged_in()  # get last plugged in camera
cam = cv2.VideoCapture(0)
def update_keys():
    k = cv2.waitKey(5) & 0xFF
    if k == 27:  # escape key
        return False
    elif k == 119:
        set_relative_exposure(cam, 1)
        print(get_exposure(cam))
    elif k == 115:
        set_relative_exposure(cam, -1)
        print(get_exposure(cam))
    return True

def draw_img(img):
    cv2.imshow('window', img)


frame_width = cam.get(cv2.CAP_PROP_FRAME_WIDTH)   # float
frame_height = cam.get(cv2.CAP_PROP_FRAME_HEIGHT) # float
frame_center = (frame_height/2, frame_width/2)
diagonalView = math.radians(68.5)
horizontalAspect = 4
verticalAspect = 3
diagonalAspect = math.hypot(horizontalAspect, verticalAspect)
horizontalView = math.atan(math.tan(diagonalView/2) * (horizontalAspect / diagonalAspect)) * 2
verticalView = math.atan(math.tan(diagonalView/2) * (verticalAspect / diagonalAspect)) * 2
hFocalLength = frame_width / (2 * math.tan((horizontalView/2)))
vFocalLength = frame_height / (2*math.tan((verticalView/2)))
print(hFocalLength, vFocalLength)


#left rect clockwise starting at bottom left
#right rect clockwise starting at bot tom left
#anchor point is midpoint between the two tips of the tilted rectangles
#model points are in inches and are displaced from the reference point
model_points = np.array([
    (-7.3554, -4.8095, 0.0), #bottom left of left rectangle 5
    (-5.4235, -5.3126, 0.0), #bottom right of left rectangle 6
    (-4.00, 0.0, 0.0), #top right of left rectangle 2
    (-5.9319, 0.5092, 0.0), #top left of left rectangle 1
    (5.4235, -5.3126, 0.0), #bottom left of right rectangle 7
    (7.3554, -4.8095, 0.0), #bottom right of right rectangle 8
    (5.9319, 0.5092, 0.0), #top right of right rectangle 4
    (4.00, 0, 0.0), #top left of right rectangle 3
])
'''
model_points = np.array([(0, 0, 0),
    (23, 7, 0),
    (42, -58, 0),
    (19, -64, 0),
    (




        )
'''
#(371, 201, 0), 11
##                    (394, 194, 0), 2
#                    (413, 259, 0), 3
#                    (390, 265, 0),
#                    (249, 196, 0),5
#                    (272, 202, 0),6
#                    (254, 267, 0),7
#                    (231, 260, 0)], dtype="double")'''

is_zeroed = False
zeroes = [0, 0, 0]
debug_red = False
is_flashing = False
flash_tick = False

dist_avg = [0.0]
max_avg = 45
def avg_dist(distance):
    global dist_avg
    dist_avg.append(distance)
    dist_avg = dist_avg[-max_avg:]
    return sum(dist_avg) / len(dist_avg)

while update_keys():
    img, orig_img = get_cam_img(cam)  # get the processed and unprocessed images from the camera

    contours = make_contours(img)  # make contours with the processed image
    bounding_boxes = autogen_bboxes(img)  # make bounding boxes using the processed image
    pairs = make_pairs(bounding_boxes)  # make pairs using the bounding boxes

    for pair in pairs:
        l = pair.left
        r = pair.right
        # print(", ".join(map(str, list(map(tuple, [l.topleft, l.topright, l.bottomright, l.bottomleft, r.topleft, r.topright, r.bottomright, r.bottomleft])))))
        # ret, rotmax, transmax, inliers = solvepnp_pair(pair, model_points, img, custom_algo=cv2.solvePnPRansac)
        ret, rotmax, transmax = solvepnp_pair(pair, model_points, img)
        rvec, jac = cv2.Rodrigues(rotmax)
        projmax = np.hstack((rvec, transmax))
        #print(projvec)
        eulerAngles = -cv2.decomposeProjectionMatrix(projmax)[6]
        #print(eulerAngles)
        #totalrotmax = np.array([[rvec[0,0], rvec[0, 1], rvec[0, 2], transvec[0]],
        #    [rvec[1,0], rvec[1,1], rvec[1,2], transvec[1]],
        #    [rvec[2,0], rvec[2,1], rvec[2,2], transvec[2]],
        #    [0,0,0,1]])
        #worldToCamera = np.mat(totalrotmax)
        #inverserotmax = np.linalg.inv(totalrotmax)
        #print(inverserotmax)
        #print(list(map(int, [yaw, pitch])))
        # print(rvec)
        # print(rotvec[0], rotvec[1], rotvec[2])
        # (end_point, jacobian) = cv2.projectPoints(np.array([(0, 0, 1000.0)]), rotmax, transmax, get_cam_matrix(img), np.zeros((4,1)))
        yaw = round(float(eulerAngles[1]))
        pitch = round(float(eulerAngles[0]))
        roll = round(float(eulerAngles[2]))
        #p1 = (int((l.topright[0] + r.topleft[0])/2), int((l.topright[1] + r.topleft[1])/2))
        #p2 = (int(end_point[0][0][0]), int(end_point[0][0][1]))
        #cv2.line(orig_img, p1, p2, (255, 0, 255), 2)

        # print("roll: " + str(round(float(eulerAngles[2]))) + " - pitch: " + str(round(float(eulerAngles[0]))) + " - yaw: " + str(yaw))
        if not is_zeroed:
            zeroes = [roll, pitch, yaw]
            is_zeroed = True
        roll -= zeroes[0]
        pitch -= zeroes[1]
        #yaw -= zeroes[2]
        # when yaw goes negative, why does pitch shoot up ~+300?
        distance = get_avg_distance(rvec, model_points, transmax)
        distance = avg_dist(distance)
        # distance = avg_dist(adist)
        print(f"roll: {roll} - pitch: {pitch} - yaw: {yaw} - distance: {distance}")
        # print(distance)
        # draw_point(r.bottomright, orig_img)
        dt = lambda x, y: draw_text(orig_img, x, tuple(y))
        dt("rbr", r.bottomright)
        dt("rbl", r.bottomleft)
        dt("ltl", l.topleft)
        dt("ltr", l.topright)
        dt("lbl", l.bottomleft)
        dt("lbr", l.bottomright)
        dt("rtr", r.topright)
        dt("rtl", r.topleft)
        if debug_red:
            if yaw < 0:  # if neg (debug: error)
                if not is_flashing:
                    is_flashing = True
                else:
                    flash_tick = not flash_tick
                    if flash_tick:
                        orig_img[::4] = (0, 0, 255)
    draw_pairs(pairs, orig_img)  # draw the pairs onto the original image for display
    draw_img(orig_img)  # draw the original image
