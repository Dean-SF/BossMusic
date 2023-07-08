package com.DeanSF.bossmusic;

import org.bukkit.plugin.java.JavaPlugin;

import com.DeanSF.bossmusic.command.BossMusicCMD;
import com.DeanSF.bossmusic.command.BossMusicCMDCompleter;
import com.DeanSF.bossmusic.listener.PlayerListener;
import com.DeanSF.bossmusic.music.Music;


public final class BossMusic extends JavaPlugin {

    private static BossMusic bossMusic;

    @Override
    public void onEnable() {

        saveDefaultConfig();

        BossMusic.bossMusic = this;

        Music.loadMusicList();
        PlayerListener.loadMobs();

        //Register listener
        getServer().getPluginManager().registerEvents( new PlayerListener(),this);

        //Register command
        getCommand("bossmusic").setExecutor(new BossMusicCMD());
        getCommand("bossmusic").setTabCompleter(new BossMusicCMDCompleter());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static BossMusic getBossMusicInstance(){
        return BossMusic.bossMusic;
    }

}
