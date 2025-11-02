package farn.randomMob.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import farn.randomMob.mixin.accessor.EntityRendererAccessor;
import net.minecraft.client.render.entity.SpiderEntityRenderer;
import net.minecraft.entity.mob.SpiderEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SpiderEntityRenderer.class)
public class SpiderRendererMixin {

    SpiderEntity randommob_spider;

    //apply skin system to spider eyes
    @WrapOperation(
            method = "bindTexture(Lnet/minecraft/entity/mob/SpiderEntity;IF)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/entity/SpiderEntityRenderer;bindTexture(Ljava/lang/String;)V"
            )
    )
    public void randommob_useSkinUrl(SpiderEntityRenderer render, String s, Operation<Void> original) {
        ((EntityRendererAccessor) render).bindDownloadableTextures(randommob_spider.skinUrl, s);
    }

    @Inject(method = "bindTexture(Lnet/minecraft/entity/mob/SpiderEntity;IF)Z", at = @At("HEAD"))
    public void randommob_capturedSpiderInstance(SpiderEntity spiderEntity, int i, float f, CallbackInfoReturnable<Boolean> cir) {
        randommob_spider = spiderEntity;
    }
}
