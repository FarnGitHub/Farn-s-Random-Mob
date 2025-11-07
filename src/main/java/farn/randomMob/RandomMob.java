package farn.randomMob;

import farn.randomMob.mixin.accessor.MinecraftAccessor;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import net.modificationstation.stationapi.api.client.event.resource.TexturePackLoadedEvent;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.Null;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import org.apache.logging.log4j.Logger;

public class RandomMob {

    @SuppressWarnings("unused")
    @Entrypoint.Namespace
    public static Namespace NAMESPACE;

    @SuppressWarnings("unused")
    @Entrypoint.Logger
    public static Logger LOGGER = Null.get();

    //properties list
    private static final Map<String, Properties> propertiesCache = new HashMap<>();
    //old texture variant list
    private static final Map<String, String[]> textureVariantsCache = new HashMap<>();

    //apply skin url for non player entity
    public static void setRandomMobSkinURL(Entity entity) {
        if (entity.skinUrl == null && entity instanceof LivingEntity && !(entity instanceof PlayerEntity)) {
            if (entity.world.isRemote) {
                entity.skinUrl = "randommob_" + entity.id + "_" + getBiomeForEntity(entity).toLowerCase();
            } else {
                EntityRandomMobData id = (EntityRandomMobData) entity;
                if("unknown".equals(id.randommob_getBiome())) {
                    id.randommob_setBiome(getBiomeForEntity(entity));
                }
                entity.skinUrl = "randommob_" + id.randommob_getID() + "_" + id.randommob_getBiome().toLowerCase();
            }
        }
    }

    //clear cache when changing texturepack
    public static void clearRandomMobTextureCache() {
        propertiesCache.clear();
        textureVariantsCache.clear();
    }

    //get custom variant texture by using properties file for the baseTexture or using the old way of getting texture
    public static int getRandomMobTexture(String skinUrl, String baseTexture) {
        if (!skinUrl.startsWith("randommob_") || baseTexture == null) return -1;
        skinUrl = skinUrl.substring("randommob_".length());

        String[] parts = skinUrl.split("_", 2);
        String idPart = parts[0];
        String biome = parts.length > 1 ? parts[1] : "unknown";

        int entityId;
        try {
            entityId = Math.abs(Integer.parseInt(idPart));
        } catch (NumberFormatException e) {
            entityId = Math.abs(skinUrl.hashCode()); // fallback
        }

        Properties props = getPropertiesForTexture(baseTexture);
        if (props != null) {
            String key = "biome." + biome.toLowerCase().replace(' ', '_');

            String rule = props.getProperty(key);
            if (rule == null) rule = props.getProperty("biome.default");

            if (rule != null) {
                String[] variants = Arrays.stream(rule.split(","))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .toArray(String[]::new);
                if (variants.length > 0) {
                    String chosen = variants[entityId % variants.length];
                    if(hasResource(chosen)) {
                        return getTextureId(chosen);
                    }
                }
            }
        }
        String[] variants = textureVariantsCache.computeIfAbsent(baseTexture, RandomMob::getTextureVariants);
        if (variants.length > 1) {
            int idx = entityId % variants.length;
            if(hasResource(variants[idx])) {
                return getTextureId(variants[idx]);
            }
        }
        return -1;
    }

    private static Properties getPropertiesForTexture(String texture) {
        return propertiesCache.computeIfAbsent(texture, RandomMob::loadProperties);
    }

    private static Properties loadProperties(String baseTexture) {
        String path = baseTexture.replace(".png", ".properties");
        try (InputStream in = getResource(path)) {
            if (in != null) {
                Properties p = new Properties();
                p.load(in);
                return p;
            }
        } catch (IOException ignored) {}
        return null;
    }

    private static String[] getTextureVariants(String texture) {
        int dot = texture.lastIndexOf('.');
        if (dot < 0) return new String[]{texture};

        String prefix = texture.substring(0, dot);
        String suffix = texture.substring(dot);
        List<String> list = new ArrayList<>();
        list.add(texture);

        for (int i = 2; i < 1000; i++) {
            String candidate = prefix + i + suffix;
            try (InputStream in = getResource(candidate)) {
                if (in == null) break;
                list.add(candidate);
            } catch (IOException e) {
                break;
            }
        }
        return list.toArray(new String[0]);
    }

    public static InputStream getResource(String resource) {
        try {
            return getMinecraft().texturePacks.selected.getResource(resource);
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean hasResource(String resource) {
         return RandomMob.getResource(resource) != null;
    }

    public static Minecraft getMinecraft() {
        return MinecraftAccessor.getInstance();
    }

    public static int getTextureId(String tex) {
        return getMinecraft().textureManager.getTextureId(tex);
    }

    public static String getBiomeForEntity(Entity e) {
        if (e == null || e.world == null) return "unknown";
        Biome biome = e.world.method_1781().getBiome(MathHelper.floor(e.x), MathHelper.floor(e.z));
        return (biome != null && biome.name != null) ? biome.name : "unknown";
    }

    @SuppressWarnings("unused")
    @EventListener
    public void clearTextureCache(TexturePackLoadedEvent.After after) {
        RandomMob.clearRandomMobTextureCache();
    }

}
