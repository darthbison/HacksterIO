"""
LED Demo
""" 

from mpi4py import MPI
import RPi.GPIO as GPIO

# Set up GPIO
GPIO.cleanup()
GPIO.setmode(GPIO.BCM)
GPIO.setup(17, GPIO.OUT) #master - Green LED
GPIO.setup(18, GPIO.OUT) #Slave 1 - Purple LED
GPIO.setup(23, GPIO.OUT) #Slave 2 - Red LED 

# MPI variables for communication, rank, size, and name of the node
comm = MPI.COMM_WORLD
rank = comm.Get_rank()
size = comm.Get_size()
name = MPI.Get_processor_name()

total_requests = 2500 

# This is the master node.
if rank == 0:
    request = 0
    process = 1

    # Send the first batch of processes to the nodes.
    while process < size and request < total_requests:
        comm.send(request, dest=process, tag=1)
        print "Sending request",request,"to process",process
        request += 1
        process += 1

    # Wait for the data to come back
    received_processes = 0
    while received_processes < total_requests:
        process = comm.recv(source=MPI.ANY_SOURCE, tag=1)
        print "Received data from process", process 
        if (process == 1):
	    GPIO.output(18, True)
	    GPIO.output(23, False)
	elif (process == 2):
	    GPIO.output(23, True)
	    GPIO.output(18, False)
        received_processes += 1

        if request < total_requests:
            comm.send(request, dest=process, tag=1)
            print "Sending request",request,"to process", process 
            GPIO.output(17, True)
            request += 1

    # Send the shutdown signal
    for process in range(1,size):
        comm.send(-1, dest=process, tag=1)
    print "End"

# These are the slave nodes, where rank > 0. They receive the request from the master node and simply send a response back 
else:
    while True:
        start = comm.recv(source=0, tag=1)
        if start == -1: break
        comm.send(rank, dest=0, tag=1)


#Clean up GPIO activities after all processing takes place
GPIO.cleanup()
