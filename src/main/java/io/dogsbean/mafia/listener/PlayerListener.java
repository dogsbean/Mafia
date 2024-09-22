package io.dogsbean.mafia.listener;

import io.dogsbean.mafia.Main;
import io.dogsbean.mafia.game.GameEndReason;
import io.dogsbean.mafia.role.Role;
import io.dogsbean.mafia.role.RoleManager;
import io.dogsbean.mafia.role.roles.Mafia;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        Role role = Main.getInstance().getRoleManager().getRole(player);

        if (!Main.getInstance().getGameManager().isGameInProgress()) {
            return;
        }

        if (event.getRightClicked().getType().equals(EntityType.PLAYER)) {
            Player targetPlayer = (Player) event.getRightClicked();
            if (targetPlayer != null) {
                if (!Main.getInstance().getGameManager().getPlayers().contains(targetPlayer)) {
                    return;
                }
                Role targetRole = Main.getInstance().getRoleManager().getRole(targetPlayer);
                if (targetRole != null) {
                    targetRole.setTarget(player);
                    targetRole.performAction();
                } else {
                    player.sendMessage(targetPlayer.getName() + "의 역할을 찾을 수 없습니다.");
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        if (!Main.getInstance().getGameManager().isGameInProgress()) {
            return;
        }

        Role role = Main.getInstance().getRoleManager().getRole(player);
        if (role == null) {
            return;
        }

        boolean isMafia = role instanceof Mafia;

        Main.getInstance().getGameManager().removePlayer(player);

        if (isMafia) {
            int remainingMafia = Main.getInstance().getGameManager().getRemainingPlayersByRole().getOrDefault("Mafia", 0);
            if (remainingMafia < 1) {
                Main.getInstance().getGameManager().endGame("Game Ended.", GameEndReason.MAFIA_DEFEATED);
            } else {
                Main.getInstance().getGameManager().addSpectator(player, false);
                Bukkit.broadcastMessage(ChatColor.RED + "마피아가 사망했습니다. 남은 마피아: " + remainingMafia);
            }
        } else {
            int remainingCitizens = Main.getInstance().getGameManager().getRemainingCitizens();
            if (remainingCitizens < 1) {
                Main.getInstance().getGameManager().endGame("Game Ended.", GameEndReason.CITIZENS_DEFEATED);
            } else {
                Main.getInstance().getGameManager().addSpectator(player, false);
                Bukkit.broadcastMessage(ChatColor.RED + "시민이 사망했습니다. 남은 시민: " + remainingCitizens);
            }
        }
    }

    @EventHandler
    public void onPoliceAttack(EntityDamageByEntityEvent event) {
        if (!Main.getInstance().getGameManager().isGameInProgress()) {
            return;
        }

        if (event.getEntity().getType() != EntityType.PLAYER) {
            return;
        }

        if (event.getDamager().getType() != EntityType.IRON_GOLEM) {
            return;
        }

        Player player = (Player) event.getEntity();
        IronGolem ironGolem = (IronGolem) event.getDamager();

        if (Main.getInstance().getPoliceSystem().getPolices().get(player.getUniqueId()) != ironGolem) {
            return;
        }

        if (!Main.getInstance().getPoliceSystem().getReportedPlayer().contains(player.getUniqueId())) {
            return;
        }

        if (!Main.getInstance().getPoliceSystem().getArrested().contains(player.getUniqueId())) {
            Main.getInstance().getPoliceSystem().arrestPlayer(player);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        IronGolem police = Main.getInstance().getPoliceSystem().getPolices().get(player.getUniqueId());

        if (police != null) {
            double distance = police.getLocation().distance(player.getLocation());

            if (distance <= 10) {
                police.setTarget(player);
            } else if (distance > 30) {
                police.setTarget(null);
            }
        }
    }
}
