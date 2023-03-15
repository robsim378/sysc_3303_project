# L2G1_milestone_3

## Group 1 
- Liam Gaudet
- Ian Holmes
- Khalid Merai
- Andrei Popescu
- Robert Simionescu

## Files

All files can be found within the GitHub repository, located at:
https://github.com/robsim378/sysc_3303_project

## test

The test package and its subpackages contain unit tests for all classes. State machines are tested in the class using them (for example, the tests for the Scheduler subsystem's state machine are included in the file `SchedulerTest.java`).

In order to run the tests in Eclipse, you need to have both JUnit and the sysc_3303_project package in your workspace. In Eclipse, the tests can be run in the following ways: For the tests to function, the project classpath needs to contain all necessary dependencies, including JUnit and the sysc_3303_project package. When right-clicking a test class file, such as SchedulerTest.java, select "Run As" followed by "JUnit Test" from the context menu. The results of the tests, including any that were incorrect or failed, will be displayed in Eclipse's JUnit view.

## logging

- Logger.java 
    - Provides basic logging functionality to make messages consistent.

## sysc_3303_project

### ./common
- Direction
    - enumeration to specify Elevator directions, UP or DOWN.

### ./common/configuration

- ResourceManager.java
    - manages system constants/values
- Subsystem.java
    - enumeration for the type of subsystem
- SystemProperties.java (DEPRECATED)
    - the config values for the system

### ./common/events

- DelayTimerThread.java
    - used to create threads to pass events to a buffer after a given delay.
- Event.java
    - represents a simple event object to be used in event buffers. Provides ways to get the event type, sender and payload of the event.
- EventBuffer.java
    - provides a way to queue up events of a given type for a subsystem to respond to. This allows threads to synchronize correctly.
- RequestData.java
    - represent the individual requests parsed by the Floor
    system which are passed between the Floor, Scheduler, and Elevator.

### ./common/messaging

- UDPMessager.java
    - abstract class containing common methods for the messaging
- UDPMessagerIncoming.java
    - handles incoming UDP connections
- UDPMessagerOutgoing.java
    - handles outgoing UDP connections

### ./common/state

- State.java
    - interface is implemented by all state classes for each subsystem. It provides only methods to implement entry/exit actions.


### ./elevator_subsystem

- Elevator.java
    - The Elevator is responsible for receiving requests from the Scheduler, moving
      to the appropriate floor, and then sending a response back to the Scheduler.

- Door.java
  - the elevator door.
- Motor.java
  - the elevator motor.
- ElevatorLamp.java
  - lamp class for the elevator buttons.
- ElevatorButton.java
  - the elevator buttons.

- ElevatorEventType.java
     - an enumeration for the Elevator event types.
   
- ElevatorMain.java
    - the main method for running the subsystem.

#### /elevator_subsystem/states
- contains all Elevator state classes
    - ElevatorApproachingFloorsState: Elevator is moving and approaching a door state
    - ElevatorDoorsClosedState: Elevators doors are closed state
    - ElevatorDoorsClosingState: Elevators doors are closing state
    - ElevatorDoorsOpeningState: Elevators doors are opening state
    - ElevatorDoorsOpenState: Elevator doors open state
    - ElevatorMovingState: Elevator in movement state
    - ElevatorState: Base abstract state 


### ./scheduler_subsystem


- Scheduler.java

    - the core of the Scheduler subsystem. It maintains a state machine, and is responsible for routing the elevator, including ordering it to open/close its doors, as well as whether to stop at a floor or not. It also keeps track of all pending and in-progress requests.

- SchedulerEventType.java
    - contains an enumeration which defines the different kinds of events that the Scheduler subsystem is expected to act on.

- SchedulerMain.java
    - the main method for running the subsystem.
- ElevatorTracker.java
    - manages elevators for the scheduler.

#### ./scheduler_subsystem/states

- contains all the classes for the Scheduler class's state machine.
    - SchedulerProcessingState: Schedule is processing data state
    - SchedulerWaitingState: Scheduler is waiting for input state
    - SchedulerState: Abstract scheduler state



### ./floor_subsystem

- FloorSystem.java
    - responsible for parsing requests from a text file,
    - returning them as a list of Requests,
    - and sending the requests to the Scheduler.

- FloorEventType.java
    - enum class for determining request types for the FloorSystem to process

- FloorMain.java
    - the main method for running the subsystem.

- InputFileController.java
    - Reads the input file and sends its contents as events to their destinations.

- Lamps.java
    - handles functionality for floor lamps

#### ./floor_subsystem/states
- contains all the classes for the FloorSystem class's state machine. It contains two states
    - FloorIdleState: Floor is waiting for input state
    - FloorState: Abstract floor state


## Set Up Instructions
1. Open the Eclipse IDE.
2. Go to File > Import.
3. Select General/Existing Projects into Workspace from the import wizard menu and click "Next".
4. Click the "Select archive file:" option and click "Browse" or type the filepath into the box.
5. Click "Finish".
6. If you want to adjust the hosts running each procedure, navigate to resources -> config.properties and enter for each "XXX.hostname=" the host running that process. If this step is not done, all processes must be run on the same machine.
7. In the Package Explorer view in Eclipse, navigate through LA2G1_milestone_3 -> src -> sysc_3303_project -> scheduler_subsystem.
8. Right click on "SchedulerMain.java" and select "Run As" -> "Java Application".
9. In the Package Explorer view in Eclipse, navigate through LA2G1_milestone_3 -> src -> sysc_3303_project -> elevator_subsystem.
10. Right click on "ElevatorMain.java" and select "Run As" -> "Java Application".
11. In the Package Explorer view in Eclipse, navigate through LA2G1_milestone_3 -> src -> sysc_3303_project -> floor_subsystem.
12. Right click on "FloorMain.java" and select "Run As" -> "Java Application".

## Testing
The `test` package includes the unit tests for all classes. To run multiple unit tests in Eclipse, right-click any of the `test` subpackages (such as `test.common`) in the Package Explorer or Packages window, then select "Run As" > "JUnit Test". Some tests may take longer to run than others.

In order to run the tests in Eclipse, you need to have both JUnit and the sysc_3303_project package in your workspace. In Eclipse, the tests can be run in the following ways: For the tests to function, the project classpath needs to contain all necessary dependencies, including JUnit and the sysc_3303_project package. When right-clicking a test class file, such as SchedulerTest.java, select "Run As" followed by "JUnit Test" from the context menu. The results of the tests, including any that were incorrect or failed, will be displayed in Eclipse's JUnit view.

## Contributions

### Liam Gaudet

Iteration 1:
- Created the FloorSystem class
- Created the RequestData class
- Created the Direction Enumeration
- Generated testing data for the first iteration

Iteration 2:
- Generated the Sequence Diagram
- Created and implemented the FloorSystem state machine
- Participated in the design of program architecture 
- Created unit tests for Elevator states

Iteration 3:
- Designed and implemented UDP Messaging System
- Helped with functional testing
- Developed test case files


### Ian Holmes

Iteration 1:
- Created the Elevator class
- Helped with functional testing
- Contributed to UML class diagram
- Constructed README.md

Iteration 2:
- Designed and implemented Elevator state machine
- Created unit tests for Elevator and Scheduler states
- Helped with functional testing
- Contributed to UML class diagram

Iteration 3:
- Updated Elevator subsystem (updated event sending, added new event actions, implemented elevator lamps)
- Helped with functional testing
- Updated Elevator States unit tests
- Updated sequence and class UML diagrams

### Khalid Merai
Iteration 1:
 - created the JUnit test classes for the Scheduler,requestData and Elevator
 - The testing section of the README.md 
 
 Iteration 2: 
 - Adjusted the test classes for the Scheduler,requestData and Elevator
 - Created two new junit test cases which are EventBufferTest and DelayTimerThreadTest
 - Helped in designing the state machine diagram of the elevator

Iteration 3:
- unit testing : maintained the test classes after changes made for iteration3
- created junit test classes for the messager package (incoming and outgoing)

### Andrei Popescu

Iteration 1:
- created the Scheduler class and relevant parts of the UML class diagram
- created UML sequence diagram
- wrote Javadocs for RequestData

Iteration 2:
- created the new Scheduler class and state machine
- participated in the design of program architecture
- created the Logger class
- created UML state diagrams

Iteration 3:
- Redesigned Scheduler subsystem to use UDP messaging
- Created ElevatorTracker class to manage elevators within the Scheduler Subsystem
- Updated Scheduler unit testing

### Robert Simionescu

Iteration 1:
- Created the Main file
- Added Javadocs to unit tests

Iteration 2:
- Designed and implemented Elevator state machine
- Added Javadocs

Iteration 3:
- Updated Floor subsystem (updated event sending, implemented floor lamps)
- Helped with functional testing
- Updated Floor unit testing
- Separated input parsing into InputFileController class

## Known Errors
- If two requests are made at a floor in the same direction, two elevators are dispatched instead of one. Small fix, but out of time.