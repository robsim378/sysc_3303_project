# L2G1_milestone_1

## Group 1 
- Liam Gaudet
- Ian Holmes
- Khalid Merai
- Andrei Popescu
- Robert Simionescu

## Files

### Scheduler.java
The Scheduler class is responsible for passing requests from the Floor to 
the Elevator, and passing the responses from the Elevator back to the Floor.
It acts as a monitor for Iteration 1.

### FloorSystem.java
The Floor system is responsible for parsing requests from a text file, 
returning them as a list of Requests, and sending the requests to the Scheduler.
The Floor system also receives responses back from the Scheduler.

### Elevator.java
The Elevator is responsible for receiving requests from the Scheduler, moving
to the appropriate floor, and then sending a response back to the Scheduler.

### RequestData.java
RequestData objects represent the individual requests parsed by the Floor
system which are passed between the Floor, Scheduler, and Elevator.

### Direction.java
The Direction enumeration specifies Elevator directions, UP or DOWN.

### Main.java
The Main file contains the main method for running the program.

## Set Up Instructions
1. Open the Eclipse IDE.
2. Go to File > Import.
3. Select General/Existing Projects into Workspace from the import wizard menu and click "Next".
4. Click the "Select archive file:" option and click "Browse" or type the filepath into the box.
5. Click "Finish".
6. Click the run button within Eclipse.

## Testing

## Contributions

#### Liam Gaudet

- Created the FloorSystem class
- Created the RequestData class
- Created the Direction Enumeration
- Generated testing data for the first iteration

#### Ian Holmes

- creating the Elevator class
- helped with functional testing
- contributed to UML class diagram
- constructed README.md

#### Khalid Merai

#### Andrei Popescu

#### Robert Simionescu