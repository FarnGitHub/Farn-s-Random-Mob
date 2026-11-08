package farn.randomMob;

import farn.randomMob.util.Util;
import farn.randomMob.util.TextureRule;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.entity.LivingEntity;

import java.util.*;

public final class RandomMob {

    private static final boolean noStapi = !FabricLoader.getInstance().isModLoaded("stationapi");

    private static final Map<String, TextureRule> textureRule = new HashMap<>();
    public static final List<String> cachedTextures = new ArrayList<>();

    private RandomMob() {
    }

    public static void clearTextures() {
        textureRule.clear();
        cachedTextures.forEach(RandomMob::delete);
        cachedTextures.clear();
    }

    public static String getTexture(LivingEntity ent, String backup) {
        return textureRule.computeIfAbsent(backup, TextureRule::new)
                .getTexture(ent.randomMob_getBiome(), ent.randomMob_getID());
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
            if(Util.hasResource(candidate))  {
                list.add(candidate);
                addToCache(candidate);
            } else break;
        }
        return list.toArray(new String[0]);
    }

    public static void addToCache(Collection<String> texture) {
        if(noStapi) cachedTextures.addAll(texture);
    }

    public static void addToCache(String texture) {
        if(noStapi) cachedTextures.add(texture);
    }

    public static void delete(String texture) {
        TextureManager manager = Minecraft.INSTANCE.textureManager;
        manager.delete(manager.getTextureId(texture));
        manager.textures.remove(texture);
    }

}
