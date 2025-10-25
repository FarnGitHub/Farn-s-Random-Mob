package farn.randomMob.transform;

import farn.randomMob.RandomMob;
import net.lenni0451.classtransform.InjectionCallback;
import net.lenni0451.classtransform.annotations.CShadow;
import net.lenni0451.classtransform.annotations.CTarget;
import net.lenni0451.classtransform.annotations.CTransformer;
import net.lenni0451.classtransform.annotations.injection.CInject;
import net.lenni0451.classtransform.annotations.injection.COverride;
import net.lenni0451.classtransform.annotations.injection.CRedirect;
import net.lenni0451.classtransform.annotations.injection.CWrapCatch;
import net.minecraft.src.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.awt.image.BufferedImage;

@CTransformer(RenderEngine.class)
public abstract class TransformRenderEngine {
    //clear texture cache
    @CInject(method="refreshTextures", target = @CTarget("HEAD"))
    public void randommob_beforeRefreshTextures() {
        RandomMob.clearTextureCache();
    }

    @CRedirect(method="refreshTextures", target = @CTarget(value="INVOKE", target = "Ljava/io/IOException;printStackTrace()V"))
    public void randommob_ignoreInputIsNull(IOException excep) {
        if(!"input == null!".equals(excep.getMessage())) {
            excep.printStackTrace();
        }
    }


    //turn every IllegalArgumentException into IOException so the game don't crash when it tried to load non-exist texture
    @CWrapCatch(value="readTextureImage")
    public BufferedImage randommob_preventMissingTextureCrash(IllegalArgumentException ilExcep) throws IOException {
        throw new IOException(ilExcep.getMessage());
    }
}
