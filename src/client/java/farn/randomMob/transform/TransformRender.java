package farn.randomMob.transform;

import farn.randomMob.RenderAccessor;
import net.lenni0451.classtransform.annotations.CShadow;
import net.lenni0451.classtransform.annotations.CTransformer;
import net.minecraft.src.Render;

@CTransformer(Render.class)
public class TransformRender implements RenderAccessor {

    @CShadow
    protected native boolean loadDownloadableImageTexture(String string, String string2);

    public boolean FarnloadHttpsTexture(String skin, String og) {
        return loadDownloadableImageTexture(skin, og);
    }


}
