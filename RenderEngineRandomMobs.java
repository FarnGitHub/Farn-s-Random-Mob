package net.minecraft.src;

import java.lang.IllegalArgumentException;

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
		try {
			super.refreshTextures();
		} catch (IllegalArgumentException theSuperAnnoyingCrash) {
		}
	}
}
