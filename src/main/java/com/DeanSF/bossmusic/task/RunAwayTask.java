package com.DeanSF.bossmusic.task;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.DeanSF.bossmusic.battle.Battle;
import com.DeanSF.bossmusic.battle.BattleManager;
public class RunAwayTask extends BukkitRunnable {

    private Battle battle;

    public RunAwayTask(Battle battle){
        this.battle = battle;
    }

    @Override
    public void run() {
        Entity entity = battle.getEntity();
        Player player = battle.getPlayer();
        if(player.getLocation().distance(entity.getLocation()) > 50)
            BattleManager.stopBattle(battle.getPlayer());
        else{
            this.battle.resetRunAwayTask();
        }
    }
}
