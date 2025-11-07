package farn.randomMob.mixin.render;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import farn.randomMob.mixin.main.EntityRendererMixin;
import net.minecraft.client.render.entity.SheepEntityRenderer;
import net.minecraft.entity.passive.SheepEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SheepEntityRenderer.class)
public class SheepRendererMixin extends EntityRendererMixin {

    //apply skin system to sheep fur
    @WrapOperation(
            method = "bindTexture(Lnet/minecraft/entity/passive/SheepEntity;IF)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/entity/SheepEntityRenderer;bindTexture(Ljava/lang/String;)V"
            )
    )
    public void randommob_useSkinUrl(SheepEntityRenderer render, String s, Operation<Void> original, @Local(index=1, argsOnly = true) SheepEntity ent) {
        this.bindDownloadedTexture(ent.skinUrl, s);
    }

}
