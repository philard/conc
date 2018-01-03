package phil.conc.ch2.stubs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

public interface Servlet {
    default void encodeIntoResponse(ServletResponse resp, Integer[] factors) {
        System.out.println("encodeIntoResponse");
    }

    default int extractFromRequest(ServletRequest req) {
        return ThreadLocalRandom.current().nextInt(0, 100);
    }

    default Integer[] factor(Integer a) {
        int upperLimit = (int)(Math.sqrt(a));
        ArrayList<Integer> factors = new ArrayList<Integer>();
        for(int i=1;i <= upperLimit; i+= 1){
            if(a%i == 0){
                factors.add(i);
                if(i != a/i){
                    factors.add(a/i);
                }
            }
        }
        Collections.sort(factors);
        return (Integer[]) factors.toArray();
    }
}
