package farn.randomMob.transform;

import farn.randomMob.RenderEntityAccessor;
import net.lenni0451.classtransform.annotations.CShadow;
import net.lenni0451.classtransform.annotations.CTransformer;
import net.minecraft.src.Render;

@CTransformer(Render.class)
public class TransformRender implements RenderEntityAccessor {

    @CShadow
    protected native void func_140_a(String string, String string2);

    //acess a way to load skin texture
    public void FarnloadHttpsTexture(String skin, String og) {
        func_140_a(skin, og);
    }


}
