import java.util.Scanner;

public class Calculator {
    public static void main(String[] args) throws InterruptedException {

        Scanner sc = new Scanner(System.in);

        new oneLine(sc); // Calc 2.0

        float first, second, res = 0;
        char op;
        
        System.out.println("\n~~~~~~~~~Ein kleiner Taschenrechner~~~~~~~~~\n");
        System.out.println("Es stehen folgende Rechenarten zu Verfügung + - * /\n");
        
        System.out.print("Geben Sie bitte die erste Zahl ein:\t");
        sc.nextLine();
        first = sc.nextFloat();
        System.out.print("\nGeben Sie bitte die Rechenart ein:\t");
        op = sc.next().charAt(0);
        System.out.print("\nGeben Sie bitte die zweite Zahl ein:\t");
        second = sc.nextFloat();

        switch (op) {
            case '+':
                res = first + second;
                break;
            case '-':
                res = first - second;
                break;
            case '*':
                res = first * second;
                break;
            case '/':
                res = first / second;
                break;
            case '%':
                res = second * first / 100.0f;
                break;
        }

        System.out.println("\nDas Ergebnis lautet:\t" + res);
        sc.close();
    }

}

class oneLine {

    public oneLine(Scanner scanner) throws InterruptedException{
        // Scanner scanner = new Scanner(System.in);
        System.out.print("~~~~~~~~~OneLineCalc~~~~~~~~~\n");

        System.out.print("Eingabe: ");
        float result = 0;
        boolean retry = true;

        while (retry) {
            try {
                String input = scanner.next();
                result = oneLineCalc(input);
                retry = false;
            } catch (Exception e) {
                System.out.println("Bad input!");
                System.out.print("Eingabe: ");
            }
        }

        System.out.println("Ergebnis: " + result);
        System.out.println("\nWait 1.");
        Thread.sleep(1000);
    }

    public float oneLineCalc(String input) {
        String op = null;
        float res = 0;
        float[] newArr = { 0, 0, 0 };
        String[] deli = { "0", "0", "0" };

        if (input.contains("+")) {
            op = "+";
            deli = input.split("\\+");
        } else if (input.contains("-")) {
            op = "-";
            deli = input.split("\\-");
        } else if (input.contains("*")) {
            op = "*";
            deli = input.split("\\*");
        } else if (input.contains("/")) {
            op = "/";
            deli = input.split("\\/");
        }

        for (int i = 0; i < deli.length; i++) {
            newArr[i] = Float.parseFloat(deli[i]);
        }

        switch (op) {
            case "+":
                res = newArr[0] + newArr[1];
                break;
            case "-":
                res = newArr[0] - newArr[1];
                break;
            case "*":
                res = newArr[0] * newArr[1];
                break;
            case "/":
                res = newArr[0] / newArr[1];
                break;
            default:
                break;
        }

        return res;
    }

}
