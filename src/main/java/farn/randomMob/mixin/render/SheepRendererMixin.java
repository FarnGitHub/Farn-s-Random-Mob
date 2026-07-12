package farn.randomMob.mixin.render;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import farn.randomMob.RandomMob;
import net.minecraft.client.render.entity.SheepEntityRenderer;
import net.minecraft.entity.passive.SheepEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = SheepEntityRenderer.class, priority = 2000)
public class SheepRendererMixin {

    @ModifyArg(
            method = "bindTexture(Lnet/minecraft/entity/passive/SheepEntity;IF)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/entity/SheepEntityRenderer;bindTexture(Ljava/lang/String;)V"
            )
    )
    public String randomMob_useRandomTexture(String backup, @Local(argsOnly = true) SheepEntity sheep) {
        String randomTex = RandomMob.getRandomMobTexture(sheep, backup);
        if (randomTex != null && !randomTex.isEmpty()) backup = randomTex;
        return backup;
    }

}
