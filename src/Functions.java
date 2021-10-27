import java.util.Random;

public class Functions {
    public static int getNumFromRange(int max, int min) {
        Random rand = new Random();
        return min + rand.nextInt(
                max
                        - min);
    }
}
