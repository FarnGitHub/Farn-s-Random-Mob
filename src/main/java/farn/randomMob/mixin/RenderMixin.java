package farn.randomMob.mixin;

import farn.randomMob.RandomMob;
import net.minecraft.src.Render;
import net.minecraft.src.RenderEngine;
import net.minecraft.src.RenderManager;
import net.minecraft.src.RenderPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Render.class)
public abstract class RenderMixin {

    @Shadow protected abstract void func_140_a(String string, String string2);

    @Shadow
    protected RenderManager renderManager;

    Render self = (Render)(Object)this;

    @Inject(method="func_140_a", at = @At("HEAD"), cancellable = true)
    protected void random_bindRandomMobTextures(String string, String string2, CallbackInfo ci) {
        if(!(self instanceof RenderPlayer)) {
            RenderEngine var3 = this.renderManager.renderEngine;
            int randomTex = RandomMob.getTextureRandomMob(string, string2);
            var3.bindTexture(randomTex >= 0 ? randomTex : var3.getTexture(string2));
            ci.cancel();
        }
    }


}
