package farn.farnRandomMob.mixin;

import farn.farnRandomMob.RandomMob;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(World.class)
public class WorldMixin {

	@Inject(method = "onEntityAdded", at = @At("HEAD"))
	public void onSkinInjector(Entity entity, CallbackInfo info) {
		RandomMob.entityLoaded(entity);
	}

}
