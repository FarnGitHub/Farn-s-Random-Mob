package farn.randomMob.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import farn.randomMob.RandomMob;
import net.minecraft.client.render.texture.TextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

@Mixin(TextureManager.class)
public abstract class TextureManagerMixin {

    //clear texture cache
    @Inject(method="reload", at = @At("HEAD"))
    public void randommob_beforeRefreshTextures(CallbackInfo ci) {
        RandomMob.clearRandomMobTextureCache();
    }

    @WrapOperation(method="reload", at = @At(value= "INVOKE", target = "Ljava/io/IOException;printStackTrace()V"))
    public void randommob_ignoreInputIsNull(IOException instance, Operation<Void> original) {
        if(!"input == null!".equals(instance.getMessage())) {
            original.call(instance);
        }
    }


    //turn every IllegalArgumentException into IOException so the game don't crash when it tried to load non-exist texture
    @WrapMethod(method="readImage")
    public BufferedImage randommob_preventMissingTextureCrash(InputStream is, Operation<BufferedImage> original) throws IOException {
         try {
			 return original.call(is);
		 } catch (IllegalArgumentException e) {
			 throw new IOException(e.getMessage());
		 }
    }


}
