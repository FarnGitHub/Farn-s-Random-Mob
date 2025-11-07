package farn.randomMob.mixin.main;

import farn.randomMob.RandomMob;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(World.class)
public class WorldMixin {

    //see RandomMob
    @Inject(method="notifyEntityAdded", at = @At("HEAD"))
    public void randommob_setSkinUrl(Entity entity, CallbackInfo info) {
        RandomMob.setRandomMobSkinURL(entity);
    }


}
