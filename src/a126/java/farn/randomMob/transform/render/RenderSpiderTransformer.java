package farn.randomMob.transform.render;

import farn.randomMob.RandomMob;
import net.lenni0451.classtransform.annotations.CLocalVariable;
import net.lenni0451.classtransform.annotations.CTransformer;
import net.lenni0451.classtransform.annotations.injection.CModifyConstant;
import net.minecraft.src.EntitySpider;
import net.minecraft.src.RenderSpider;

@CTransformer(RenderSpider.class)
public class RenderSpiderTransformer {

    @CModifyConstant(method="a(Lnet/minecraft/src/EntitySpider;IF)Z", stringValue = "/mob/spider_eyes.png", optional = true)
    public String randomMob_useRandomTextureSpider(String backup, @CLocalVariable(index = 1) EntitySpider living) {
        return RandomMob.getTexture(living, backup);
    }
}
