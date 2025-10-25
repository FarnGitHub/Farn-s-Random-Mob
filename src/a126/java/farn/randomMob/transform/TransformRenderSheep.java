package farn.randomMob.transform;

import farn.randomMob.RenderEntityAccessor;
import net.lenni0451.classtransform.InjectionCallback;
import net.lenni0451.classtransform.annotations.CTarget;
import net.lenni0451.classtransform.annotations.CTransformer;
import net.lenni0451.classtransform.annotations.injection.CInject;
import net.lenni0451.classtransform.annotations.injection.CRedirect;
import net.minecraft.src.*;

@CTransformer(RenderSheep.class)
public class TransformRenderSheep {

    EntitySheep randommob_sheep;

    //apply skin system to sheep fur
    @CRedirect(
            method = "a(Lnet/minecraft/src/EntitySheep;I)Z",
            target = @CTarget(
                    value = "INVOKE",
                    target = "Lnet/minecraft/src/RenderSheep;loadTexture(Ljava/lang/String;)V"
            )
    )
    public void randommob_useSkin(RenderSheep render, String original) {
        ((RenderEntityAccessor) render).calledLoadSkinTexture(randommob_sheep.skinUrl, original);
    }

    @CInject(method = "a(Lnet/minecraft/src/EntitySheep;I)Z", target = @CTarget(value="HEAD"))
    public void randommob_getSheep(EntitySheep entitySheep, int i, InjectionCallback callback) {
        randommob_sheep = entitySheep;
    }

}
