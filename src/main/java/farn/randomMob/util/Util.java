package farn.randomMob.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.world.biome.Biome;

import java.io.InputStream;
import java.util.*;

public final class Util {

    private Util() {
    }

    public static String[] splitAndTrim(String lines) {
        String[] split = lines.split(",");
        for(int index = 0; index < split.length; index++)
            split[index] = split[index].trim();
        return split;
    }

    public static InputStream getResource(String resource) {
        try {
            return Minecraft.INSTANCE.texturePacks.selected.getResource(resource);
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean hasResource(String resource) {
        return getResource(resource) != null;
    }

    public static String getBiome(Entity e) {
        Biome biome = Minecraft.INSTANCE.world.method_1781().getBiome((int)e.x, (int)e.z);
        return  (biome != null && biome.name != null) ? biome.name : "default";
    }

    public static String validateBiomeId(String biome) {
        if(biome == null || biome.isEmpty())
            biome = "default";

        if(!biome.startsWith("biome."))
            biome = "biome." + biome;

        biome = biome.replace(" ", "_");

        return biome;
    }
}
