package farn.randomMob.mixin;

import farn.randomMob.RandomMob;
import net.minecraft.src.Entity;
import net.minecraft.src.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(World.class)
public class WorldMixin {

    //see RandomMob
    @Inject(method="obtainEntitySkin", at=@At("HEAD"))
    public void setSkinUrl(Entity par1, CallbackInfo ci) {
        RandomMob.entityLoaded(par1);
    }


}
