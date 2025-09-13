package farn.randomMob.transform;

import farn.randomMob.IEntitySkinID;
import farn.randomMob.UniminedRandomMob;
import net.lenni0451.classtransform.InjectionCallback;
import net.lenni0451.classtransform.annotations.CTarget;
import net.lenni0451.classtransform.annotations.CTransformer;
import net.lenni0451.classtransform.annotations.injection.CInject;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.NBTTagCompound;

@CTransformer(EntityLiving.class)
public class TransformEntityLiving implements IEntitySkinID {

    private EntityLiving self = (EntityLiving) (Object) this;

    public int entitySkinID = self.entityId;
    public String biomeRandomMob = "unknown";

    @CInject(method = "readEntityFromNBT", target = @CTarget("TAIL"))
    public void readNbt(NBTTagCompound nbt, InjectionCallback ci) {
        entitySkinID = nbt.getInteger("randomMobEntityID");
        if(!nbt.hasKey("randomMobEntityID")) {
            entitySkinID = self.entityId;
        }
        biomeRandomMob = nbt.getString("randomMobBiome");
        if(!nbt.hasKey("randomMobBiome")) {
            biomeRandomMob = UniminedRandomMob.getBiomeForEntity(self);
        }
    }

    @CInject(method = "writeEntityToNBT", target = @CTarget("TAIL"))
    public void writeNbt(NBTTagCompound nbt, InjectionCallback ci) {
        nbt.setInteger("randomMobEntityID", this.entitySkinID);
        nbt.setString("randomMobBiome", this.biomeRandomMob);
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
