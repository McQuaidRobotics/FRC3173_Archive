import cv, socket

print "Beginning stream capture..."
vcap = cv.CaptureFromFile("http://10.31.73.11/mjpg/video.mjpg")
print "Stream capture buffer done"

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect(("127.0.0.1", 9999))

while True:
	frame = cv.QueryFrame(vcap)
	if frame is None:
		print "E_NOFRAME"
		break
	else:
		# Ugh, terrible hack to convert to JPEG.
		# If there isn't a better way to do this then the file should
		# probably be on a RAM disk. (eg http://www.vanemery.com/Linux/Ramdisk/ramdisk.html)
		cv.SaveImage('/tmp/frame.jpg',frame)
		imageData=open('/tmp/frame.jpg').read()
		s.sendall("Content-type: image/jpeg\n"+"Content-length: "+str(len(imageData))+"\n\n"+imageData+"\n\n--spionisto\n")
