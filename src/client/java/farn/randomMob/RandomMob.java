package farn.randomMob;

import net.minecraft.client.Minecraft;
import net.minecraft.src.*;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
                entity.skinUrl = entity.entityId + "_" + getBiomeForEntity(entity).toLowerCase();
            } else {
                EntitySaveSkinNBT id = (EntitySaveSkinNBT) entity;
                if("unknown".equals(id.getBiomeSpawn())) {
                    id.setBiomeSpawn(getBiomeForEntity(entity));
                }
                entity.skinUrl = id.getEntitySkinID() + "_" + id.getBiomeSpawn().toLowerCase();
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
                    return getTextureNormal(chosen);
                }
            }
        }
        String[] variants = textureVariantsCache.computeIfAbsent(baseTexture, RandomMob::getTextureVariants);
        if (variants.length > 1) {
            int idx = entityId % variants.length;
            if (!variants[idx].equals(variants[0])) {
                return getTextureNormal(variants[idx]);
            }
        }
        return -1;
    }

    private static Properties getPropertiesForTexture(String texture) {
        return propertiesCache.computeIfAbsent(texture, RandomMob::loadProperties);
    }

    private static Properties loadProperties(String baseTexture) {
        String path = baseTexture.replace(".png", ".properties");
        try (InputStream in = getInputStream(path)) {
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
            try (InputStream in = getInputStream(candidate)) {
                if (in == null) break;
                list.add(candidate);
            } catch (IOException e) {
                break;
            }
        }
        return list.toArray(new String[0]);
    }

    public static InputStream getInputStream(String resource) {
        return getMinecraftInstance().texturePackList.selectedTexturePack.getResourceAsStream(resource);
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

    public static int getTextureNormal(String tex) {
        return getMinecraftInstance().renderEngine.getTexture(tex);
    }

    public static boolean doesClassExist(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static boolean isUsingShader() {
        if(RandomMob.doesClassExist("mod_GLSL")) {
            try {
                Class shaderToggle = Class.forName("net.mine_diver.glsl.ToggleShaders");
                String currentShader = (String)getPrivateValue(shaderToggle, (Object)null, "currentShaders");
                return !currentShader.equals("default");
            } catch(Exception e) {
                e.printStackTrace();
                return false;
            }

        }
        return false;
    }

    public static void removeShader() {
        try {
            Class shader = Class.forName("net.mine_diver.glsl.Shaders");
            Method method = shader.getDeclaredMethod("destroy");
            method.invoke((Object)null);
            setPrivateValue(shader, (Object)null, "isInitialized", false);
            System.out.println("Farn RandomMob Remove Shader");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }


    public static void setPrivateValue(RenderEngine engine, String fieldName, Object value) {
        try {
            setPrivateValue(RenderEngine.class, engine, fieldName, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void setPrivateValue(Class instanceclass, Object instance, String field, Object value) {
        try {
            Field e = instanceclass.getDeclaredField(field);
            e.setAccessible(true);
            e.set(instance, value);
        } catch (Exception illegalAccessException6) {
        }

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

    public static BufferedImage readTextureImageCustom(String value) {
        try {
            Class config = Class.forName("com.pclewis.mcpatcher.mod.TextureUtils");
            Method method = config.getDeclaredMethod("getResourceAsBufferedImage", String.class);
            return (BufferedImage)method.invoke((Object)null, value);
        } catch(Exception e) {
            return null;
        }
    }

    public static void OFsetFontRenderer(boolean value) {
        try {
            Class config = Class.forName("Config");
            Method method = config.getDeclaredMethod("setFontRendererUpdated", boolean.class);
            method.invoke((Object)null, value);
        } catch(Exception e) {
        }
    }

    public static void OFclearTextureDataMap(RenderEngine engine) {
        try {
            Field privateObjectField = RenderEngine.class.getDeclaredField("textureDataMap");
            privateObjectField.setAccessible(true);
            Object privateObjectInstance = privateObjectField.get(engine);
            Class<?> innerClass = privateObjectInstance.getClass();
            Method publicVoidMethod = innerClass.getDeclaredMethod("clear");
            publicVoidMethod.invoke(privateObjectInstance);
        } catch(Exception e) {
        }
    }
    public static String getBiomeForEntity(Entity e) {
        if (e == null || e.worldObj == null) return "unknown";
        int x = MathHelper.floor_double(e.posX);
        int z = MathHelper.floor_double(e.posZ);
        BiomeGenBase biome = e.worldObj.getWorldChunkManager().getBiomeGenAt(x, z);
        return (biome != null && biome.biomeName != null) ? biome.biomeName : "unknown";
    }

}
