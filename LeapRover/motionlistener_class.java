package rover;

import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Listener;

public class MotionListener extends Listener {

    private static final String accessToken = "<your_photon_access_token>";
    private static final String deviceID = "<your_photon_device_id>";

    //Constants for hands and fingers
    private static final int totalHands = 1;
    private static final int forward = 5;
    private static final int left = 1;
    private static final int right = 2;
    private static final int back = 4;
   
    private static String controlURL;

    @Override
    public void onConnect(Controller controller) {
        System.out.println("Connected");
    }

    @Override
    public void onFrame(Controller controller) {
        Frame frame = controller.frame();

        //Determine number of hands and fingers from the leap motion
        int hands = frame.hands().count();
        int fingers = frame.fingers().count();

        determineMovement(hands, fingers);
    }

    private static void determineMovement(int hands, int fingers) {
        
        //Build control URL
        StringBuilder url = new StringBuilder();
        url.append("https://api.spark.io/v1/devices/");
        url.append(deviceID);
        url.append("/control");

        controlURL = url.toString();

        if (hands == totalHands) {
            switch (fingers) {
            case forward:
                executeCommand("F-100");
                break;
            case right:
                executeCommand("R-50");
                break;
            case left:
                executeCommand("L-50");
                break;
            case back:
                executeCommand("B-75");
                break;
            default:
                executeCommand("S");
                break;

            }
        }
        else {
          
            //Stop the Rover if wrong number of hands are detected
            executeCommand("S");
        }
    }

    private static void executeCommand(String command) {
        URL url;
        
        //Send command to Rover via POST
        try {
            url = new URL(controlURL);

            HttpURLConnection hConnection = (HttpURLConnection) url
                    .openConnection();
            HttpURLConnection.setFollowRedirects(true);

            hConnection.setDoOutput(true);
            hConnection.setRequestMethod("POST");

            PrintStream ps = new PrintStream(hConnection.getOutputStream());
            ps.print("params=" + command + "&access_token=" + accessToken);
            ps.close();

            hConnection.connect();
            
            hConnection.getResponseCode();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
