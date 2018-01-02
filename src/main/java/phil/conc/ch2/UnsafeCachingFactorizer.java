package phil.conc.ch2;

import net.jcip.annotations.NotThreadSafe;
import phil.conc.ch2.stubs.Servlet;
import phil.conc.ch2.stubs.ServletRequest;
import phil.conc.ch2.stubs.ServletResponse;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicReference;

class CountingFactorizer {

    public static void main(String[] args) {
        System.out.println("Go...");
    }

    @NotThreadSafe
    public class UnsafeCachingFactorizer implements Servlet {
        private final AtomicReference<BigInteger> lastNumber =
                new AtomicReference<>();
        private final AtomicReference<BigInteger[]> lastFactors =
                new AtomicReference<>();
        public void service(ServletRequest req, ServletResponse resp) {
            BigInteger i = extractFromRequest(req);
            if (i.equals(lastNumber.get()))
                encodeIntoResponse(resp, lastFactors.get());
            else {
                BigInteger[] factors = factor(i);
                lastNumber.set(i);
                lastFactors.set(factors);
                encodeIntoResponse(resp, factors);
            }
        }

        private BigInteger extractFromRequest(ServletRequest req) {
            return null;
        }

        private BigInteger[] factor(BigInteger i) {
            return new BigInteger[0];
        }

        private void encodeIntoResponse(ServletResponse resp, BigInteger[] bigIntegers) {
        }
    }
}