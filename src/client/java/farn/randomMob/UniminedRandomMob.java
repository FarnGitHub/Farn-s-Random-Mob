package farn.randomMob;

import net.minecraft.client.Minecraft;
import net.minecraft.src.*;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UniminedRandomMob {

    private static Map textureVariantsMap = new HashMap();
    private static Minecraft mc;

    public static void entityLoaded(Entity entity) {
        if(entity.skinUrl == null) {
            if(entity instanceof EntityLiving) {
                if(!(entity instanceof EntityPlayer)) {
                    int randomId = entity.entityId;
                    entity.skinUrl = "" + randomId;
                }
            }
        }
    }


    public static void clearTextureCache() {
        textureVariantsMap.clear();
    }

    public static int getTexture(String skinUrl, String texture) {
        if(texture != null && skinUrl != null && skinUrl.length() > 1) {
            char ch = skinUrl.charAt(0);
            if(ch >= 48 && ch <= 57) {
                int num = Math.abs(skinUrl.hashCode());
                String[] texs = (String[])(textureVariantsMap.get(texture));
                if(texs == null) {
                    texs = getTextureVariants(texture);
                    textureVariantsMap.put(texture, texs);
                }

                if(texs != null && texs.length > 0) {
                    int index = num % texs.length;
                    String tex = texs[index];
                    return tex == texs[0] ? -1 : getTextureNormal(tex);
                }
            }
        }

        return -1;
    }

    private static String[] getTextureVariants(String texture) {
        String[] texs = new String[]{texture};
        int pointPos = texture.lastIndexOf(46);
        if(pointPos < 0) {
            return texs;
        } else {
            String prefix = texture.substring(0, pointPos);
            String suffix = texture.substring(pointPos);
            int countVariants = getCountTextureVariants(texture, prefix, suffix);
            if(countVariants <= 1) {
                return texs;
            } else {
                texs = new String[countVariants];
                texs[0] = texture;

                for(int i = 1; i < texs.length; ++i) {
                    int texNum = i + 1;
                    texs[i] = prefix + texNum + suffix;
                }

                return texs;
            }
        }
    }

    private static int getCountTextureVariants(String texture, String prefix, String suffix) {
        short maxNum = 1000;

        for(int num = 2; num < maxNum; ++num) {
            String variant = prefix + num + suffix;

            try {
                InputStream e = getInputStream(variant);
                if(e == null) {
                    return num - 1;
                }

                e.close();
            } catch (IOException iOException8) {
                return num - 1;
            }
        }

        return maxNum;
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
        if(UniminedRandomMob.doesClassExist("mod_GLSL")) {
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


}
