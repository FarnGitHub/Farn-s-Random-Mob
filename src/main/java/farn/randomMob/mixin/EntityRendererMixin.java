package farn.randomMob.mixin;

import farn.randomMob.RandomMob;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {

    private EntityRenderer self_render = (EntityRenderer) (Object) this;

    @Shadow
	protected EntityRenderDispatcher dispatcher;

    @Inject(method = "bindHttpTexture", at = @At("HEAD"), cancellable = true)
    public void randommob_redirectLoadImageTexture(String path, String defaultPath, CallbackInfoReturnable<Boolean> cir) {
        if (!(self_render instanceof PlayerEntityRenderer)) {
            int threaddownloadimagedata = RandomMob.getRandomMobTexture(path, defaultPath);
            if (threaddownloadimagedata >= 0) {
                dispatcher.textureManager.bind(threaddownloadimagedata);
                cir.setReturnValue(true);
            }
        }
    }

}
