# -*- coding: UTF-8 -*-
from __future__ import division # Dividing ints will now give floats if need be! Yay!
import cv
from sys import maxint

def within(num,goal,vary):
	"""Returns True if num is between (goal + vary) and (goal - vary)."""
	return (goal+vary > num and num > goal-vary)

class Vision(object):
	def __init__(self,w,h):
		# The following will need to be adjusted based on the image source:
		# Width/height of the input image
		self.width, self.height = (w,h)
		# The minimum/maximum colors to consider a target (in (R,G,B) format)
		#self.rangeMin, self.rangeMax = (cv.Scalar(0, 100, 0), cv.Scalar(100, 255, 100))
		self.rangeMin, self.rangeMax = (cv.Scalar(160, 0, 0), cv.Scalar(255, 170, 220))
		# How to perform the erosion (see OpenCV docs for details)
		self.erodeMethod=self.createStructuringElement(2,cv.CV_SHAPE_RECT)
		self.dilateMethod=self.createStructuringElement(6,cv.CV_SHAPE_RECT)
	
		# Create structures needed for processing
		self.imgSmooth = cv.CreateMat(self.height,self.width,cv.CV_8UC3)
		self.imgRange  = cv.CreateMat(self.height,self.width,cv.CV_8U)
		self.imgDilate = cv.CreateMat(self.height,self.width,cv.CV_8U)
		self.imgErode  = cv.CreateMat(self.height,self.width,cv.CV_8U)
		self.cvMemStorage = cv.CreateMemStorage()
		self.font = cv.InitFont(cv.CV_FONT_HERSHEY_SIMPLEX,1,1)
		self.AXIS_H_DEG_PER_PIX = self.AXIS_H_FOV/self.width
		self.AXIS_V_DEG_PER_PIX = self.AXIS_V_FOV/self.height
#		self.prev_targets = []
#		self.next_target_id=0
	def createStructuringElement(self,size, shape):
		"""Create a symmetrical, non-offset structuring element."""
		return cv.CreateStructuringElementEx(size, size, 0, 0, shape)
		
	# Ratios are width / height (inches); see section 2.2.3 of the game manual
	RATIO_MIDDLE_GOAL = 54 / 21 # 2.57...
	RATIO_HIGH_GOAL   = 54 / 12 # 4.5
	RATIO_LOW_GOAL    = 29 / 24 # 1.21...
	
	TOLERANCE_RATIO   = 0.5
	
	AXIS_H_FOV = 47.0#° (Field Of View of the Axis camera; horizontal)
	AXIS_V_FOV = 48.0#° (Field Of View of the Axis camera; vertical)
		
	def process(self, imgRaw):
		cv.Smooth(imgRaw,self.imgSmooth)
		cv.InRangeS(self.imgSmooth, self.rangeMin, self.rangeMax, self.imgRange)
		cv.Erode(self.imgRange, self.imgErode, self.erodeMethod)
		cv.Dilate(self.imgErode, self.imgDilate, self.dilateMethod)
		contours=cv.FindContours(self.imgDilate,self.cvMemStorage)
		highTarget=False
		#targets=[]
		while (contours != None) and (len(contours) > 0):
			target=Target(contours, self)
			hull = cv.ConvexHull2(contours, self.cvMemStorage, cv.CV_CLOCKWISE, 1) #TODO remove
			if target.ratio == -1: # zero-height rect
				print target.rect,len(contours)
				contours = contours.h_next()
				continue
			if (within(target.angle, 0, 5) and cv.ContourArea(hull) >= 10 and # <-- This is where the decision if it's a target goes
					(highTarget == False or highTarget.top > target.top)):
				highTarget = target
				#target.id=self.next_target_id
				#self.next_target_id+=1
				#targets.append(target)
				#cv.DrawContours(imgRaw,target.contour,(0,0,255),(255,0,0),0,2)
				#cv.PutText(imgRaw,str(len(targets))+target.type,(target.center_x,target.center_y),self.font,cv.Scalar(0,255,0))
				#cv.ShowImage('Image',imgRaw)
				#cv.WaitKey(0)
			contours = contours.h_next()
#		for target in targets:
#			for oldtarget in self.prev_targets:
#				if (target.type==oldtarget.type and within(target.center_x,oldtarget.center_x,10)
#						and within(target.center_y,oldtarget.center_y,10)):
#					target.id=oldtarget.id # It's the same blob!
#					continue
#		self.prev_targets=targets
#		if self.next_target_id > maxint-20:
			# Note: subtract 20 so it won't overflow even if we have 20 targets next time through the loop
			# (Yes, 20 targets is a lot.)
#			self.next_target_id=0
		return highTarget #(imgRaw,targets)
class Target(object):
	def __init__(self, contour, vision):
		self.contour=contour
		self.rect=cv.MinAreaRect2(contour)
		# Aargh...why are these returned as floats?!
		self.center_x=int(self.rect[0][0])
		self.center_y=int(self.rect[0][1])
		self.width_px=int(self.rect[1][0])
		self.height_px=int(self.rect[1][1])
		self.angle=self.rect[2]
		if self.height_px == 0: #TODO why are there zero-height rects?
			self.ratio=-1
		else:
			self.ratio=self.width_px/self.height_px
		self.top=self.center_y - (self.height_px/2)
		self.left=self.center_x - (self.width_px/2)
		self.offset_h=((self.left-(vision.width/2))*vision.AXIS_H_DEG_PER_PIX)-1
		self.offset_v=((self.top-(vision.height/2))*vision.AXIS_V_DEG_PER_PIX)
		#TODO distance
		"""if   within(self.ratio, Vision.RATIO_MIDDLE_GOAL, Vision.TOLERANCE_RATIO):
			self.type="M"
		elif within(self.ratio, Vision.RATIO_HIGH_GOAL,   Vision.TOLERANCE_RATIO):
			self.type="H"
		elif within(self.ratio, Vision.RATIO_LOW_GOAL,    Vision.TOLERANCE_RATIO):
			self.type="L"
		else:
			self.type="?" """

if __name__ == '__main__':
	imgRaw = cv.LoadImageM("../opencv/example_images/target2_high_hit.jpg")
	vision=Vision(imgRaw.width,imgRaw.height)
	target=vision.process(imgRaw)
	#cv.ShowImage("Image",imgProc[0])
	#cv.WaitKey(0)
	print target

