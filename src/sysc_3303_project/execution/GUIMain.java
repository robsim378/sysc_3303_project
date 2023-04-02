package sysc_3303_project.execution;

import java.util.LinkedList;

import logging.Logger;
import sysc_3303_project.common.configuration.Subsystem;
import sysc_3303_project.common.events.EventBuffer;
import sysc_3303_project.common.messaging.UDPMessagerIncoming;
import sysc_3303_project.ui_subsystem.GuiEventType;
import sysc_3303_project.ui_subsystem.GuiModel;
import sysc_3303_project.ui_subsystem.view.SystemFrame;

public class GUIMain {
    public static void main(String[] args) {
    	Logger.getLogger().logNotification(GUIMain.class.getSimpleName(), "GUI Subsystem starting");
    	
    	// Initializing the UI and model
		EventBuffer<GuiEventType> guiBuffer = new EventBuffer<>();
    	GuiModel model = new GuiModel(guiBuffer, null);
        new SystemFrame(model);
        Thread modelThread = new Thread(model);
        modelThread.start();

        //Incoming buffer
        UDPMessagerIncoming<GuiEventType> in = new UDPMessagerIncoming<GuiEventType>(new LinkedList<>() {{add(guiBuffer);}}, Subsystem.GUI);
        Thread guiIncoming = new Thread(in);
        guiIncoming.start();
    }

}
