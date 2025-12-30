package farn.randomMob.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import farn.randomMob.RandomMob;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.awt.image.BufferedImage;
import java.io.InputStream;

@Mixin(RenderEngine.class)
public abstract class RenderEngineMixin {
    //clear texture cache
    @Inject(method="refreshTextures", at = @At("HEAD"))
    public void randommob_beforeRefreshTextures(CallbackInfo ci) {
        RandomMob.clearTextureCache();
    }

    @WrapOperation(method="refreshTextures", at = @At(value="INVOKE", target = "Ljava/io/IOException;printStackTrace()V"))
    public void randommob_ignoreInputIsNull(IOException instance, Operation<Void> original) {
        if(!"input == null!".equals(instance.getMessage())) {
            original.call(instance);
        }
    }


    //turn every IllegalArgumentException into IOException so the game don't crash when it tried to load non-exist texture
    @WrapMethod(method="readTextureImage")
    public BufferedImage randommob_preventMissingTextureCrash(InputStream par1, Operation<BufferedImage> original) {
        try {
            return original.call(par1);
        } catch (Exception e) {
            return null;
        }
    }
}
