import java.util.List;
import java.util.Random;

/**
 * Represents a container in the cloud environment
 */
class Container implements Runnable
{
    private final int containerId;
    private final List<CloudResource> resources;
    private final boolean enableSync;
    private final SimulationConfig config;
    private final Random random;
    private final MetricsCollector metrics;

    private volatile boolean running = false;

    public Container(final int id,
                     final List<CloudResource> resources,
                     final boolean enableSync,
                     final SimulationConfig config,
                     final MetricsCollector metrics)
    {
        this.containerId = id;
        this.resources   = resources;
        this.enableSync  = enableSync;
        this.config      = config;
        this.metrics     = metrics;
        random           = new Random();

        // Seed the random number generator with container ID for reproducibility
        random.setSeed(System.currentTimeMillis() + id);
    }

    public void start()
    {
        running = true;
        new Thread(this).start();
    }

    public void stop()
    {
        running = false;
    }

    @Override
    public void run()
    {
        while (running)
        {
            // Select a random resource to access
            int resourceIndex      = random.nextInt(resources.size());
            CloudResource resource = resources.get(resourceIndex);

            // Access the resource with or without synchronization
            if (enableSync)
            {
                resource.accessWithSync(containerId, random, config, metrics);
            }
            else
            {
                resource.accessWithoutSync(containerId, random, config, metrics);
            }

            // Wait before next request
            int waitTime = Math.max(1, (int)(random.nextGaussian() *
                    config.requestRateStdDevMs +
                    config.requestRateMeanMs));
            try
            {
                Thread.sleep(waitTime);
            }
            catch (final InterruptedException e)
            {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}