package phil.custom;

import java.util.concurrent.*;

public class ForkJoin {


    public static void main(String[] args)
    {
        ForkJoinPool pool = new ForkJoinPool(2);

        int[] array = {1, 2, 3, 4};
        SumAction sumAction = new SumAction(array, 0, 4);
        pool.invoke(sumAction);
        System.out.println(sumAction.sum);

        Integer[] integerArray = {1, 2, 3, 4};
        SumTask sumTask = new SumTask(integerArray, 0, 4);
        System.out.println(pool.invoke(sumTask));
    }


    static class SumAction extends RecursiveAction {

        private static final int SEQUENTIAL_THRESHOLD = 3;
        private final int[] array;
        private final int start, end;
        private int sum;

        private SumAction(int[] array, int start, int end)
        {
            this.array = array;
            this.start = start;
            this.end = end;
        }

        @Override
        protected void compute()
        {
            boolean isDivideTask = end - start <= SEQUENTIAL_THRESHOLD;
            if (isDivideTask) {
                divideTask();
            } else {
                sequentiallyCompute();
            }

        }

        private void sequentiallyCompute()
        {
            for (int i = start; i < end; i++) {
                sum += array[i];
            }
        }

        private void divideTask()
        {
            int mid = start + (end - start / 2);
            SumAction left = new SumAction(array, start, mid);
            SumAction right = new SumAction(array, mid, end);

            invokeAll(left, right);
            sum = left.sum + right.sum;
        }
    }


    static class SumTask extends RecursiveTask<Integer> {

        private static boolean multiThreaded = true;
        private static final int SEQUENTIAL_THRESHOLD = 3;
        private final Integer[] array;
        private final int start;
        private final int end;

        private SumTask(Integer[] array, int start, int end)
        {
            this.array = array;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Integer compute()
        {
            boolean isDivideTask = end - start <= SEQUENTIAL_THRESHOLD;
            return isDivideTask ? divideTask() : sequentiallyCompute();
        }

        private Integer sequentiallyCompute()
        {
            Integer sum = 0;
            for (int i = start; i < end; i++) {
                sum += array[i];
            }
            return sum;
        }

        private Integer divideTask()
        {
            int mid = start + ((end - start) / 2);
            SumTask left = new SumTask(array, start, mid);
            SumTask right = new SumTask(array, mid, end);

            if(multiThreaded) {
                left.fork();
                return right.compute() + left.join();
            } else {
                return right.compute() + left.compute();
            }
        }
    }
}
