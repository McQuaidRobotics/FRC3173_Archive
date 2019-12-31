import numpy as np

def _bbox_gettilt(points: list, farthests=False, raise_error=True):  # unsorted list of points, try to determine tilt
    if len(points) != 4:
        if raise_error:
            raise ValueError(f"'points' does not have 4 elements (has {len(points)} elements)")
    # determine tilt
    tilt = ""  # "left", "right", "unknown", where tilt is determined based on the x direction from a bottom point to its equivalent-side point on the top
    # get top points and bottom points (this is probably where we should implement a minimum size check)
    y_sort = list(sorted(points, key=lambda x: x[1]))  # sort by each value's y
    toppoints = y_sort[:2]  # get first two y-sorted points (lowest ys)
    bottompoints = y_sort[2:]  # get last two y-sorted points (highest ys)

    toppoints_sorted = list(sorted(toppoints, key=lambda x: x[0]))  # sort by each x value
    bottompoints_sorted = list(sorted(bottompoints, key=lambda x: x[0]))  # sort by each x value
    toppoints_sorted_y = list(sorted(toppoints, key=lambda x: x[1]))  # sort by each y value
    bottompoints_sorted_y = list(sorted(bottompoints, key=lambda x: x[1]))  # sort by each y value
    farthest_top_x = toppoints_sorted[1][0]  # get last in sorted list
    farthest_bottom_x = bottompoints_sorted[1][0]  # get last in sorted list
    farthest_top_y = toppoints_sorted_y[1][1]  # get last in y-sorted list
    farthest_bottom_y = bottompoints_sorted_y[1][1]  # get last in y-sorted list
    if farthest_top_x > farthest_bottom_x:  # if the farthest top x is farther to the right than the farthest bottom x
        tilt = "right"  # it's tilting right
    elif farthest_top_x == farthest_bottom_x:  # if the right side is a straight vertical line
        tilt = "unknown"  # figure out what to do with this case
    else:  # if the farthest top x is farther to the *left* than the farthest bottom x
        tilt = "left"  # it's tilting left
    
    farthest_x = farthest_top_x if farthest_top_x >= farthest_bottom_x else farthest_bottom_x  # used for getting the farthest x value
    farthest_y = farthest_top_y if farthest_top_y >= farthest_bottom_y else farthest_bottom_y  # used for getting the farthest y value

    if not farthests:
        return [*toppoints_sorted, *bottompoints_sorted], tilt  # return the points from top left, top right, bottom left, and bottom right, and tilt
    else:
        return [*toppoints_sorted, *bottompoints_sorted], tilt, farthest_x, farthest_y  # returns same as above but also the farthest x and y


# bounding_box.topleft (tuple)
# bounding_box.topright (tuple)
# bounding_box.bottomleft (tuple)
# bounding_box.bottomright (tuple)
# bounding_box.tiltdirection (string, "left" or "right")
# bounding_box.pair_with(bbox: BoundingBox) (Pair)

class BoundingBox:
    """
    A class for creating bounding boxes out of given points, unsorted or sorted.
    Takes a list 'coords', which can be any given list of four points (tuples).
    """
    def __init__(self, coords):  # coords = [topleft, topright, bottomleft, bottomright]
        if len(coords) < 4:
            raise ValueError("too few coordinates in 'coords' (must be 4)")
        elif len(coords) > 4:
            raise ValueError("too many coordinates in 'coords' (must be 4)")

        self.np_points = coords
        # self.topleft, self.topright, self.bottomleft, self.bottomright = coords.tolist()  # unpack coords into vars  # antiquated with the introduction of __box_gettilt
        # self.tiltdirection = self.__get_tilt()  # antiquated with the introduction of __bbox_gettilt
        (self.topleft, self.topright, self.bottomleft, self.bottomright), self.tiltdirection, self.farthest_x, self.farthest_y = _bbox_gettilt(coords.tolist(), farthests=True)
    
    def __get_tilt(self):  # legacy, surpassed by __bbox_gettilt
        if self.bottomleft[0] < self.topleft[0]:  # it's tilting right
            return "right"
        if self.bottomleft[0] > self.topleft[0]:  # it's tilting left
            return "left"
        else:  # uh oh
            return "unknown"
    
    def __str__(self):
        return self.__repl__()
    
    def __repl__(self):
        return f"({self.topleft}, {self.topright}, {self.bottomleft}, {self.bottomright})"

    def pair_with(self, bbox):
        return Pair(self, bbox)

# bbox_pair.left (BoundingBox)
# bbox_pair.right (BoundingBox)

class Pair:
    def __init__(self, bbox1, bbox2):
        # self.left, self.right = self.__get_pair(bbox1, bbox2)
        self.right, self.left = self.__get_pair(bbox1, bbox2)
    
    def __get_pair(self, bbox1, bbox2):
        # find which one is tilting left
        if bbox1.tiltdirection == "left":
            return bbox1, bbox2
        elif bbox1.tiltdirection == "right":
            return bbox2, bbox1
        else:  # tiltdirection == "unknown"
            print("unknown tiltdirection for bbox1! defaulting to left")
            return bbox1, bbox2
