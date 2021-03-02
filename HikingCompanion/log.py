from sense_hat import SenseHat
import subprocess, os

#Class to enable the user to record voice logs
def runLog():
	sense = SenseHat()
	sense.clear()

	sense.show_message("Rec...")
	logcount = subprocess.check_output('ls -l logs | grep -v ^l | wc -l', shell=True)
	logcount = int(logcount) - 1
	logname = "LOG"

	logFile = logname + str(logcount) + ".mp3"

        #Do a 15 sec recording and save to the logs directory as a mp3
	os.system("sox -t alsa plughw:1 logs/" + logFile + " trim 0 00:00:15")
	logcount = logcount + 1
        sense.show_message("Done.")

