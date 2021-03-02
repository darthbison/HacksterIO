/*
 * Source file for Morse Code translator
 */
package projects;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;


public class Morse {

    /** The morse code map. */
    private static Map<Character, String> code = new HashMap<Character, String>();

    /** The gpio pin. */
    private static int gpioPIN = 17;

    /** The gpio port. */
    private static int gpioPORT = 0;

    /** The gpio. */
    private static GpioController gpio = GpioFactory.getInstance();

    /**
     * Instantiates a new morse.
     */
    public Morse() {
    }

    /**
     * Gets the morse map.
     *
     * @return the morse map
     */
    private Map<Character, String> getMorseMap() {
        code.put(' ', " ");
        code.put('\'', ".----.");
        code.put('(', "-.--.-");
        code.put(')', "-.--.-");
        code.put(',', "--..--");
        code.put('-', "-....-");
        code.put('.', ".-.-.-");
        code.put('/', "-..-.");
        code.put('0', "-----");
        code.put('1', ".----");
        code.put('2', "..---");
        code.put('3', "...--");
        code.put('4', "....-");
        code.put('5', ".....");
        code.put('6', "-....");
        code.put('7', "--...");
        code.put('8', "---..");
        code.put('9', "----.");
        code.put(':', "---...");
        code.put(';', "-.-.-.");
        code.put('?', "..--..");
        code.put('A', ".-");
        code.put('B', "-...");
        code.put('C', "-.-.");
        code.put('D', "-..");
        code.put('E', ".");
        code.put('F', "..-.");
        code.put('G', "--.");
        code.put('H', "....");
        code.put('I', "..");
        code.put('J', ".---");
        code.put('K', "-.-");
        code.put('L', ".-..");
        code.put('M', "--");
        code.put('N', "-.");
        code.put('O', "---");
        code.put('P', ".--.");
        code.put('Q', "--.-");
        code.put('R', ".-.");
        code.put('S', "...");
        code.put('T', "-");
        code.put('U', "..-");
        code.put('V', "...-");
        code.put('W', ".--");
        code.put('X', "-..-");
        code.put('Y', "-.--");
        code.put('Z', "--..");
        code.put('_', "..--.-");

        return code;
    }

    /**
     * The main method.
     *
     * @param args
     *            the arguments
     * @throws Exception
     *             the exception
     */
    public static void main(String[] args) throws Exception {

        Morse morse = new Morse();

        Scanner in = new Scanner(System.in);

        System.out.println("*** Morse Code Translator ***");
        System.out.println("Type '<end>' to end the program.\n");

        Runtime.getRuntime().exec("gpio mode " + gpioPORT + " OUT");
        while (true) {
            System.out.println("Enter your message: ");

            String message = in.nextLine();

            if (!message.equals("<end>")) {
                for (char c : message.toUpperCase().toCharArray()) {
                    morse.translate(morse.getMorseMap().get(c));
                }
                System.out.println("\n\n");
            } else {
                System.out.println("Done");
                in.close();
                gpio.shutdown();
                System.exit(1);
            }

        }

    }

    /**
     * Translate.
     *
     * @param morse
     *            the morse String
     * @throws Exception
     *             the exception
     */
    private void translate(String morse) throws Exception {

        System.out.print(morse);
        String command = "gpio -g write " + gpioPIN;
        for (char c : morse.toCharArray()) {
            if (c == '.') {

                Runtime.getRuntime().exec(command + " 1");
                Thread.sleep(200);
                Runtime.getRuntime().exec(command + " 0");
                Thread.sleep(200);
            } else if (c == '-') {
                Runtime.getRuntime().exec(command + " 1");
                Thread.sleep(500);
                Runtime.getRuntime().exec(command + " 0");
                Thread.sleep(200);
            }
        }
    }

}