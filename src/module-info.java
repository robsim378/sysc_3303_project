/**
 * 
 */
/**
 * @author apope
 *
 */
module sysc_3303_project {
	exports sysc_3303_project;
    exports sysc_3303_project.elevator_subsystem; //for reflection in unit tests
    exports test.common;
    exports test.elevator_subsystem;
    exports test.floor_subsystem;
    exports test.floor_subsystem.states;
    exports test.scheduler_subsystem;
    requires junit;
	requires org.junit.jupiter.api;
}