package phil.conc.ch2;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import phil.conc.ch2.stubs.Servlet;
import phil.conc.ch2.stubs.ServletRequest;
import phil.conc.ch2.stubs.ServletResponse;

import java.math.BigInteger;

@ThreadSafe
public class CachedFactorizer implements Servlet {
    @GuardedBy("this")
    private Integer lastNumber;
    @GuardedBy("this")
    private Integer[] lastFactors;
    @GuardedBy("this")
    private long hits;
    @GuardedBy("this")
    private long cacheHits;

    public synchronized long getHits() {
        return hits;
    }

    public synchronized double getCacheHitRatio() {
        return (double) cacheHits / (double) hits;
    }

    public void service(ServletRequest req, ServletResponse resp) {
        Integer requested = extractFromRequest(req);
        Integer[] factors = null;
        synchronized (this) {
            ++hits;
            if (requested.equals(lastNumber)) {
                ++cacheHits;
                factors = lastFactors.clone();
            }
        }
        if (factors == null) {
            factors = factor(requested);
            synchronized (this) {
                lastNumber = requested;
                lastFactors = factors.clone();
            }
        }
        encodeIntoResponse(resp, factors);
    }

    private BigInteger[] factor(BigInteger i) {
        return new BigInteger[0];
    }

}

