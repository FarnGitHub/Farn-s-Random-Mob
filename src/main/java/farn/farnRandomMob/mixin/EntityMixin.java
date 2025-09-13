package farn.farnRandomMob.mixin;

import farn.farnRandomMob.IEntitySkinID;
import farn.farnRandomMob.RandomMob;
import net.minecraft.entity.living.LivingEntity;
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

	@Unique
	public String biomeRandomMob = "unknown";

	@Inject(method = "readCustomNbt", at = @At("TAIL"))
	public void readNbt(NbtCompound nbt, CallbackInfo ci) {
		entitySkinID = nbt.getInt("randomMobEntityID");
		if(!nbt.contains("randomMobEntityID")) {
			entitySkinID = self.networkId;
		}
		biomeRandomMob = nbt.getString("randomMobBiome");
		if(!nbt.contains("randomMobBiome")) {
			biomeRandomMob = RandomMob.getBiomeForEntity(self);
		}
	}

	@Inject(method = "writeCustomNbt", at = @At("TAIL"))
	public void writeNbt(NbtCompound nbt, CallbackInfo ci) {
		nbt.putInt("randomMobEntityID", this.entitySkinID);
		nbt.putString("randomMobBiome", this.biomeRandomMob);
	}

	@Override
	public int getEntitySkinID() {
		return entitySkinID;
	}

	@Override
	public String getBiomeSpawn() {
		return biomeRandomMob;
	}

	@Override
	public void setBiomeSpawn(String biome) {
		biomeRandomMob = biome;
	}
}
