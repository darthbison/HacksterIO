package drone;

public class demo {

    
    public static void main(String[] args) {
        performDemo(new Brain());
    }

    
    private static void performDemo(Brain br) {

        //Attempt to connect to the drone
        if (br.connect()) {
            try {

                //Execute drone commands
                commands(br);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                br.disconnect();
            }

        } else {
            System.out.println("No Drone Connection.");
        }

    }

    private static void commands(Brain br) {

        br.takeoff().hold(3000);
        br.stay().hold(3000);
        br.land();
    }

}
