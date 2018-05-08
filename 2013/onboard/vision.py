from __future__ import division
from lib_vision import Vision
import cv, socket, struct
from Queue import Queue
from threading import Thread
import SocketServer

print "Beginning stream capture..."
vcap = cv.CaptureFromFile("http://10.31.73.11/mjpg/video.mjpg")
print "Stream capture buffer done"

queues=[]
vision=Vision()
packer=struct.Struct("!bddd")

class QueueTcpHandler(SocketServer.BaseRequestHandler):
	def handle(self):
		global queues
		q = Queue()
		queues.append(q)
		while True:
			try:
				self.request.sendall(q.get())
			except:
				if q in self.queues:
					self.queues.remove(q)
				return

class VisionThread(Thread):
	def run(self):
		while True:
			global queues
			frame = cv.QueryFrame(vcap)
			if frame is None:
				print "E_NOFRAME"
				break
			else:
				target=vision.process(frame)
				if target == False:
					data=packer.pack(0x42,-1,0,0) # Distance of -1 means error
				else:
					data=packer.pack(0x42,target.distance,target.offset_h,target.offset_v)
				for q in queues:
					q.put(data)

VisionThread().start()

server = SocketServer.TCPServer(('', 3173), MyTCPHandler)
server.serve_forever()

