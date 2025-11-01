package farn.randomMob.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import farn.randomMob.mixin.accessor.EntityRendererAccessor;
import net.minecraft.client.render.entity.SheepEntityRenderer;
import net.minecraft.entity.passive.SheepEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SheepEntityRenderer.class)
public class SheepRendererMixin {

    SheepEntity randommob_sheep;

    //apply skin system to sheep fur
    @WrapOperation(
            method = "bindTexture(Lnet/minecraft/entity/passive/SheepEntity;IF)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/entity/SheepEntityRenderer;bindTexture(Ljava/lang/String;)V"
            )
    )
    public void randommob_useSkinUrl(SheepEntityRenderer render, String s, Operation<Void> original) {
        ((EntityRendererAccessor) render).randommob_loadHttpsTexture(randommob_sheep.skinUrl, s);
    }

    @Inject(method = "bindTexture(Lnet/minecraft/entity/passive/SheepEntity;IF)Z", at = @At("HEAD"))
    public void randommob_capturedSheepInstance(SheepEntity sheepEntity, int i, float f, CallbackInfoReturnable<Boolean> cir) {
        randommob_sheep = sheepEntity;
    }

}
