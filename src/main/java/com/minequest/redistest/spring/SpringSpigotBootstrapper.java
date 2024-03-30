package com.minequest.redistest.spring;

import lombok.NoArgsConstructor;
import org.bukkit.plugin.java.JavaPlugin;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.DefaultResourceLoader;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by YannicK S. on 04.03.2022
 */
@NoArgsConstructor
public class SpringSpigotBootstrapper {

    /**
     * Initialize a Spring application context for a plugin.
     *
     * @param plugin           The plugin that is using the Spring Framework.
     * @param applicationClass The class that contains the main method.
     * @return A ConfigurableApplicationContext
     */
    public static ConfigurableApplicationContext initialize(JavaPlugin plugin, Class<?> applicationClass) throws ExecutionException, InterruptedException {
        CompoundClassLoader classLoader = new CompoundClassLoader(Arrays.asList(plugin.getClass().getClassLoader(), Thread.currentThread().getContextClassLoader()));
        SpringApplicationBuilder builder = new SpringApplicationBuilder(applicationClass);
        builder.web(WebApplicationType.NONE);
        return initialize(plugin, classLoader, builder);
    }

    /**
     * It creates a new thread, sets the classloader to the plugin's classloader, creates a new properties file if it
     * doesn't exist, loads the properties file, sets the resource loader to the plugin's classloader, and then runs the
     * SpringApplicationBuilder
     *
     * @param plugin      The JavaPlugin instance
     * @param classLoader The classloader that will be used to load the Spring Application Context.
     * @param builder     The SpringApplicationBuilder that will be used to create the application context.
     * @return A ConfigurableApplicationContext
     */
    public static ConfigurableApplicationContext initialize(JavaPlugin plugin, ClassLoader classLoader, SpringApplicationBuilder builder) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            Future<ConfigurableApplicationContext> contextFuture = executor.submit(() -> {
                Thread.currentThread().setContextClassLoader(classLoader);

                if (!plugin.getDataFolder().exists())
                    plugin.getDataFolder().mkdirs();

                Properties props = new Properties();
                try {
                    File file = new File(plugin.getDataFolder() + "/spring.properties");
                    if (!file.exists())
                        file.createNewFile();

                    props.load(new FileInputStream(file));
                } catch (Exception ignored) {
                }

                if (builder.application().getResourceLoader() == null) {
                    DefaultResourceLoader loader = new DefaultResourceLoader(classLoader);
                    builder.resourceLoader(loader);
                }

                return builder.properties(props)
                        .run();
            });
            return contextFuture.get();
        } finally {
            executor.shutdown();
        }
    }

}
