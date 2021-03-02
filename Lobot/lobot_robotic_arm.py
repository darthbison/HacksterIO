import time
from BrickPi import *   

BrickPiSetup()  # setup the serial port for communication

BrickPi.MotorEnable[PORT_B] = 1 #Enable the Motor B
BrickPi.MotorEnable[PORT_C] = 1 #Enable the Motor C
BrickPi.MotorEnable[PORT_A] = 1 #Enable the Motor A


BrickPiSetupSensors()   #Send the properties of sensors to BrickPi

BrickPi.MotorSpeed[PORT_B] = 0	#first setting all speeds to zero
BrickPi.MotorSpeed[PORT_C] = 0
BrickPi.MotorSpeed[PORT_A] = 0
BrickPiUpdateValues()

elapsed = time.time()
futureTime = time.time() + 10 #Run for 10 seconds

while elapsed < futureTime:
  elapsed = time.time()
  BrickPi.MotorSpeed[PORT_B] = -125 #Left
  BrickPi.MotorSpeed[PORT_A] = -90 #open
  BrickPiUpdateValues()
  time.sleep(.1)

futureTime = time.time() + 15  #Run for 15 seconds
while elapsed < futureTime:
  elapsed = time.time()
  BrickPi.MotorSpeed[PORT_B] = 125 #Right
  BrickPi.MotorSpeed[PORT_A] = 90 #close
  BrickPiUpdateValues()
  time.sleep(.1)
