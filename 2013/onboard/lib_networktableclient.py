# A rather shoddy native Python NetworkTables implementation.
# Currently, it is read-only, since I have no need for writing to it.

import socket, struct
from threading import Thread, Event

class NetworkTableClient(Thread):
	def __init__(self,host,port=1735):
		Thread.__init__(self)
		self.sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
		self.sock.connect((host, port))
		self.recvbuf = ""
		self.onchange=False
		self.names={}
		# Handshake
		self.sock.sendall(chr(0x01)+chr(0x02)+chr(0x00)) # Client Hello
		# Start the thread
	def run(self):
		while True:
			msg_type=self.get(1)
			if   msg_type == 0x02: # Protocol Unsupported
				print "Err: Server does not support version 0x02+0x00!"
			elif msg_type == 0x10: # Entry Assignment
				entry_name=self.getString()
				(entry_type,entry_id,entry_seq)=(self.get(1),self.get(2),self.get(2))
				entry_data=self.getData(entry_type)
				print "Entry: "+entry_name+"="+str(entry_data)
				self.names[entry_id] = entry_name
				if self.onchange != False: self.onchange(entry_name, entry_data)
				#self.db.table[entry_id] = {'name': entry_name, 'type': entry_type, 'seq': entry_seq}
			elif msg_type == 0x11: # Entry Update
				entry_id = self.get(2)
				entry_seq = self.get(2)
				entry_data=getData(self.db.table[entry_id]['type'])
				if self.onchange != False: self.onchange(self.names[entry_id], entry_data)
				#self.db.table[entry_id]['seq']  = entry_seq
				#self.db.table[entry_id]['data'] = entry_data
			elif msg_type == 0x20: # Begin Transaction
				print "Begin transaction"
				#self.db.transactionFlag.clear()
			elif msg_type == 0x21: # End   Transaction
				print "End transaction"
				#self.db.transactionFlag.set()
			else:
				# NB: 0x03 appears to be some sort of `end Entry Assignment list' message
				print "Unknown message type: "+hex(msg_type)+"!"
	def getData(self,entry_type):
		if   entry_type == 0x00: # Boolean
			return self.get(1)==0x01
		elif entry_type == 0x01: # Double
			return self.getDouble()
		elif entry_type == 0x02: # String
			return self.getString()
		else:
			print "Unknown entry type: "+hex(entry_type)+"!"
	def get(self, num):
		if   num == 1:
			return struct.unpack("!B",self.recv(1))[0]
		elif num == 2:
			return struct.unpack("!H",self.recv(2))[0]
		else:
			raise Exception("Unknown num: "+hex(num))
	def getDouble(self):
		return struct.unpack("!d",self.recv(8))[0]
	def getString(self):
		length=self.get(2)
		# TODO this isn't *quite* the proper representation
		# However, it's close enough since our strings are going to be
		# fairly ordinary ones
		return struct.unpack("!"+str(length)+"s",self.recv(length))[0]
	def recv(self,count):
		if len(self.recvbuf) >= count:
			ret=self.recvbuf[:count]
			self.recvbuf=self.recvbuf[count:]
			#print " ".join(map(hex,map(ord,iter(ret))))
			return ret
		else:
			data=self.sock.recv(1024)
			self.recvbuf+=data
			if not data: raise EOFException()
			return self.recv(count)

class EOFException(Exception):
	pass

if __name__ == '__main__':
	client=NetworkTableClient('127.0.0.1',1735)
	client.run()
