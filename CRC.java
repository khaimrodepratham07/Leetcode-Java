import java.util.*;

public class CRC {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Input data and generator
        System.out.print("Enter message bits: ");
        String message = sc.nextLine();
        System.out.print("Enter generator bits: ");
        String generator = sc.nextLine();

        int[] data = new int[message.length() + generator.length() - 1];
        int[] divisor = new int[generator.length()];

        for (int i = 0; i < message.length(); i++) 
            data[i] = message.charAt(i) - '0';
        for (int i = 0; i < generator.length(); i++) 
            divisor[i] = generator.charAt(i) - '0';

        // Division to find CRC
        for (int i = 0; i < message.length(); i++) {
            if (data[i] == 1)
                for (int j = 0; j < divisor.length; j++)
                    data[i + j] ^= divisor[j];
        }

        // Append CRC to original message
        System.out.print("Checksum code: ");
        for (int i = 0; i < message.length(); i++)
            System.out.print(message.charAt(i));
        for (int i = message.length(); i < data.length; i++)
            System.out.print(data[i]);
        System.out.println();

        // Receiver side check
        System.out.print("Enter received code: ");
        String recv = sc.nextLine();
        data = new int[recv.length() + generator.length() - 1];
        for (int i = 0; i < recv.length(); i++) 
            data[i] = recv.charAt(i) - '0';

        for (int i = 0; i < recv.length(); i++) {
            if (data[i] == 1)
                for (int j = 0; j < divisor.length; j++)
                    data[i + j] ^= divisor[j];
        }

        boolean valid = true;
        for (int bit : data) {
            if (bit == 1) { valid = false; break; }
        }
        System.out.println(valid ? "Data is valid ✅" : "Data is invalid ❌");
        sc.close();
    }
}
