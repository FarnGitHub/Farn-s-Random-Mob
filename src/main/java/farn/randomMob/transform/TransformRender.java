package farn.randomMob.transform;

import farn.randomMob.RandomMob;
import farn.randomMob.RenderEntityAccessor;
import net.lenni0451.classtransform.InjectionCallback;
import net.lenni0451.classtransform.annotations.CShadow;
import net.lenni0451.classtransform.annotations.CTarget;
import net.lenni0451.classtransform.annotations.CTransformer;
import net.lenni0451.classtransform.annotations.injection.CInject;
import net.minecraft.src.*;

@CTransformer(Render.class)
public class TransformRender implements RenderEntityAccessor {

    private Render self = (Render) (Object) this;

    @CShadow
    protected RenderManager renderManager;

    @CShadow
    protected native boolean loadDownloadableImageTexture(String string, String string2);

    @CInject(method = "loadDownloadableImageTexture", target = @CTarget("HEAD"), cancellable = true)
    public void randommob_redirectLoadImageTexture(String string, String string2, InjectionCallback callback) {
        if (!(self instanceof RenderPlayer)) {
            int threaddownloadimagedata = RandomMob.getRandomMobTexture(string, string2);
            if (threaddownloadimagedata >= 0) {
                renderManager.renderEngine.bindTexture(threaddownloadimagedata);
                callback.setReturnValue(true);
            }
        }
    }

    public boolean randommob_loadHttpsTexture(String skin, String og) {
        return loadDownloadableImageTexture(skin, og);
    }

}
