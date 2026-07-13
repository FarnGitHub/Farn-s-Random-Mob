package farn.randomMob.impl;

public interface RandomMobImpl {

    default String randomMob_getBiome() {
        return "default";
    }

    default void randomMob_setBiome(String biome){
    }

    default int randomMob_getID() {
        return 0;
    }

    default void randomMob_setId(int id){
    }
}
