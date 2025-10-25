package farn.randomMob.transform;

import farn.randomMob.RenderEntityAccessor;
import net.lenni0451.classtransform.annotations.CTarget;
import net.lenni0451.classtransform.annotations.CTransformer;
import net.lenni0451.classtransform.annotations.injection.CInject;
import net.lenni0451.classtransform.annotations.injection.CRedirect;
import net.minecraft.src.EntitySpider;
import net.minecraft.src.RenderSpider;

@CTransformer(RenderSpider.class)
public class TransformRenderSpider {

    EntitySpider randommob_spider;

    //apply skin system to spider eyes
    @CRedirect(
            method = "a(Lnet/minecraft/src/EntitySpider;I)Z",
            target = @CTarget(
                    value = "INVOKE",
                    target = "Lnet/minecraft/src/RenderSpider;loadTexture(Ljava/lang/String;)V"
            )
    )
    public void randommob_useSkin(RenderSpider render, String original) {
        ((RenderEntityAccessor) render).calledLoadSkinTexture(randommob_spider.skinUrl, original);
    }

    @CInject(method = "a(Lnet/minecraft/src/EntitySpider;I)Z", target = @CTarget(value="HEAD"))
    public void randommob_getSpiderEntity(EntitySpider entitySpider, int i) {
        randommob_spider = entitySpider;
    }
}
