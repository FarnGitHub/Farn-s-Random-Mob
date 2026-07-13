package farn.randomMob.mixin;

import farn.randomMob.impl.RandomMobImpl;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin implements RandomMobImpl {

    @Unique
    public int randommob_id = randommob_self().id;

    @Unique
    public String randommob_biome = "default";

    @SuppressWarnings("MissingUnique")
    public LivingEntity randommob_self() {
        return (LivingEntity)(Object)this;
    }

    @Inject(method="<init>", at = @At("TAIL"))
    public void randommob_init(CallbackInfo ci) {
        randomMob_setBiome(getEntityCurrentBiome(randommob_self()));
    }

    //read nbt for skinID and biome that they are currently in
	@Inject(method = "readNbt", at = @At("TAIL"))
	public void randommob_readNbt(NbtCompound nbt, CallbackInfo ci) {
        if(((LivingEntity)(Object)this).world.isRemote) return;
		randomMob_setId(nbt.getInt("randomMobEntityID"));
        randomMob_setBiome(nbt.getString("randomMobBiome"));
	}

	@Inject(method = "writeNbt", at = @At("TAIL"))
	public void randommob_writeNbt(NbtCompound nbt, CallbackInfo ci) {
        if(((LivingEntity)(Object)this).world.isRemote) return;
		nbt.putInt("randomMobEntityID", randomMob_getID());
		nbt.putString("randomMobBiome", randomMob_getBiome());
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

    @Override
    public void randomMob_setId(int id) {
        randommob_id = id;
    }

    @SuppressWarnings("MissingUnique")
    private static String getEntityCurrentBiome(Entity e) {
        if (e == null || e.world == null) return "default";
        Biome biome = e.world.method_1781().getBiome((int)e.x, (int)e.z);
        return (biome != null && biome.name != null) ? biome.name : "default";
    }
}
