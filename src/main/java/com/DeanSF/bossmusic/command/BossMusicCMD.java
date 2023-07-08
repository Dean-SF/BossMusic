package com.DeanSF.bossmusic.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import com.DeanSF.bossmusic.BossMusic;
import com.DeanSF.bossmusic.listener.PlayerListener;
import com.DeanSF.bossmusic.music.Music;

import java.util.ArrayList;

public class BossMusicCMD implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender.hasPermission("bossmusic.command")) {
            switch (args.length) {
                case 0:
                    sender.sendMessage(ChatColor.GREEN + "BossMusic" + ChatColor.WHITE + " version " + ChatColor.GREEN + BossMusic.getBossMusicInstance().getDescription().getVersion()
                            + ChatColor.WHITE + " by " + ChatColor.GREEN + "DeanSF");

                    return true;
                case 1:
                    switch (args[0].toLowerCase()) {
                        case "reload":
                            BossMusic.getBossMusicInstance().reloadConfig();
                            Music.loadMusicList();
                            PlayerListener.loadMobs();
                            sender.sendMessage(ChatColor.GREEN + "BossMusic " + ChatColor.WHITE + "has been " + ChatColor.GREEN + "reloaded");
                            return true;
                        case "version":
                            sender.sendMessage(ChatColor.GREEN + "BossMusic" + ChatColor.WHITE + " version " + ChatColor.GREEN + BossMusic.getBossMusicInstance().getDescription().getVersion()
                                    + ChatColor.WHITE + " by " + ChatColor.GREEN + "DeanSF");
                            return true;
                        case "remove":
                        case "add":
                            sender.sendMessage(ChatColor.GREEN + "/bossmusic " + args[0] + " " + ChatColor.WHITE + ChatColor.BOLD + "<mobType> " + ChatColor.RESET + ChatColor.ITALIC + "<name> <sound> <duration>");
                            return true;
                        case "show":
                            if(BossMusic.getBossMusicInstance().getConfig().contains("music")){
                                String message = "The list of "+ChatColor.GREEN+"sounds"+ChatColor.WHITE+" :\n";
                                FileConfiguration config = BossMusic.getBossMusicInstance().getConfig();
                                for(String mobType: config.getConfigurationSection("music").getKeys(false)){
                                    message+= ChatColor.RED + mobType + ":\n";
                                    for(String customName: config.getConfigurationSection("music."+mobType).getKeys(false)) {
                                        message+= "    " + ChatColor.GOLD + customName + ":\n" + "    ";
                                        for(String music : config.getStringList("music."+mobType+"."+customName+".sound")) {
                                            message+= ChatColor.YELLOW + "- " + music + "\n";
                                        }
                                        
                                    }
                                    //message+="\n";
                                }

                                sender.sendMessage(message);
                            }else
                                sender.sendMessage(ChatColor.RED+"There is no sounds");
                            return true;
                    }
                    break;
                case 2:
                    switch (args[0].toLowerCase()) {
                        case "remove":
                        case "add":
                            sender.sendMessage(ChatColor.GREEN + "/bossmusic " + args[0] + " " + ChatColor.WHITE + ChatColor.BOLD + "<mobType> " + ChatColor.RESET + ChatColor.ITALIC + "<name> <sound> <duration>");
                            return true;
                    }
                    break;
                case 3:
                    switch (args[0].toLowerCase()) {
                        case "remove":
                        case "add":
                            sender.sendMessage(ChatColor.GREEN + "/bossmusic " + args[0] + " " + ChatColor.WHITE + ChatColor.BOLD + "<mobType> " + ChatColor.RESET + ChatColor.ITALIC + "<name> <sound> <duration>");
                            return true;
                    }
                    break;
                default:
                    if(args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("remove")) {
                        try{
                            EntityType.valueOf(args[1].toUpperCase());
                        }catch(IllegalArgumentException ex){
                            sender.sendMessage(ChatColor.RED+args[1]+" is not a valid entity name");
                            return true;
                        }
                        int lastestIndex = 0;
                        String entityName = "";
                        boolean firstQuote = false;
                        boolean secondQuote = false;
                        for(int i = 2; i < args.length; i++) {
                            if(args[i].charAt(0) == '"') {
                                firstQuote = true;
                                if(args[i].charAt(args[i].length()-1) == '"') {
                                    secondQuote = true;
                                    entityName += args[i].substring(1,args[i].length()-1);
                                    continue;
                                }
                                entityName += args[i].substring(1) + " ";
                                continue;
                            } else if(args[i].charAt(args[i].length()-1) == '"') {
                                secondQuote = true;
                                entityName += args[i].substring(0,args[i].length()-1);
                                lastestIndex = i;
                                break;
                            } else {
                                entityName += args[i] + " ";
                                lastestIndex = i;
                            }
                        }

                        if(!firstQuote) {
                            sender.sendMessage(ChatColor.RED+"Name should start with \"");
                            return true;
                        }

                        if(!secondQuote) {
                            sender.sendMessage(ChatColor.RED+"Name should end with \"");
                            return true;
                        }

                        if((args.length-1)-lastestIndex != 2) {
                            sender.sendMessage(ChatColor.RED+"Incorrect amount of arguments");
                            return true;
                        }

                        try {
                            Integer.parseInt(args[args.length-1]);
                        } catch (Exception e) {
                            sender.sendMessage(ChatColor.RED+"Duration as to be a number");
                            return true;
                        }

                        if(args[args.length-2].charAt(args[args.length-2].length()-1) == '"' || args[args.length-2].charAt(0) == '"') {
                            sender.sendMessage(ChatColor.RED+"There is no sound name or is between quotes");
                            return true;
                        }

                        args[args.length-1] = args[args.length-2] + " " + args[args.length-1];

                        ArrayList<String> soundList = BossMusic.getBossMusicInstance().getConfig().contains("music."+args[1].toLowerCase()+"."+entityName+".sound") ?
                                new ArrayList<>(BossMusic.getBossMusicInstance().getConfig().getStringList("music."+args[1].toLowerCase()+"."+entityName+".sound")) :
                                new ArrayList<>();
                        String soundType = "Resource Pack";
                        if(args[0].equalsIgnoreCase("add")){
                            soundList.add(args[args.length-1]);
                            sender.sendMessage("'" + ChatColor.GREEN + args[args.length-1] + ChatColor.WHITE + "' has been added to " + ChatColor.GREEN + args[1] + ChatColor.WHITE + " named "+ChatColor.GREEN+entityName+ChatColor.WHITE+" has a " + ChatColor.GREEN + soundType + ChatColor.WHITE + " sound");
                        }else{
                            soundList.remove(args[args.length-1]);
                            sender.sendMessage("'" + ChatColor.GREEN + args[args.length-2] + ChatColor.WHITE + "' has been removed from " + ChatColor.GREEN + args[1] + ChatColor.WHITE + " named "+ChatColor.GREEN+entityName);
                        }

                        if(soundList.size() > 0)
                            BossMusic.getBossMusicInstance().getConfig().set("music."+args[1].toLowerCase()+"."+entityName+".sound", soundList);
                        else
                            BossMusic.getBossMusicInstance().getConfig().set("music."+args[1].toLowerCase()+"."+entityName,null);
                        BossMusic.getBossMusicInstance().saveConfig();
                        BossMusic.getBossMusicInstance().reloadConfig();
                        Music.loadMusicList();

                        return true;
                    }
            }
        }
        return false;
    }
}
