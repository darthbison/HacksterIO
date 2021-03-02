#!/usr/bin/python
from sense_hat import SenseHat
import os,sys
import time
import compass
import log
import gps
import temphumidity
import pygame  
from pygame.locals import *

#initialize pygame
pygame.init()
pygame.display.set_mode((640, 480))

sense = SenseHat()
sense.show_message("ENV")
sense.clear()  # Blank the LED matrix


def temperature():
	temphumidity.runTempHumidity()

def GPS():
	gps.runGPS()

def COMP():
	compass.runCompass()

def REC():
	log.runLog()

#Listen for joystick events
def handle_event(event):
    if event.key == pygame.K_DOWN:
        temperature()
    elif event.key == pygame.K_UP:
        COMP()
    elif event.key == pygame.K_LEFT:
        REC()
    elif event.key == pygame.K_RIGHT:
        GPS()

i = 0
while True:
  try: 
    for event in pygame.event.get():
        #For whatever reason, pygame seems to run event.get commands twice, so the programs run twice. I put this logic in to prevent that
        if i < 1 :
            handle_event(event)
 	    i = i + 1	
	else :
            i = 0
  except (KeyboardInterrupt):
        sense.clear()
        sys.exit(0)

