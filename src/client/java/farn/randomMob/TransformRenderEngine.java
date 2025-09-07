package farn.randomMob;

import net.lenni0451.classtransform.InjectionCallback;
import net.lenni0451.classtransform.annotations.CShadow;
import net.lenni0451.classtransform.annotations.CTarget;
import net.lenni0451.classtransform.annotations.CTransformer;
import net.lenni0451.classtransform.annotations.injection.CInject;
import net.lenni0451.classtransform.annotations.injection.COverride;
import net.minecraft.src.*;

import java.io.InputStream;
import java.util.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.awt.image.BufferedImage;

@CTransformer(RenderEngine.class)
public abstract class TransformRenderEngine {
    @CShadow
    private HashMap textureMap;
    @CShadow
    private HashMap field_28151_c;
    @CShadow
    private HashMap textureNameToImageMap;
    @CShadow
    private Map urlToImageDataMap;
    @CShadow
    private boolean clampTexture;
    @CShadow
    private boolean blurTexture;
    @CShadow
    private TexturePackList texturePack;
    @CShadow
    public native void setupTexture(BufferedImage bufferedImage, int i);
    @CShadow
    private native int[] func_28147_a(BufferedImage bufferedImage, int[] is);
    @CShadow
    private native int allocateAndSetupTexture(BufferedImage bufferedImage);
    @CShadow
    private native int getTexture(String string);
    @CShadow
    private native BufferedImage unwrapImageByColumns(BufferedImage bufferedImage);

    @CInject(method="getTextureForDownloadableImage", target=@CTarget("HEAD"), cancellable = true)
    public void getTextureForDownloadableImage(String string, String string2, InjectionCallback callback) {
        int threaddownloadimagedata = mod_RandomMobUnimined.getTexture(string, string2);
        if(threaddownloadimagedata >= 0) {
            callback.setReturnValue(threaddownloadimagedata);
        }
    }

    @COverride
    public void refreshTextures() {
        mod_RandomMobUnimined.clearTextureCache();
        if(mod_RandomMobUnimined.doesClassExist("Config")) {
            this.OFclearTextureDataMap();
            setPrivateValue("dynamicTexturesUpdated", false);
            this.OFsetFontRenderer(false);
        }
        TexturePackBase texturePackBase1 = this.texturePack.selectedTexturePack;
        Iterator iterator2 = this.textureNameToImageMap.keySet().iterator();

        BufferedImage bufferedImage4;
        while(iterator2.hasNext()) {
            int i3 = ((Integer)iterator2.next()).intValue();
            bufferedImage4 = (BufferedImage)this.textureNameToImageMap.get(i3);
            this.setupTexture(bufferedImage4, i3);
        }

        ThreadDownloadImageData threadDownloadImageData8;
        for(iterator2 = this.urlToImageDataMap.values().iterator(); iterator2.hasNext(); threadDownloadImageData8.textureSetupComplete = false) {
            threadDownloadImageData8 = (ThreadDownloadImageData)iterator2.next();
        }

        iterator2 = this.textureMap.keySet().iterator();

        String string9;
        while(iterator2.hasNext()) {
            string9 = (String)iterator2.next();

            try {
                if(string9.startsWith("##")) {
                    bufferedImage4 = this.unwrapImageByColumns(this.readTextureImageCustom(string9.substring(2)));
                } else if(string9.startsWith("%clamp%")) {
                    this.clampTexture = true;
                    bufferedImage4 = this.readTextureImageCustom(string9.substring(7));
                } else if(string9.startsWith("%blur%")) {
                    this.blurTexture = true;
                    bufferedImage4 = this.readTextureImageCustom(string9.substring(6));
                } else {
                    bufferedImage4 = this.readTextureImageCustom(string9);
                }

                int i5 = ((Integer)this.textureMap.get(string9)).intValue();
                this.setupTexture(bufferedImage4, i5);
                this.blurTexture = false;
                this.clampTexture = false;
            } catch (Exception iOException7) {
            }
        }

        iterator2 = this.field_28151_c.keySet().iterator();

        while(iterator2.hasNext()) {
            string9 = (String)iterator2.next();

            try {
                if(string9.startsWith("##")) {
                    bufferedImage4 = this.unwrapImageByColumns(this.readTextureImageCustom(string9.substring(2)));
                } else if(string9.startsWith("%clamp%")) {
                    this.clampTexture = true;
                    bufferedImage4 = this.readTextureImageCustom(string9.substring(7));
                } else if(string9.startsWith("%blur%")) {
                    this.blurTexture = true;
                    bufferedImage4 = this.readTextureImageCustom(string9.substring(6));
                } else {
                    bufferedImage4 = this.readTextureImageCustom(string9);
                }

                this.func_28147_a(bufferedImage4, (int[])this.field_28151_c.get(string9));
                this.blurTexture = false;
                this.clampTexture = false;
            } catch (Exception iOException6) {
                iOException6.printStackTrace();
            }
        }

        if(isUsingShader()) {
            this.removeShader();
        }

    }

    private boolean isUsingShader() {
        if(mod_RandomMobUnimined.doesClassExist("mod_GLSL")) {
            try {
                Class shaderToggle = Class.forName("net.mine_diver.glsl.ToggleShaders");
                String currentShader = (String)ModLoader.getPrivateValue(shaderToggle, (Object)null, "currentShaders");
                return !currentShader.equals("default");
            } catch(Exception e) {
                e.printStackTrace();
                return false;
            }

        }
        return false;
    }

    private void removeShader() {
        try {
            Class shader = Class.forName("net.mine_diver.glsl.Shaders");
            Method method = shader.getDeclaredMethod("destroy");
            method.invoke((Object)null);
            ModLoader.setPrivateValue(shader, (Object)null, "isInitialized", false);
            System.out.println("Farn RandomMob Remove Shader");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }


    private void setPrivateValue(String fieldName, Object value) {
        try {
            ModLoader.setPrivateValue(RenderEngine.class, (RenderEngine) (Object)this, fieldName, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private BufferedImage readTextureImageCustom(String value) {
        if(mod_RandomMobUnimined.doesClassExist("com.pclewis.mcpatcher.mod.TextureUtils")) {
            try {
                Class config = Class.forName("com.pclewis.mcpatcher.mod.TextureUtils");
                Method method = config.getDeclaredMethod("getResourceAsBufferedImage", String.class);
                return (BufferedImage)method.invoke((Object)null, value);
            } catch(Exception e) {
                return null;
            }
        } else {
            try {
                Method method = RenderEngine.class.getDeclaredMethod("a", InputStream.class);
                method.setAccessible(true);
                return (BufferedImage)method.invoke(this, ModLoader.getMinecraftInstance().texturePackList.selectedTexturePack.getResourceAsStream(value));
            } catch(Exception e) {
                return null;
            }
        }
    }

    private void OFsetFontRenderer(boolean value) {
        try {
            Class config = Class.forName("Config");
            Method method = config.getDeclaredMethod("setFontRendererUpdated", boolean.class);
            method.invoke((Object)null, value);
        } catch(Exception e) {
        }
    }

    private void OFclearTextureDataMap() {
        try {
            Field privateObjectField = RenderEngine.class.getDeclaredField("textureDataMap");
            privateObjectField.setAccessible(true);
            Object privateObjectInstance = privateObjectField.get(this);
            Class<?> innerClass = privateObjectInstance.getClass();
            Method publicVoidMethod = innerClass.getDeclaredMethod("clear");
            publicVoidMethod.invoke(privateObjectInstance);
        } catch(Exception e) {
        }
    }
}
