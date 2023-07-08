package com.DeanSF.bossmusic.listener;

import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.GameMode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.DeanSF.bossmusic.BossMusic;
import com.DeanSF.bossmusic.battle.Battle;
import com.DeanSF.bossmusic.battle.BattleManager;

public class PlayerListener implements Listener {

    private static HashMap<String, HashSet<String>> mobList;

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        if(!(event.getDamager() instanceof LivingEntity && event.getEntity() instanceof LivingEntity)) {
            return;
        }
        LivingEntity damagedEntity = (LivingEntity) event.getEntity();
        LivingEntity damagerEntity = (LivingEntity) event.getDamager();
        String damagedType = damagedEntity.getType().toString().toLowerCase();
        String damagedCustomName = damagedEntity.getCustomName();

        if (!(mobList.containsKey(damagedType) && mobList.get(damagedType).contains(damagedCustomName))) {
            return;
        }

        Boolean ignorePvp = BossMusic.getBossMusicInstance().getConfig().getBoolean("ignore-playervsplayer");
        Boolean ignoreCreative = BossMusic.getBossMusicInstance().getConfig().getBoolean("ignore-creative");
        if(damagedEntity.getHealth()-event.getDamage() <= 0 || 
           !(damagedEntity instanceof Player || damagerEntity instanceof Player)) {
            return;
        }
        Player player = (Player) (damagedEntity instanceof Player ? damagedEntity : damagerEntity);
        Entity entity = damagedEntity instanceof Player ? damagerEntity : damagedEntity;
        if((entity instanceof Player) && ignorePvp ||
           ignoreCreative && player.getGameMode() == GameMode.CREATIVE) {
            return;
        }
        if (BattleManager.isPlayerFighting(player)) {
            BattleManager.getBattle(player).resetRunAwayTask();
        } else
            BattleManager.createBattle(player, entity);
        
    }

    @EventHandler
    public void onTargetChange(EntityTargetLivingEntityEvent event) {
        if (!(event.getEntity() instanceof LivingEntity && event.getEntity() instanceof Monster) ||
                !(event.getTarget() instanceof Player)) {
            return;
        }
        String damagedType = event.getEntity().getType().toString().toLowerCase();
        String damagedCustomName = event.getEntity().getCustomName();
        if (!(mobList.containsKey(damagedType) && mobList.get(damagedType).contains(damagedCustomName)))
            return;
        Player player = (Player) event.getTarget();
        Entity entity = event.getEntity();
        Boolean ignoreCreative = BossMusic.getBossMusicInstance().getConfig().getBoolean("ignore-creative");
        if (ignoreCreative && player.getGameMode() == GameMode.CREATIVE) {
            return;
        }
        if (!BattleManager.isPlayerFighting(player))
            BattleManager.createBattle(player, entity);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (BattleManager.isPlayerFighting(event.getPlayer()))
            BattleManager.stopBattle(event.getPlayer());
    }

    @EventHandler
    public void onGameModeChange(PlayerGameModeChangeEvent event) {
        if (BattleManager.isPlayerFighting(event.getPlayer()) && event.getNewGameMode() == GameMode.CREATIVE
                && BossMusic.getBossMusicInstance().getConfig().getBoolean("ignore-creative"))
            BattleManager.stopBattle(event.getPlayer());
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        for (Battle battle : BattleManager.getBattles())
            if (battle.containsEntity(event.getEntity()))
                battle.removeEntity(event.getEntity());

    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        for (Battle battle : BattleManager.getBattles())
            if (battle.containsEntity(event.getEntity()))
                battle.removeEntity(event.getEntity());
    }

    public static void loadMobs() {
        FileConfiguration config = BossMusic.getBossMusicInstance().getConfig();
        mobList = new HashMap<String, HashSet<String>>();
        for(String subsection : config.getConfigurationSection("music").getKeys(false)){
            HashSet<String> customNames = new HashSet<String>();
            for(String customName : config.getConfigurationSection("music."+subsection).getKeys(false))
                customNames.add(customName);
            mobList.put(subsection, customNames);
        }
    }
}
