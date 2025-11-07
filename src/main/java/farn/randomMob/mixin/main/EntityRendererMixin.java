package farn.randomMob.mixin.main;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import farn.randomMob.RandomMob;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin {

    private EntityRenderer self_render = (EntityRenderer) (Object) this;

    @Shadow
	protected EntityRenderDispatcher dispatcher;

    @Shadow
    protected boolean bindDownloadedTexture(String url, String backup) {
        throw new AssertionError();
    }

    @WrapMethod(method="bindDownloadedTexture")
    public boolean randommob_redirectLoadImageTexture(String path, String backup, Operation<Boolean> original) {
        if (!(self_render instanceof PlayerEntityRenderer)) {
            int threaddownloadimagedata = RandomMob.getRandomMobTexture(path, backup);
            if (threaddownloadimagedata >= 0) {
                dispatcher.textureManager.bindTexture(threaddownloadimagedata);
                return true;
            }
        }
        return original.call(path, backup);
    }

}
