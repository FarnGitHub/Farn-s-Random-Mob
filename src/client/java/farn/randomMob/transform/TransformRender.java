package farn.randomMob.transform;

import farn.randomMob.RenderEntityAccessor;
import net.lenni0451.classtransform.annotations.CShadow;
import net.lenni0451.classtransform.annotations.CTransformer;
import net.minecraft.src.Render;

@CTransformer(Render.class)
public class TransformRender implements RenderEntityAccessor {

    @CShadow
    protected native boolean loadDownloadableImageTexture(String string, String string2);

    //acess a way to load skin texture
    public boolean FarnloadHttpsTexture(String skin, String og) {
        return loadDownloadableImageTexture(skin, og);
    }


}
