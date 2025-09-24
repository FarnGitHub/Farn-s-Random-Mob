package farn.randomMob.transform;

import farn.randomMob.RandomMob;
import farn.randomMob.RenderEntityAccessor;
import net.lenni0451.classtransform.InjectionCallback;
import net.lenni0451.classtransform.annotations.CShadow;
import net.lenni0451.classtransform.annotations.CTarget;
import net.lenni0451.classtransform.annotations.CTransformer;
import net.lenni0451.classtransform.annotations.injection.CInject;
import net.minecraft.src.Render;
import net.minecraft.src.RenderEngine;
import net.minecraft.src.RenderManager;
import net.minecraft.src.RenderPlayer;

@CTransformer(Render.class)
public class TransformRender implements RenderEntityAccessor {

    @CShadow
    protected native void func_140_a(String string, String string2);

    @CShadow
    protected RenderManager renderManager;

    Render self = (Render)(Object)this;

    //acess a way to load skin texture
    public void calledLoadSkinTexture(String skin, String og) {
        func_140_a(skin, og);
    }

    @CInject(method="func_140_a", target = @CTarget("HEAD"), cancellable = true)
    protected void bindHttpsTexture(String string, String string2, InjectionCallback callback) {
        if(!(self instanceof RenderPlayer)) {
            RenderEngine var3 = this.renderManager.renderEngine;
            int randomTex = RandomMob.getTextureRandomMob(string, string2);
            var3.bindTexture(randomTex >= 0 ? randomTex : var3.getTexture(string2));
            callback.setCancelled(true);
        }
    }


}
