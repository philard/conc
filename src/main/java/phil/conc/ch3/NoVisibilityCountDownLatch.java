package phil.conc.ch3;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class NoVisibilityCountDownLatch {
    private boolean ready;
    private int number = 0;

    private CountDownLatch countDownLatch = new CountDownLatch(1);

    private class MyThread extends Thread {
        public void run() {
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

//             synchronized (NoVisibilityCountDownLatch.this) {
//            System.out.println(Thread.currentThread().getName() + " reading " + number);
            if (number == 0) {
                System.err.println(number);
            }
//             }
        }
    }

    public NoVisibilityCountDownLatch() {
        List<Thread> threads = generateThreads();
        startAll(threads);
        synchronized (this) {
            countDownLatch.countDown();
            for (int i = 0; i < 9; i++) System.out.println("");
            number = 42;
        }
    }

    public static void main(String[] args) {
        new NoVisibilityCountDownLatch();
    }

    private List<Thread> generateThreads() {

        List<Thread> tasks = IntStream.range(0, 10).boxed()
                .map(i -> new MyThread()).collect(Collectors.toList());
        return tasks;
    }

    private void startAll(List<Thread> threads) {
        for (Thread thread : threads) {
            thread.start();
        }
    }
}
