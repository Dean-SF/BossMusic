package com.DeanSF.bossmusic.music;

import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import com.DeanSF.bossmusic.BossMusic;
import com.DeanSF.bossmusic.task.SongLoopTask;

public class RPMusic extends Music {
    private int duration;
    private SongLoopTask loop;

    public RPMusic(String sound, int duration) {
        super(MusicType.RESSOURCEPACK, sound);
        this.duration = duration;
        this.loop = null;
    }

    @Override
    public void play(Player player) {
        try {
            player.playSound(player.getLocation(), Sound.valueOf(getSound()), SoundCategory.RECORDS, 1.0f, 1.0f);
        } catch (Exception e) {
            player.playSound(player.getLocation(), getSound(), SoundCategory.RECORDS,1.0f, 1.0f);
        }
        this.loop = new SongLoopTask(this, player);
        if(duration > 0) {
            this.loop.runTaskLater(BossMusic.getBossMusicInstance(), (duration * 20));
        }
    }

    @Override
    public void stop(Player player) {
        if(this.loop != null) {
            this.loop.cancel();
        }
        try {
            player.stopSound(Sound.valueOf(getSound()), SoundCategory.RECORDS);
        } catch (Exception exception) {
            player.stopSound(getSound(), SoundCategory.RECORDS);
        }
    }
}
