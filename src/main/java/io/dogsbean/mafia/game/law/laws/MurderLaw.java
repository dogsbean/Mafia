package io.dogsbean.mafia.game.law.laws;

import io.dogsbean.mafia.game.law.Law;
import io.dogsbean.mafia.game.law.actions.AssaultAction;
import io.dogsbean.mafia.game.law.actions.MurderAction;

public class MurderLaw extends Law {
    public MurderLaw() {
        super("살인", new MurderAction());
    }
}
