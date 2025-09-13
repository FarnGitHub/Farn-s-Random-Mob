package farn.randomMob.transform;

import farn.randomMob.UniminedRandomMob;
import net.lenni0451.classtransform.InjectionCallback;
import net.lenni0451.classtransform.annotations.CShadow;
import net.lenni0451.classtransform.annotations.CTarget;
import net.lenni0451.classtransform.annotations.CTransformer;
import net.lenni0451.classtransform.annotations.injection.CInject;
import net.lenni0451.classtransform.annotations.injection.COverride;
import net.minecraft.src.*;

import java.io.InputStream;
import java.util.*;
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
    private native BufferedImage readTextureImage(InputStream inputStream);
    @CShadow
    private native BufferedImage unwrapImageByColumns(BufferedImage bufferedImage);

    @CInject(method="getTextureForDownloadableImage", target=@CTarget("HEAD"), cancellable = true)
    public void getTextureForDownloadableImage(String string, String string2, InjectionCallback callback) {
        int threaddownloadimagedata = UniminedRandomMob.getTextureRandomMob(string, string2);
        if(threaddownloadimagedata >= 0) {
            callback.setReturnValue(threaddownloadimagedata);
        }
    }

    @COverride
    public void refreshTextures() {
        UniminedRandomMob.clearTextureCache();
        if(UniminedRandomMob.doesClassExist("Config")) {
            UniminedRandomMob.OFclearTextureDataMap((RenderEngine) (Object)this);
            UniminedRandomMob.setPrivateValue((RenderEngine) (Object)this,"dynamicTexturesUpdated", false);
            UniminedRandomMob.OFsetFontRenderer(false);
        }
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

            }
        }

        if(UniminedRandomMob.isUsingShader()) {
            UniminedRandomMob.removeShader();
        }

    }

    private BufferedImage readTextureImageCustom(String value) {
        if(UniminedRandomMob.doesClassExist("com.pclewis.mcpatcher.mod.TextureUtils")) {
            return UniminedRandomMob.readTextureImageCustom(value);
        } else {
            return readTextureImage(this.texturePack.selectedTexturePack.getResourceAsStream(value));
        }
    }
}
