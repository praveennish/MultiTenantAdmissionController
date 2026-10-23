package controller;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class MultiTenantAdmissionController{
    private final ConcurrentHashMap<String, TokenBucket> tenantBuckets = new ConcurrentHashMap<>();

}

class TokenBucket{
    private final long maxCapacity;
    private final long refillRatePerMs;
    private final AtomicLong availableTokens;
    private final AtomicLong lastRefillTimeStamp;

    public TokenBuket(long maxCapacity, long refillRatePerMs){
        this.maxCapacity = maxCapacity;
        this.refillRatePerMs = refillRatePerMs;
        this.availableTokens = new AtomicLong(maxCapacity);
        this.lastRefillTimeStamp = new AtomicLong(System.currentTimeMillis());
    }
    
    public boolean consume(long tokensToConsume){
        while(true){
            long now = System.currentTimeMillis();
            long lastRefill = lastRefillTimeStamp.get();
            last currentTokens = availableTokens.get();

            long duration = Math.max(0, now - lastRefill);
            long newTokensGenerated = duration * refillRatePerMs;

            long updatedTokens = Math.min(maxCapacity, currentTokens + newTokensGenerated);

            if (updatedToken < tokensToConsume)
                return false;

            if (lastRefillTimeStamp.compareAndSet(lastRefill, now)){
                if (availableTokens.compareAndSet(currentTokens, updatedTokens - tokensToConsume))
                    return true; // Allowed
            }
        }
    }
}