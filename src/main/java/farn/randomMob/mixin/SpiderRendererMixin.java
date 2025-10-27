package farn.randomMob.mixin;

import farn.randomMob.mixin.accessor.EntityRendererAccessor;
import net.minecraft.client.render.entity.SpiderRenderer;
import net.minecraft.entity.living.mob.hostile.SpiderEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SpiderRenderer.class)
public class SpiderRendererMixin {

    SpiderEntity randommob_spider;

    //apply skin system to spider eyes
    @Redirect(
            method = "bindTexture(Lnet/minecraft/entity/living/mob/hostile/SpiderEntity;IF)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/entity/SpiderRenderer;bindTexture(Ljava/lang/String;)V"
            )
    )
    public void randommob_useSkinUrl(SpiderRenderer render, String original) {
        ((EntityRendererAccessor) render).randommob_loadHttpsTexture(randommob_spider.skin, original);
    }

    @Inject(method = "bindTexture(Lnet/minecraft/entity/living/mob/hostile/SpiderEntity;IF)Z", at = @At("HEAD"))
    public void randommob_capturedSpiderInstance(SpiderEntity spiderEntity, int i, float f, CallbackInfoReturnable<Boolean> cir) {
        randommob_spider = spiderEntity;
    }
}
