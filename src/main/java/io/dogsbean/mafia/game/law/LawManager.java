package io.dogsbean.mafia.game.law;

import io.dogsbean.mafia.Main;
import io.dogsbean.mafia.game.law.actions.ArsonAction;

import java.util.ArrayList;
import java.util.List;

public class LawManager {
    private List<Law> laws;

    public LawManager() {
        laws = new ArrayList<>();
    }

    public void addLaw(Law law) {
        laws.add(law);
    }

    public List<Law> getLaws() {
        return laws;
    }
}
