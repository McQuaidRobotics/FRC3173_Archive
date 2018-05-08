#!/bin/bash

keepRunning() {
	# Restart a program if it crashes, forever.
	while true; do
		"$@"
		echo "Error: '$@' crashed, restarting!"
	done
}

# Commands to run *once* when the robot starts
sudo python -c 'from lib_leds import setLED;setLED(1,True)' # Turn on vision LEDs

keepRunning sudo python leds.py
