package farn.randomMob.transform;

import farn.randomMob.UniminedRandomMob;
import net.lenni0451.classtransform.InjectionCallback;
import net.lenni0451.classtransform.annotations.CTarget;
import net.lenni0451.classtransform.annotations.CTransformer;
import net.lenni0451.classtransform.annotations.injection.CInject;
import net.minecraft.src.Entity;
import net.minecraft.src.World;

@CTransformer(World.class)
public class TransformWorld {

    @CInject(method="obtainEntitySkin", target=@CTarget("HEAD"))
    public void setSkinUrl(Entity entity, InjectionCallback info) {
        UniminedRandomMob.entityLoaded(entity);
    }


}
