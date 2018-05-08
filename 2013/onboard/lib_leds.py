import RPi.GPIO as GPIO

# Mapping of physical GPIO pins to logical LED numbers
# Index 0 is invalid because logical LED numbers are 1-indexed
pinMap=(-1,7,8,9,25)

GPIO.setmode(GPIO.BCM)
for pin in pinMap:
	if pin != -1:
		GPIO.setup(pin,GPIO.OUT)

def setLED(led,to):
	if 0 >= led or led >= len(pinMap):
		print "LED "+str(led)+" out of range!"
		return
	print "LED "+str(led)+" (GPIO "+str(pinMap[led])+") = "+("HIGH" if to else "LOW")
	GPIO.output(pinMap[led],GPIO.HIGH if to else GPIO.LOW)

