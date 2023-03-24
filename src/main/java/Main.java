import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    static AtomicInteger three = new AtomicInteger(0);
    static AtomicInteger four = new AtomicInteger(0);
    static AtomicInteger five = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {

        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        Thread threeThread = new Thread(() -> {
            for (String text : texts) {
                if (text.length() == 3 && text.charAt(0) == text.charAt(1) && text.charAt(0) == text.charAt(2) || text.equals("abc")) {
                    three.getAndIncrement();
                }
            }
        });
        threeThread.start();
        threeThread.join();


        Thread fourThread = new Thread(() -> {
            for (String text : texts) {
                if (text.length() == 4) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(text.charAt(3));
                    stringBuilder.append(text.charAt(2));
                    if (text.substring(0, 2).equals(stringBuilder.toString())) {
                        four.getAndIncrement();
                    }
                }
            }
        });
        fourThread.start();
        fourThread.join();

        Thread fiveThread = new Thread(() -> {
            for (String text : texts) {
                if (text.length() == 5) {
                    AtomicInteger atomicInteger = new AtomicInteger(0);
                    for (int i = 1; i < text.length(); i++) {
                        if (text.charAt(atomicInteger.get()) < text.charAt(i)) {
                            break;
                        }
                        atomicInteger.getAndIncrement();
                    }
                    if (text.length() - 1 == atomicInteger.get()) {
                        five.getAndIncrement();
                    }
                }
            }
        });
        fiveThread.start();
        fiveThread.join();

        System.out.println("Красивых слов с длиной 3: " + three + " шт");
        System.out.println("Красивых слов с длиной 4: " + four + " шт");
        System.out.println("Красивых слов с длиной 5: " + five + " шт");

    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}
