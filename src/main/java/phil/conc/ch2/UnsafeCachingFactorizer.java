package phil.conc.ch2;

import net.jcip.annotations.NotThreadSafe;
import phil.conc.ch2.stubs.Servlet;
import phil.conc.ch2.stubs.ServletRequest;
import phil.conc.ch2.stubs.ServletResponse;

import java.util.concurrent.atomic.AtomicReference;

class CountingFactorizer {

    public static void main(String[] args) {
        System.out.println("Go...");
    }

    @NotThreadSafe
    public class UnsafeCachingFactorizer implements Servlet {
        private final AtomicReference<Integer> lastNumber = new AtomicReference<>();
        private final AtomicReference<Integer[]> lastFactors = new AtomicReference<>();

        public void service(ServletRequest req, ServletResponse resp) {
            Integer i = extractFromRequest(req);
            if (i.equals(lastNumber.get()))
                encodeIntoResponse(resp, lastFactors.get());
            else {
                Integer[] factors = factor(i);
                lastNumber.set(i);
                lastFactors.set(factors);
                encodeIntoResponse(resp, factors);
            }
        }
    }
}