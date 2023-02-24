# L2G1_milestone_2

## Group 1 
- Liam Gaudet
- Ian Holmes
- Khalid Merai
- Andrei Popescu
- Robert Simionescu

## Files

All files can be found within the GitHub repository, located at:
https://github.com/robsim378/sysc_3303_project

### logging

#### Logger.java

Provides basic logging functionality to make messages consistent.

### ./common

#### DelayTimerThread.java
The DelayTimerThread is used to create threads to pass events to a buffer after a given delay.

#### Direction.java
The Direction enumeration specifies Elevator directions, UP or DOWN.

#### Event.java
The Event class represents a simple event object to be used in event buffers. Provides ways to get the event type, sender and payload of the event.

#### EventBuffer.java
The EventBuffer class provides a way to queue up events of a given type for a subsystem to respond to. This allows threads to synchronize correctly.

#### RequestData.java
RequestData objects represent the individual requests parsed by the Floor
system which are passed between the Floor, Scheduler, and Elevator.

#### State.java
The State interface is implemented by all state classes for each subsystem. It provides only methods to implement entry/exit actions.

### ./elevator_subsystem

- contains all Elevator Subsystem related files.

#### /elevator_subsystem/states

   - contains all Elevator state classes

#### Elevator.java

  - The Elevator is responsible for receiving requests from the Scheduler, moving
    to the appropriate floor, and then sending a response back to the Scheduler.
  
  #### ElevatorEventType.java

  - an enumeration for the Elevator event types.

### ./scheduler_subsystem

This package contains the classes for the Scheduler subsystem.

#### Scheduler.java

The Scheduler class is the core of the Scheduler subsystem. It maintains a state machine, and is responsible for routing the elevator, including ordering it to open/close its doors, as well as whether to stop at a floor or not. It also keeps track of all pending and in-progress requests.


#### ./scheduler_subsystem/states

This package contains all the classes for the Scheduler class's state machine.

#### SchedulerEventType.java

This file contains an enumeration which defines the different kinds of events that the Scheduler subsystem is expected to act on.

### ./floor_subsystem

- contains all Floor Subsystem related files.

#### ./scheduler_subsystem/states

This package contains all the classes for the FloorSystem class's state machine. It contains two states
- FloorState: An abstract state used for any floor states
- FloorIdleState: The only state the floor system takes part in, handles IO operations.

#### FloorSystem.java
The Floor system is responsible for parsing requests from a text file,
returning them as a list of Requests, and sending the requests to the Scheduler.
The Floor system also receives responses back from the Scheduler.

#### FloorEventType
An enum class for determining request types for the FloorSystem to process

### Main.java
The Main file contains the main method for running the program.

## Set Up Instructions
1. Open the Eclipse IDE.
2. Go to File > Import.
3. Select General/Existing Projects into Workspace from the import wizard menu and click "Next".
4. Click the "Select archive file:" option and click "Browse" or type the filepath into the box.
5. Click "Finish".
6. In the Package Explorer view in Eclipse, navigate through LA2G1_milestone_2 -> src -> sysc_3303_project.
7. Right click on "Main.java" and select "Run As" -> "Java Application".

## Testing
 SchedulerTest.java: The package sysc_3303_project contains the tests for the Scheduler class that make up Testing. Methods like hasRequests, addRequest, hasResponses, and addResponse are examined for correctness. The tests produce data and compare actual results to expected results.

 RequestDataTest.java: contains the tests for the RequestData class in the sysc_3303_project package. It tests methods like getRequestTime, getCurrentFloor, getDirection, and getDestinationFloor to make sure they work correctly. The tests produce data and compare actual results to expected results.
 
EventBufferTest: Is a generic class that provides a buffer for storing and retrieving Event objects. The tests cover the basic functionality of the EventBuffer, including adding and retrieving events, as well as handling cases where the buffer is empty.

DelayTimerThreadTest:The test checks whether the run() method of the DelayTimerThread class correctly adds an event to an event buffer after a specified delay. The test creates an event buffer, an event, and a DelayTimerThread instance with a 100-millisecond delay. It then starts the thread and waits for it to complete, and checks that the event was correctly added to the buffer after the delay. This test ensures that the delay timer thread works as expected and adds events to the buffer after the specified delay.

 In order to run the tests in Eclipse, you need to have both JUnit and the sysc_3303_project package in your workspace. In Eclipse, the tests can be run in the following ways: For the tests to function, the project classpath needs to contain all necessary dependencies, including JUnit and the sysc_3303_project package. When right-clicking a test class file, such as SchedulerTest.java, select "Run As" followed by "JUnit Test" from the context menu. The results of the tests, including any that were incorrect or failed, will be displayed in Eclipse's JUnit view.

## Contributions

#### Liam Gaudet

Iteration 1:
- Created the FloorSystem class
- Created the RequestData class
- Created the Direction Enumeration
- Generated testing data for the first iteration

Iteration 2:
- Generated the Sequence Diagram
- Created and implemented the FloorSystem state machine
- Participated in the design of program architecture 

#### Ian Holmes

- Designed and implemented Elevator state machine
- Helped with functional testing
- Contributed to UML class diagram

#### Khalid Merai
Iteration 1:
 - created the JUnit test classes for the Scheduler,requestData and Elevator
 - The testing section of the README.md 
 
 Iteration 2: 
 - Adjusted the test classes for the Scheduler,requestData and Elevator
 - Created two new junit test cases which are EventBufferTest and DelayTimerThreadTest
 - Helped in designing the state machine diagram of the elevator

#### Andrei Popescu

Iteration 1:
- created the Scheduler class and relevant parts of the UML class diagram
- created UML sequence diagram
- wrote Javadocs for RequestData

Iteration 2:
- created the new Scheduler class and state machine
- participated in the design of program architecture
- created the Logger class
- created UML state diagrams

#### Robert Simionescu
- Designed and implemented Elevator state machine
- Added Javadocs
