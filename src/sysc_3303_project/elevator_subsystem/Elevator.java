/**
 * SYSC3303 Project
 * Group 1
 * @version 1.0
*/

package sysc_3303_project.elevator_subsystem;

import logging.Logger;
import sysc_3303_project.common.Direction;
import sysc_3303_project.common.Event;
import sysc_3303_project.common.EventBuffer;
import sysc_3303_project.elevator_subsystem.states.ElevatorDoorsOpenState;
import sysc_3303_project.elevator_subsystem.states.ElevatorState;
import sysc_3303_project.scheduler_subsystem.SchedulerEventType;

/**
 * @author Ian Holmes & Robert Simionescu
 * Represents an Elevator to move between Floors.
 * Context class for the elevator state machine.
 */
public class Elevator implements Runnable {

    private final EventBuffer<SchedulerEventType> schedulerBuffer;
    private final int elevatorID;
    private int elevatorFloor;
    private Direction direction;
    private ElevatorState state;
    private int destinationFloor;
    private EventBuffer<ElevatorEventType> eventBuffer;


    /**
     * Constructor for the Elevator class.
     *
     * @param schedulerBuffer EventBuffer, the scheduler to receive requests from
     * @param elevatorID int, the ID of the Elevator
     */
    public Elevator(EventBuffer<SchedulerEventType> schedulerBuffer, EventBuffer<ElevatorEventType> eventBuffer, int elevatorID) {
        this.schedulerBuffer = schedulerBuffer;
        this.elevatorID = elevatorID;
        this.elevatorFloor = 0;
        state = new ElevatorDoorsOpenState(this);
        this.eventBuffer = eventBuffer;
    }

    public EventBuffer<ElevatorEventType> getEventBuffer() {
        return eventBuffer;
    }

    /**
     * Getter for the elevatorID
     * @return int, the ID number of this elevator.
     */
    public int getElevatorID() {
        return elevatorID;
    }

    public EventBuffer<SchedulerEventType> getSchedulerBuffer() {
        return schedulerBuffer;
    }

    /**
     * Getter for the elevator's current floor
     * @return int, the current floor the elevator is on.
     */
    public int getFloor() {
        return elevatorFloor;
    }

    /**
     * Setter for the direction
     * @param direction Direction, the direction to move the elevator.
     */
    public void setDirection(Direction direction) {
        this.direction = direction;
    }
    
    public Direction getDirection() {
    	return direction;
    }

    /**
     * Move the elevator one floor towards its destination
     */
    public void moveElevator() {    // TODO: This should maybe be moved into ElevatorMovingState, but I'm not sure.
        int resultFloor;
        if (direction == Direction.UP) {
            resultFloor = elevatorFloor + 1;
        }
        else {
            resultFloor = elevatorFloor - 1;
        }
        Logger.getLogger().logNotification(this.getClass().getName(), "Elevator " + elevatorID + " moving from floor " + elevatorFloor + " to floor " + resultFloor);
        elevatorFloor = resultFloor;

        // Sleep for however long
    }


    /**
     * The run method for the Elevator thread.
     * Contains the state machine for the Elevator.
     */
    public void run() {
        System.out.println("Elevator thread started");
        Event<ElevatorEventType> event;
        

        while (true) {

            event = eventBuffer.getEvent();

    		Logger.getLogger().logNotification(this.getClass().getName(), "Event: " + event.getEventType() + ", State: " + state.getClass().getName());    		

            state.doExit();

            switch (event.getEventType()) {
                case OPEN_DOORS:
                    state = state.openDoors();
                    break;
                case OPEN_DOORS_TIMER:
                    state = state.openDoorsTimer();
                    break;
                case CLOSE_DOORS:
                    state = state.closeDoors();
                    break;
                case CLOSE_DOORS_TIMER:
                    state = state.closeDoorsTimer();
                    break;
                case START_MOVING_IN_DIRECTION:
                    state = state.setDirection((Direction) event.getPayload());
                    break;
                case MOVING_TIMER:
                    state = state.travelThroughFloorsTimer();
                    break;
                case CONTINUE_MOVING:
                    state = state.continueMoving();
                    break;
                case STOP_AT_NEXT_FLOOR:
                    state = state.stopAtNextFloor();
                    break;
            }
            state.doEntry();
        }
    }
}