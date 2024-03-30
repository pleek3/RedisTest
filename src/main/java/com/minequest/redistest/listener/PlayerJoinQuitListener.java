package com.minequest.redistest.listener;

import com.minequest.redistest.RedisTest;
import com.minequest.redistest.services.PlayerManagementService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PlayerJoinQuitListener implements Listener {

    private final static String SERVER_ID = "pvp";

    private final PlayerManagementService playerManagementService;

    @Autowired
    public PlayerJoinQuitListener(PlayerManagementService service) {
        this.playerManagementService = service;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        Bukkit.getScheduler().runTaskAsynchronously(RedisTest.getInstance(), () -> {
            int newPlayerCount = this.playerManagementService.getOnlinePlayers(SERVER_ID) + 1;
            this.playerManagementService.updateOnlinePlayers(SERVER_ID, newPlayerCount);
            player.sendMessage("Onlinespieler: " + this.playerManagementService.getOnlinePlayers(SERVER_ID));
        });

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        Bukkit.getScheduler().runTaskAsynchronously(RedisTest.getInstance(), () -> {
            int newPlayerCount = this.playerManagementService.getOnlinePlayers(SERVER_ID) - 1;
            this.playerManagementService.updateOnlinePlayers(SERVER_ID, newPlayerCount);
            player.sendMessage("Onlinespieler: " + this.playerManagementService.getOnlinePlayers(SERVER_ID));
        });
    }


}
