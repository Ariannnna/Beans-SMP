package com.arianna.beans.smp;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Note;
import org.bukkit.OfflinePlayer;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementDisplay;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.plugin.java.JavaPlugin;
public final class SMP extends JavaPlugin {

   // This is used for the UHC alert every 5 minutes
   Timer timer = new Timer();
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new Chat(), this);
        Bukkit.getPluginManager().registerEvents(new JoinLeave(), this);
        Bukkit.getPluginManager().registerEvents(new RegisterDeath(), this);
        Bukkit.getPluginManager().registerEvents(new AchievementUnlock(), this);
//        Bukkit.getPluginManager().registerEvents(new Scoreboard(), this);
        Objects.requireNonNull(getCommand("prefix")).setExecutor(new ImmortalPrefixCommand());
        Objects.requireNonNull(getCommand("taunt")).setExecutor(new Taunt());
        Objects.requireNonNull(getCommand("gm")).setExecutor(new GamemodeCommand());
        Objects.requireNonNull(getCommand("uhc")).setExecutor(new UHCCommand());
        Objects.requireNonNull(getCommand("setlevel")).setExecutor(new LevelsCommand());
        Objects.requireNonNull(getCommand("teleport")).setExecutor(new TeleportCommand());
        timer.schedule(new TimerTask() {
            public void run() {
                Bukkit.broadcastMessage(" ");
                Bukkit.broadcastMessage(UtilColor.white + UtilColor.bold + " A game of " + UtilColor.yellow + UtilColor.bold + "UHC Remastered" + UtilColor.white + UtilColor.bold + " is about to start! ");
                Bukkit.broadcastMessage(" ");
                Bukkit.getOnlinePlayers().forEach(p1 -> p1.playNote(p1.getLocation(), Instrument.PLING, Note.sharp(1, Note.Tone.E)));
            }
        }, 0L, 300000L);
    }

    public void onDisable() {
        timer.cancel();
    }

    class JoinLeave implements Listener {
        @EventHandler
        public void onJoin(PlayerJoinEvent event) {
            event.setJoinMessage(UtilColor.aqua + UtilColor.bold + "[JOIN] " + UtilColor.darkAqua + event.getPlayer().getName());
            Config config = new Config(event.getPlayer().getUniqueId());
            if (getConfig().get(event.getPlayer().getUniqueId().toString()) == null) {
                config.setInt("immortal", 0);
                config.setInt("deaths", 0);
            }
        }

        @EventHandler
        public void onLeave(PlayerQuitEvent event) {
            event.setQuitMessage(UtilColor.red + UtilColor.bold + "[QUIT] " + UtilColor.darkAqua + event.getPlayer().getName());
        }
    }

    static class AchievementUnlock implements Listener {
        public String[][] normal = new String[][] {
                { "story/mine_stone", "Stone Age" },
                { "story/upgrade_tools", "Getting an Upgrade" },
                { "story/smelt_iron", "Acquire Hardware" },
                { "story/obtain_armor", "Suit Up" },
                { "story/lava_bucket", "Hot Stuff" },
                { "story/iron_tools", "Isn't It Iron Pick" },
                { "story/deflect_arrow", "Not Today, Thank You" },
                { "story/form_obsidian", "Ice Bucket Challenge" },
                { "story/mine_diamond", "Diamonds!" },
                { "story/enter_the_nether", "We Need to Go Deeper" },
                { "story/shiny_gear", "Cover Me With Diamonds" },
                { "story/enchant_item", "Enchanter" },
                { "story/follow_ender_eye", "Eye Spy" },
                { "story/enter_the_end", "The End?" },
                { "nether/find_fortress", "A Terrible Fortress" },
                { "nether/get_wither_skull", "Spooky Scary Skeleton" },
                { "nether/obtain_blaze_rod", "Into Fire" },
                { "nether/summon_wither", "Withering Heights" },
                { "nether/brew_potion", "Local Brewery" },
                { "nether/create_beacon", "Bring Home the Beacon" },
                { "nether/find_bastion", "Those Were the Days" },
                { "nether/obtain_ancient_debris", "Hidden in the Depths" },
                { "nether/obtain_crying_obsidian", "Who is Cutting Onions?" },
                { "nether/distract_piglin", "Oh Shiny" },
                { "nether/ride_strider", "This Boat Has Legs" },
                { "nether/loot_bastion", "War Pigs" },
                { "nether/use_lodestone", "Country Lode, Take Me Home" },
                { "nether/charge_respawn_anchor", "Not Quite \"Nine\" Lives" },
                { "end/kill_dragon", "Free the End" },
                { "end/enter_end_gateway", "Remote Getaway" },
                { "end/find_end_city", "The City at the End of the Game" },
                { "adventure/voluntary_exile", "Voluntary Exile" },
                { "adventure/kill_a_mob", "Monster Hunter" },
                { "adventure/trade", "What a Deal!" },
                { "adventure/honey_block_slide", "Sticky Situation" },
                { "adventure/ol_betsy", "Ol' Betsy" },
                { "adventure/sleep_in_bed", "Sweet Dreams" },
                { "adventure/throw_trident", "A Throwaway Joke" },
                { "adventure/shoot_arrow", "Take Aim" },
                { "adventure/whos_the_pillager_now", "Who's the Pillager Now?" },
                { "adventure/very_very_frightening", "Very Very Frightening" },
                { "husbandry/safely_harvest_honey", "Bee Our Guest" },
                { "husbandry/breed_an_animal", "The Parrots and the Bats" },
                { "husbandry/tame_an_animal", "Best Friends Forever" },
                { "husbandry/fishy_business", "Fishy Business" },
                { "husbandry/silk_touch_nest", "Total Beelocation" },
                { "husbandry/plant_seed", "A Seedy Place" },
                { "husbandry/tactical_fishing", "Tactical Fishing" } };

        public String[][] goal = new String[][] {
                { "story/cure_zombie_villager", "Zombie Doctor" },
                { "nether/create_full_beacon", "Beaconator" },
                { "end/dragon_egg", "The Next Generation" },
                { "end/respawn_dragon", "The End... Again..." },
                { "end/dragon_breath", "You Need a Mint" },
                { "end/elytra", "Sky's the Limit" },
                { "adventure/totem_of_undying", "Postmortal" },
                { "adventure/summon_iron_golem", "Hired Help" }
        };

        public String[][] challenge = new String[][] {
                { "nether/return_to_sender", "Return to Sender" },
                { "nether/fast_travel", "Subspace Bubble" },
                { "nether/uneasy_alliance", "Uneasy Alliance" },
                { "nether/all_potions", "A Furious Cocktail" },
                { "nether/all_effects", "How Did We Get Here?" },
                { "nether/netherite_armor", "Cover Me in Debris" },
                { "nether/explore_nether", "Hot Tourist Destinations" },
                { "end/levitate", "Great View From Up Here" },
                { "adventure/hero_of_the_village", "Hero of the Village" },
                { "adventure/kill_all_mobs", "Monsters Hunted" },
                { "adventure/two_birds_one_arrow", "Two Birds, One Arrow" },
                { "adventure/arbalistic", "Arbalistic" },
                { "adventure/adventuring_time", "Adventuring Time" },
                { "adventure/sniper_duel", "Sniper Duel" },
                { "adventure/bullseye", "Bullseye" },
                { "husbandry/bred_all_animals", "Two by Two" },
                { "husbandry/complete_catalogue", "A Complete Catalogue" },
                { "husbandry/balanced_diet", "A Balanced Diet" },
                { "husbandry/break_diamond_hoe", "Serious Dedication" },
                { "husbandry/obtain_netherite_hoe", "Serious Dedication" } };

        public String find(String ad) {
            int i;
            for (i = 0; i < normal.length; i++) {
                if (ad.equals(normal[i][0])) return normal[i][1];
            }
            for (i = 0; i < goal.length; i++) {
                if (ad.equals(goal[i][0])) return goal[i][1];
            }
            for (i = 0; i < challenge.length; i++) {
                if (ad.equals(challenge[i][0])) return challenge[i][1];
            }
            return ad;
        }

        private boolean check(String adv) {
            if (adv.contains("root") || adv.contains("recipes")) return false;
            return true;
        }

        @EventHandler
        public void onRegisteredAchievement(PlayerAdvancementDoneEvent event) {

            if (check(event.getAdvancement().getKey().getKey())) {
                String title = find(event.getAdvancement().getKey().getKey());
                Bukkit.broadcastMessage(UtilColor.blue + "Advancement> " + UtilColor.yellow + event.getPlayer().getName() + UtilColor.gray + " has advanced through " + UtilColor.yellow + title + UtilColor.gray + ".");
            }

//            Bukkit.broadcastMessage(UtilColor.blue + "Advancement> " + UtilColor.gray + "[" + UtilColor.yellow + event.getAdvancement().getDisplay().getDescription() + UtilColor.gray + "].");
        }
    }

    static class Taunt implements CommandExecutor {
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (sender instanceof org.bukkit.command.ConsoleCommandSender) Bukkit.getConsoleSender().sendMessage("no");
            else if (args.length != 1) sender.sendMessage(UtilColor.blue + "Taunt> " + UtilColor.gray + "/taunt [player]");
            else {
                Player player = ((Player)sender).getPlayer();
                Player args2 = Bukkit.getPlayer(args[0]);
                if (!Objects.requireNonNull(args2).isOnline()) sender.sendMessage(UtilColor.blue + "Taunt> " + UtilColor.gray + "/taunt [player]");
                else Bukkit.broadcastMessage(UtilColor.blue + "Taunt> " + UtilColor.yellow + Objects.requireNonNull(player).getName() + UtilColor.gray + " has blown a kiss at " + UtilColor.yellow + args2.getName() + UtilColor.gray + ".");
            }
            return false;
        }
    }

    class ImmortalPrefixCommand implements CommandExecutor {
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (sender instanceof org.bukkit.command.ConsoleCommandSender) {
                Bukkit.getConsoleSender().sendMessage("no");
            } else if (args.length == 0 || args.length == 3) {
                sendHelp(sender);
            } else {
                String color;
                if (args.length == 2) color = args[0].toLowerCase() + " " + args[1].toLowerCase();
                else color = args[0].toLowerCase();
                setColor(Objects.requireNonNull(((Player)sender).getPlayer()), color);
            }
            return false;
        }

        public void setColor(Player player, String color) {
            Config config = new Config(player.getUniqueId());
            config.setInt("immortal", switchIntColor(color.toLowerCase()));
            player.sendMessage(UtilColor.blue + "Prefix Color> " + UtilColor.gray + "Your new prefix color is " + switchColor(color) + color);
        }

        public Integer switchIntColor(String color) {
            switch (color) {
                case "yellow": return 1;
                case "aqua": return 2;
                case "purple": return 3;
                case "green": return 4;
                case "red": return 5;
                case "dark aqua": return 6;
                case "black": return 7;
                case "white": return 8;
                case "blue": return 9;
                case "dark green": return 10;
                case "gray": return 11;
                case "dark gray": return 12;
            }
            return 0;
        }

        public String swapColor(Integer color) {
            switch (color) {
                case 1: return UtilColor.yellow;
                case 2: return UtilColor.aqua;
                case 3: return UtilColor.purple;
                case 4: return UtilColor.green;
                case 5: return UtilColor.red;
                case 6: return UtilColor.darkAqua;
                case 7: return UtilColor.black;
                case 8: return UtilColor.white;
                case 9: return UtilColor.darkBlue;
                case 10: return UtilColor.darkGreen;
                case 11: return UtilColor.gray;
                case 12: return UtilColor.darkGray;
            }
            return "";
        }

        public String switchColor(String color) {
            switch (color) {
                case "yellow": return UtilColor.yellow;
                case "aqua": return UtilColor.aqua;
                case "purple": return UtilColor.purple;
                case "green": return UtilColor.green;
                case "red": return UtilColor.red;
                case "dark aqua": return UtilColor.darkAqua;
                case "black": return UtilColor.black;
                case "white": return UtilColor.white;
                case "blue": return UtilColor.darkBlue;
                case "dark green": return UtilColor.darkGreen;
                case "gray": return UtilColor.gray;
                case "dark gray": return UtilColor.darkGray;
            }
            return "None";
        }

        public void sendHelp(CommandSender sender) {
            sender.sendMessage(UtilColor.blue + "Prefix Color> " + UtilColor.gray + "Your current prefix color is ");
            sender.sendMessage(UtilColor.blue + "Prefix Color> " + UtilColor.gray + "Options:");
            sender.sendMessage(UtilColor.gray + "- " + UtilColor.yellow + "Yellow");
            sender.sendMessage(UtilColor.gray + "- " + UtilColor.aqua + "Aqua");
            sender.sendMessage(UtilColor.gray + "- " + UtilColor.purple + "Pink");
            sender.sendMessage(UtilColor.gray + "- " + UtilColor.green + "Green");
            sender.sendMessage(UtilColor.gray + "- " + UtilColor.red + "Red");
            sender.sendMessage(UtilColor.gray + "- " + UtilColor.darkAqua + "Dark Aqua");
            sender.sendMessage(UtilColor.gray + "- " + UtilColor.black + "Black");
            sender.sendMessage(UtilColor.gray + "- " + UtilColor.white + "White");
            sender.sendMessage(UtilColor.gray + "- " + UtilColor.darkBlue + "Blue");
            sender.sendMessage(UtilColor.gray + "- " + UtilColor.darkGreen + "Dark Green");
            sender.sendMessage(UtilColor.gray + "- " + UtilColor.gray + "Gray");
            sender.sendMessage(UtilColor.gray + "- " + UtilColor.darkGray + "Dark Gray");
            sender.sendMessage(UtilColor.gray + "- " + UtilColor.white + "Reset");
        }
    }

    static class GamemodeCommand implements CommandExecutor {
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (sender instanceof org.bukkit.command.ConsoleCommandSender) {
                Bukkit.getConsoleSender().sendMessage("no");
            } else {
                Player player = ((Player)sender).getPlayer();
                assert player != null;
                if (!player.isOp()) player.sendMessage("Unknown command. Type \"/help\" for help.");
                else {
                    switch (player.getGameMode()) {
                        case CREATIVE:
                            player.setGameMode(GameMode.SURVIVAL);
                            player.sendMessage(UtilColor.blue + "Gamemode> " + UtilColor.gray + "Gamemode: " + UtilColor.red + "False");
                            break;
                        case SURVIVAL:
                            player.setGameMode(GameMode.CREATIVE);
                            player.sendMessage(UtilColor.blue + "Gamemode> " + UtilColor.gray + "Gamemode: " + UtilColor.green + "True");
                            break;
                    }
                }
            }
            return false;
        }
    }

    static class UHCCommand implements CommandExecutor {
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (sender instanceof org.bukkit.command.ConsoleCommandSender) Bukkit.getConsoleSender().sendMessage("no");
            else {
                if (!sender.isOp()) return false;
                Bukkit.broadcastMessage(" ");
                Bukkit.broadcastMessage(UtilColor.white + UtilColor.bold + " A game of " + UtilColor.yellow + UtilColor.bold + "UHC Remastered" + UtilColor.white + UtilColor.bold + " is about to start! ");
                Bukkit.broadcastMessage(" ");
                Bukkit.getOnlinePlayers().forEach(p1 -> p1.playNote(p1.getLocation(), Instrument.PLING, Note.sharp(1, Note.Tone.E)));
            }
            return false;
        }
    }

    static class MessageCommand implements CommandExecutor {
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (sender instanceof org.bukkit.command.ConsoleCommandSender) Bukkit.getConsoleSender().sendMessage("no");
            else if (args.length != 1) sender.sendMessage(UtilColor.blue + "Taunt> " + UtilColor.gray + "/taunt [player]");
            else {
                Player player = ((Player)sender).getPlayer();
                assert player != null;
                Player args2 = Bukkit.getPlayer(args[0]);
                assert args2 != null;
                if (!args2.isOnline()) sender.sendMessage(UtilColor.blue + "Taunt> " + UtilColor.gray + "/taunt [player]");
                else Bukkit.broadcastMessage(UtilColor.blue + "Taunt> " + UtilColor.yellow + player.getName() + UtilColor.gray + " has blown a kiss at " + UtilColor.yellow + args2.getName() + UtilColor.gray + ".");

            }
            return false;
        }
    }

    class LevelsCommand implements CommandExecutor {
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (sender instanceof org.bukkit.command.ConsoleCommandSender) {
                Bukkit.getConsoleSender().sendMessage("no");
            } else {
                if (!sender.isOp())
                    return false;
                if (args.length != 2) {
                    sender.sendMessage(UtilColor.blue + "Levels> " + UtilColor.gray + "/setlevel [player] [0-inf..]");
                } else {
                    String name = args[0];
                    int deaths = Integer.parseInt(args[1]);
                    OfflinePlayer player = Bukkit.getOfflinePlayer(name);
                    Config config = new Config(player.getUniqueId());
                    config.setInt("deaths", deaths);
                    sender.sendMessage(UtilColor.blue + "Levels> " + UtilColor.gray + "set.");
                }
            }
            return false;
        }
    }

    static class ConsoleSendSayCommand implements CommandExecutor {
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (sender instanceof Player) Bukkit.getConsoleSender().sendMessage("no");
            else {
                if (!sender.isOp()) return false;
                Bukkit.broadcastMessage(UtilColor.darkRed + "Ariannna ");
            }
            return false;
        }
    }

    static class TeleportCommand implements CommandExecutor {
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (sender instanceof ConsoleCommandSender) Bukkit.getConsoleSender().sendMessage("no");
            else if (sender.isOp()) {
                if (args.length == 1 || args.length == 2) {
                    if (args[0].equalsIgnoreCase("here")) {
                        Player target = Bukkit.getPlayer(args[1]);
                        if (target == null) sender.sendMessage(UtilColor.blue + "Teleport> " + UtilColor.gray + "Failed to locate [" + UtilColor.yellow + args[1] + UtilColor.gray + "].");
                        else if (target.getName().equalsIgnoreCase(sender.getName())) sender.sendMessage(UtilColor.blue + "Teleport> " + UtilColor.gray + "It seems like you tried to teleport to yourself.");
                        else {
                            Location executorTargetLoc = ((Player)sender).getLocation();
                            target.teleport(executorTargetLoc);
                            sender.sendMessage(UtilColor.blue + "Teleport> " + UtilColor.gray + "You have teleported " + UtilColor.yellow + args[1] + UtilColor.gray + " to yourself.");
                            target.sendMessage(UtilColor.blue + "Teleport> " + UtilColor.gray + "You have been teleported to " + UtilColor.yellow + sender.getName() + UtilColor.gray + ".");
                        }
                    } else if (args[0].equalsIgnoreCase("all") || args[0].equalsIgnoreCase("@a")) {
                        sender.sendMessage(UtilColor.blue + "Teleport> " + UtilColor.gray + "You have teleported " + UtilColor.yellow + "Everyone" + UtilColor.gray + " to yourself.");
                        Bukkit.getOnlinePlayers().forEach(target -> {
                            Location executorTargetLoc = ((Player)sender).getLocation();
                            target.teleport(executorTargetLoc);
                            target.sendMessage(UtilColor.blue + "Teleport> " + UtilColor.gray + "You have been teleported to " + UtilColor.yellow + sender.getName() + UtilColor.gray + ".");
                        });
                    } else {
                        Player target = Bukkit.getPlayer(args[0]);
                        if (target == null) {
                            sender.sendMessage(UtilColor.blue + "Teleport> " + UtilColor.gray + "Failed to locate [" + UtilColor.yellow + args[1] + UtilColor.gray + "].");
                        } else if (args.length == 1) {
                            Location targetLocation = target.getLocation();
                            sender.sendMessage(UtilColor.blue + "Teleport> " + UtilColor.gray + "You have teleported yourself to " + UtilColor.yellow + target.getName() + UtilColor.gray + ".");
                            ((Player)sender).teleport(targetLocation);
                        } else {
                            Player target2 = Bukkit.getPlayer(args[1]);
                            if (target2 == null) sender.sendMessage(UtilColor.blue + "Teleport> " + UtilColor.gray + "It seems like " + UtilColor.yellow + args[1] + UtilColor.gray + " isn't online right now.");
                            else {
                                Location targetLocation = target2.getLocation();
                                sender.sendMessage(UtilColor.blue + "Teleport> " + UtilColor.gray + "You have teleported " + UtilColor.yellow + target.getName() + UtilColor.gray + " to " + UtilColor.yellow + target2.getName() + UtilColor.gray + ".");
                                target.sendMessage(UtilColor.blue + "Teleport> " + UtilColor.gray + "You have been teleported to " + UtilColor.yellow + target2.getName() + UtilColor.gray + ".");
                                target.teleport(targetLocation);
                            }
                        }
                    }
                }
            }
            return false;
        }
    }

