package sysc_3303_project;

public abstract class ElevatorState {

    public ElevatorState closeDoors() {
        throw new IllegalStateException("closeDoors() must be called from the ElevatorDoorsOpenState.");
    }

    public ElevatorState openDoors() {
        throw new IllegalStateException("openDoors() must be called from the ElevatorDoorsClosedState.");
    }

    public ElevatorState setDirection() {
        throw new IllegalStateException("setDirection() must be called from the ElevatorDoorsClosedState.");
    }

    public ElevatorState stopAtNextFloor() {
        throw new IllegalStateException("stopAtNextFloor() must be called from the ElevatorMovingState.");
    }

    public ElevatorState doNotStopAtNextFloor() {
        throw new IllegalStateException("doNotStopAtNextFloor() must be called from the ElevatorMovingState.");
    }


}
