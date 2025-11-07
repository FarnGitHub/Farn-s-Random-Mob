package farn.randomMob.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import farn.randomMob.EntityRandomMobData;
import farn.randomMob.RandomMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin implements EntityRandomMobData {

    private LivingEntity randommob_self = (LivingEntity) (Object) this;

    @Unique
    public int randommob_id = randommob_self.id;

    @Unique
    public String randommob_biome = "unknown";

    //read nbt for skinID and biome that they are currently in
	@Inject(method = "readNbt", at = @At("TAIL"))
	public void readNbt(NbtCompound nbt, CallbackInfo ci) {

        if(randommob_self.world.isRemote) return;
		randommob_id = nbt.getInt("randomMobEntityID");
		if(!nbt.contains("randomMobEntityID")) {
			randommob_id = randommob_self.id;
		}
		randommob_biome = nbt.getString("randomMobBiome");
		if(!nbt.contains("randomMobBiome")) {
			randommob_biome = RandomMob.getBiomeForEntity(randommob_self);
		}
	}

	@Inject(method = "writeNbt", at = @At("TAIL"))
	public void writeNbt(NbtCompound nbt, CallbackInfo ci) {
        if(randommob_self.world.isRemote) return;
		nbt.putInt("randomMobEntityID", this.randommob_id);
		nbt.putString("randomMobBiome", this.randommob_biome);
	}

    //get skinID
    @Override
    public int randommob_getID() {
        return randommob_id;
    }

    //get current biome
    @Override
    public String randommob_getBiome() {
        return randommob_biome;
    }

    //set current biome
    @Override
    public void randommob_setBiome(String biome) {
        randommob_biome = biome;
    }
}
