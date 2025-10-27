package farn.randomMob.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//God Damn it why it not seperate from HttpTexture
@Mixin(targets = "net.minecraft.client.render.texture.HttpTexture$1", priority = 900)
public class HttpTextureThreadMixin {

    //don't make it flood console log with bunch of MalformedURLException
    @Inject(method="run", at = @At("HEAD"), cancellable = true)
    public void randommob_run(CallbackInfo ci) {
        if (f_4140703 == null || !f_4140703.startsWith("http")) {
			ci.cancel();
        }

    }

	//url of the texture
	@Shadow(remap = false) private String f_4140703;
}
