package com.DeanSF.bossmusic.task;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.DeanSF.bossmusic.music.RPMusic;

public class SongLoopTask extends BukkitRunnable {
    private RPMusic music;
    private Player player;

    public SongLoopTask(RPMusic music, Player player){
        this.music = music;
        this.player = player;
    }

    @Override
    public void run() {
        music.play(player);
    }
}
