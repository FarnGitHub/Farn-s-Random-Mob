package farn.randomMob.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.texture.ImageDownload;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//God Damn it why it not seperate from HttpTexture
@Mixin(targets="net.minecraft.client.texture.ImageDownload$Thread", priority = 2000)
public class HttpTextureThreadMixin {

    @Shadow @Final private String url;

    //don't make it flood console log with bunch of MalformedURLException
    //@Inject(method="run", at = @At("HEAD"), cancellable = true, remap = false)
    @WrapMethod(method="run")
    public void randommob_run(Operation<Void> original) {
        if (url != null && url.startsWith("http")) {
            original.call();
        }
    }
}
