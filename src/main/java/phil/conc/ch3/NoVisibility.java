package phil.conc.ch3;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class NoVisibility {
    private static boolean ready;
    private static int number;

    private static class MyTheead extends Thread {
        public void run() {
            while (!ready)
                Thread.yield();
            System.out.println(number);
        }
    }

    public static void main(String[] args) {
        List<Thread> threads = generateRunnables();
        startAll(threads);

        ready = true;
        System.out.println("spacer");
        System.out.println("spacer");
        number = 42;


    }

    private static List<Thread> generateRunnables() {

        List<Thread> tasks = IntStream.range(1, 100).boxed()
                .map(i-> new MyTheead()).collect(Collectors.toList());
        return tasks;
    }

    private static void startAll(List<Thread> threads) {
        for (Thread thread : threads) {
            thread.start();
        }
    }
}