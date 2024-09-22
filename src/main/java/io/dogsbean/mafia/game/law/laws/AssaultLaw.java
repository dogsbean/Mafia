package io.dogsbean.mafia.game.law.laws;

import io.dogsbean.mafia.game.law.Law;
import io.dogsbean.mafia.game.law.actions.ArsonAction;
import io.dogsbean.mafia.game.law.actions.AssaultAction;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class AssaultLaw extends Law {
    public AssaultLaw() {
        super("폭행", new AssaultAction());
    }
}
