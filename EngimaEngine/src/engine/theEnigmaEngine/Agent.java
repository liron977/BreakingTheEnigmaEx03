package engine.theEnigmaEngine;

import java.io.Serializable;

public class Agent implements Serializable {
    private int id;
    public Agent(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
}