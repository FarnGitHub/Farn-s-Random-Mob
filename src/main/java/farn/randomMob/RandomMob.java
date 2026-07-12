package farn.randomMob;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.biome.Biome;

import java.io.InputStream;

import net.modificationstation.stationapi.api.client.event.resource.TexturePackLoadedEvent;
import net.modificationstation.stationapi.api.client.texture.TextureHelper;
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

    public static final Object2ObjectMap<String, TextureRule> textureRule = new Object2ObjectOpenHashMap<>();

    public static String getRandomMobTexture(LivingEntity entity, String baseTexture) {
        String biome = entity.randomMob_getBiome();
        biome = "biome." + biome.toLowerCase().replace(' ', '_');
        int entityId = entity.randomMob_getID();

        TextureRule rule = getTextureRule(baseTexture);
        if(rule != null) {
            String randomTex = rule.get(biome, entityId);
            if(hasResource(randomTex))
                return rule.get(biome, entityId);
        }
        return baseTexture;
    }

    private static TextureRule getTextureRule(String tex) {
        return textureRule.computeIfAbsent(tex, p -> new TextureRule(tex));
    }

    public static InputStream getResource(String resource) {
        try {
            return TextureHelper.getTextureStream(resource);
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean hasResource(String resource) {
         return RandomMob.getResource(resource) != null;
    }

    public static String getEntityCurrentBiome(Entity e) {
        if (e == null || e.world == null) return "default";
        Biome biome = e.world.method_1781().getBiome((int)e.x, (int)e.z);
        return (biome != null && biome.name != null) ? biome.name : "default";
    }

    @SuppressWarnings("unused")
    @EventListener
    public void clearTextureCacheStapi(TexturePackLoadedEvent.After after) {
        textureRule.clear();
    }

}
