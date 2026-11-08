package farn.randomMob.mixin.entity;

import farn.randomMob.impl.RandomMobImpl;
import farn.randomMob.util.Util;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = LivingEntity.class, priority = 500)
public abstract class LivingEntityMixin extends Entity implements RandomMobImpl {

    @Unique
    public int randommob_id = 0;

    @Unique
    public String randommob_biome = "default";

    public LivingEntityMixin(World world) {
        super(world);
    }

    @Inject(method="<init>", at = @At("TAIL"))
    public void randommob_init(CallbackInfo ci) {
        this.randomMob_setID(this.id);
        this.randomMob_setBiome(Util.getBiome(this));
    }

    //read nbt for skinID and biome that they are currently in
	@Inject(method = "readNbt", at = @At("TAIL"))
	public void randommob_readNbt(NbtCompound nbt, CallbackInfo ci) {
        if(this.world.isRemote) return;
        this.randomMob_setID(nbt.getInt("randomMobEntityID"));
        this.randomMob_setBiome(nbt.getString("randomMobBiome"));
	}

	@Inject(method = "writeNbt", at = @At("TAIL"))
	public void randommob_writeNbt(NbtCompound nbt, CallbackInfo ci) {
        if(this.world.isRemote) return;
        nbt.putInt("randomMobEntityID", this.randomMob_getID());
        nbt.putString("randomMobBiome", this.randomMob_getBiome());
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
        randommob_biome = Util.validateBiomeId(biome);
    }

    @Override
    public void randomMob_setID(int id) {
        randommob_id = id;
    }
}
