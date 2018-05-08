from lib_vision import Vision
import cv

vcap = cv.CaptureFromFile("http://10.31.73.11/mjpg/video.mjpg")#"http://10.31.73.11/mjpg/video.mjpg")
frame = cv.QueryFrame(vcap)
print frame
vision=Vision(frame.width,frame.height)
data=open("calibrate.txt","w")

cv.NamedWindow('Video')

for i in range (1, 55): # field is 54 ft long; put an extra data point on the end
	print "Distance:",str(i)+"ft"
	frameOK=False
	while not frameOK:
		k=0
		while k != 32: # Spacebar
			frame = cv.QueryFrame(vcap)
			if frame is None:
				print "E_NOFRAME"
			else:
				target=vision.process(frame)
				cv.ShowImage('Video',frame)
				k=cv.WaitKey(1)
		if target.distance==-1:
			print "Wrong number of targets!"
		else:
			cv.ShowImage('Video',processed)
			k=cv.WaitKey(0)
			if k == 121: # Lowercase 'y'
				data.write(i+"\t"+target.offset_h+"\t"+target.offset_v)#TODO write shooter speed
				frameOK=true
