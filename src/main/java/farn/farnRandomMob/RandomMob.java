package farn.farnRandomMob;

import farn.farnRandomMob.mixin.MinecraftAccessor;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.living.LivingEntity;
import net.minecraft.entity.living.player.PlayerEntity;
import net.ornithemc.osl.lifecycle.api.WorldEvents;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RandomMob implements ClientModInitializer {

	private static Map textureVariantsMap = new HashMap();

	@Override
	public void onInitializeClient() {
	}

	public static void entityLoaded(Entity entity) {
		if(entity.skin == null) {
			if(entity instanceof LivingEntity) {
				if(!(entity instanceof PlayerEntity)) {
					if(entity.world.isMultiplayer) {
						int randomId = entity.networkId;
						entity.skin = "" + randomId;
					} else {
						IEntitySkinID entityCustom = (IEntitySkinID)entity;
						entity.skin = "" + entityCustom.getEntitySkinID();
					}
				}
			}
		}
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
					return tex == texs[0] ? -1 : getTextureNormal(tex);
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

	public static InputStream getInputStream(String resource) {
		return MinecraftAccessor.getInstance().texturePacks.selected.getResource(resource);
	}

	public static int getTextureNormal(String tex) {
		return MinecraftAccessor.getInstance().textureManager.load(tex);
	}

}
