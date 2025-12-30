package farn.randomMob.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.src.EntitySpider;
import net.minecraft.src.RenderSpider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderSpider.class)
public abstract class RenderSpiderMixin extends RenderMixin {

    EntitySpider randommob_spider;

    //apply skin system to spider eyes
    @WrapOperation(
            method = "a(Lnet/minecraft/src/EntitySpider;I)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/src/RenderSpider;loadTexture(Ljava/lang/String;)V"
            )
    )
    public void randommob_useSkin(RenderSpider instance, String originalTex, Operation<Void> original) {
        func_140_a(randommob_spider.skinUrl, originalTex);
    }

    @Inject(method = "a(Lnet/minecraft/src/EntitySpider;I)Z", at = @At(value="HEAD"))
    public void randommob_getSpiderEntity(EntitySpider entitySpider, int par2, CallbackInfoReturnable<Boolean> cir) {
        randommob_spider = entitySpider;
    }
}
