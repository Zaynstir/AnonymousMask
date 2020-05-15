package io.github.zaynstir.anonymous;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class Anonymous extends JavaPlugin implements Listener{

    Team team = null;
    List<SaveTeam> hiddenPlayers = new ArrayList<SaveTeam>();
    public String pluginFolder = getDataFolder().getAbsolutePath();

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
        createTeam();
        readJSON("hiddenPlayersList", "", "");
    }

    @Override
    public void onDisable() {
        writeJSON("hiddenPlayersList", "");
    }

    public void writeJSON(String fileName, String subPath) {
        JSONObject main = new JSONObject();

        for(int i = 0; i < hiddenPlayers.size(); i++){
            main.put((hiddenPlayers.get(i).getCurrentPlayer().toString()),(hiddenPlayers.get(i).getCurrentTeam() == null ? null : hiddenPlayers.get(i).getCurrentTeam().getName()));
        }

        try {
            File file = new File(pluginFolder + File.separator + subPath + fileName + ".json");
            File filePath = new File(pluginFolder + File.separator + subPath);
            filePath.mkdirs();
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(main.toString());
            fileWriter.flush();
            fileWriter.close();
            getLogger().info("SUCCESS: on write file hiddenPlayersList.json for Plugin: Anonymous");
        } catch (Exception e) {
            getLogger().info("ERROR: on write file hiddenPlayersList.json for Plugin: Anonymous");}
    }

    public void readJSON(String fileName, String subPath, String object) {
        String var = null;
        try {
            JSONParser parser = new JSONParser();

            File file = new File(pluginFolder + File.separator + subPath + fileName + ".json");
            Object obj = parser.parse(new FileReader(file));

            JSONObject jsonObject = (JSONObject) obj;
            Scoreboard s = Bukkit.getScoreboardManager().getMainScoreboard();

            //var = (String) jsonObject.get(object);

            for(Iterator iterator = jsonObject.keySet().iterator(); iterator.hasNext();){
                String key = (String) iterator.next();
                try{
                    hiddenPlayers.add(new SaveTeam(UUID.fromString(key), (s.getTeam(jsonObject.get(key).toString()) == null ? null : s.getTeam(jsonObject.get(key).toString()))));
                }catch(Exception e){
                    getLogger().info("Adding Failed");
                }

            }
            getLogger().info("SUCCESS: on read file hiddenPlayersList.json for Plugin: Anonymous");
        } catch (Exception e) {
            getLogger().info("ERROR: on read file hiddenPlayersList.json for Plugin: Anonymous");
        }
        //return var;
    }

    public void createTeam() {
        final Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        team = scoreboard.getTeam("hidden");
        if(team == null) {
            team = scoreboard.registerNewTeam("hidden");
        }
        team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);

        /*Team t = scoreboard.getTeam("testTeam");
        if(t == null) {
            t = scoreboard.registerNewTeam("testTeam");
        }*/
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(label.equalsIgnoreCase("mask")) {
            if(!(sender instanceof Player)) {
                sender.sendMessage("ERROR: Only Players can use this command.");
                return true;
            }
            Player p = (Player) sender;
            if(p.hasPermission("mask.use")) {
                if(args.length == 1){
                    if(args[0].equalsIgnoreCase("list")){
                        p.sendMessage(ChatColor.DARK_GREEN + "The Hidden: ");
                        p.sendMessage(ChatColor.DARK_GREEN + "-----------------------------------");
                        p.sendMessage(ChatColor.DARK_GREEN + "--Real Name | Display Name | Team Name--");
                        p.sendMessage(ChatColor.DARK_GREEN + "-----------------------------------");
                        for(int i = 0; i < hiddenPlayers.size(); i++){
                            String str = "";
                            try{
                                //p.sendMessage((hiddenPlayers.get(i).getCurrentPlayer() == null ? "null" : hiddenPlayers.get(i).getCurrentPlayer().toString()));

                                Player p2 = Bukkit.getPlayer(hiddenPlayers.get(i).getCurrentPlayer());
                                //p.sendMessage(("" + Bukkit.getPlayer(hiddenPlayers.get(i).getCurrentPlayer())));
                                str += (p2 == null ? "Unknown Playername" : p2.getName() + " | " + p2.getDisplayName()) + " &2| ";

                            }catch(Exception e){
                                p.sendMessage("There was an error recieving the list: Player");
                                getLogger().info("Anonymous Mask Player List Error");
                            }
                            try{
                                //p.sendMessage(hiddenPlayers.get(i).getCurrentTeam())
                                //p.sendMessage(""+(hiddenPlayers.get(i).getCurrentTeam() == null ? "null" : hiddenPlayers.get(i).getCurrentTeam()));
                                str += (hiddenPlayers.get(i).getCurrentTeam() == null ? "Unknown Team" : hiddenPlayers.get(i).getCurrentTeam().getName());

                            }catch(Exception e){
                                p.sendMessage("There was an error recieving the list: Team");
                                getLogger().info("Anonymous Mask Team List Error");
                            }
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&2"+str));
                            /*try{
                                p.sendMessage((hiddenPlayers.get(i).getCurrentPlayer() == null ? "null" : hiddenPlayers.get(i).getCurrentPlayer().toString()) + " - " + (hiddenPlayers.get(i).getCurrentTeam().getName() == null ? "null" : hiddenPlayers.get(i).getCurrentTeam().getName()));
                             }catch (Exception e){
                                p.sendMessage("There was an error recieving the list");
                                getLogger().info("Anonymous Mask List Error");
                            }*/
                        }
                        p.sendMessage(ChatColor.DARK_GREEN + "-----------------------------------");
                        return true;
                    }
                    if(args[0].equalsIgnoreCase("help")){
                        p.sendMessage(ChatColor.DARK_GREEN + "---------- Help - Mask ----------");
                        p.sendMessage(ChatColor.DARK_GREEN + "mask - recieve the Anonymous Mask");
                        p.sendMessage(ChatColor.DARK_GREEN + "mask list - output's a list of all 'hidden' players and their previous teams.");
                        return true;
                    }
                }
                else{
                    if(p.getInventory().firstEmpty() == -1) {
                        p.sendMessage(ChatColor.DARK_RED + "Please empty an inventory slot");
                        return true;
                    }
                    else {
                        p.getInventory().addItem(getMask());
                        p.sendMessage(ChatColor.YELLOW + "You have received the Anonymous Mask.");
                        return true;
                    }
                }
            }

        }
        return false;
    }

    public ItemStack getMask() {
        ItemStack mask = new ItemStack(Material.BONE_BLOCK);
        ItemMeta meta = mask.getItemMeta();

        meta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "Anonymous Mask");
        List<String> lore = new ArrayList<String>();
        lore.add("");
        lore.add(ChatColor.GOLD + "" +ChatColor.ITALIC + "Right-click to place it on your head.");
        lore.add(ChatColor.GOLD + "" +ChatColor.ITALIC + "This will hide your nametag.");
        meta.setLore(lore);
        mask.setItemMeta(meta);

        return mask;
    }

    @EventHandler()
    public void InventoryInteractEvent(InventoryClickEvent e) {
        if(e.getSlot() == 39) {
            if(e.getCurrentItem().getType().equals(Material.BONE_BLOCK)) {
                if(e.getCurrentItem().getItemMeta().getDisplayName().contains("Anonymous Mask")) {
                    if(e.getCurrentItem().getItemMeta().hasLore()) {
                        Player p = (Player) e.getWhoClicked();
                        team.removeEntry(p.getName());
                        for(int i = 0; i < hiddenPlayers.size(); i++){
                            //Bukkit.broadcastMessage(hiddenPlayers.get(i).getCurrentPlayer().toString() +"-"+p.getUniqueId().toString());
                            //Bukkit.broadcastMessage(hiddenPlayers.get(i).getCurrentPlayer().toString().equals(p.getUniqueId().toString()) ? "MATCHED" : "UNMATCHED");
                            if(hiddenPlayers.get(i).getCurrentPlayer().toString().equals(p.getUniqueId().toString())){
                                if(hiddenPlayers.get(i).getCurrentTeam() != null){
                                    hiddenPlayers.get(i).getCurrentTeam().addEntry(p.getName());
                                    //Bukkit.broadcastMessage(p.getName() + " is JOINING TEAM " + hiddenPlayers.get(i).getCurrentTeam().getName());
                                }
                                hiddenPlayers.remove(i);
                                break;
                            }
                        }
                        p.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "Your nametag is now visible.");
                    }
                }
            }
        }
    }


    //@EventHandler(priority = EventPriority.HIGH)
    @EventHandler()
    public void onPlayerInteractEvent(PlayerInteractEvent e) {
        if(e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.BONE_BLOCK)) {
            if(e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("Anonymous Mask")) {
                if(e.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasLore()) {
                    e.setCancelled(true);
                    Player p = (Player) e.getPlayer();
                    if(p.getInventory().getHelmet() != null) {
                        p.sendMessage(ChatColor.DARK_RED + "Please remove your helmet.");
                    }
                    else {
                        p.getInventory().removeItem(getMask());
                        p.getInventory().setHelmet(getMask());
                        Player p2 = Bukkit.getPlayer(p.getUniqueId());
                        //p.sendMessage("" + p2.getName() + " | " + p2.getDisplayName());
                        //p.sendMessage("" + p.getScoreboard().getEntryTeam(p2.getName()));
                        //p.sendMessage("" + p2.getDisplayName());
                        //p.sendMessage("" + p2.getName());
                        hiddenPlayers.add(new SaveTeam(p.getUniqueId(), p.getScoreboard().getEntryTeam(p2.getName())));
                        team.addEntry(p2.getName());
                        p.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "Your nametag is now invisible.");
                    }
                }
            }
        }
    }

}


