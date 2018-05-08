from lib_networktableclient import NetworkTableClient
from lib_leds import setLED
import RPi.GPIO as GPIO

def ledsOnChange(key,value):
	if key[:9] == "/leds/led":
		try:
			led=int(key[9:])
		except ValueError:
			return # Must not be an int at the end of that string...
	setLED(led,value)

ntc=NetworkTableClient('127.0.0.1')
ntc.onchange=ledsOnChange
ntc.run() # Note: *not* as a separate thread; we don't need that here
