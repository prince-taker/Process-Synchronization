# Cloud OS Process Synchronization Simulation

This project simulates and analyses process synchronisation in cloud operating systems, focusing on semaphore-based approaches to prevent race conditions in multi-container environments.

## Overview

Modern cloud environments present unique challenges for process synchronisation due to their distributed nature, multi-tenant environments, and variable workloads. This project implements a comprehensive simulation framework to evaluate the effectiveness of semaphore-based synchronisation strategies in preventing race conditions while measuring their performance impact.

## Key Features

- **Distributed Semaphore Implementation**: Scalable semaphore mechanism with configurable permits and timeouts
- **Cloud Environment Simulation**: Realistic modelling of containerised applications accessing shared resources
- **Workload Testing**: Various concurrency patterns (low, medium, high, burst, mixed)
- **Performance Metrics**: Comprehensive data collection including throughput, response time, and conflict rates
- **Comparative Analysis**: Direct comparison between synchronised and non-synchronised operations

## Project Structure

```
├── src/
│   ├── CloudSyncSimulation.java         # Main simulation framework
│   ├── WorkloadSimulation.java          # Concurrency pattern testing
│   └── SemaphorePerformanceTest.java    # Semaphore configuration analysis
├── results/
│   ├── simulation_metrics.csv           # Raw simulation data
│   ├── semaphore_configurations.csv     # Configuration test results
│   └── workload_test_results/           # Results from different workload patterns
├── docs/
│   └── research_paper.md                # Scientific analysis and findings
└── README.md
```

## Key Findings

Our simulation results demonstrate that:

1. **Race Condition Prevention**: Semaphore-based synchronisation completely eliminates race conditions across all concurrency levels.

2. **Conflict Rates Without Synchronization**:
   - Low concurrency (10 clients): 52% of operations experience conflicts
   - Medium concurrency (25 clients): 92% of operations experience conflicts
   - High concurrency (50 clients): 98% of operations experience conflicts

3. **Performance Trade-off**:
   - Low concurrency: 24% increase in response time with synchronisation
   - Medium concurrency: 511% increase in response time with synchronisation 
   - High concurrency: 1452% increase in response time with synchronisation

4. **Resource Utilisation**: Synchronisation enables optimal resource utilisation without exceeding safe capacity limits.

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 8 or higher
- Any Java IDE (Intellij IDEA, Eclipse, etc.) or command-line compiler

### Running the Simulation

1. Clone the repository:
   ```
   git clone https://github.com/yourusername/cloud-os-synchronization.git
   ```

2. Compile the Java files:
   ```
   javac src/*.java -d bin/
   ```

3. Run the main simulation:
   ```
   java -cp bin/ CloudSyncSimulation
   ```

4. Run specific test scenarios:
   ```
   java -cp bin/ WorkloadSimulation
   java -cp bin/ SemaphorePerformanceTest
   ```

5. Results will be saved to CSV files in the results/ directory

## Customising Simulations

You can customise various simulation parameters in the code:

- Number of containers
- Number of resources
- Semaphore permits (max concurrent access)
- Network latency distribution
- Processing time distribution
- Simulation duration

## Research Methodology

Our research follows a systematic approach:
1. **Design**: Implementation of a distributed semaphore with cloud-appropriate features
2. **Simulation**: Realistic modelling of cloud resource access patterns
3. **Measurement**: Collection of performance and consistency metrics
4. **Analysis**: Evaluation of trade-offs between consistency and performance
5. **Validation**: Comparison with theoretical models and existing research

## Conclusion

This project demonstrates that semaphore-based synchronisation is highly effective for preventing race conditions in cloud environments, though with a performance overhead that increases with concurrency. The results provide valuable insights for designing synchronisation strategies in real-world cloud applications.

## Acknowledgments

- This project was developed as part of the Operating Systems course (SCOB031) at the University of Limpopo.
- Research draws upon seminal work in distributed systems and cloud computing synchronisation strategies
