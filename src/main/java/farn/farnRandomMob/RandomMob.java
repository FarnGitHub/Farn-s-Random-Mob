package farn.farnRandomMob;

import farn.farnRandomMob.mixin.MinecraftAccessor;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.living.LivingEntity;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class RandomMob implements ClientModInitializer {

	// Cache for .properties -> texture rules
	private static final Map<String, Properties> propertiesCache = new HashMap<>();
	// Cache for old-style numeric variants (texture.png, texture2.png…)
	private static final Map<String, String[]> textureVariantsCache = new HashMap<>();

	@Override
	public void onInitializeClient() { }

	public static void entityLoaded(Entity entity) {
		if (entity.skin == null && entity instanceof LivingEntity && !(entity instanceof PlayerEntity)) {
			if (entity.world.isMultiplayer) {
				entity.skin = entity.networkId + "_" + getBiomeForEntity(entity);
			} else {
				IEntitySkinID id = (IEntitySkinID) entity;
				if("unknown".equals(id.getBiomeSpawn())) {
					id.setBiomeSpawn(getBiomeForEntity(entity));
				}
				entity.skin = String.valueOf(id.getEntitySkinID()) + "_" + id.getBiomeSpawn().toLowerCase();
			}
		}
	}

	public static void clearTextureCache() {
		propertiesCache.clear();
		textureVariantsCache.clear();
	}

	public static int getTextureRandomMob(String skinUrl, String baseTexture) {
		if (skinUrl == null || baseTexture == null) return -1;

		// Split "<id>_<biome>"
		String[] parts = skinUrl.split("_", 2);
		String idPart = parts[0];
		String biome = parts.length > 1 ? parts[1] : "unknown";

		int entityId;
		try {
			entityId = Math.abs(Integer.parseInt(idPart));
		} catch (NumberFormatException e) {
			entityId = Math.abs(skinUrl.hashCode());
		}

		Properties props = getPropertiesForTexture(baseTexture);
		if (props != null) {
			String key = "biome." + biome.toLowerCase().replace(' ', '_');

			String rule = props.getProperty(key);
			if (rule == null) rule = props.getProperty("biome.default");

			if (rule != null) {
				String[] variants = Arrays.stream(rule.split(","))
					.map(String::trim)
					.filter(s -> !s.isEmpty())
					.toArray(String[]::new);
				if (variants.length > 0) {
					String chosen = variants[entityId % variants.length];
					return getTextureNormal(chosen);
				}
			}
		}

		String[] variants = textureVariantsCache.computeIfAbsent(baseTexture, RandomMob::getTextureVariants);
		if (variants.length > 1) {
			int idx = entityId % variants.length;
			if (!variants[idx].equals(variants[0])) {
				return getTextureNormal(variants[idx]);
			}
		}
		return -1;
	}

	private static Properties getPropertiesForTexture(String texture) {
		return propertiesCache.computeIfAbsent(texture, RandomMob::loadProperties);
	}

	private static Properties loadProperties(String baseTexture) {
		String path = baseTexture.replace(".png", ".properties");
		try (InputStream in = getInputStream(path)) {
			if (in != null) {
				Properties p = new Properties();
				p.load(in);
				return p;
			}
		} catch (IOException ignored) {}
		return null;
	}

	private static String[] getTextureVariants(String texture) {
		int dot = texture.lastIndexOf('.');
		if (dot < 0) return new String[]{texture};

		String prefix = texture.substring(0, dot);
		String suffix = texture.substring(dot);
		List<String> list = new ArrayList<>();
		list.add(texture);

		for (int i = 2; i < 1000; i++) {
			String candidate = prefix + i + suffix;
			try (InputStream in = getInputStream(candidate)) {
				if (in == null) break;
				list.add(candidate);
			} catch (IOException e) {
				break;
			}
		}
		return list.toArray(new String[0]);
	}

	public static InputStream getInputStream(String resource) {
		return MinecraftAccessor.getInstance().texturePacks.selected.getResource(resource);
	}

	public static int getTextureNormal(String tex) {
		return MinecraftAccessor.getInstance().textureManager.load(tex);
	}

	public static String getBiomeForEntity(Entity e) {
		if (e == null || e.world == null) return "unknown";
		int x = MathHelper.floor(e.x);
		int z = MathHelper.floor(e.z);
		Biome biome = e.world.getBiomeSource().getBiome(x, z);
		return (biome != null && biome.name != null) ? biome.name : "unknown";
	}

}
