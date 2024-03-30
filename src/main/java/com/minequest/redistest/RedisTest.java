package com.minequest.redistest;

import com.minequest.redistest.spring.SpringSpigotBootstrapper;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.ExecutionException;

@Getter
@Setter
public final class RedisTest extends JavaPlugin {

    @Getter
    private static RedisTest instance;

    private boolean contextInitialized = false;

    private ConfigurableApplicationContext applicationContext;

    @Override
    public void onEnable() {
        instance = this;
        initializeContext();
        registerListener();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void initializeContext() {
        try {
            applicationContext = SpringSpigotBootstrapper.initialize(this, Application.class);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void registerListener() {
        getApplicationContext().getBeansOfType(Listener.class).forEach(
                (beanName, listener) -> {
                    System.out.println("REGISTER " + beanName);
                    getServer().getPluginManager().registerEvents(listener, this);
                });
    }

}
