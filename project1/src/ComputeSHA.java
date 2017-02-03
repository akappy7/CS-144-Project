/**
 * Created by Nero on 1/18/17.
 */
import java.io.FileReader;
import java.io.IOException;
import java.security.*;

public class ComputeSHA {

    MessageDigest messageDigest = null;

    public ComputeSHA() {
        try {
            this.messageDigest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public void readFile(String filename) throws IOException{
        FileReader in = null;

        try {
            in = new FileReader(filename);

            int c;
            while ((c = in.read()) != -1) {
                messageDigest.update((byte)c);
            }
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }

    public void printSHA() {
        byte[] bytes = messageDigest.digest();
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : bytes) {
            stringBuilder.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }
        System.out.println(stringBuilder.toString());
    }

    public static void main(String[] args) throws IOException, IllegalArgumentException{
        if (args.length != 1) {
            throw new IllegalArgumentException();
        }
        ComputeSHA computeSHA = new ComputeSHA();
        computeSHA.readFile(args[0]);
        computeSHA.printSHA();
    }
}
