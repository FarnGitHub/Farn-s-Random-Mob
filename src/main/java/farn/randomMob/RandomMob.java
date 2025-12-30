package farn.randomMob;

import net.minecraft.client.Minecraft;
import net.minecraft.src.*;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;

public class RandomMob {

    //properties list
    private static final Map<String, Properties> propertiesCache = new HashMap<>();
    //old texture variant list
    private static final Map<String, String[]> textureVariantsCache = new HashMap<>();
    //minecraft instance
    private static Minecraft mc;

    //apply skin url for non player entity
    public static void entityLoaded(Entity entity) {
        if (entity.skinUrl == null && entity instanceof EntityLiving && !(entity instanceof EntityPlayer)) {
            if (entity.worldObj.multiplayerWorld) {
                entity.skinUrl = entity.field_620_ab + "_" + getBiomeForEntity(entity).toLowerCase();
            } else {
                EntitySaveSkinNBT id = (EntitySaveSkinNBT) entity;
                if("unknown".equals(id.randommob_getBiome())) {
                    id.randommob_setBiome(getBiomeForEntity(entity));
                }
                entity.skinUrl = id.randommob_getId() + "_" + id.randommob_getBiome().toLowerCase();
            }
        }
    }

    //clear cache when changing texturepack
    public static void clearTextureCache() {
        propertiesCache.clear();
        textureVariantsCache.clear();
    }

    //get custom variant texture by using properties file for the baseTexture or using the old way of getting texture
    public static int getTextureRandomMob(String skinUrl, String baseTexture) {
        if (skinUrl == null || baseTexture == null) return -1;
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
                    return getTextureID(chosen);
                }
            }
        }
        String[] variants = textureVariantsCache.computeIfAbsent(baseTexture, RandomMob::getTextureVariants);
        if (variants.length > 1) {
            int idx = entityId % variants.length;
            if (!variants[idx].equals(variants[0])) {
                return getTextureID(variants[idx]);
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
        return getMinecraftInstance().texturePackList.selectedTexturePack.func_6481_a(resource);
    }

    public static Minecraft getMinecraftInstance() {
        if(mc == null) {
            try {
                ThreadGroup e = Thread.currentThread().getThreadGroup();
                int count = e.activeCount();
                Thread[] threads = new Thread[count];
                e.enumerate(threads);

                for(int i = 0; i < threads.length; ++i) {
                    if(threads[i].getName().equals("Minecraft main thread")) {
                        mc = (Minecraft)getPrivateValue(Thread.class, threads[i], "target");
                        break;
                    }
                }
            } catch (SecurityException securityException4) {
                throw new RuntimeException(securityException4);
            }
        }

        return mc;
    }

    public static int getTextureID(String tex) {
        return getMinecraftInstance().renderEngine.getTexture(tex);
    }

    private static Object getPrivateValue(Class instanceclass, Object instance, String field) {
        try {
            Field e = instanceclass.getDeclaredField(field);
            e.setAccessible(true);
            return e.get(instance);
        } catch (Exception illegalAccessException4) {
            return null;
        }
    }

    public static String getBiomeForEntity(Entity e) {
        if (e == null || e.worldObj == null) return "unknown";
        int x = MathHelper.floor_double(e.posX);
        int z = MathHelper.floor_double(e.posZ);
        MobSpawnerBase biome = e.worldObj.func_4075_a().func_4073_a(x, z);
        return (biome != null && biome.biomeName != null) ? biome.biomeName : "unknown";
    }

}
