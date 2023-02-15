package sysc_3303_project;

public class ElevatorMovingState extends ElevatorState {

    private boolean moving;
    private Direction direction;

    @Override
    public ElevatorState stopAtNextFloor() {
        return null;
    }

    @Override
    public ElevatorState doNotStopAtNextFloor() {
        return null;
    }
}
