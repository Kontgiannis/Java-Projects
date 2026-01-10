package mini_apps;
/**
 * Checks Age and ticket type given by user and determent if they enter and if yes to which type Regular ( ticket type 1 or VIP ticket type 2)
 * age >= 18
 * @author Giannis
 * @vesion 1.0.1
 */

import java.util.Scanner;


public class EntrySystemApp {

     private static final int VIP = 2;
     private static final int AGE = 18;
     private static final int MAX_RETRIES = 3;

    public static void main(String[] args){

        Scanner scanner = new Scanner(System.in);
        int age = 0;
        int type = 0;

        // Validation outer loop
        while (true) {
            System.out.print("Give your age: ");
            age = scanner.nextInt();
            if (age <= 0) {
                System.out.println("Your age can't be negative or zero...");
                continue;
            }

            // Retries to enter correct ticket type loop
            int retries = MAX_RETRIES;
            while (retries > 0) {
                System.out.print("Give your type of ticket: ");
                type = scanner.nextInt();

                if (type != 1 && type != 2) {
                    retries--;
                    System.out.println("Your ticket type can only be 1 ( Regular )  OR 2 (VIP)");
                    System.out.println("You have " + retries + " tries before information reset");
                } else break;
            }

            if (retries == 0) {
                System.out.println("Information reset please re - enter it");
                continue;
            }
            break;  // End outer loop
        }

        // Entry check
        if (age < AGE) {
            System.out.print("Access Denied");
        } else if (type != VIP) {
            if (age >= 100) {
                System.out.println("How the hell are you still alive ?!"); // Easter egg
            }
            System.out.print("Regular access granted - but not VIP");
        } else {
            if (age >= 100) {
                System.out.println("How the hell are you still alive ?!"); // Easter egg
            }
            System.out.print("Welcome Sir");
        }
        scanner.close();
    }

}
