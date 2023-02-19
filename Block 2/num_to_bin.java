import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class num_to_bin {


    public static void main(String[] args) {
        
        boolean loopExit = true;
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.print("[1]Dec to Bin [2]Bin to Dec [3]NegDec to Bin [4]Exit  ---> ");
            int key = scanner.nextInt();
            switch (key) {
                case 1:
                    System.out.print("\nGebe eine Zahl ein: ");
                    System.out.println("\nDezimal zu Binär: " + decToBin(scanner.nextInt()) + "\n");
                    break;
                case 2:
                    System.out.print("\nGebe eine bin. Zahl ein: ");
                    System.out.println("\nBinär zu Dezimal: " + binToDec(scanner.next()) + "\n");
                    break;
                case 3:
                    System.out.print("\nGebe eine neg. Zahl ein: ");
                    System.out.println("\nNegative Bin repr: " + negDecToBin(scanner.nextInt()) + "\n");
                    break;
                case 4:
                default:
                    loopExit = false;
                    scanner.close();
                    System.out.println("ENDE");
                    break;
            }

        } while (loopExit);

    }

    public static String negDecToBin(int input) {
        String bin = "";


        for (int n = Math.abs(input); n != 0; n = n / 2)
            bin += Integer.toString(n % 2);



            
        char[] newArr = new StringBuffer(bin).reverse().toString().toCharArray();

        for (int i = 0; i < newArr.length; i++) {
            if (newArr[i] == '1')
                newArr[i] = '0';
            else
                newArr[i] = '1';
        }

        //newArr[i] ~= newArr[i];

        List<Character> list = addOneBit(newArr);
        list.add(0, '1');

        return list.toString().replaceAll("[\\[\\],]", "").replaceAll(" ", "");
    }

    public static int binToDec(String input) {

        String reverse = new StringBuffer(input).reverse().toString();
        int dezi = 0;
        for (int i = 0, multiPl = 1; i < reverse.length(); i++) {
            if ((reverse.charAt(i)) != '0')
                dezi += Character.getNumericValue(reverse.charAt(i)) * multiPl;
            multiPl *= 2;
        }

        return dezi;
    }

    public static String decToBin(int input) {
        String bin = "";

        for (int n = Math.abs(input); n != 0; n = n / 2) {
            bin += Integer.toString(n % 2);
            System.out.println(n + " : " + 2 + " = " + n / 2 + " Rest " + n % 2);
        }

        return new StringBuffer(bin).reverse().toString();
    }

    public static List<Character> addOneBit(char[] arr) {

        List<Character> list = new ArrayList<Character>();

        for (Character character : arr) {
            list.add(character);
        }

        int countBit = 1;
        for (int i = list.size() - 1; i >= 0 && list.get(i) == '1'; i--) {
            countBit++;
            list.set(i, '0');
        }
        int tIndex = arr.length - countBit;
        if (tIndex > 0)
            list.set(tIndex, '1');
        else
            list.add(0, '1');

        return list;
    }

}
