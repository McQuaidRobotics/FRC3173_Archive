import RPi.GPIO as GPIO

GPIO.setmode(GPIO.BCM)
GPIO.setup(7,GPIO.OUT)
while True:
	raw_input("Turn on")
	GPIO.output(7,GPIO.LOW)
	raw_input("Turn off")
	GPIO.output(7,GPIO.HIGH)

