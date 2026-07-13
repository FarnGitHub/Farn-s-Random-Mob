package farn.randomMob.mixin.render;

import com.llamalad7.mixinextras.sugar.Local;
import farn.randomMob.RandomMob;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = LivingEntityRenderer.class, priority = 2000)
public class LivingRendererMixin {

    @ModifyArg(method="render(Lnet/minecraft/entity/LivingEntity;DDDFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;bindDownloadedTexture(Ljava/lang/String;Ljava/lang/String;)Z"), index = 1)
    public String randomMob_useRandomTexture(String backup, @Local(argsOnly = true)LivingEntity living) {
        String randomTex = RandomMob.getRandomMobTexture(living, backup);
        if (randomTex != null && !randomTex.isEmpty()) backup = randomTex;
        return backup;
    }

}
