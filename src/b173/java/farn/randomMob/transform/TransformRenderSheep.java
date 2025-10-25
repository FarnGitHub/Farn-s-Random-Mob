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
            method = "setWoolColorAndRender",
            target = @CTarget(
                    value = "INVOKE",
                    target = "Lnet/minecraft/src/RenderSheep;loadTexture(Ljava/lang/String;)V"
            )
    )
    public void randommob_useSkinUrl(RenderSheep render, String original) {
        ((RenderEntityAccessor) render).randommob_loadHttpsTexture(randommob_sheep.skinUrl, original);
    }

    @CInject(method = "setWoolColorAndRender", target = @CTarget(value="HEAD"))
    public void randommob_capturedSheepInstance(EntitySheep entitySheep, int i, float f, InjectionCallback callback) {
        randommob_sheep = entitySheep;
    }

}
