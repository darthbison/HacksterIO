package drone;

import java.util.List;

import org.autonomous4j.interfaces.A4jBrain;
import org.autonomous4j.listeners.xyz.A4jErrorListener;
import org.autonomous4j.listeners.xyz.A4jNavDataListener;
import org.autonomous4j.listeners.xyz.A4jReadyStateChangeListener;
import org.autonomous4j.tracking.A4jBlackBox;
import org.autonomous4j.tracking.A4jBlackBox.Movement;

import com.dronecontrol.droneapi.DroneController;
import com.dronecontrol.droneapi.ParrotDroneController;
import com.dronecontrol.droneapi.commands.composed.PlayLedAnimationCommand;
import com.dronecontrol.droneapi.data.Config;
import com.dronecontrol.droneapi.data.enums.LedAnimation;

public class Brain implements A4jBrain {

    private Config cfg;
    private DroneController controller;
    private final A4jBlackBox recorder;
    private boolean isRecording;
    
    public Brain() {
        cfg = new Config("ProtoDrone", "profile", 1);
        this.recorder = new A4jBlackBox();
        isRecording = true;
    }
    
    @Override
    public boolean connect() {
        return connectToDrone("192.168.1.1");
    }

    public boolean connectToDrone(String ipAddress) {
        try {
            controller = ParrotDroneController.build();
            controller.start(cfg);

            controller.addNavDataListener(new A4jNavDataListener());    
            controller.addReadyStateChangeListener(new A4jReadyStateChangeListener());
            controller.addErrorListener(new A4jErrorListener());
        } catch (Exception ex) {
            System.err.println("Exception creating new drone connection: " + ex.getMessage());
            return false;
        }
        return true;
    }
    
    
    @Override
    public void disconnect() {
        if (controller != null) {
            controller.stop();
        }
        recorder.shutdown();
    }

    @Override
    public A4jBrain doFor(long ms) {
        return hold(ms);
    }

    @Override
    public A4jBrain hold(long ms) {
        System.out.println("Hold for " + ms + " milliseconds...");
        try {
            Thread.sleep(ms);
            if (isRecording) {
                recorder.recordDuration(ms);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        return this;
    }

    @Override
    public A4jBrain stay() {
        return hover();
    }
    
    private A4jBrain hover() {
        System.out.println("--Hover--");
        controller.move(0, 0, 0, 0);
        if (isRecording) {
            recorder.recordAction(A4jBlackBox.Action.STAY);
        }
        
        return this;
    }

    @Override
    public A4jBrain goHome() {
        processRecordedMovements(recorder.home());
        return this;
    }

    @Override
    public A4jBrain replay() {
        processRecordedMovements(recorder.getRecording());
        return this;
    }

    @Override
    public void processRecordedMovements(List<Movement> moves) {
        // Disable recording for playback
        isRecording = false;

        for (Movement curMov : moves) {
            switch(curMov.getAction()) {
                case FORWARD:
                    forward(curMov.getSpeed());
                    break;
                case BACKWARD:
                    backward(curMov.getSpeed());
                    break;
                case RIGHT:
                    goRight(curMov.getSpeed());
                    break;
                case LEFT:
                    goLeft(curMov.getSpeed());
                    break;
                case UP:
                    up(curMov.getSpeed());
                    break;
                case DOWN:
                    down(curMov.getSpeed());
                    break;
                case STAY:
                    stay();
                    break;
                case TAKEOFF:
                    takeoff();
                    break;
                case LAND:
                    land();
                    break;
                case LIGHTS:
                    playLedAnimation(curMov.getSpeed(), (int) curMov.getDuration()/1000);
                    break;
            }
            hold(curMov.getDuration());
            System.out.println(curMov);
        }
            
        // Re-enable recording
        isRecording = true;
        
    }
    
    public void land() {
        System.out.println("Land.");
        controller.land();
        if (isRecording) {
            recorder.recordAction(A4jBlackBox.Action.LAND);
        }
    }

    public A4jBrain takeoff() {
        System.out.println("Takeoff!");
        controller.takeOff();
        if (isRecording) {
            recorder.recordAction(A4jBlackBox.Action.TAKEOFF);
        }
        
        return this;
    }
    
    public A4jBrain forward(int speed) {
        System.out.println("Forward @" + speed);
        if (isRecording) {
            recorder.recordAction(A4jBlackBox.Action.FORWARD, speed);
        }
        
        return move(0f, -perc2float(speed), 0f, 0f);
    }

   
    public A4jBrain backward() {
        return backward(100);
    }
    
    public A4jBrain backward(int speed) {
        System.out.println("Backward @" + speed);
        if (isRecording) {
            recorder.recordAction(A4jBlackBox.Action.BACKWARD, speed);
        }

        return move(0f, perc2float(speed), 0f, 0f);
    }
    
    public A4jBrain up() {
        return up(100);
    }

    public A4jBrain up(int speed) {
        System.out.println("up @" + speed);
        if (isRecording) {
            recorder.recordAction(A4jBlackBox.Action.UP, speed);
        }

        return move(0f, 0f, perc2float(speed), 0f);
    }

    public A4jBrain down() {
        return down(100);
    }

    public A4jBrain down(int speed) {
        System.out.println("down @" + speed);
        if (isRecording) {
            recorder.recordAction(A4jBlackBox.Action.DOWN, speed);
        }

        return move(0f, 0f, -perc2float(speed), 0f);
    }

    public A4jBrain goRight(int speed) {
        System.out.println("goRight @" + speed);
        if (isRecording) {
            recorder.recordAction(A4jBlackBox.Action.RIGHT, speed);
        }

        return move(perc2float(speed), 0f, 0f, 0f);
    }

    public A4jBrain goLeft(int speed) {
        System.out.println("goLeft @" + speed);
        if (isRecording) {
            recorder.recordAction(A4jBlackBox.Action.LEFT, speed);
        }

        return move(-perc2float(speed), 0f, 0f, 0f);
    }

    public A4jBrain playLedAnimation(float frequency, int durationSeconds) {
        // "Default" LED animation sequence is blank for now
        playLedAnimation(LedAnimation.BLANK, frequency, durationSeconds);
        return this;
    }
    
    public A4jBrain playLedAnimation(LedAnimation animation, float frequency, int durationSeconds) {
        if (isRecording) {
            recorder.recordAction(A4jBlackBox.Action.LIGHTS, (int) frequency);
        }
        controller.executeCommandsAsync(
                new PlayLedAnimationCommand(cfg.getLoginData(), 
                        animation, frequency, durationSeconds));
        hold(durationSeconds * 1000);
        System.out.println("Blinking " + animation.name() + ", Frequency: " + 
            frequency + " for " + durationSeconds + " seconds.");
        return this;
    }
    
    public A4jBrain move(float roll ,float pitch, float gaz, float yaw) {
        roll = limit(roll, -1f, 1f);
        pitch = limit(pitch, -1f, 1f);
        gaz = limit(gaz, -1f, 1f);
        yaw = limit(yaw, -1f, 1f);
        
        controller.move(roll, pitch, yaw, gaz);
        
        return this;
    }
    
    private float limit(float f, float min, float max) {
        return (f > max ? max : (f < min ? min : f));
    }
    
    private float perc2float(int speed) {
        return (float) (speed / 100.0f);
    }
    

}
