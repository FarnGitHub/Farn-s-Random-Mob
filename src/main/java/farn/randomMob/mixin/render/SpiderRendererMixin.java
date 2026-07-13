package farn.randomMob.mixin.render;

import com.llamalad7.mixinextras.sugar.Local;
import farn.randomMob.RandomMob;
import net.minecraft.client.render.entity.SpiderEntityRenderer;
import net.minecraft.entity.mob.SpiderEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(SpiderEntityRenderer.class)
public class SpiderRendererMixin{

    @ModifyArg(
            method = "bindTexture(Lnet/minecraft/entity/mob/SpiderEntity;IF)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/entity/SpiderEntityRenderer;bindTexture(Ljava/lang/String;)V"
            )
    )
    public String randomMob_useRandomTexture(String backup, @Local(argsOnly = true) SpiderEntity sheep) {
        String randomTex = RandomMob.getRandomMobTexture(sheep, backup);
        if (randomTex != null && !randomTex.isEmpty()) backup = randomTex;
        return backup;
    }
}
