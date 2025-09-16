package farn.randomMob.transform;

import farn.randomMob.EntitySaveSkinNBT;
import farn.randomMob.RandomMob;
import net.lenni0451.classtransform.InjectionCallback;
import net.lenni0451.classtransform.annotations.CTarget;
import net.lenni0451.classtransform.annotations.CTransformer;
import net.lenni0451.classtransform.annotations.injection.CInject;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.NBTTagCompound;

@CTransformer(EntityLiving.class)
public class TransformEntityLiving implements EntitySaveSkinNBT {

    private EntityLiving self = (EntityLiving) (Object) this;

    public int entitySkinID = self.entityId;
    public String biomeRandomMob = "unknown";

    //read nbt for skinID and biome that they are currently in
    @CInject(method = "readEntityFromNBT", target = @CTarget("TAIL"))
    public void readNbt(NBTTagCompound nbt, InjectionCallback ci) {
        entitySkinID = nbt.getInteger("randomMobEntityID");
        if(!nbt.hasKey("randomMobEntityID")) {
            entitySkinID = self.entityId;
        }
        biomeRandomMob = nbt.getString("randomMobBiome");
        if(!nbt.hasKey("randomMobBiome")) {
            biomeRandomMob = RandomMob.getBiomeForEntity(self);
        }
    }

    //set nbt for skinID and biome that they are currently in
    @CInject(method = "writeEntityToNBT", target = @CTarget("TAIL"))
    public void writeNbt(NBTTagCompound nbt, InjectionCallback ci) {
        nbt.setInteger("randomMobEntityID", this.entitySkinID);
        nbt.setString("randomMobBiome", this.biomeRandomMob);
    }

    //get skinID
    @Override
    public int getEntitySkinID() {
        return entitySkinID;
    }

    //get current biome
    @Override
    public String getBiomeSpawn() {
        return biomeRandomMob;
    }

    //set current biome
    @Override
    public void setBiomeSpawn(String biome) {
        biomeRandomMob = biome;
    }
}
