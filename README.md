# Radar Simulation

A Java-based radar simulation application using JavaFX for graphical representation and Java Sound API for audio feedback.

## Features

- **Radar Sweep**: Simulates a radar sweep with adjustable speed.
- **Target Detection**: Detects targets within the radar's range and highlights them.
- **Track-While-Scan (TWS) Mode**: Allows following a specific target.
- **Target Lock**: Locks onto a target for tracking.
- **Launch Mode**: Simulates a launch sequence with sound effects.

## Prerequisites

- Java 8 or higher
- Maven
- JavaFX SDK

## Installation

1. **Clone the repository**:
    ```sh
    git clone https://github.com/ArdaKoksall/radar-simulation.git
    cd radar-simulation
    ```

2. **Build the project using Maven**:
    ```sh
    mvn clean install
    ```

3. **Run the application**:
    ```sh
    mvn javafx:run
    ```

## Usage

- **Adjust Sweep Speed**: Use the combo box at the top to change the radar sweep speed.
- **Toggle TWS Mode**: Press `Z` to toggle TWS mode.
- **Switch TWS Target**: Press `A` to switch to the next target, `D` to switch to the previous target.
- **Lock Target**: Press `X` to lock onto a target.
- **Toggle Launch Mode**: Press `C` to toggle launch mode (only available when a target is locked).

## Project Structure

- `src/main/java/org/example/radar/`:
  - `RadarSimulation.java`: Main application class.
  - `Functions.java`: Contains functions for target detection, TWS mode, and sound effects.
  - `Target.java`: Represents a target on the radar.

- `src/main/resources/`: Contains audio files for sound effects.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

