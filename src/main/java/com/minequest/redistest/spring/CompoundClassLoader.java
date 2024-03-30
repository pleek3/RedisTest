package com.minequest.redistest.spring;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

/**
 * Created by YannicK S. on 04.03.2022
 */
@Getter
@RequiredArgsConstructor
public class CompoundClassLoader extends ClassLoader {

    private final Collection<ClassLoader> classLoaders;

    @Nullable
    @Override
    public URL getResource(String name) {
        return this.classLoaders.stream()
                .filter(Objects::nonNull)
                .map(classLoader -> classLoader.getResource(name))
                .filter(Objects::nonNull).findFirst().orElse(null);
    }

    @Nullable
    @Override
    public InputStream getResourceAsStream(String name) {
        return this.classLoaders.stream()
                .filter(Objects::nonNull)
                .map(classLoader -> classLoader.getResourceAsStream(name))
                .filter(Objects::nonNull).findFirst().orElse(null);
    }

    @Override
    public Enumeration<URL> getResources(String name) {
        List<URL> urls = new ArrayList<>();
        this.classLoaders.stream()
                .filter(Objects::nonNull)
                .map(classLoader -> {
                    try {
                        return classLoader.getResources(name);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .filter(urlEnumeration -> Objects.nonNull(urlEnumeration) && urlEnumeration.hasMoreElements())
                .map(Enumeration::nextElement)
                .filter(url -> Objects.nonNull(url) && !urls.contains(url))
                .forEach(urls::add);

        return Collections.enumeration(urls);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        for (ClassLoader loader : classLoaders) {
            if (loader != null) {
                try {
                    return loader.loadClass(name);
                } catch (ClassNotFoundException ignore) {
                }
            }
        }
        throw new ClassNotFoundException();
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        return loadClass(name);
    }

    @Override
    public String toString() {
        return String.format("CompoundClassloader %s", classLoaders);
    }
}
