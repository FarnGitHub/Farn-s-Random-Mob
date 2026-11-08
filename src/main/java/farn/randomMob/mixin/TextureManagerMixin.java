package farn.randomMob.mixin;

import farn.randomMob.RandomMob;
import net.minecraft.client.texture.TextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TextureManager.class)
public class TextureManagerMixin {

    @Inject(method="reload", at = @At("HEAD"))
    public void randommob_clearTextureRule(CallbackInfo ci) {
        RandomMob.clearTextures();
    }
}
