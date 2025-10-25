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

    private EntityLiving randommob_self = (EntityLiving) (Object) this;

    public int randommob_id = randommob_self.entityId;
    public String randommob_biome = "unknown";

    //read nbt for skinID and biome that they are currently in
    @CInject(method = "readEntityFromNBT", target = @CTarget("TAIL"))
    public void randomob_readNbt(NBTTagCompound nbt, InjectionCallback ci) {
        randommob_id = nbt.getInteger("randomMobEntityID");
        if(!nbt.hasKey("randomMobEntityID")) {
            randommob_id = randommob_self.entityId;
        }
        randommob_biome = nbt.getString("randomMobBiome");
        if(!nbt.hasKey("randomMobBiome")) {
            randommob_biome = RandomMob.getBiomeForEntity(randommob_self);
        }
    }

    //set nbt for skinID and biome that they are currently in
    @CInject(method = "writeEntityToNBT", target = @CTarget("TAIL"))
    public void randommob_writeNbt(NBTTagCompound nbt, InjectionCallback ci) {
        nbt.setInteger("randomMobEntityID", this.randommob_id);
        nbt.setString("randomMobBiome", this.randommob_biome);
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
