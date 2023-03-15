/**
 * SYSC3303 Project
 * Group 1
 * @version 3.0
*/

package sysc_3303_project.elevator_subsystem;

import logging.Logger;
import sysc_3303_project.common.Direction;
import sysc_3303_project.common.configuration.ResourceManager;
import sysc_3303_project.common.configuration.Subsystem;
import sysc_3303_project.common.events.Event;
import sysc_3303_project.common.events.EventBuffer;
import sysc_3303_project.elevator_subsystem.physical_components.Door;
import sysc_3303_project.elevator_subsystem.physical_components.ElevatorButton;
import sysc_3303_project.elevator_subsystem.physical_components.ElevatorLamp;
import sysc_3303_project.elevator_subsystem.physical_components.Motor;
import sysc_3303_project.elevator_subsystem.states.ElevatorDoorsOpenState;
import sysc_3303_project.elevator_subsystem.states.ElevatorState;
import sysc_3303_project.floor_subsystem.FloorEventType;
import sysc_3303_project.floor_subsystem.Lamps;

import java.util.Arrays;

/**
 * Represents an Elevator to move between Floors.
 * Context class for the elevator state machine.
 *
 * @author Ian Holmes & Robert Simionescu
 */
public class Elevator implements Runnable {

    private final EventBuffer<Enum<?>> outputBuffer;
    private final int elevatorID;
    private int elevatorFloor;
    private Direction direction;
    private ElevatorState state;
    private final EventBuffer<ElevatorEventType> inputBuffer;
    private final ElevatorLamp[] buttonLamps;
    private final ElevatorButton[] buttons;
    private final Motor motor;
    private final Door door;
    private final Lamps directionLamps;


    /**
     * Constructor for the Elevator class.
     *
     * @param outputBuffer EventBuffer, the scheduler to receive requests from
     * @param elevatorID int, the ID of the Elevator
     */
    public Elevator(EventBuffer<Enum<?>> outputBuffer, EventBuffer<ElevatorEventType> inputBuffer, int elevatorID) {
        this.outputBuffer = outputBuffer;
        this.elevatorID = elevatorID;
        this.elevatorFloor = 0;
        state = new ElevatorDoorsOpenState(this);
        this.inputBuffer = inputBuffer;
        this.buttonLamps = new ElevatorLamp[ResourceManager.get().getInt("count.floors")];
        this.buttons = new ElevatorButton[ResourceManager.get().getInt("count.floors")];
        createLamps();
        createButtons();
        this.motor = new Motor();
        this.door = new Door();
        this.directionLamps = new Lamps();
    }

    /**
     * Create the button lamps.
     */
    private void createLamps() {
        for (int i = 0; i < ResourceManager.get().getInt("count.floors"); i++) {
            buttonLamps[i] = new ElevatorLamp(i);
        }
    }

    /**
     * Create the buttons.
     */
    private void createButtons() {
        for (int i = 0; i < ResourceManager.get().getInt("count.floors"); i++) {
            buttons[i] = new ElevatorButton(i);
        }
    }

    /**
     * Getter for the elevator door.
     *
     * @return Door, the door
     */
    public Door getDoor() {
        return door;
    }

    /**
     * Getter for the elevator lamps.
     *
     * @return ElevatorButtons[], the lamps
     */
    public ElevatorLamp[] getButtonLamps() {
        return buttonLamps;
    }

    /**
     * Getter for the elevator buttons.
     *
     * @return ElevatorButtons[], the buttons
     */
    public ElevatorButton[] getButtons() {
        return buttons;
    }

    /**
     * Getter for the elevator motor.
     *
     * @return Motor, the motor
     */
    public Motor getMotor() {
        return motor;
    }

    /**
     * Getter for the elevator event buffer.
     *
     * @return int, the ID number of this elevator.
     */
    public EventBuffer<ElevatorEventType> getInputBuffer() {
        return inputBuffer;
    }

    /**
     * Getter for the elevatorID.
     *
     * @return int, the ID number of this elevator.
     */
    public int getElevatorID() {
        return elevatorID;
    }

    /**
     * Getter for the scheduler event buffer.
     *
     * @return int, the ID number of this elevator
     */
    public EventBuffer<Enum<?>> getOutputBuffer() {
        return outputBuffer;
    }

    /**
     * Getter for the elevator's current floor.
     *
     * @return int, the current floor the elevator is on
     */
    public int getFloor() {
        return elevatorFloor;
    }

    /**
     * Setter for the direction.
     *
     * @param direction Direction, the direction to move the elevator
     */
    public void setDirection(Direction direction) {
        directionLamps.lightDirectionalLamp(direction);
        this.direction = direction;
        
        outputBuffer.addEvent(new Event<Enum<?>>(Subsystem.FLOOR, -1, Subsystem.ELEVATOR, elevatorID, FloorEventType.UPDATE_ELEVATOR_DIRECTION, direction));
    }
    
    public Direction getDirection() {
        return direction;
    }

    public void turnOffLamp(int lampNumber) {
        this.buttonLamps[lampNumber].turnOff();
    }

    /**
     * Move the elevator one floor towards its destination.
     */
    public void moveElevator() {
        int resultFloor;
        if (direction == Direction.UP) {
            resultFloor = elevatorFloor + 1;
        }
        else {
            resultFloor = elevatorFloor - 1;
        }
        Logger.getLogger().logNotification(this.getClass().getSimpleName(), "Elevator " + elevatorID + " moving from floor " + elevatorFloor + " to floor " + resultFloor);
        elevatorFloor = resultFloor;
    }


    /**
     * The run method for the Elevator thread.
     * Contains the state machine for the Elevator.
     */
    public void run() {
        Logger.getLogger().logNotification(this.getClass().getSimpleName(),"Elevator " + elevatorID + " running.");
        Event<ElevatorEventType> event;

        while (true) {
            event = inputBuffer.getEvent();

            if (event.getPayload() instanceof Integer) {    // this should be moved to a better location
                int lampNumber = (int) event.getPayload();  // I'm sorry Liam :(
                buttonLamps[lampNumber].turnOn();
            }

            ElevatorState newState = null;

            Logger.getLogger().logDebug(this.getClass().getSimpleName(),"Elevator " + elevatorID
                    + " Lamps: " + Arrays.toString(buttonLamps));

            Logger.getLogger().logNotification(this.getClass().getSimpleName(), "Event: " + event.getEventType()
                    + ", State: " + state.getClass().getName());

            switch (event.getEventType()) {
                case OPEN_DOORS -> newState = state.openDoors();
                case OPEN_DOORS_TIMER -> newState = state.openDoorsTimer();
                case CLOSE_DOORS -> newState = state.closeDoors();
                case CLOSE_DOORS_TIMER -> newState = state.closeDoorsTimer();
                case START_MOVING_IN_DIRECTION -> newState = state.setDirection((Direction) event.getPayload());
                case MOVING_TIMER -> newState = state.travelThroughFloorsTimer();
                case CONTINUE_MOVING -> newState = state.continueMoving();
                case STOP_AT_NEXT_FLOOR -> newState = state.stopAtNextFloor();
                case PASSENGERS_UNLOADED -> newState = state.handlePassengersUnloaded();
                case ELEVATOR_BUTTON_PRESSED -> newState = state.handleElevatorButtonPressed((int) event.getPayload());
            }

            if (newState != null) {
                state.doExit();
                state = newState;
                state.doEntry();
            }
        }
    }
}
