package phil.conc;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

class Main {

    public static void main(String[] args) {
        System.out.println("Go...");
        one();
        two();
        three();
    }

    static void one() {
        System.out.println("\n** ExecutorService::submit **");
        AtomicInteger atomicInt = new AtomicInteger(0);

        List<Future> futures = new LinkedList<Future>();
        ExecutorService es = Executors.newFixedThreadPool(2);
        makeRunnables(atomicInt)
                .forEach(runnable -> {
                    futures.add(es.submit(runnable));
                });
        for(Future future: futures) { future.isDone(); }
        //try{ es.awaitTermination(1, TimeUnit.SECONDS); } catch (InterruptedException e) { e.printStackTrace(); }
        es.shutdown();

        System.out.println("ExecutorService::submit: " + atomicInt.get());
        //Random completion time means this is sometimes less than 2000...
    }


    static void two() {
        System.out.println("\n** ExecutorService::invokeAll **");

        AtomicInteger atomicInt = new AtomicInteger(0);

        ExecutorService es = Executors.newFixedThreadPool(2);
        try {
            List<Future<Integer>> futures = es.invokeAll(makeCallables(atomicInt));
        } catch (InterruptedException e) {}
        es.shutdown();

        System.out.println("ExecutorService::invokeAll: " + atomicInt.get());
    }

    static void three() {
        System.out.println("\n** CompletableFuture::runAsync **");
        AtomicInteger atomicInt = new AtomicInteger(0);

        ExecutorService es = Executors.newFixedThreadPool(2);
        CompletableFuture<?>[] futures = makeRunnables(atomicInt).stream()
                .map(task -> CompletableFuture.runAsync(task, es))
                .toArray(CompletableFuture[]::new);
        CompletableFuture.allOf(futures).join();
        es.shutdown();

        System.out.println("CompletableFuture::runAsync " + atomicInt.get());
    }

    private static List<Callable<Integer>> makeCallables(AtomicInteger atomicInt) {
        Callable<Integer> addTask = () -> atomicInt.updateAndGet(n -> n + 2);
        List<Callable<Integer>> tasks =  Collections.nCopies(1000, addTask);
        return tasks;
    }

    private static List<Runnable> makeRunnables(AtomicInteger atomicInt) {
        Runnable addTask = () -> atomicInt.updateAndGet(n -> n + 2);
        List<Runnable> tasks =  Collections.nCopies(1000, addTask);
        return tasks;
    }
}