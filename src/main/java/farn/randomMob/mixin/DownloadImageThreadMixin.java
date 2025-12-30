package farn.randomMob.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.src.ThreadDownloadImage")
public class DownloadImageThreadMixin {

    //don't make it flood console log with bunch of MalformedURLException
    @Inject(method="run", at = @At("HEAD"), cancellable = true)
    public void randommob_run(CallbackInfo ci) {
        if (location == null || !location.startsWith("http")) {
            ci.cancel();
        }
    }

    @Shadow
    private String location;
}
