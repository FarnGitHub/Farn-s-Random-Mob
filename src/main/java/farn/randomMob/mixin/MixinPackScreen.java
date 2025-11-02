package farn.randomMob.mixin;

import net.minecraft.client.gui.screen.pack.PackScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PackScreen.class)
public class MixinPackScreen extends MixinScreenBase{

    public void randommob_removedScreen(char keyCode, int par2, CallbackInfo ci) {
        this.minecraft.textureManager.reload();
    }
}
