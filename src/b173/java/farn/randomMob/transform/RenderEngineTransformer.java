package farn.randomMob.transform;

import farn.randomMob.RandomMob;
import farn.randomMob.impl.RenderEngineImpl;
import farn.randomMob.util.Util;
import net.lenni0451.classtransform.InjectionCallback;
import net.lenni0451.classtransform.annotations.CShadow;
import net.lenni0451.classtransform.annotations.CTarget;
import net.lenni0451.classtransform.annotations.CTransformer;
import net.lenni0451.classtransform.annotations.injection.CInject;
import net.lenni0451.classtransform.annotations.injection.CWrapCatch;
import net.minecraft.src.RenderEngine;

import java.awt.image.BufferedImage;
import java.util.HashMap;

@SuppressWarnings("unused")
@CTransformer(RenderEngine.class)
public class RenderEngineTransformer implements RenderEngineImpl {

    @CShadow
    private HashMap<String, Integer> textureMap;

    @CInject(method="refreshTextures", target = @CTarget("HEAD"))
    public void randommob_clearTextureRule(InjectionCallback ci) {
        RandomMob.clearTextures();
    }

    @Override
    public void randomMob_deleteTexture(String texture) {
        textureMap.remove(texture);
    }

    @CWrapCatch("readTextureImage")
    public BufferedImage randommob_wrapRead(Exception e) {
        return Util.missingTexture;
    }
}
