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
SchedulerTest.java: The package sysc_3303_project contains the tests for the Scheduler class that make up Testing. Methods like hasRequests, addRequest, hasResponses, and addResponse are examined for correctness. The tests produce data and compare actual results to expected results.

RequestDataTest.java: contains the tests for the RequestData class in the sysc_3303_project package. It tests methods like getRequestTime, getCurrentFloor, getDirection, and getDestinationFloor to make sure they work correctly. The tests produce data and compare actual results to expected results.

In order to run the tests in Eclipse, you need to have both JUnit and the sysc_3303_project package in your workspace. In Eclipse, the tests can be run in the following ways: For the tests to function, the project classpath needs to contain all necessary dependencies, including JUnit and the sysc_3303_project package. When right-clicking a test class file, such as SchedulerTest.java, select "Run As" followed by "JUnit Test" from the context menu. The results of the tests, including any that were incorrect or failed, will be displayed in Eclipse's JUnit view.

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
-created the JUnit test classes for the Scheduler,requestData and Elevator
- The testing section of the README.md 

#### Andrei Popescu

- created the Scheduler class and relevant parts of the UML class diagram
- created UML sequence diagram
- wrote Javadocs for RequestData

#### Robert Simionescu
