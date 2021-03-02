package rover;

import java.io.IOException;

import com.leapmotion.leap.Controller;

public class photonRover {

    public static void main(String[] args) 
    {
        //Instance of Motion Listener
        MotionListener listener = new MotionListener();
        
        //Leap motion controller instance
        Controller controller = new Controller();
        
        //Add motion listener to controller
        controller.addListener(listener);

        // Keep this process running until Enter is pressed
        System.out.println("Press Enter to quit...");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Remove the listener when done
        controller.removeListener(listener);
    }

}
