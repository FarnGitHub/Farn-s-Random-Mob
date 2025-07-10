package net.minecraft.src;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import net.minecraft.client.Minecraft;

public class mod_RandomMobs extends BaseMod {
	private static Map textureVariantsMap = new HashMap();
	private static Random random;

	public mod_RandomMobs() {
		ModLoader.SetInGameHook(this, true, false);
		ModLoader.getMinecraftInstance().renderEngine = new RenderEngineRandomMobs();
		fixMcPatcher();
	}

	public boolean OnTickInGame(net.minecraft.client.Minecraft mc) {
		random = new Random(mc.theWorld.getRandomSeed());
		List entityList = mc.theWorld.getLoadedEntityList();

		for(int e = 0; e < entityList.size(); ++e) {
			Entity entity = (Entity)entityList.get(e);
			if(entity.skinUrl == null) {
				if(entity instanceof EntityLiving) {
					if(!(entity instanceof EntityPlayer)) {
						entity.skinUrl = "" + random.nextInt(entity.entityId + 1);
					}
				}
			}
		}

		return true;

	}

	public static void clearTextureCache() {
		textureVariantsMap.clear();	
	}

	public static int getTexture(String skinUrl, String texture) {
		if(texture != null && skinUrl != null && skinUrl.length() > 1) {
			char ch = skinUrl.charAt(0);
			if(ch >= 48 && ch <= 57) {
				int num = Math.abs(skinUrl.hashCode());
				String[] texs = (String[])(textureVariantsMap.get(texture));
				if(texs == null) {
					texs = getTextureVariants(texture);
					textureVariantsMap.put(texture, texs);
				}

				if(texs != null && texs.length > 0) {
					int index = num % texs.length;
					String tex = texs[index];
					return tex == texs[0] ? -1 : getTexture(tex);
				}
			}
		}

		return -1;			
	}

	private static String[] getTextureVariants(String texture) {
		String[] texs = new String[]{texture};
		int pointPos = texture.lastIndexOf(46);
		if(pointPos < 0) {
			return texs;
		} else {
			String prefix = texture.substring(0, pointPos);
			String suffix = texture.substring(pointPos);
			int countVariants = getCountTextureVariants(texture, prefix, suffix);
			if(countVariants <= 1) {
				return texs;
			} else {
				texs = new String[countVariants];
				texs[0] = texture;

				for(int i = 1; i < texs.length; ++i) {
					int texNum = i + 1;
					texs[i] = prefix + texNum + suffix;
				}

				return texs;
			}
		}
	}

	private static int getCountTextureVariants(String texture, String prefix, String suffix) {
		short maxNum = 1000;

		for(int num = 2; num < maxNum; ++num) {
			String variant = prefix + num + suffix;

			try {
				InputStream e = getInputStream(variant);
				if(e == null) {
					return num - 1;
				}

				e.close();
			} catch (IOException iOException8) {
				return num - 1;
			}
		}

		return maxNum;
	}

	public String Version() {
		return "1.4";
	}

	public static InputStream getInputStream(String resource) {
		return ModLoader.getMinecraftInstance().texturePackList.selectedTexturePack.getResourceAsStream(resource);
	}

	public static int getTexture(String tex) {
		return ModLoader.getMinecraftInstance().renderEngine.getTexture(tex);
	}

	public static void fixMcPatcher() {
		try {
			Class patcher = Class.forName("com.pclewis.mcpatcher.mod.TextureUtils");
			Method method = patcher.getDeclaredMethod("refreshTextureFX", new Class[]{List.class});
			method.invoke((Object)null, new Object[]{(List)ModLoader.getPrivateValue(RenderEngine.class, ModLoader.getMinecraftInstance().renderEngine, 6)});
		} catch(Exception e) {
		}
	}
}
