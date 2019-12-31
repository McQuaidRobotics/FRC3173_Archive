import grip_gen_one
import cv2
import time
import numpy as np

cam = cv2.VideoCapture(1)
pipeline = grip_gen_one.ImageProcessor()
frame_width = cam.get(cv2.CAP_PROP_FRAME_WIDTH)   # float
frame_height = cam.get(cv2.CAP_PROP_FRAME_HEIGHT) # float

font = cv2.FONT_HERSHEY_SIMPLEX
fontscale = 0.41
fontcolor = (0, 0, 255)
linetype = 1

debugprint = ""

'''
model_points = np.array([
    (0.0, 0.0, 0.0), #left box top left
    (21.0, -6.0, 0.0), #left box top right
    (-18.0, -57.0, 0.0), #left box bot left
    (3.0, -63.0, 0.0), #left box bot right
    (106.0, -9.0, 0.0), #right box top left
    (128.0, -4.0, 0.0), #right box top right
    (121.0, -68.0, 0.0), #right box bot left
    (143.0, -63.0, 0.0) #right box top right
])'''

'''def compositeArea(width, height):
    return (width * height)

def isPerpendicular(area1, area2, errorBound):
    return (abs(area1 - area2) <= errorBound)


def ip2(x1, x2, deadzone=10):
    return x1 - deadzone >= x2 >= x1 + deadzone

def att2(x1, x2):
    global debugprint
    if ip2(x1, x2):
       return 90.0
    else:
        pass'''


# bounding_box.topleft (tuple)
# bounding_box.topright (tuple)
# bounding_box.bottomleft (tuple)
# bounding_box.bottomright (tuple)
# bounding_box.tiltdirection (string, "left" or "right")
# bounding_box.pair_with(bbox: BoundingBox) (Pair)

class BoundingBox:
    def __init__(self, coords):  # coords = [topleft, topright, bottomleft, bottomright]
        if len(coords) < 4:
            raise ValueError("too few coordinates in 'coords' (must be 4)")
        elif len(coords) > 4:
            raise ValueError("too many coordinates in 'coords' (must be 4)")

        self.numpy_points = coords
        self.topleft, self.topright, self.bottomleft, self.bottomright = coords.tolist()  # unpack coords into vars
        self.tiltdirection = self.__get_tilt()
    
    def __get_tilt(self):
        if self.bottomleft[0] < self.topleft[0]:  # it's tilting right
            return "right"
        if self.bottomleft[0] > self.topleft[0]:  # it's tilting left
            return "left"
        else:  # uh oh
            return "unknown"

    def pair_with(self, bbox):
        return Pair(self, bbox)

# bbox_pair.left (BoundingBox)
# bbox_pair.right (BoundingBox)

class Pair:
    def __init__(self, bbox1, bbox2):
        self.left, self.right = self.__get_pair(bbox1, bbox2)
    
    def __get_pair(self, bbox1, bbox2):
        # find which one is tilting left
        if bbox1.tiltdirection == "left":
            return bbox1, bbox2
        elif bbox1.tiltdirection == "right":
            return bbox2, bbox1
        else:  # tiltdirection == "unknown"
            print("unknown tiltdirection for bbox1! defaulting to left")
            return bbox1, bbox2


while True:
    _, framesrc = cam.read()
    pipeline.process(framesrc)
    frame = pipeline.cv_dilate_output
    camimg = framesrc
    contours, hierarchy = cv2.findContours(frame, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
    # cv2.drawContours(frame, contours, -1, (0,255,0), 3)
    # breakpoint()
    #bounding_boxes = []
    bounding_rects = []

    # make list of BoundingBoxes in a list comprehension
    bounding_boxes = [BoundingBox(np.int0(cv2.boxPoints(cv2.minAreaRect(contour)))) for contour in contours]
    # sort bounding boxes left to right
    sorted_bounding_boxes = list(sorted(bounding_boxes, key=lambda x: x.topleft[0]))

    for contour in contours:  # print out the rotated bounding boxes using contours
        #rotated
        rect = cv2.minAreaRect(contour)
        box = cv2.boxPoints(rect)
        box = np.int0(box)
        cv2.drawContours(camimg, [box], 0, (0, 255, 255), 1)
        bounding_rects.append(box)

        for x, y in box:
            cv2.putText(camimg, f"({x}, {y})", (x, y), font, fontscale, fontcolor, linetype)

        """
        lefttopleft 276 191
        leftopright 297 197
        leftbotleft 258 248
        leftbotright 279 254

        righttopleft 382 200
        righttopright 404 195
        rightbotleft 397 259
        rightbotright 419 254
        """

        """
        cv2.putText(camimg, f"({x}, {y})", (x - 50, y - 5), font, fontscale, fontcolor, linetype)
        cv2.putText(camimg, f"({x + w}, {y + h})", ((x + w), (y + h)), font, fontscale, fontcolor, linetype)
        cv2.putText(camimg, f"({w}, {h})", ((x + w // 2) - 28,  (y + h // 2)), font, fontscale, fontcolor, linetype)
        cv2.putText(camimg, f"area: {w * h}", (x - 65, (y + h + 15)), font, fontscale, fontcolor, linetype)
        """

    #if (bounding_rects[0][0][0] > bounding_rects[1][0][0]):
    #    bounding_rects = [bounding_rects[1], bounding_rects[0]]
    
    '''image_points = np.array([
        (bounding_rects[0][2]),
        (bounding_rects[0][3]),
        (bounding_rects[0][1]),
        (bounding_rects[0][0]),
        (bounding_rects[1][1]),
        (bounding_rects[1][2]),
        (bounding_rects[1][0]),
        (bounding_rects[1][3])], dtype="double")
    '''

    '''try:
        res = isPerpendicular(compositeArea(bounding_boxes[0][2], bounding_boxes[0][3]), compositeArea(bounding_boxes[1][2], bounding_boxes[1][3]), 200)
        #res = isPerpendicular(bounding_boxes[0][0], bounding_boxes[1][0], 10)
        #print("ret:", res)
    except IndexError:
        pass'''
    
    #cv2.namedWindow("frame")
    cv2.namedWindow("camimg")
    # cv2.putText(camimg, debugprint, (50, 0), font, fontscale, fontcolor, linetype)
    #cv2.imshow('frame', frame)
    cv2.imshow('camimg', camimg)
    k = cv2.waitKey(5) & 0xFF
    if k == 27:  # escape key
        break
    # time.sleep(0.25)
    # breakpoint




cv2.destroyAllWindows()
