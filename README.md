#  Sports Performance Analysis

A **JavaFX-based desktop application** that allows users to input detailed sports performance data and visualize it through graphs, tables, and history views.  
Data is stored in **Apache Cassandra** (running via Docker Desktop), enabling scalable and efficient performance tracking.  

---

##  Features
- 📋 **Multi-tab UI** for input, graphs, and history view.
- 📊 **Graphical representation** of player performance over time.
- 🗄️ **Cassandra integration** for persistent data storage.
- 🖥 **User-friendly interface** built with JavaFX.
- 🔄 Works with multiple sports data formats.

---

##  Tools & Technologies Used

| Tool / Technology   | Version        | Purpose                                |
|---------------------|---------------|----------------------------------------|
| **Java**            | JDK 23/11     | JavaFX UI development                  |
| **Java**            | JDK 11        | Apache Cassandra compatibility         |
| **JavaFX SDK**      | 23.0.2        | UI framework for the application       |
| **Apache Cassandra**| 4.x (Docker)  | NoSQL database for storing player data |
| **Docker Desktop**  | Latest        | Running Cassandra container            |
| **IntelliJ IDEA**   | Latest        | Integrated development environment     |


---

##  Data Flow
1. User inputs performance data via the JavaFX UI.
2. Data is **validated** and stored in **Cassandra** (via Docker container).
3. Stored data can be retrieved and **displayed in history** or **visualized in graphs**.

---

==========================================
Sports Performance Analysis - Run Guide
==========================================

This document explains how to:
1. Start Apache Cassandra in Docker
2. Run the Sports Performance Analysis JavaFX application
3. View player data stored in Cassandra
4. View saved user credentials (if needed)

------------------------------------------
# 1️⃣ Step 1 - Start Apache Cassandra in Docker
------------------------------------------
# Make sure Docker is running, then start your Cassandra container:
docker start cassandra-container

------------------------------------------
# 2️⃣ Step 2 - Run the JavaFX Application via CMD
------------------------------------------
# Replace "path\to" with your own installation paths.
# The --module-path points to your JavaFX SDK's lib folder.
# The --add-modules flag adds the required JavaFX modules.
# The -jar flag runs the compiled application JAR.

# Syntax:
"path\to\jdk-23\bin\java.exe" --module-path "path\to\javafx-sdk-23.0.2\lib" --add-modules javafx.controls,javafx.fxml,javafx.swing -jar "path\to\SportsPerformanceAnalysis\target\SportsPerformanceAnalysis-1.0-SNAPSHOT.jar"

# Example (Windows):
"D:\jdk-23_windows-x64_bin\jdk-23.0.2\bin\java.exe" --module-path "A:\BDT\openjfx-23.0.2_windows-x64_bin-sdk\javafx-sdk-23.0.2\lib" --add-modules javafx.controls,javafx.fxml,javafx.swing -jar "A:\MPJ\SportsPerformanceAnalysis\target\SportsPerformanceAnalysis-1.0-SNAPSHOT.jar"

------------------------------------------
# 3️⃣ Step 3 - View Player Data in Cassandra
------------------------------------------
# Option 1: Connect to Cassandra specifying IP and port
docker exec -it cassandra-container cqlsh 127.0.0.1 9042

# Option 2: Connect without specifying IP/port
docker exec -it cassandra-container cqlsh

# Once inside cqlsh:
USE sports_analysis;

# View all player performance data:
SELECT * FROM player_performance;

# View specific columns:
SELECT player_id, player_name, runs_scored FROM sports_analysis.player_performance;

------------------------------------------
# 4️⃣ Step 4 - View Saved Passwords (Optional)
------------------------------------------
# Connect to Cassandra:
docker exec -it cassandra-container cqlsh 127.0.0.1 9042

# List all tables in the current keyspace:
DESCRIBE TABLES;

# View stored users table:
SELECT * FROM users;

------------------------------------------
# 🐞 Known Issues / Notes
------------------------------------------
- The application will NOT start directly from the IDE "Run" button; you must run it via CMD with the --module-path and --add-modules flags.
- The database connection will fail if the Docker container is not started before running the application.
- You must update the file paths in the run command for your local system.


  
