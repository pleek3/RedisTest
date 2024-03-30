package com.minequest.redistest.services;

import org.redisson.api.RFuture;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayerManagementService {

    private final RMap<String, Integer> serverOnlinePlayersMap;

    @Autowired
    public PlayerManagementService(RedissonClient client) {
        this.serverOnlinePlayersMap = client.getMap("onlinePlayers");
    }

    public void updateOnlinePlayers(String server, int onlinePlayers) {
        this.serverOnlinePlayersMap.put(server, onlinePlayers);
    }

    public int getOnlinePlayers(String server) {
        return this.serverOnlinePlayersMap.getOrDefault(server, 0);
    }

    public RFuture<Integer> getOnlinePlayersAsync(String server) {
        return this.serverOnlinePlayersMap.getAsync(server);
    }


}
