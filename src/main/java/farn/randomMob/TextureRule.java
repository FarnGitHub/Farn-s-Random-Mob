package farn.randomMob;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class TextureRule {
    public Object2ObjectMap<String, String[]> biomeVariants = null;
    public final String[] defVariants;

    public TextureRule(@NotNull String texture) {
        InputStream stream = RandomMob.getResource(texture.replace(".png", ".properties"));
        //Check if we have .properties file
        //so we don't have to create new object for biomeVariants when it doesn't exist
        if(stream != null) {
            RandomMob.LOGGER.info("{} contain .properties file", texture);
            try {
                Properties props = new Properties();
                props.load(stream);
                biomeVariants = new Object2ObjectOpenHashMap<>();
                for(Map.Entry<?, ?> entry : props.entrySet()) {
                    String key = (String)entry.getKey();
                    if(!key.startsWith("biome.")) continue;
                    String[] variants = Arrays.stream
                             (((String)entry.getValue()).split(","))
                            .map(String::trim)
                            .filter(s -> !s.isEmpty())
                            .toArray(String[]::new);
                    if(key.equals("biome.default"))
                        //default texture variant if biome don't match
                        biomeVariants.defaultReturnValue(variants);
                    else
                        biomeVariants.put(key, variants);
                }
            } catch (IOException ignored) {
            }
        }
        defVariants = getOldFormatVariants(texture);
    }

    //A getter for random texture
    public String get(String biome, int id) {
        if(biomeVariants != null) {
            String[] variants = biomeVariants.get(biome);
            if(variants != null) {
                return variants[id % variants.length];
            }
        }
        return defVariants[id % defVariants.length];
    }

    //the old format for random texture (mob<2-1000>.png)
    private static String[] getOldFormatVariants(String texture) {
        int dot = texture.lastIndexOf('.');
        if (dot < 0) return new String[]{texture};

        String prefix = texture.substring(0, dot);
        String suffix = texture.substring(dot);
        List<String> list = new ArrayList<>();
        list.add(texture);

        for (int i = 2; i < 1000; i++) {
            String candidate = prefix + i + suffix;
            try (InputStream in = RandomMob.getResource(candidate)) {
                if (in == null) break;
                list.add(candidate);
            } catch (IOException e) {
                break;
            }
        }
        return list.toArray(new String[0]);
    }

    //if it has same default texture then return true
    public boolean equals(Object other) {
        if (other == this) return true;
        return other instanceof TextureRule rule &&
               rule.defVariants[0].equals(defVariants[0]);
    }
}
