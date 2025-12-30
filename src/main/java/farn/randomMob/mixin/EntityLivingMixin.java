package farn.randomMob.mixin;

import farn.randomMob.EntitySaveSkinNBT;
import farn.randomMob.RandomMob;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityLiving.class)
public class EntityLivingMixin implements EntitySaveSkinNBT {

    private EntityLiving self = (EntityLiving) (Object) this;

    public int entitySkinID = self.field_620_ab;
    public String biomeRandomMob = "unknown";

    //read nbt for skinID and biome that they are currently in
    @Inject(method = "readEntityFromNBT", at = @At("TAIL"))
    public void randommob_readNbt(NBTTagCompound nbt, CallbackInfo ci) {
        entitySkinID = nbt.getInteger("randomMobEntityID");
        if(!nbt.hasKey("randomMobEntityID")) {
            entitySkinID = self.field_620_ab;
        }
        biomeRandomMob = nbt.getString("randomMobBiome");
        if(!nbt.hasKey("randomMobBiome")) {
            biomeRandomMob = RandomMob.getBiomeForEntity(self);
        }
    }

    //set nbt for skinID and biome that they are currently in
    @Inject(method = "writeEntityToNBT", at = @At("TAIL"))
    public void randommob_writeNbt(NBTTagCompound nbt, CallbackInfo ci) {
        nbt.setInteger("randomMobEntityID", this.entitySkinID);
        nbt.setString("randomMobBiome", this.biomeRandomMob);
    }

    //get skinID
    @Override
    public int randommob_getId() {
        return entitySkinID;
    }

    //get current biome
    @Override
    public String randommob_getBiome() {
        return biomeRandomMob;
    }

    //set current biome
    @Override
    public void randommob_setBiome(String biome) {
        biomeRandomMob = biome;
    }
}
