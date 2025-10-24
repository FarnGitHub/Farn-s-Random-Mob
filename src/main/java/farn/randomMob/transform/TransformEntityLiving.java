package farn.randomMob.transform;

import farn.randomMob.EntityRandomMobData;
import farn.randomMob.RandomMob;
import net.lenni0451.classtransform.InjectionCallback;
import net.lenni0451.classtransform.annotations.CTarget;
import net.lenni0451.classtransform.annotations.CTransformer;
import net.lenni0451.classtransform.annotations.injection.CInject;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.NBTTagCompound;

@CTransformer(EntityLiving.class)
public class TransformEntityLiving implements EntityRandomMobData {

    private EntityLiving self = (EntityLiving) (Object) this;

    public int entitySkinID = self.entityId;
    public String biomeRandomMob = "unknown";

    //read nbt for skinID and biome that they are currently in
    @CInject(method = "readEntityFromNBT", target = @CTarget("TAIL"))
    public void randomob_readNbt(NBTTagCompound nbt, InjectionCallback ci) {
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
    public void randommob_writeNbt(NBTTagCompound nbt, InjectionCallback ci) {
        nbt.setInteger("randomMobEntityID", this.entitySkinID);
        nbt.setString("randomMobBiome", this.biomeRandomMob);
    }

    //get skinID
    @Override
    public int randommob_getEntitySkinID() {
        return entitySkinID;
    }

    //get current biome
    @Override
    public String randommob_getBiomeSpawn() {
        return biomeRandomMob;
    }

    //set current biome
    @Override
    public void randommob_setBiomeSpawn(String biome) {
        biomeRandomMob = biome;
    }
}
