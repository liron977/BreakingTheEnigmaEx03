package Controllers;

import java.util.EventObject;

//video: 100187 - Events Mechanism [JAD, JavaFX] | Powered by SpeaCode
public interface EventsHandler {
    void eventHappened (EventObject event) throws Exception;
}