//    class Scoreboard implements Listener {
//        private JScoreboardTeam team;
//
//        JGlobalScoreboard scoreboard = new JGlobalScoreboard(() -> "Beans", () -> Collections.singletonList("Bean one!"));
//
//        @EventHandler
//        public void onPlayerJoin(PlayerJoinEvent event) {
//            if (event.getPlayer().getName().equalsIgnoreCase("ariannna") || event.getPlayer().getName().equalsIgnoreCase("gio_ty")) {
//                team = scoreboard.createTeam(event.getPlayer().getName(), UtilColor.darkPurple + UtilColor.bold + "SEXY ", ChatColor.WHITE);
//                team.addPlayer(event.getPlayer());
//            } else {
//                team = scoreboard.createTeam(event.getPlayer().getName(), (new App.ImmortalPrefixCommand()).swapColor(Integer.valueOf(Integer.parseInt((new App.Config(event.getPlayer().getUniqueId())).getPropertyString("immortal")))) + UtilColor.bold + "IMMORTAL ", ChatColor.WHITE);
//                scoreboard.addPlayer(event.getPlayer());
//            }
//        }
//    }

    class Config {
        UUID uuid;

        public Config(UUID uuid) {this.uuid = uuid;}

        public Object getProperty(String property) {return getConfig().get(uuid.toString() + "." + property);}

        public String getPropertyString(String property) {return Objects.requireNonNull(getConfig().get(uuid.toString() + "." + property)).toString();}

        public void setProperty(String property, String replacement) {getConfig().set(uuid.toString() + "." + property, replacement);}

        public void setInt(String property, Integer replacement) {
            getConfig().set(uuid.toString() + "." + property, replacement);
            saveConfig();
        }

        public void setString(String property, String replacement) {
            getConfig().set(uuid.toString() + "." + property, replacement);
            saveConfig();
        }
    }

    class LoadConfig implements Listener {
        @EventHandler
        public void onJoin(AsyncPlayerPreLoginEvent event) {
            Config config = new Config(event.getUniqueId());
            if (config.getProperty(event.getUniqueId().toString()) != null) return;
            config.setInt("immortal", 0);
            config.setInt("deaths", 0);
        }
    }

    class Chat implements Listener {
        @EventHandler
        public void onChat(AsyncPlayerChatEvent event) {
            event.setCancelled(true);
            String message = event.getMessage();
            Config config = new Config(event.getPlayer().getUniqueId());
            int x = Integer.parseInt(config.getPropertyString("immortal"));
            int level = Integer.parseInt(config.getPropertyString("deaths"));
            if (message.contains("%")) message.replaceAll("%", " "); // We have this to patch the % bug with chat async.
            if (message.startsWith("#")) Bukkit.broadcastMessage(UtilColor.white + UtilColor.bold + "TEAM " + new Levels().switchColor(level) + UtilColor.gray + event.getPlayer().getName() + UtilColor.white + " " + message.replaceFirst("#", ""));
            else if (message.startsWith("@")) {
                String newMessage1 = message.replaceFirst("@", "");
                if (newMessage1.contains("&l")) newMessage1.replaceAll("&l", "");
                String newMessage = newMessage1.replace("&", ChatColor.COLOR_CHAR + "");
                Bukkit.broadcastMessage(UtilColor.purple + UtilColor.bold + "PARTY " + new Levels().switchColor(level) + UtilColor.white + UtilColor.bold + event.getPlayer().getName() + " " + UtilColor.purple + newMessage);
            } else if (message.startsWith("!")) {
                String newMessage = message.replace("!", "");
                if (!event.getPlayer().isOp()) {
                    Bukkit.broadcastMessage(UtilColor.aqua + UtilColor.bold + "StaffRequest " + UtilColor.darkAqua + UtilColor.bold + event.getPlayer().getName() + " " + UtilColor.aqua + newMessage);
                } else {
                    Bukkit.broadcastMessage(UtilColor.aqua + UtilColor.bold + "StaffRequest " + UtilColor.gold + UtilColor.bold + "Mod CookieBilly " + UtilColor.aqua + newMessage);
                }
            } else if (event.getPlayer().getName().equalsIgnoreCase("daavide")) {
                Bukkit.broadcastMessage(new Levels().switchColor(level) + UtilColor.darkAqua + UtilColor.bold + "IDIOT " + UtilColor.gray + event.getPlayer().getName() + UtilColor.white + " " + message);
            } else if (event.getPlayer().isOp()) {
                Bukkit.broadcastMessage(new Levels().switchColor(level) + UtilColor.darkRed + event.getPlayer().getName() + UtilColor.white + " " + message);
            } else {
                System.out.println(x);
                System.out.println(level);
                switch (x) {
                    case 1:
                        Bukkit.broadcastMessage(new Levels().switchColor(level) + UtilColor.yellow + UtilColor.bold + "IMMORTAL " + UtilColor.gray + event.getPlayer().getName() + UtilColor.white + " " + message);
                        return;
                    case 2:
                        Bukkit.broadcastMessage((new Levels()).switchColor(level) + UtilColor.aqua + UtilColor.bold + "IMMORTAL " + UtilColor.gray + event.getPlayer().getName() + UtilColor.white + " " + message);
                        return;
                    case 3:
                        Bukkit.broadcastMessage((new Levels()).switchColor(level) + UtilColor.purple + UtilColor.bold + "IMMORTAL " + UtilColor.gray + event.getPlayer().getName() + UtilColor.white + " " + message);
                        return;
                    case 4:
                        Bukkit.broadcastMessage(new Levels().switchColor(level) + UtilColor.green + UtilColor.bold + "IMMORTAL " + UtilColor.gray + event.getPlayer().getName() + UtilColor.white + " " + message);
                        return;
                    case 5:
                        Bukkit.broadcastMessage(new Levels().switchColor(level) + UtilColor.red + UtilColor.bold + "IMMORTAL " + UtilColor.gray + event.getPlayer().getName() + UtilColor.white + " " + message);
                        return;
                    case 6:
                        Bukkit.broadcastMessage(new Levels().switchColor(level) + UtilColor.darkAqua + UtilColor.bold + "IMMORTAL " + UtilColor.gray + event.getPlayer().getName() + UtilColor.white + " " + message);
                        return;
                    case 7:
                        Bukkit.broadcastMessage(new Levels().switchColor(level) + UtilColor.black + UtilColor.bold + "IMMORTAL " + UtilColor.gray + event.getPlayer().getName() + UtilColor.white + " " + message);
                        return;
                    case 8:
                        Bukkit.broadcastMessage(new Levels().switchColor(level) + UtilColor.white + UtilColor.bold + "IMMORTAL " + UtilColor.gray + event.getPlayer().getName() + UtilColor.white + " " + message);
                        return;
                    case 9:
                        Bukkit.broadcastMessage(new Levels().switchColor(level) + UtilColor.darkBlue + UtilColor.bold + "IMMORTAL " + UtilColor.gray + event.getPlayer().getName() + UtilColor.white + " " + message);
                        return;
                    case 10:
                        Bukkit.broadcastMessage(new Levels().switchColor(level) + UtilColor.darkGreen + UtilColor.bold + "IMMORTAL " + UtilColor.gray + event.getPlayer().getName() + UtilColor.white + " " + message);
                        return;
                    case 11:
                        Bukkit.broadcastMessage(new Levels().switchColor(level) + UtilColor.gray + UtilColor.bold + "IMMORTAL " + UtilColor.gray + event.getPlayer().getName() + UtilColor.white + " " + message);
                        return;
                    case 12:
                        Bukkit.broadcastMessage(new Levels().switchColor(level) + UtilColor.darkGray + UtilColor.bold + "IMMORTAL " + UtilColor.gray + event.getPlayer().getName() + UtilColor.white + " " + message);
                        return;
                }
                Bukkit.broadcastMessage(new Levels().switchColor(level) + UtilColor.gray + event.getPlayer().getName() + UtilColor.white + " " + message);
            }
        }
    }

    static class Levels {
        public boolean isBetween(int x, int lower, int upper) {
            return (lower <= x && x <= upper);
        }

        public String switchColor(Integer level) {
            int x = 1;
            if (isBetween(level, 0, 19)) x = 1;
            else if (isBetween(level, 20, 39)) x = 2;
            else if (isBetween(level, 40, 59)) x = 3;
            else if (isBetween(level, 60, 79)) x = 4;
            else if (isBetween(level, 80, 99)) x = 5;
            else if (isBetween(level, 100, 119)) x = 6;
            else if (isBetween(level, 120, 139)) x = 7;
            else if (isBetween(level, 140, 159)) x = 8;
            else if (isBetween(level, 160, 179)) x = 9;
            else if (isBetween(level, 160, 179)) x = 10;
            else if (isBetween(level, 180, 199)) x = 11;
            else if (level == 200 || level > 6969 || level < 6968) x = 12;
            else if (level == 6969) x = 13;

            switch (x) {
                case 1: return UtilColor.gray + level + " ";
                case 2: return UtilColor.blue + level + " ";
                case 3: return UtilColor.darkGreen + level + " ";
                case 4: return UtilColor.gold + level + " ";
                case 5: return UtilColor.red + level + " ";
                case 6: return UtilColor.darkRed + level + " ";
                case 7: return UtilColor.aqua + level + " ";
                case 8: return UtilColor.white + level + " ";
                case 9: return UtilColor.darkBlue + level + " ";
                case 10: return UtilColor.darkGray + level + " ";
                case 11: return UtilColor.darkPurple + level + " ";
                case 12: return UtilColor.aqua + UtilColor.bold + level + " ";
                case 13: return UtilColor.red + UtilColor.bold + "6" + UtilColor.green + UtilColor.bold + "9" + UtilColor.red + UtilColor.bold + "6" + UtilColor.green + UtilColor.bold + "9 ";
            }
            return null;
        }
    }

    class RegisterDeath implements Listener {
        @EventHandler
        public void onDeath(PlayerDeathEvent event) {
            event.setDeathMessage(UtilColor.blue + "Death> " + UtilColor.red + event.getEntity().getName() + UtilColor.gray + " died due to " + UtilColor.yellow + "being an idiot" + UtilColor.gray + ".");
            new Config(event.getEntity().getUniqueId()).setInt("deaths", Math.addExact((Integer) new Config(event.getEntity().getUniqueId()).getProperty("deaths"), 1));
        }
    }
}
