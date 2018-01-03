package phil.conc.ch3;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class NoVisibility {
    private boolean ready;
    private int number = 0;

    private class MyThread extends Thread {
        public void run() {
            while (!ready)
                Thread.yield();

            // synchronized (NoVisibility.this) {
            System.out.println(Thread.currentThread().getName() + " reading " + number);

            if(number == 0) {
                System.err.println(number);
            }
            // }
        }
    }

    public NoVisibility() {
        List<Thread> threads = generateThreads();
        startAll(threads);

        synchronized (this) {
            ready = true;
            for(int i = 0; i < 10; i++) System.out.println("==== ready -> true ====");
            number = 42;
            System.out.println("==== number -> 42 ====");
        }
    }

    public static void main(String[] args) {
        new NoVisibility();
    }

    private List<Thread> generateThreads() {

        List<Thread> tasks = IntStream.range(0, 10).boxed()
                .map(i-> new MyThread()).collect(Collectors.toList());
        return tasks;
    }

    private void startAll(List<Thread> threads) {
        for (Thread thread : threads) {
            thread.start();
        }
    }
}