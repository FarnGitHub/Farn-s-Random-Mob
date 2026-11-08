package farn.randomMob;

import farn.randomMob.impl.EntityImpl;
import farn.randomMob.impl.RenderEngineImpl;
import farn.randomMob.util.Util;
import farn.randomMob.util.TextureRule;
import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.RenderEngine;

import java.lang.reflect.Field;
import java.util.*;

public final class RandomMob {

    private static final Map<String, TextureRule> textureRule = new HashMap<>();
    public static final List<String> cachedTextures = new ArrayList<>();
    private static Minecraft mc;

    private RandomMob() {
    }

    public static void clearTextures() {
        textureRule.clear();
        cachedTextures.forEach(RandomMob::delete);
        cachedTextures.clear();
    }

    public static String getTexture(EntityLiving entLiving, String backup) {
        EntityImpl ent = (EntityImpl) entLiving;
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
        cachedTextures.addAll(texture);
    }

    public static void addToCache(String texture) {
        cachedTextures.add(texture);
    }

    public static void delete(String texture) {
        RenderEngine manager = mc.renderEngine;
        manager.deleteTexture(manager.getTexture(texture));
        ((RenderEngineImpl)manager).randomMob_deleteTexture(texture);
    }

    public static Minecraft getMinecraft() {
        if(mc == null) {
            try {
                ThreadGroup e = Thread.currentThread().getThreadGroup();
                int count = e.activeCount();
                Thread[] threads = new Thread[count];
                e.enumerate(threads);

                for (Thread thread : threads) {
                    if (thread.getName().equals("Minecraft main thread")) {
                        Field threadField = Thread.class.getDeclaredField("target");
                        threadField.setAccessible(true);
                        mc = (Minecraft) threadField.get(thread);
                        break;
                    }
                }
            } catch (Exception securityException4) {
                throw new RuntimeException(securityException4);
            }
        }
        return mc;
    }

}
