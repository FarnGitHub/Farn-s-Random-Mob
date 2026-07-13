package farn.randomMob;

import farn.randomMob.util.TextureRule;
import farn.randomMob.util.Util;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.entity.LivingEntity;

import net.modificationstation.stationapi.api.client.event.resource.TexturePackLoadedEvent;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.Null;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class RandomMob {

    @SuppressWarnings("unused")
    @Entrypoint.Namespace
    public static Namespace NAMESPACE;

    @SuppressWarnings("unused")
    @Entrypoint.Logger
    public static Logger LOGGER = Null.get();

    public static final Map<String, TextureRule> textureRule = new HashMap<>();

    public static String getRandomMobTexture(LivingEntity entity, String baseTexture) {
        String biome = entity.randomMob_getBiome();
        biome = "biome." + biome.toLowerCase().replace(' ', '_');
        int entityId = entity.randomMob_getID();

        TextureRule rule = getTextureRule(baseTexture);
        if(rule != null) {
            String randomTex = rule.getTexture(biome, entityId);
            if(Util.hasResource(randomTex))
                return randomTex;
        }
        return baseTexture;
    }

    private static TextureRule getTextureRule(String tex) {
        return textureRule.computeIfAbsent(tex, TextureRule::new);
    }

    @SuppressWarnings("unused")
    @EventListener
    public void clearTextureCacheStapi(TexturePackLoadedEvent.After after) {
        textureRule.clear();
    }

}
