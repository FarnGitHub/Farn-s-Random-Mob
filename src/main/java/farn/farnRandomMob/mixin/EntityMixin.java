package farn.farnRandomMob.mixin;

import farn.farnRandomMob.IEntitySkinID;
import net.minecraft.entity.living.LivingEntity;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class EntityMixin implements IEntitySkinID {


	private LivingEntity self = (LivingEntity) (Object) this;

	@Unique
	public int entitySkinID = self.networkId;

	@Inject(method = "readCustomNbt", at = @At("TAIL"))
	public void readNbt(NbtCompound nbt, CallbackInfo ci) {
		entitySkinID = nbt.getInt("randomMobEntityID");
		if(!nbt.contains("randomMobEntityID")) {
			entitySkinID = self.networkId;
		}
	}

	@Inject(method = "writeCustomNbt", at = @At("TAIL"))
	public void writeNbt(NbtCompound nbt, CallbackInfo ci) {
		nbt.putInt("randomMobEntityID", this.entitySkinID);
	}

	@Override
	public int getEntitySkinID() {
		return entitySkinID;
	}
}
