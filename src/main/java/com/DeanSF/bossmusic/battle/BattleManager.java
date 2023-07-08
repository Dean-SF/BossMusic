package com.DeanSF.bossmusic.battle;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.DeanSF.bossmusic.music.Music;

import java.util.ArrayList;
import java.util.HashMap;

public class BattleManager {
    private static HashMap<Player,Battle> battleList;

    static{
        battleList = new HashMap<>();
    }

    public static Battle createBattle(Player player, Entity entity){
        Battle battle = new Battle(player, entity, Music.getMusicFromEntity(entity));
        BattleManager.battleList.put(player, battle);
        return battle;
    }

    public static Battle createBattle(Player player, Entity entity, Music music){
        Battle battle = new Battle(player,entity,music);
        BattleManager.battleList.put(player,battle);

        return battle;
    }

    public static Battle getBattle(Player player){
        return BattleManager.battleList.get(player);
    }

    public static boolean isPlayerFighting(Player player){
        return BattleManager.battleList.containsKey(player);
    }

    public static void removeBattle(Player player){
        BattleManager.getBattle(player).getRunAwayTask().cancel();
        BattleManager.battleList.remove(player);
    }

    public static void stopBattle(Player player){
        BattleManager.getBattle(player).getMusic().stop(player);
        BattleManager.removeBattle(player);
    }

    public static ArrayList<Battle> getBattles(){
        return new ArrayList<Battle>(BattleManager.battleList.values());
    }
}
