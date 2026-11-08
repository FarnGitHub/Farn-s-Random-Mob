package farn.randomMob.transform;

import farn.randomMob.RandomMob;
import farn.randomMob.impl.RenderEngineImpl;
import net.lenni0451.classtransform.InjectionCallback;
import net.lenni0451.classtransform.annotations.CShadow;
import net.lenni0451.classtransform.annotations.CTarget;
import net.lenni0451.classtransform.annotations.CTransformer;
import net.lenni0451.classtransform.annotations.injection.CInject;
import net.minecraft.src.RenderEngine;

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
}
