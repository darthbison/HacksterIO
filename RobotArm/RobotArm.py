# Motor B controls Left-Right movement
# Motor C controls the height of arm
# Motor A controls the fingers

# Direction keys - to move the arm
# Space will open the claw
# Z will close the claw

# The light sensor will be configured to also close if a certain 
# threshold is reached with an object being in front of it




from __future__ import print_function # use python 3 syntax but make it compatible with python 2
from __future__ import division       #                           ''

import time     # import the time library for the sleep function
import brickpi3 # import the BrickPi3 drivers
import sys      # import sys for sys.exit()
import curses #curses library is used to get realtime keypress and time for sleep function


BP = brickpi3.BrickPi3() # Create an instance of the BrickPi3 class. BP will be the BrickPi3 object.


PORT_MOTOR_LATERAL = BP.PORT_B
PORT_MOTOR_VERTICAL  = BP.PORT_C
PORT_MOTOR_CLAW = BP.PORT_A


stdscr = curses.initscr()	#initialize the curses object
curses.cbreak()			#to get special key characters 
stdscr.keypad(1)		#for getting values such as KEY_UP


key = ''
while key != ord('q'):		#press 'q' to quit from program
    

    BP.reset_all()
    BP.offset_motor_encoder(PORT_MOTOR_LATERAL, BP.get_motor_encoder(PORT_MOTOR_LATERAL))
    BP.offset_motor_encoder(PORT_MOTOR_VERTICAL, BP.get_motor_encoder(PORT_MOTOR_VERTICAL))
    BP.offset_motor_encoder(PORT_MOTOR_CLAW, BP.get_motor_encoder(PORT_MOTOR_CLAW))

    #lightValue = BrickPi.Sensor[PORT_2]
    #print lightValue;
    key = stdscr.getch()	#get a character from terminal
    stdscr.refresh()
    
        #change the motor speed based on key value
    if key == curses.KEY_LEFT : 
        BP.set_motor_power(PORT_MOTOR_LATERAL, -90)
    elif key == curses.KEY_RIGHT : 
        BP.set_motor_power(PORT_MOTOR_LATERAL , 90)
    elif key == curses.KEY_UP :
        BP.set_motor_power(PORT_MOTOR_VERTICAL , 90)
    elif key == curses.KEY_DOWN :
        BP.set_motor_power(PORT_MOTOR_VERTICAL , -90)
    elif key == 32 :
        BP.set_motor_power(PORT_MOTOR_CLAW , -25)
    elif key == 122 :
        BP.set_motor_power(PORT_MOTOR_CLAW , 25)
   

    #After setting the motor speeds, send values to BrickPi
    time.sleep(.1)	#pause for 100 ms
curses.endwin()
