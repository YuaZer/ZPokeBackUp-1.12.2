package io.github.yuazer.zpokebackup;

import io.github.yuazer.zpokebackup.Commands.MainCommand;
import io.github.yuazer.zpokebackup.Utils.PokeUtils;
import io.github.yuazer.zpokebackup.Utils.YamlUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;

public class Main extends JavaPlugin {
    private static Main instance;

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        this.getLogger().info("§a宝可梦备份系统-§b1.12.2");
        logLoaded(this);
        saveDefaultConfig();
        Bukkit.getPluginCommand("zpokebackup").setExecutor(new MainCommand());
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!YamlUtils.getConfigMessage("Message.startSave").isEmpty()){
                    System.out.println(YamlUtils.getConfigMessage("Message.startSave"));
                }
                for (Player player : Bukkit.getOnlinePlayers()) {
                    try {
                        PokeUtils.savePlayerPCData(player);
                        PokeUtils.savePlayerPokeData(player);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                if (!YamlUtils.getConfigMessage("Message.overSave").isEmpty()){
                    System.out.println(YamlUtils.getConfigMessage("Message.overSave"));
                }
            }
        }.runTaskTimerAsynchronously(this, 0L, YamlUtils.getConfigInt("GlobalSetting.time") * 20L);
    }

    @Override
    public void onDisable() {
        logDisable(this);
    }

    public static void logLoaded(JavaPlugin plugin) {
        Bukkit.getLogger().info(String.format("§e[§b%s§e] §f已加载", plugin.getName()));
        Bukkit.getLogger().info("§b作者:§eZ菌");
        Bukkit.getLogger().info("§b版本:§e" + plugin.getDescription().getVersion());
    }

    public static void logDisable(JavaPlugin plugin) {
        Bukkit.getLogger().info(String.format("§e[§b%s§e] §c已卸载", plugin.getName()));
    }
}
