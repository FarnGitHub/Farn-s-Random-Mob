package farn.randomMob.transform;

import farn.randomMob.RenderAccessor;
import net.lenni0451.classtransform.InjectionCallback;
import net.lenni0451.classtransform.annotations.CTarget;
import net.lenni0451.classtransform.annotations.CTransformer;
import net.lenni0451.classtransform.annotations.injection.CInject;
import net.lenni0451.classtransform.annotations.injection.COverride;
import net.lenni0451.classtransform.annotations.injection.CRedirect;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;

@CTransformer(RenderSheep.class)
public class TransformSheepFur {

    @COverride
    protected boolean setWoolColorAndRender(EntitySheep entitySheep, int i, float f) {
        if (i == 0 && !entitySheep.getSheared()) {
            ((RenderAccessor)this).FarnloadHttpsTexture(entitySheep.skinUrl, "/mob/sheep_fur.png");
            float var4 = entitySheep.getEntityBrightness(f);
            int var5 = entitySheep.getFleeceColor();
            GL11.glColor3f(
                    var4 * EntitySheep.fleeceColorTable[var5][0], var4 * EntitySheep.fleeceColorTable[var5][1], var4 * EntitySheep.fleeceColorTable[var5][2]
            );
            return true;
        } else {
            return false;
        }
    }


}
