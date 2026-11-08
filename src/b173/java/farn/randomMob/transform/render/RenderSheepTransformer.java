package farn.randomMob.transform.render;

import farn.randomMob.RandomMob;
import net.lenni0451.classtransform.annotations.CLocalVariable;
import net.lenni0451.classtransform.annotations.CTransformer;
import net.lenni0451.classtransform.annotations.injection.CModifyConstant;
import net.minecraft.src.EntitySheep;
import net.minecraft.src.RenderSheep;

@CTransformer(value = RenderSheep.class)
public class RenderSheepTransformer {

    @CModifyConstant(method="setWoolColorAndRender(Lnet/minecraft/src/EntitySheep;IF)Z", stringValue = "/mob/sheep_fur.png", optional = true)
    public String randomMob_useRandomTextureSheep(String backup, @CLocalVariable(index = 1) EntitySheep living) {
        return RandomMob.getTexture(living, backup);
    }

}
