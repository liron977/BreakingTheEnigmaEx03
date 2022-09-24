package engine.theEnigmaEngine;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Agents implements Serializable {
    List<Agent> agents;
    private int amountOfAgents;
    public Agents(int numberOfAgents) {
        this.amountOfAgents = numberOfAgents;
        agents = new ArrayList<>();
    }
    public int getAmountOfAgents(){
        return amountOfAgents;
    }
}