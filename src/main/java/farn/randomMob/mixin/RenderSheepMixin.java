package farn.randomMob.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderSheep.class)
public abstract class RenderSheepMixin extends RenderMixin {

    EntitySheep randommob_sheep;

    //apply skin system to sheep fur
    @WrapOperation(
            method = "a(Lnet/minecraft/src/EntitySheep;I)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/src/RenderSheep;loadTexture(Ljava/lang/String;)V"
            )
    )
    public void randommob_useSkin(RenderSheep instance, String s, Operation<Void> original) {
        func_140_a(randommob_sheep.skinUrl, s);
    }

    @Inject(method = "a(Lnet/minecraft/src/EntitySheep;I)Z", at = @At(value="HEAD"))
    public void randommob_getSheep(EntitySheep i2, int par2, CallbackInfoReturnable<Boolean> cir) {
        randommob_sheep = i2;
    }

}
