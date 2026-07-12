package farn.randomMob.mixin;

import farn.randomMob.SinglePlayerSkinData;
import farn.randomMob.RandomMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin implements SinglePlayerSkinData {

    private LivingEntity randommob_self = (LivingEntity) (Object) this;

    @Unique
    public int randommob_id = randommob_self.id;

    @Unique
    public String randommob_biome = "default";

    @Inject(method="<init>", at = @At("TAIL"))
    public void randommob_init(CallbackInfo ci) {
        randommob_biome = RandomMob.getEntityCurrentBiome(randommob_self);
    }

    //read nbt for skinID and biome that they are currently in
	@Inject(method = "readNbt", at = @At("TAIL"))
	public void randommob_readNbt(NbtCompound nbt, CallbackInfo ci) {
        if(randommob_self.world.isRemote) return;
		randommob_id = nbt.getInt("randomMobEntityID");
        randomMob_setBiome(nbt.getString("randomMobBiome"));
	}

	@Inject(method = "writeNbt", at = @At("TAIL"))
	public void randommob_writeNbt(NbtCompound nbt, CallbackInfo ci) {
        if(randommob_self.world.isRemote) return;
		nbt.putInt("randomMobEntityID", this.randommob_id);
		nbt.putString("randomMobBiome", this.randommob_biome);
	}

    //get skinID
    @Override
    public int randomMob_getID() {
        return randommob_id;
    }

    //get current biome
    @Override
    public String randomMob_getBiome() {
        return randommob_biome;
    }

    //set current biome
    @Override
    public void randomMob_setBiome(String biome) {
        if(randommob_biome == null || randommob_biome.isEmpty())
            biome = "default";
        randommob_biome = biome;
    }
}
