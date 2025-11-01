package farn.randomMob.mixin;

import farn.randomMob.EntityRandomMobData;
import farn.randomMob.RandomMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin implements EntityRandomMobData {

    private LivingEntity randommob_self = (LivingEntity) (Object) this;

    public int randommob_id = randommob_self.id;
    public String randommob_biome = "unknown";
    public boolean randommob_useDefaultTexture = false;

    //read nbt for skinID and biome that they are currently in
	@Inject(method = "readNbt", at = @At("TAIL"))
	public void readNbt(NbtCompound nbt, CallbackInfo ci) {
		randommob_id = nbt.getInt("randomMobEntityID");
		if(!nbt.contains("randomMobEntityID")) {
			randommob_id = randommob_self.id;
		}
		randommob_biome = nbt.getString("randomMobBiome");
		if(!nbt.contains("randomMobBiome")) {
			randommob_biome = RandomMob.getBiomeForEntity(randommob_self);
		}
        randommob_useDefaultTexture = nbt.getBoolean("randomUseDefaultTexture");
        if(!nbt.contains("randomUseDefaultTexture")) {
            randommob_useDefaultTexture = randommob_self.world.random.nextBoolean();
        }
	}

	@Inject(method = "writeNbt", at = @At("TAIL"))
	public void writeNbt(NbtCompound nbt, CallbackInfo ci) {
		nbt.putInt("randomMobEntityID", this.randommob_id);
		nbt.putString("randomMobBiome", this.randommob_biome);
        nbt.putBoolean("randomUseDefaultTexture", this.randommob_useDefaultTexture);
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

    public boolean randommob_shouldUseDefaultTexture() {
        return randommob_useDefaultTexture;
    }
}
