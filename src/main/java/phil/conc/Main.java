package phil.conc;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

class Main {

    public static void main(String[] args)
    {
        System.out.println("Go...");
        one();
        two();
        three();
    }

    static void one()
    {
        System.out.println("\n** ExecutorService::submit **");
        AtomicInteger atomicInt = new AtomicInteger(0);
        ExecutorService es = Executors.newFixedThreadPool(2);

        for(Runnable adder: makeRunnableAdders(atomicInt)) {
            es.submit(adder);
        }
//      Dangerous alternative:  for(Future future: futures) { future.isDone(); }
        try{ es.awaitTermination(1, TimeUnit.SECONDS); } catch (InterruptedException e) { e.printStackTrace(); }

        es.shutdown();
        System.out.println("ExecutorService::submit: " + atomicInt.get());
    }


    static void two()
    {
        System.out.println("\n** ExecutorService::invokeAll **");
        AtomicInteger atomicInt = new AtomicInteger(0);
        ExecutorService es = Executors.newFixedThreadPool(2);

        try {
            List<Future<Integer>> futures = es.invokeAll(makeCallableAdders(atomicInt));
            System.out.println("futures have return values e.g. " + futures.get(futures.size() - 1).get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        es.shutdown();
        System.out.println("ExecutorService::invokeAll: " + atomicInt.get());
    }

    static void three()
    {
        System.out.println("\n** CompletableFuture::runAsync **");
        AtomicInteger atomicInt = new AtomicInteger(0);
        ExecutorService es = Executors.newFixedThreadPool(2);

        CompletableFuture[] futures = mapToFutures(makeRunnableAdders(atomicInt),
                adder -> CompletableFuture.runAsync(adder, es));
        CompletableFuture.allOf(futures).join();

        es.shutdown();
        System.out.println("CompletableFuture::runAsync " + atomicInt.get());
    }

    private static CompletableFuture[] mapToFutures(
            List<Runnable> toRun,
            Function<Runnable, CompletableFuture> mapper)
    {
        CompletableFuture[] completableFutures = toRun.stream().map(mapper).toArray(CompletableFuture[]::new);
        return completableFutures;
    }

    private static List<Callable<Integer>> makeCallableAdders(AtomicInteger atomicInt)
    {
        Callable<Integer> addTask = () ->
                atomicInt.updateAndGet(n -> n + 2);
        return Collections.nCopies(1000, addTask);
    }

    private static List<Runnable> makeRunnableAdders(AtomicInteger atomicInt)
    {
        Runnable addTask = () -> {
            atomicInt.updateAndGet(n -> n + 2);
        };
        return Collections.nCopies(1000, addTask);
    }
}