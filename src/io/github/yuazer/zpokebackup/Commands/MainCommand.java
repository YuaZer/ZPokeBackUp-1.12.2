package io.github.yuazer.zpokebackup.Commands;

import io.github.yuazer.zpokebackup.Main;
import io.github.yuazer.zpokebackup.Utils.PokeUtils;
import io.github.yuazer.zpokebackup.Utils.YamlUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

public class MainCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("zpokebackup")) {
            if (args.length == 0 || args[0].equalsIgnoreCase("help") && sender.isOp()) {
                sender.sendMessage("§b/zpokebackup §a简写-> §b/zpbu");
                sender.sendMessage("§b/zpokebackup savePC 玩家ID §a保存玩家PC宝可梦数据");
                sender.sendMessage("§b/zpokebackup savePoke 玩家ID §a保存玩家背包宝可梦数据");
                sender.sendMessage("§b/zpokebackup loadPC 玩家ID §a加载玩家PC宝可梦数据");
                sender.sendMessage("§b/zpokebackup loadPoke 玩家ID §a加载玩家背包宝可梦数据");
                sender.sendMessage("§b/zpokebackup reload §a重载配置文件");
                return true;
            }
            if (args[0].equalsIgnoreCase("reload") && sender.isOp()) {
                Main.getInstance().reloadConfig();
                sender.sendMessage(YamlUtils.getConfigMessage("Message.reload"));
                return true;
            }
            if (args[0].equalsIgnoreCase("savePC") && sender.isOp()) {
                Player player = Bukkit.getPlayer(args[1]);
                if (player != null) {
                    try {
                        PokeUtils.savePlayerPCData(player);
                        sender.sendMessage(YamlUtils.getConfigMessage("Message.pcSaveSuccess"));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("savePoke") && sender.isOp()) {
                Player player = Bukkit.getPlayer(args[1]);
                if (player != null) {
                    try {
                        PokeUtils.savePlayerPokeData(player);
                        sender.sendMessage(YamlUtils.getConfigMessage("Message.pcSaveSuccess"));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("loadPC") && sender.isOp()) {
                Player player = Bukkit.getPlayer(args[1]);
                if (player != null) {
                    try {
                        PokeUtils.loadPCData(player);
                        sender.sendMessage(YamlUtils.getConfigMessage("Message.pcLoadSuccess"));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("loadPoke") && sender.isOp()) {
                Player player = Bukkit.getPlayer(args[1]);
                if (player != null) {
                    try {
                        PokeUtils.loadPokeData(player);
                        sender.sendMessage(YamlUtils.getConfigMessage("Message.pokeLoadSuccess"));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                return true;
            }
        }
        return false;
    }
}
