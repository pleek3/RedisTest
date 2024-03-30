package com.minequest.redistest.configuration;

import io.papermc.paper.adventure.providers.MiniMessageProviderImpl;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.scheduler.BukkitScheduler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

@Configuration
@EnableCaching
@ComponentScan(value = "com.minequest")
@ConditionalOnClass({Bukkit.class})
class SpigotBeansConfiguration {

    @Bean
    AuditorAware<String> auditorProvider() {
        return () -> Optional.of("System");
    }

    @Bean
    Server serverBean() {
        return Bukkit.getServer();
    }

    @Bean
    BukkitScheduler schedulerBean(Server server) {
        return server.getScheduler();
    }




}