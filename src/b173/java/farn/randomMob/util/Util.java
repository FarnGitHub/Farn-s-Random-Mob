package farn.randomMob.util;

import farn.randomMob.RandomMob;
import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.Entity;

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
            return RandomMob.getMinecraft().texturePackList.selectedTexturePack.getResourceAsStream(resource);
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean hasResource(String resource) {
        return getResource(resource) != null;
    }

    public static String getBiome(Entity e) {
        BiomeGenBase biome = RandomMob.getMinecraft().theWorld.getWorldChunkManager().getBiomeGenAt((int)e.posX, (int)e.posZ);
        return  (biome != null && biome.biomeName != null) ? biome.biomeName : "default";
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
