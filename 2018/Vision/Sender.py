from __future__ import print_function, division
import os
import time
import socket


class Sender:
    def __init__(self, teamnum: str, camres: tuple=(480, 360), delimiter: bytes=b'|'):
        '''An easy socket sender for interaction with the roboRIO.'''
        self.teamnum = str(teamnum)
        self.camres = camres
        self.delimiter = delimiter

        self.connected = False
        self.connect()

    def connect(self, retries: int=float('inf'), retrytime: int=2):
        '''Connect to roboRIO.'''
        self.sock = socket.socket()
        self.connected = False
        try:
            self.sock.connect('roborio-{}-frc.local'.format(self.teamnum))
            self.connected = True
        except socket.error as e:  # couldn't connect or ambiguous error
            print("couldn't find roboRIO...")
            print("exception: " + str(e))
            retries -= 1
            if not retries:
                os._exit(1)
            else:
                print('retrying...')
                time.sleep(retrytime)

        print('connected')

    def send(self, x1: int, y1: int, x2: int, y2: int, errorfactor: int=10):  # errorfactor = pixels that box can be horizontally off-center in pixels
        '''Calculates the pixel distance from the middle of the camera, with an optional error correction factor.'''
        ltr_len = y2 - y1
        ttb_len = x2 - x1
        center = self.camres[0]//2, self.camres[1]//2
        x_frame_of_error = (ttb_len//2 - errorfactor, ttb_len//2 + errorfactor)
        topside = (x1, x1 + ltr_len)  # takes place *from* y1
        leftside = (y1, y1 + ttb_len)  # takes place *from* x1
        rightside = (y1, y1 + ttb_len)  # takes place *from* x2

        try:
            if x_frame_of_error[0] <= x1 + topside <= x_frame_of_error[1]:
                data = b'0' + self.delimiter
                self.sock.send(data)
            else:
                if x1 < x_frame_of_error[0]:
                    difference = -(x_frame_of_error[0] - x1)  # negative = left x amount
                    difference = str(difference).encode() + self.delimiter
                    self.sock.send(difference)
                elif x1 > x_frame_of_error[1]:
                    difference = x1 - x_frame_of_error[1]
                    difference = str(difference).encode() + self.delimiter
                    self.sock.send(difference)
                else:
                    raise NotImplementedError('nani?! topside={};x_frame_of_error={};ltr_len={};ttb_len={};errorfactor={}'.format(topside, x_frame_of_error, ltr_len, ttb_len, errorfactor))  # debugging
        except socket.error:
            self.connect()
