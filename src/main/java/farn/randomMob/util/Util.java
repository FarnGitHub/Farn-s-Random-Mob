package farn.randomMob.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.biome.Biome;

import java.io.InputStream;
import java.util.*;

public class Util {

    private static final Map<String, TextureRule> textureRule = new HashMap<>();
    private static final List<String> cachedTextures = new ArrayList<>();

    public static String getRandomTexture(LivingEntity ent, String backup) {
        return textureRule.computeIfAbsent(backup, TextureRule::new)
                .getTexture(ent.randomMob_getBiome(), ent.randomMob_getID());
    }

    public static void clearTextureRule() {
        textureRule.clear();
        TextureManager manager = Minecraft.INSTANCE.textureManager;
        for(String texture : cachedTextures) {
            manager.delete(manager.getTextureId(texture));
            manager.textures.remove(texture);
        }
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
        boolean hasResource = getResource(resource) != null;
        if(hasResource) {
            cachedTextures.add(resource);
        }
        return hasResource;
    }

    public static String[] getOldFormatVariants(String texture) {
        int dot = texture.lastIndexOf('.');
        if (dot < 0) return new String[]{texture};

        String prefix = texture.substring(0, dot);
        String suffix = texture.substring(dot);
        List<String> list = new ArrayList<>();
        list.add(texture);

        for (int i = 2; i < 100; i++) {
            String candidate = prefix + i + suffix;
            if(!hasResource(candidate)) break;
            list.add(candidate);
        }
        return list.toArray(new String[0]);
    }

    public static String getBiome(Entity e) {
        Biome biome = e.world.method_1781().getBiome((int)e.x, (int)e.z);
        return  (biome != null && biome.name != null) ? biome.name : "default";
    }

    public static String getBiome(PlayerEntity player) {
        int x = player.getSpawnPos().x;
        int z = player.getSpawnPos().z;
        Biome biome = player.world.method_1781().getBiome(x, z);
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
