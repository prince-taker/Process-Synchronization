/**
 * Cloud Process Synchronization Simulation
 *
 * This simulation demonstrates process synchronization in a cloud environment
 * using semaphores. Multiple containers compete for access to shared resources,
 * and we compare the performance with and without proper synchronization.
 */
public class CloudSyncSimulation
{

    public static void main(String[] args)
    {
        // Create simulation configuration
        SimulationConfig config = new SimulationConfig();

        // Create and run the simulation
        CloudSimulation simulation = new CloudSimulation(config);
        simulation.setup();
        simulation.run();
    }
}