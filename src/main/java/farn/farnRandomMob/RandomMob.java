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

	/** Called when an entity is loaded; assigns skin id if needed. */
	public static void entityLoaded(Entity entity) {
		if (entity.skin == null && entity instanceof LivingEntity && !(entity instanceof PlayerEntity)) {
			if (entity.world.isMultiplayer) {
				entity.skin = String.valueOf(entity.networkId);
			} else {
				IEntitySkinID id = (IEntitySkinID) entity;
				if("unknown".equals(id.getBiomeSpawn())) {
					id.setBiomeSpawn(getBiomeForEntity(entity));
				}
				entity.skin = String.valueOf(id.getEntitySkinID()) + "_" + id.getBiomeSpawn().toLowerCase();
				System.out.println(entity.skin);
			}
		}
	}

	/** Clears all caches, used on resource reload. */
	public static void clearTextureCache() {
		propertiesCache.clear();
		textureVariantsCache.clear();
	}

	/**
	 * Resolve texture for a mob using its skinUrl:
	 *   <entityId>_<biome>
	 * Example: "42_Forest"
	 */
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
			entityId = Math.abs(skinUrl.hashCode()); // fallback
		}

		// --- 1️⃣ try properties file for biome ---
		Properties props = getPropertiesForTexture(baseTexture);
		if (props != null) {
			// Normalize biome name to match properties keys
			String key = "biome." + biome.toLowerCase().replace(' ', '_');

			// Get the actual texture path(s) from the properties
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

		// --- 2️⃣ fallback to old random naming ---
		String[] variants = textureVariantsCache.computeIfAbsent(baseTexture, RandomMob::getTextureVariants);
		if (variants.length > 1) {
			int idx = entityId % variants.length;
			if (!variants[idx].equals(variants[0])) {
				return getTextureNormal(variants[idx]);
			}
		}
		return -1;
	}

	// ----------------------------------------------------------------------
	// Helpers
	// ----------------------------------------------------------------------

	/** Loads and caches the .properties file for a given base texture. */
	private static Properties getPropertiesForTexture(String texture) {
		return propertiesCache.computeIfAbsent(texture, RandomMob::loadProperties);
	}

	/** Actually load a .properties from the resource pack. */
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

	/** Old-style numbered texture discovery. */
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

	/** Open a resource as stream from current texture pack. */
	public static InputStream getInputStream(String resource) {
		return MinecraftAccessor.getInstance().texturePacks.selected.getResource(resource);
	}

	/** Ask TextureManager to load a texture and return its GL id. */
	public static int getTextureNormal(String tex) {
		return MinecraftAccessor.getInstance().textureManager.load(tex);
	}

	/** Utility: get biome name for an entity’s current position. */
	public static String getBiomeForEntity(Entity e) {
		if (e == null || e.world == null) return "unknown";
		int x = MathHelper.floor(e.x);
		int z = MathHelper.floor(e.z);
		Biome biome = e.world.getBiomeSource().getBiome(x, z);
		return (biome != null && biome.name != null) ? biome.name : "unknown";
	}

}
