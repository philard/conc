package phil.conc.ch3;

import net.jcip.annotations.Immutable;
import net.jcip.annotations.ThreadSafe;
import phil.conc.ch2.stubs.Servlet;
import phil.conc.ch2.stubs.ServletRequest;
import phil.conc.ch2.stubs.ServletResponse;

import java.util.Arrays;

@ThreadSafe
public class VolatileCachedFactorizer implements Servlet {
    private volatile OneValueCache cache = new OneValueCache(null, null);

    public void service(ServletRequest req, ServletResponse resp) {
        Integer requested = extractFromRequest(req);
        Integer[] factors = cache.getFactors(requested);
        if (factors == null) {
            factors = factor(requested);
            cache = new OneValueCache(requested, factors);
        }
        encodeIntoResponse(resp, factors);
    }

    @Immutable
    class OneValueCache {
        private final Integer lastNumber;
        private final Integer[] lastFactors;

        public OneValueCache(Integer i,
                             Integer[] factors) {
            lastNumber = i;
            lastFactors = Arrays.copyOf(factors, factors.length);
        }

        public Integer[] getFactors(int i) {
            if (lastNumber == null || !lastNumber.equals(i))
                return null;
            else
                return Arrays.copyOf(lastFactors, lastFactors.length);
        }
    }

}

