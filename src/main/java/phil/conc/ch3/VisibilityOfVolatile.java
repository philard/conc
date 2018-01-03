package phil.conc.ch3;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class VisibilityOfVolatile {
    private volatile boolean  ready;
    private volatile int number = 0;

    private class MyThread extends Thread {
        public void run() {
            while (!ready)
                Thread.yield();

            //System.out.println(Thread.currentThread().getName() + " reading " + number);

            if(number == 0) {
                System.err.println(number);
            }
        }
    }

    public VisibilityOfVolatile() {
        List<Thread> threads = generateThreads();
        startAll(threads);

        number = 42;
        System.out.println("==== number -> 42 ====");

        ready = true;
        System.out.println("==== ready -> true ====");


    }

    public static void main(String[] args) {
        new VisibilityOfVolatile();
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