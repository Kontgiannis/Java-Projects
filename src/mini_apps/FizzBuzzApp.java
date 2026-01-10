package mini_apps;

import java.util.Scanner;

/**
 * user gives an integer n and the program prints the numbers 1,...,n but the numbers divisible by 3 get replaced with Fizz the ones
 * with 5 get replaced by Buzz and those divisible by both of em by FizzBuzz
 * @author Giannis
 * @version 1.0.0
 */

public class FizzBuzzApp {
    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);
        System.out.print("Give an integer number: ");
        int num = reader.nextInt();
        int fizz = 2;
        int buzz = 4;
        int i = 1;
        while ( i <= num) {
            if ((fizz == 0) && (buzz == 0)) {
                System.out.print(" FizzBuzz ");
                fizz = 2;
                buzz = 4;
                i++;
                continue;
            }
            if (buzz == 0) {
                System.out.print(" Buzz ");
                buzz = 4;
                fizz--;
                i++;
                continue;
            }
            if (fizz == 0) {
                System.out.print(" Fizz ");
                fizz = 2;
                buzz--;
                i++;
                continue;
            }
            System.out.print(" " + i + " ");
            fizz--;
            buzz--;
            i++;
        }
        reader.close();
    }
}
