package farn.randomMob.transform;

import farn.randomMob.RandomMob;
import net.lenni0451.classtransform.InjectionCallback;
import net.lenni0451.classtransform.annotations.CShadow;
import net.lenni0451.classtransform.annotations.CTarget;
import net.lenni0451.classtransform.annotations.CTransformer;
import net.lenni0451.classtransform.annotations.injection.CInject;
import net.lenni0451.classtransform.annotations.injection.COverride;
import net.minecraft.src.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.awt.image.BufferedImage;

@CTransformer(RenderEngine.class)
public abstract class TransformRenderEngine {
    @CShadow
    private HashMap textureMap;
    @CShadow
    private HashMap textureNameToImageMap;
    @CShadow
    private Map urlToImageDataMap;
    @CShadow
    private boolean clampTexture;
    @CShadow
    private boolean blurTexture;
    @CShadow
    private TexturePackList field_6527_k;
    @CShadow
    private native BufferedImage unwrapImageByColumns(BufferedImage bufferedImage);
    @CShadow
    public native void setupTexture(BufferedImage bufferedImage, int i);

    @CShadow
    private native BufferedImage readTextureImage(InputStream inputStream);

    //don't make the game crash when there are no texture + fix compat with other hd texture mod
    @COverride
    public void refreshTextures() {
        RandomMob.clearTextureCache();
        TexturePackBase texturePackBase1 = this.field_6527_k.selectedTexturePack;
        Iterator iterator2 = this.textureNameToImageMap.keySet().iterator();

        BufferedImage bufferedImage4;
        while(iterator2.hasNext()) {
            int i3 = ((Integer)iterator2.next()).intValue();
            bufferedImage4 = (BufferedImage)this.textureNameToImageMap.get(i3);
            this.setupTexture(bufferedImage4, i3);
        }

        ThreadDownloadImageData threadDownloadImageData7;
        for(iterator2 = this.urlToImageDataMap.values().iterator(); iterator2.hasNext(); threadDownloadImageData7.textureSetupComplete = false) {
            threadDownloadImageData7 = (ThreadDownloadImageData)iterator2.next();
        }

        iterator2 = this.textureMap.keySet().iterator();

        while(iterator2.hasNext()) {
            String string8 = (String)iterator2.next();

            try {
                if(string8.startsWith("##")) {
                    bufferedImage4 = this.unwrapImageByColumns(readTextureImageCustom(string8.substring(2)));
                } else if(string8.startsWith("%clamp%")) {
                    this.clampTexture = true;
                    bufferedImage4 = readTextureImageCustom(string8.substring(7));
                } else if(string8.startsWith("%blur%")) {
                    this.blurTexture = true;
                    bufferedImage4 = readTextureImageCustom(string8.substring(6));
                } else {
                    bufferedImage4 = readTextureImageCustom(string8);
                }

                int i5 = ((Integer)this.textureMap.get(string8)).intValue();
                this.setupTexture(bufferedImage4, i5);
                this.blurTexture = false;
                this.clampTexture = false;
            } catch (Exception iOException6) {
            }
        }

    }

    private BufferedImage readTextureImageCustom(String value) {
        if(RandomMob.doesClassExist("com.pclewis.mcpatcher.mod.TextureUtils")) {
            return RandomMob.readTextureImageCustom(value);
        } else {
            return readTextureImage(this.field_6527_k.selectedTexturePack.func_6481_a(value));
        }
    }
}
