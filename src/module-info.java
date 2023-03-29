/**
 * @author apope
 *
 */
module sysc_3303_project {
    exports sysc_3303_project.elevator_subsystem.physical_components; 
    exports sysc_3303_project.floor_subsystem; 
    exports sysc_3303_project.floor_subsystem.states; 
    exports sysc_3303_project.scheduler_subsystem; 
    exports sysc_3303_project.common;
    exports sysc_3303_project.common.configuration;
    exports sysc_3303_project.common.events;
    exports test.messaging;
    exports test.common;
    exports test.scheduler_subsystem;
    exports test.elevator_subsystem;
    requires junit;
    requires org.junit.jupiter.api;
}