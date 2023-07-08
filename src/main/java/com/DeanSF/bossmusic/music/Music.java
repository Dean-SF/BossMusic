package com.DeanSF.bossmusic.music;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.DeanSF.bossmusic.BossMusic;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Music {

    public enum MusicType{
        RESSOURCEPACK,
    }

    private static HashMap<String, ArrayList<Music>> musicList;

    static{
        musicList = new HashMap<>();
    }

    private MusicType musicType;
    private String sound;

    // O B J E C T

    /**
     * Create a music object
     * @param musicType Type of music
     * @param sound The sound of the music
     */
    public Music(MusicType musicType, String sound) {
        this.musicType = musicType;
        this.sound = sound;
    }

    /**
     * Get Music Type (Ressource pack, NoteBlock or MCJukebox)
     * @return The music type
     */
    public MusicType getMusicType() {
        return musicType;
    }

    /**
     * Get the sound string
     * @return The sound string
     */
    public String getSound() {
        return sound;
    }

    /**
     * Play the music to a player
     * @param player The player to play the music
     */
    public abstract void play(Player player);

    /**
     * Stop the music of a player
     * @param player The player to stop the music
     */
    public abstract void stop(Player player);


    public String toString() {
        return "Music{" +
                "musicType=" + musicType +
                ", sound='" + sound + '\'' +
                '}';
    }


    // S T A T I C

    public static void loadMusicList(){
        try{
            loadSubsectionSound("music");
        }catch(Exception ex){
            Bukkit.getLogger().warning("Music" + " loadMusicList " + ex.toString());
            Bukkit.getLogger().warning("[BossMusic] Could not load music from config.yml. Disabling BossMusic...");
            BossMusic.getBossMusicInstance().getPluginLoader().disablePlugin(BossMusic.getBossMusicInstance());
        }
    }

    private static void loadSubsectionSound(String path){
        FileConfiguration config = BossMusic.getBossMusicInstance().getConfig();
        for(String subsection : config.getConfigurationSection(path).getKeys(false)){
            if(subsection.equals("sound")){
                ArrayList<Music> musics = new ArrayList<>();
                for(String sound: config.getStringList(path+".sound"))
                    musics.add(Music.getMusic(sound));

                musicList.put(path+"."+subsection, musics);
            }else
                Music.loadSubsectionSound(path+"."+subsection);
        }
    }

    /**
     * Get a music to play from a Entity
     * @param entity The entity
     * @return The music to play with this entity
     */
    public static Music getMusicFromEntity(Entity entity){
        String path = "music.sound";
        String type = entity.getType().toString().toLowerCase();

        if(Music.musicList.containsKey("music."+type+".sound"))
            path = "music."+type+".sound";

        if(Music.musicList.containsKey("music."+type+"."+entity.getCustomName()+".sound"))
            path = "music."+type+"."+entity.getCustomName()+".sound";

        return Music.musicList.get(path).get((int)(Math.random()*Music.musicList.get(path).size()));
    }

    /**
     * Construct the music object with the sound name
     * @param sound The sound
     * @return The music object
     */
    public static Music getMusic(String sound){

        // index 0 is music name, index 1 is duration
        String[] musicAttributes = sound.split("\\s+");

        //RessourcePack Music
        return new RPMusic(musicAttributes[0],Integer.parseInt(musicAttributes[1]));
    }
}
