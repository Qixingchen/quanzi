import java.io.IOException;

/**
 * Created by qixingchen on 15/8/19.
 */
public class Main {
    public static void main(String args[]) {
        try {
            new MacAns(Integer.valueOf(args[0]),String.valueOf(args[1]));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
