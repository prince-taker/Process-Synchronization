import java.time.Duration;
import java.time.Instant;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

class CloudResource
{
    private static final int timeOutMs = 1000;
    private final String resourceId;
    private final DistributedSemaphore semaphore;

    private final AtomicInteger currentUsers  = new AtomicInteger(0);
    private final AtomicInteger totalAccesses = new AtomicInteger(0);
    private final AtomicInteger conflictCount = new AtomicInteger(0);

    public CloudResource(final String id,
                         final int maxConcurrentAccess)
    {
        this.resourceId = id;
        this.semaphore  = new DistributedSemaphore(maxConcurrentAccess, "sem_" + id);
    }

    /**
     * Access the resource with synchronization
     */
    public void accessWithSync(final int containerId,
                               final Random random,
                               final SimulationConfig config,
                               final MetricsCollector metrics)
    {

        Instant startTime = Instant.now();

        // Try to acquire the semaphore with timeout
        boolean acquired = semaphore.acquire
                (random,
                config.networkLatencyMeanMs,
                config.networkLatencyStdDevMs,
                timeOutMs);  // 5-second timeout

        Instant acquireTime = Instant.now();
        long acquireDuration = Duration.between(startTime, acquireTime).toMillis();

        if (!acquired)
        {
            // Timeout occurred
            metrics.recordTimeout(containerId, resourceId);
            return;
        }

        // Successfully acquired the semaphore
        int users = currentUsers.incrementAndGet();
        totalAccesses.incrementAndGet();

        // Simulate processing time for using the resource
        int processingTime = Math.max(1, (int)(random.nextGaussian() *
                config.processingTimeStdDevMs +
                config.processingTimeMeanMs));
        try
        {
            Thread.sleep(processingTime);
        }
        catch (final InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }

        // Check for potential conflicts (should never happen with proper synchronization)
        if (users > semaphore.getMaxValue())
        {
            conflictCount.incrementAndGet();
            metrics.recordConflict(containerId, resourceId);
        }

        currentUsers.decrementAndGet();

        // Release the semaphore
        semaphore.release(random, config.networkLatencyMeanMs, config.networkLatencyStdDevMs);

        Instant endTime    = Instant.now();
        long totalDuration = Duration.between(startTime, endTime).toMillis();

        // Record metrics
        metrics.recordAccess(containerId, resourceId, acquireDuration, processingTime, totalDuration);
    }

    /**
     * Access the resource without synchronization (for comparison)
     */
    public void accessWithoutSync(final int containerId,
                                  final Random random,
                                  final SimulationConfig config,
                                  final MetricsCollector metrics)
    {

        Instant startTime = Instant.now();

        // Simulate network latency (but no semaphore acquisition)
        simulateNetworkLatency(random, config.networkLatencyMeanMs, config.networkLatencyStdDevMs);

        Instant acquireTime = Instant.now();
        long acquireDuration = Duration.between(startTime, acquireTime).toMillis();

        // No synchronization, just access the resource
        int users = currentUsers.incrementAndGet();
        totalAccesses.incrementAndGet();

        // Simulate processing time
        int processingTime = Math.max(1, (int)(random.nextGaussian() *
                config.processingTimeStdDevMs +
                config.processingTimeMeanMs));
        try
        {
            Thread.sleep(processingTime);
        }
        catch (final InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }

        // Check for conflicts (will happen without synchronization)
        if (users > semaphore.getMaxValue())
        {
            conflictCount.incrementAndGet();
            metrics.recordConflict(containerId, resourceId);
        }

        currentUsers.decrementAndGet();

        // Simulate network latency for completion
        simulateNetworkLatency(random, config.networkLatencyMeanMs, config.networkLatencyStdDevMs);

        Instant endTime  = Instant.now();
        long totalDuration = Duration.between(startTime, endTime).toMillis();

        // Record metrics
        metrics.recordAccess(containerId, resourceId, acquireDuration, processingTime, totalDuration);
    }

    private void simulateNetworkLatency(final Random random,
                                        final int meanMs,
                                        final int stdDevMs)
    {
        int latency = Math.max(1, (int)(random.nextGaussian() * stdDevMs + meanMs));
        try
        {
            Thread.sleep(latency);
        }
        catch (final InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
    }

    // Getters for metrics
    public int getTotalAccesses()
    {
        return totalAccesses.get();
    }

    public int getConflictCount()
    {
        return conflictCount.get();
    }

    public int getCurrentUsers()
    {
        return currentUsers.get();
    }

    public String getId()
    {
        return resourceId;
    }
}