package net.minecraft.src;

import java.lang.IllegalArgumentException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.io.InputStream;

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
		if(mod_RandomMobs.doesClassExist("Config")) {
			this.OFclearTextureDataMap();
			setPrivateValue("dynamicTexturesUpdated", false);
			this.OFsetFontRenderer(false);
		}
		TexturePackBase texturepackbase = ModLoader.getMinecraftInstance().texturePackList.selectedTexturePack;
		Iterator iterator3 = ((HashMap)getPrivateValue("d")).keySet().iterator();

		while(iterator3.hasNext()) {
			int i = ((Integer)iterator3.next()).intValue();
			BufferedImage bufferedimage = (BufferedImage)((HashMap)getPrivateValue("d")).get(i);
			this.setupTexture(bufferedimage, i);
		}

		ThreadDownloadImageData s1;
		for(iterator3 = ((Map)getPrivateValue("h")).values().iterator(); iterator3.hasNext(); s1.textureSetupComplete = false) {
			s1 = (ThreadDownloadImageData)iterator3.next();
		}

		iterator3 = ((HashMap)getPrivateValue("b")).keySet().iterator();

		BufferedImage ioexception1;
		String s11;
		while(iterator3.hasNext()) {
			s11 = (String)iterator3.next();

			try {
				if(s11.startsWith("##")) {
					ioexception1 = this.unwrapImageByColumnsCustom(this.readTextureImageCustom(s11.substring(2)));
				} else if(s11.startsWith("%clamp%")) {
					setPrivateValue("j", true);
					ioexception1 = this.readTextureImageCustom(s11.substring(7));
				} else if(s11.startsWith("%blur%")) {
					setPrivateValue("k", true);
					ioexception1 = this.readTextureImageCustom(s11.substring(6));
				} else {
					ioexception1 = this.readTextureImageCustom(s11);
				}

				int j = ((Integer)((HashMap)getPrivateValue("b")).get(s11)).intValue();
				this.setupTexture(ioexception1, j);
				setPrivateValue("k", false);
				setPrivateValue("j", false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		iterator3 = ((HashMap)getPrivateValue("c")).keySet().iterator();

		while(iterator3.hasNext()) {
			s11 = (String)iterator3.next();

			try {
				if(s11.startsWith("##")) {
					ioexception1 = this.unwrapImageByColumnsCustom(this.readTextureImageCustom(s11.substring(2)));
				} else if(s11.startsWith("%clamp%")) {
					setPrivateValue("j", true);
					ioexception1 = this.readTextureImageCustom(s11.substring(7));
				} else if(s11.startsWith("%blur%")) {
					setPrivateValue("k", true);
					ioexception1 = this.readTextureImageCustom(s11.substring(6));
				} else {
					ioexception1 = this.readTextureImageCustom(s11);
				}

				this.aMethodThingIDK(ioexception1, (int[])((int[])((HashMap)getPrivateValue("c")).get(s11)));
				setPrivateValue("k", false);
				setPrivateValue("j", false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private Object getPrivateValue(String fieldName) {
		try {
			return ModLoader.getPrivateValue(RenderEngine.class, this, fieldName);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private void setPrivateValue(String fieldName, Object value) {
		try {
			ModLoader.setPrivateValue(RenderEngine.class, this, fieldName, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private BufferedImage readTextureImageCustom(String value) {
		if(mod_RandomMobs.doesClassExist("com.pclewis.mcpatcher.mod.TextureUtils")) {
			try {
				Class config = Class.forName("com.pclewis.mcpatcher.mod.TextureUtils");
				Method method = config.getDeclaredMethod("getResourceAsBufferedImage", String.class);
				return (BufferedImage)method.invoke((Object)null, value);
			} catch(Exception e) {
				return null;
			}
		} else {
			try {
				Method method = RenderEngine.class.getDeclaredMethod("a", InputStream.class);
				method.setAccessible(true);
				return (BufferedImage)method.invoke(this, ModLoader.getMinecraftInstance().texturePackList.selectedTexturePack.getResourceAsStream(value));
			} catch(Exception e) {
				return null;
			}
		}
	}

	private BufferedImage unwrapImageByColumnsCustom(BufferedImage image) {
		try {
			Method method = RenderEngine.class.getDeclaredMethod("c", BufferedImage.class);
			method.setAccessible(true);
			return (BufferedImage)method.invoke(this, image);
		} catch(Exception e) {
			return null;
		}
	}

	private BufferedImage aMethodThingIDK(BufferedImage image, int[] ai) {
		try {
			Method method = RenderEngine.class.getDeclaredMethod("a", BufferedImage.class, int[].class);
			method.setAccessible(true);
			return (BufferedImage)method.invoke(this, image, ai);
		} catch(Exception e) {
			return null;
		}
	}

	private void OFsetFontRenderer(boolean value) {
		try {
			Class config = Class.forName("Config");
			Method method = config.getDeclaredMethod("setFontRendererUpdated", boolean.class);
			method.invoke((Object)null, value);
		} catch(Exception e) {
		}
	}

	private void OFclearTextureDataMap() {
		try {
			Field privateObjectField = RenderEngine.class.getDeclaredField("textureDataMap");			
			privateObjectField.setAccessible(true);
			Object privateObjectInstance = privateObjectField.get(this);
			Class<?> innerClass = privateObjectInstance.getClass();
			Method publicVoidMethod = innerClass.getDeclaredMethod("clear");
			publicVoidMethod.invoke(privateObjectInstance);
		} catch(Exception e) {
		}
	}
}
