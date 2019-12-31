import grip_gen_one
import cv2

cam = cv2.VideoCapture(1)
pipeline = grip_gen_one.ImageProcessor()

while True:
    _, framesrc = cam.read()
    pipeline.process(framesrc)
    frame = pipeline.cv_dilate_output
    contours, hierarchy = cv2.findContours(frame, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)
    # cv2.drawContours(frame, contours, -1, (0,255,0), 3)
    for contour in contours:
        x, y, w, h = cv2.boundingRect(contour)
        cv2.rectangle(frame, (x, y), (x + w, y + h), (255, 0, 0), 2)
    cv2.imshow('frame', frame)
    k = cv2.waitKey(5) & 0xFF
    if k == 27:  # escape key
        break
    # breakpoint

cv2.destroyAllWindows()