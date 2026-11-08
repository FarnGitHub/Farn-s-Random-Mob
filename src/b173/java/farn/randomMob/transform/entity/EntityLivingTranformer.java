package farn.randomMob.transform.entity;

import farn.randomMob.impl.EntityImpl;
import farn.randomMob.util.Util;
import net.lenni0451.classtransform.InjectionCallback;
import net.lenni0451.classtransform.annotations.CTarget;
import net.lenni0451.classtransform.annotations.CTransformer;
import net.lenni0451.classtransform.annotations.injection.CInject;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;

@CTransformer(value = EntityLiving.class)
public abstract class EntityLivingTranformer extends Entity implements EntityImpl {

    public int randommob_id = 0;

    public String randommob_biome = "default";

    public EntityLivingTranformer(World world) {
        super(world);
    }

    @CInject(method="<init>", target = @CTarget("TAIL"))
    public void randommob_init(InjectionCallback ci) {
        this.randomMob_setID(this.entityId);
        this.randomMob_setBiome(Util.getBiome(this));
    }

    //read nbt for skinID and biome that they are currently in
	@CInject(method = "Lnet/minecraft/src/EntityLiving;readEntityFromNBT(Lnet/minecraft/src/NBTTagCompound;)V", target = @CTarget("TAIL"))
	public void randommob_readNbt(NBTTagCompound nbt, InjectionCallback ci) {
        if(this.worldObj.multiplayerWorld) return;
        this.randomMob_setID(nbt.getInteger("randomMobEntityID"));
        this.randomMob_setBiome(nbt.getString("randomMobBiome"));
	}

	@CInject(method = "writeEntityToNBT(Lnet/minecraft/src/NBTTagCompound;)V", target = @CTarget("TAIL"))
	public void randommob_writeNbt(NBTTagCompound nbt, InjectionCallback ci) {
        if(this.worldObj.multiplayerWorld) return;
        nbt.setInteger("randomMobEntityID", this.randomMob_getID());
        nbt.setString("randomMobBiome", this.randomMob_getBiome());
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
