package com.DeanSF.bossmusic.battle;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.DeanSF.bossmusic.BossMusic;
import com.DeanSF.bossmusic.music.Music;
import com.DeanSF.bossmusic.task.RunAwayTask;

public class Battle {
    private Player player;
    private Entity entity;
    private Music music;
    private RunAwayTask runAwayTask;

    /**
     * Create Battle Object
     * @param player The player
     * @param entities The entity list
     * @param music The music to be play
     */
    protected Battle(Player player, Entity entity, Music music){
        this.player = player;
        this.entity = entity;
        this.music = music;
        music.play(player);

        int runawayClock = BossMusic.getBossMusicInstance().getConfig().getInt("run-away-clock");

        this.runAwayTask = new RunAwayTask(this);
        this.runAwayTask.runTaskLater(BossMusic.getBossMusicInstance(), runawayClock*20);
    }

    /**
     * Add a entity to the fight
     * @param entity
     */
    public void setEntity(Entity entity){
        this.entity = entity;
    }

    /**
     * Check if player is fighting with an entity
     * @param entity The entity
     * @return If the player is fighting with the entity
     */
    public boolean containsEntity(Entity entity){
        return this.entity.getUniqueId().equals(entity.getUniqueId());
    }

    /**
     * Remove entity from a fight. If it was the last entity, the music will be stop
     * @param entity The entity
     */
    public void removeEntity(Entity entity){
        this.entity = null;
        BattleManager.stopBattle(player);
        this.runAwayTask.cancel();
    }

    public void resetRunAwayTask(){
        int runawayClock = BossMusic.getBossMusicInstance().getConfig().getInt("run-away-clock");

        runAwayTask.cancel();
        this.runAwayTask = new RunAwayTask(this);
        this.runAwayTask.runTaskLater(BossMusic.getBossMusicInstance(), runawayClock*20);
    }

    public Music getMusic() {
        return music;
    }

    public Player getPlayer() {
        return player;
    }
    
    public Entity getEntity() {
        return this.entity;
    }

    public RunAwayTask getRunAwayTask() {
        return runAwayTask;
    }
}
