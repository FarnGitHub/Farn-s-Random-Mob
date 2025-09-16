package farn.randomMob;

public interface EntitySaveSkinNBT {

	abstract int getEntitySkinID();

	abstract String getBiomeSpawn();

	abstract void setBiomeSpawn(String biome);
}
