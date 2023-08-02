package net.minecraft.src;

import java.util.HashMap;

public class RenderEngineRandomMobs extends RenderEngine {

	public RenderEngineRandomMobs() {
		super(ModLoader.getMinecraftInstance().texturePackList, ModLoader.getMinecraftInstance().gameSettings);
	
	}

	public int getTextureForDownloadableImage(String string1, String string2) {			
		int threaddownloadimagedata = mod_RandomMobs.getTexture(string1, string2);
		if(threaddownloadimagedata >= 0) {
			return threaddownloadimagedata;
		} else {
			return super.getTextureForDownloadableImage(string1, string2);
		}
	}

	public void refreshTextures() {
		mod_RandomMobs.clearTextureCache();
		super.refreshTextures();
		ColorizerWater.func_28182_a(this.func_28149_a("/misc/watercolor.png"));
		ColorizerGrass.func_28181_a(this.func_28149_a("/misc/grasscolor.png"));
		ColorizerFoliage.func_28152_a(this.func_28149_a("/misc/foliagecolor.png"));
	}
}
