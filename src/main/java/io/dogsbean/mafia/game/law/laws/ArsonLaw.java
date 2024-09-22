package io.dogsbean.mafia.game.law.laws;

import io.dogsbean.mafia.game.law.Law;
import io.dogsbean.mafia.game.law.NPCAction;
import io.dogsbean.mafia.game.law.actions.ArsonAction;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ArsonLaw extends Law {
    public ArsonLaw() {
        super("방화", new ArsonAction());
    }
}
