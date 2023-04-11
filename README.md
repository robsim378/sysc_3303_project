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

- ElevatorEventType.java
     - an enumeration for the Elevator event types.
   
- DoorFaultDetector.java
    - Detects faults in the elevator subsystem
    

#### /elevator_subsystem/physical_components

- Door.java
     - The static door object on the floor
     
- ElevatorButton.java
     - the elevator buttons within the elevators
     
- ElevatorLamp.java
     - the lamps in the elevator
     
- Motor.java
     - the motor on the elevator
     


#### /elevator_subsystem/states
contains all Elevator state classes
- ElevatorApproachingFloorsState: Elevator is moving and approaching a door state
- ElevatorDoorsClosedState: Elevators doors are closed state
- ElevatorDoorsClosingState: Elevators doors are closing state
- ElevatorDoorsOpeningState: Elevators doors are opening state
- ElevatorDoorsOpenState: Elevator doors open state
- ElevatorMovingState: Elevator in movement state
- ElevatorState: Base abstract state 


### ./execution
- ElevatorMain.java
    - the main method for running the elevator subsystem.
- FloorMain.java
    - the main method for running the floor subsystem.
- FullExecution.java
    - The main method for running all subsystems on the same computer
- GUIMain.java
    - The main method for running the gui subsystem
- PerformanceMain.java
    - The main method for running the performance subsystem 
- SchedulerMain.java
    - the main method for running the scheduler subsystem.

### ./scheduler_subsystem

- Scheduler.java
    - the core of the Scheduler subsystem. It maintains a state machine, and is responsible for routing the elevator, including ordering it to open/close its doors, as well as whether to stop at a floor or not. It also keeps track of all pending and in-progress requests.
- SchedulerEventType.java
    - contains an enumeration which defines the different kinds of events that the Scheduler subsystem is expected to act on.
- ElevatorTracker.java
    - manages elevators for the scheduler.
- ElevatorFaultDetector.java
    - detects faults that might occur in the elevator system
- LoadRequest.java
     - Class for storing requests on the scheduler end
     
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

- InputFileController.java
    - Reads the input file and sends its contents as events to their destinations.

- Lamps.java
    - handles functionality for floor lamps

#### ./floor_subsystem/states
- contains all the classes for the FloorSystem class's state machine. It contains two states
    - FloorIdleState: Floor is waiting for input state
    - FloorState: Abstract floor state

### ./gui_subsystem

- GuiContext.java
    - Controller for the GUI subsystem
- GuiEventType.java
    - Events types that can be sent to the GUI subsystem, enum

#### ./gui_subsystem/model
- ElevatorInformation.java
    - Information to store about the state of elevators within the system
- FloorInformation.java
    - Information to store about the state of floors within the system
- SystemModel.java
    - The central storage point of all system information for the GUI subsystem

#### ./gui_subsystem/transfer_data
- DoorStatus.java
    - Door Status to send to be received by the GUI
- ElevatorLampStatus
    - The lamp status on a specific elevator's lamp to be received by the GUi 
- FloorLampStatus
    - The lamp status on a specific floor's directional button to be received by the GUI

#### ./gui_subsystem/view
- ElevatorPanel.java
    - An extention of JPanel, a panel to be displayed representing an elevators information within the GUI
- FloorPanel.java
    - An extention of JPanel, a panel to be displayed representing a floor's information within the GUI
- GuiView.java
    - An interface to use to represent the view interactions
- SystemFrame.java
    - An extention of JFrame, a fram to display the subsystem information
- ViewCommon.java
    - Helper functions and data for the gui display data
    
### ./performance_tester
- PerformanceEventType.java
    - Event types to be handled
- PerformancePayload.java
    - Payloads to be received by the performance subsystem
- PerformanceRequestData.java
    - Format for keeping track of ongoing requests
- PerformanceTester.java
    - System for measuring the performance of the system. Measures the time taken to service a request.

## Additional Resources
- config.properties
    - Properties file containing system information for execution
- labels.properties
    - Properties file containing labels to use in the GUI
- test0, test1, ... , test10
    - test files to use for executing the system
- testing_examples
    - The file to execute when initiating the system

## Set Up Instructions
1. Open the Eclipse IDE.
2. Go to File > Import.
3. Select General/Existing Projects into Workspace from the import wizard menu and click "Next".
4. Click the "Select archive file:" option and click "Browse" or type the filepath into the box.
5. Click "Finish".
6. If you want to adjust the hosts running each procedure, navigate to resources -> config.properties and enter for each "XXX.hostname=" the host running that process. If this step is not done, all processes must be run on the same machine.
7. Copy the contents of a test file in `resources` (ex: `test6`) except the first two lines, into `testing_examples` in the resource folder.
8. In the Package Explorer view in Eclipse, navigate through LA2G1_milestone_5 -> src -> sysc_3303_project -> execution.
9. If all subsystems are running on the same computer, right click on "FullExecution.java" and select "Run As -> Java Application".
10. If the subsystems are not running on the same computer, execute the subsystems in the following order by right-clicking them and select "Run As -> Java Application": 
- ElevatorMain.java
- SchedulerMain.java
- PerformanceMain.java
- GUIMain.java
- FloorMain.java

## Testing

### Unit tests

The `test` package includes the unit tests for all classes. To run multiple unit tests in Eclipse, right-click any of the `test` subpackages (such as `test.common`) in the Package Explorer or Packages window, then select "Run As" > "JUnit Test". Some tests may take longer to run than others.

In order to run the tests in Eclipse, you need to have both JUnit and the sysc_3303_project package in your workspace. In Eclipse, the tests can be run in the following ways: For the tests to function, the project classpath needs to contain all necessary dependencies, including JUnit and the sysc_3303_project package. When right-clicking a test class file, such as SchedulerTest.java, select "Run As" followed by "JUnit Test" from the context menu. The results of the tests, including any that were incorrect or failed, will be displayed in Eclipse's JUnit view.

### Scenario tests

The `resources` folder includes test input files for various scenarios, named `test1` etc. To test any one of these input files, copy its contents into the `testing_examples` file (except the first two lines). This must be done before running the program, by default `testing_examples` does not contain valid input data.

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

Iteration 4:
- Helped design the flow for errors in the system
- Implemented the main floor subsystem code for implementing and reading injected errors

Iteration 5:
- Took part in the development of the GUI frames and panels
- Iteration planning

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

Iteration 4:
- Added fault detection and handling to Elevator subsystem
- Helped with functional testing and debugging

Iteration 5:
- Contributed to GUI implementation
- Contributed to Performance subsystem design and implementation
- Contributed to performance analysis

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

Iteration 4:
- unit testing : updated everything added in the milestone

Iteration 5:
- unit testing: updated and created new unit tests for milestone5

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
- Rewrote Scheduler tests and state tests
- Created ElevatorTracker class to manage elevators within the Scheduler Subsystem
- Updated Scheduler unit testing

Iteration 4:
- Added fault detection and handling to Scheduler subsystem
- Updated class and state diagrams
- Designed integration/acceptance test scenarios

Iteration 5:
- Added GUIContext class and messages sent to it from other subsystems
- Added configuration options for timings and implemented load times
- Updated existing tests to account for GUI- and performance-related messages.

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

Iteration 4:
- Timing diagrams

Iteration 5:
- Designed and implemented the performance testing subsystem
- Contributed to performance analysis
