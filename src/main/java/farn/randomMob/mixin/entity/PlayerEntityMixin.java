package farn.randomMob.mixin.entity;

import farn.randomMob.util.Util;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(value = PlayerEntity.class, priority = 500)
public abstract class PlayerEntityMixin extends LivingEntityMixin{

    @Shadow
    public String name;

    public PlayerEntityMixin(World world) {
        super(world);
    }

    @Override
    public void randommob_init(CallbackInfo ci) {
        this.randomMob_setID(Objects.hashCode(this.name));
        this.randomMob_setBiome(Util.getBiome((PlayerEntity)(Object)this));
    }
}
