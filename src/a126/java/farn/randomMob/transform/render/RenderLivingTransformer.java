package farn.randomMob.transform.render;

import farn.randomMob.RandomMob;
import net.lenni0451.classtransform.annotations.CTarget;
import net.lenni0451.classtransform.annotations.CTransformer;
import net.lenni0451.classtransform.annotations.injection.CRedirect;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.RenderLiving;

@CTransformer(value = RenderLiving.class)
public class RenderLivingTransformer {

    @CRedirect(method="a(Lnet/minecraft/src/EntityLiving;DDDFF)V", target = @CTarget(value="INVOKE", target = "Lnet/minecraft/src/EntityLiving;getEntityTexture()Ljava/lang/String;"))
    public String randomMob_useRandomTextureLiving(EntityLiving living) {
        return RandomMob.getTexture(living, living.getEntityTexture());
    }

}
