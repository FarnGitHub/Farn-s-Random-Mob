package farn.randomMob.mixin.render;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import farn.randomMob.mixin.main.EntityRendererMixin;
import net.minecraft.client.render.entity.SpiderEntityRenderer;
import net.minecraft.entity.mob.SpiderEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SpiderEntityRenderer.class)
public class SpiderRendererMixin extends EntityRendererMixin {

    //apply skin system to spider eyes
    @WrapOperation(
            method = "bindTexture(Lnet/minecraft/entity/mob/SpiderEntity;IF)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/entity/SpiderEntityRenderer;bindTexture(Ljava/lang/String;)V"
            )
    )
    public void randommob_useSkinUrl(SpiderEntityRenderer render, String s, Operation<Void> original, @Local(index=1, argsOnly = true) SpiderEntity ent) {
        bindDownloadedTexture(ent.skinUrl, s);
    }
}
