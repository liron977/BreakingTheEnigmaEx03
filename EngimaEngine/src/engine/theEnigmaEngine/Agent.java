package engine.theEnigmaEngine;

import bruteForce.DecryptionInfoDTO;
import engineManager.EngineManager;
import machineDTO.ConvertedStringDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Agent implements Serializable {
    private int id;
    public Agent(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
}