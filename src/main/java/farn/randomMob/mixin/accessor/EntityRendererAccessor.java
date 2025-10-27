package farn.randomMob.mixin.accessor;

import net.minecraft.client.render.entity.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(EntityRenderer.class)
public interface EntityRendererAccessor {

	@Invoker("bindHttpTexture")
    boolean randommob_loadHttpsTexture(String skin, String og);
}
