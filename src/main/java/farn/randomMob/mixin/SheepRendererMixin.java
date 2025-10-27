package farn.randomMob.mixin;

import farn.randomMob.mixin.accessor.EntityRendererAccessor;
import net.minecraft.client.render.entity.SheepRenderer;
import net.minecraft.entity.living.mob.passive.animal.SheepEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SheepRenderer.class)
public class SheepRendererMixin {

    SheepEntity randommob_sheep;

    //apply skin system to sheep fur
    @Redirect(
            method = "bindTexture(Lnet/minecraft/entity/living/mob/passive/animal/SheepEntity;IF)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/entity/SheepRenderer;bindTexture(Ljava/lang/String;)V"
            )
    )
    public void randommob_useSkinUrl(SheepRenderer render, String original) {
        ((EntityRendererAccessor) render).randommob_loadHttpsTexture(randommob_sheep.skin, original);
    }

    @Inject(method = "bindTexture(Lnet/minecraft/entity/living/mob/passive/animal/SheepEntity;IF)Z", at = @At("HEAD"))
    public void randommob_capturedSheepInstance(SheepEntity sheepEntity, int i, float f, CallbackInfoReturnable<Boolean> cir) {
        randommob_sheep = sheepEntity;
    }

}
