/**
 * SYSC3303 Project
 * Group 1
 * @version 5.0
 */

package sysc_3303_project.execution;

import java.util.LinkedList;

import logging.Logger;
import sysc_3303_project.common.configuration.Subsystem;
import sysc_3303_project.common.events.EventBuffer;
import sysc_3303_project.common.messaging.UDPMessagerIncoming;
import sysc_3303_project.gui_subsystem.GuiContext;
import sysc_3303_project.gui_subsystem.GuiEventType;
import sysc_3303_project.gui_subsystem.view.SystemFrame;

/**
 * Main method for GUi subsystem
 * @author Liam
 *
 */
public class GUIMain {
    @SuppressWarnings("serial")
	public static void main(String[] args) {
    	Logger.getLogger().logNotification(GUIMain.class.getSimpleName(), "GUI Subsystem starting");
    	
    	// Initializing the UI and model
		EventBuffer<GuiEventType> guiBuffer = new EventBuffer<>();
    	GuiContext model = new GuiContext(guiBuffer);
        SystemFrame sysFrame = new SystemFrame(model);
        sysFrame.buildSystemFrame();
        sysFrame.setVisible(true);
        Thread modelThread = new Thread(model);
        modelThread.start();

        //Incoming buffer
        UDPMessagerIncoming<GuiEventType> in = new UDPMessagerIncoming<GuiEventType>(new LinkedList<>() {{add(guiBuffer);}}, Subsystem.GUI);
        Thread guiIncoming = new Thread(in);
        guiIncoming.start();
    }

}
