package farn.farnRandomMob.mixin;

import farn.farnRandomMob.RandomMob;
import net.minecraft.client.render.texture.TextureManager;
import org.spongepowered.asm.mixin.Mixin;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TextureManager.class)
public abstract class TextureManagerMixin {

	@Inject(
		method = "bindHttpTexture",
		at = @At("HEAD"),
		cancellable = true
	)
	private void onBindHttpTexture(String path, String defaultPath, CallbackInfoReturnable<Integer> cir) {
		int tex = RandomMob.getTexture(path, defaultPath);
		if (tex >= 0) {
			cir.setReturnValue(tex);
		}
	}

	@Inject(method = "reload", at = @At("HEAD"), cancellable = true)
	public void reload(CallbackInfo info) {
		RandomMob.clearTextureCache();
	}

}
