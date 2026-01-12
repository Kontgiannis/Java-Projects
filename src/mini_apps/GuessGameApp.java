package mini_apps;

/**
 * User tries to guess a secret random generated number
 * @author Giannis
 * @version 1.0.0
 */

import java.util.Scanner;

public class GuessGameApp {
    
    // initialization of to be guessed numbers and max retries
    private static final int MAX_TRIES = 5;
    private static boolean run = true;

    // Start of game
    public static void main(String[] args) {
        int genNum,retries,inNum;

        // Start of guessing game
        Scanner reader = new Scanner(System.in);
        while (run) {
            genNum = (int) (Math.random() * 101);
            retries = MAX_TRIES;

            // Start of retries loop
            while (retries > 0) {
                System.out.print("Give an integer between 0 and 100 ( 0 and 100 included): ");
                inNum = reader.nextInt();
                if (inNum == genNum) {
                    System.out.println("You guessed correctly congrats!");
                    break; // Stops retires loop
                }
                retries--;
                if (inNum > genNum) {
                        System.out.printf("Too high... You have: %d retries remains\n", retries);
                } else {
                    System.out.printf("Too low... You have: %d retries remains\n", retries);
                }

            }

            // checks if reties = 0 and asks for a rematch
            if (retries == 0) {
                System.out.println("Game lost.. the number was " + genNum);
            }

            System.out.print("Do you want a re-match?? (Yes / No): ");
            String resp = reader.next();

            if (resp.equalsIgnoreCase("Yes") || resp.equalsIgnoreCase("y") ) {
                System.out.println("//...Game Restarting...//");

            } else if (resp.equalsIgnoreCase("No") || resp.equalsIgnoreCase("n")) {
                System.out.println("//...Game Ended...//");
                break;

            }

        }
        reader.close();



    }


}
