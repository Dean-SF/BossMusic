package com.DeanSF.bossmusic.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.DeanSF.bossmusic.BossMusic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class BossMusicCMDCompleter implements TabCompleter {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> suggestion = new ArrayList<>();
        switch(args.length){
            case 1:
                suggestion = new ArrayList<>(Arrays.asList("reload","add","remove","show","version"));
                break;
            case 2:
                if(args[0].equalsIgnoreCase("add"))
                    suggestion = new ArrayList<>(Arrays.asList("creeper",
                            "ender_dragon",
                            "enderman",
                            "ghast",
                            "skeleton",
                            "spider",
                            "wither",
                            "wither_skeleton",
                            "zombie",
                            "evoker",
                            "vindicator",
                            "zombie_villager",
                            "pillager",
                            "ravager",
                            "vex",
                            "endermite",
                            "guardian",
                            "elder_guardian",
                            "shulker",
                            "husk",
                            "stray",
                            "phantom",
                            "blaze",
                            "silverfish",
                            "magma_cube",
                            "slime",
                            "cave_spider",
                            "drowned",
                            "witch",
                            "hoglin",
                            "piglin",
                            "mcjukebox:",
                            "noteblock:"));
                else if(args[0].equalsIgnoreCase("remove")) {
                    suggestion = BossMusic.getBossMusicInstance().getConfig().getStringList("music.sound");
                    suggestion.addAll(Arrays.asList("creeper",
                            "ender_dragon",
                            "enderman",
                            "ghast",
                            "skeleton",
                            "spider",
                            "wither",
                            "wither_skeleton",
                            "zombie",
                            "evoker",
                            "vindicator",
                            "zombie_villager",
                            "pillager",
                            "ravager",
                            "vex",
                            "endermite",
                            "guardian",
                            "elder_guardian",
                            "shulker",
                            "husk",
                            "stray",
                            "phantom",
                            "blaze",
                            "silverfish",
                            "magma_cube",
                            "slime",
                            "cave_spider",
                            "drowned",
                            "witch",
                            "hoglin",
                            "piglin"));
                }
                break;
            case 3:
                if(args[0].equalsIgnoreCase("remove")){
                    if(args.length == 3) {
                        Set<String> customNameSet = BossMusic.getBossMusicInstance().getConfig().getConfigurationSection("music."+args[1]).getKeys(false);
                        ArrayList<String> customNameList = new ArrayList<String>();
                        for(String customName : customNameSet) {
                            customNameList.add('"'+customName+'"');
                        }
                        suggestion = customNameList;
                    }
                } else if(args[0].equalsIgnoreCase("add")) {
                    if(args.length == 3) {
                        suggestion.add("\"a_name\"");
                    }
                }
                break;
            default:
                if(args.length < 3) {
                    break;
                }
                if(args[args.length-2].length()<=0 || args[args.length-3].length()<=0) {
                    break;
                }
                if(args[args.length-2].charAt(args[args.length-2].length()-1) == '"') {
                    suggestion.add("<Sound-Name>");
                } else if(args[args.length-3].charAt(args[args.length-3].length()-1) == '"') {
                    suggestion.add("<Duration>");
                }
                break;    
        }

        return suggestion;
    }
}
