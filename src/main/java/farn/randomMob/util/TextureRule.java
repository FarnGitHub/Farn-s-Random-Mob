package farn.randomMob.util;

import farn.randomMob.RandomMob;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Environment(EnvType.CLIENT)
public class TextureRule {
    private Map<String, String[]> biomeVariants = null;
    private String[] defVariants;
    private final String defTexture;

    public TextureRule(String texture) {
        defTexture = texture;

        String propPath = texture.replace(".png", ".properties");
        InputStream stream = Util.getResource(propPath);

        if(stream != null)
            loadProp(stream);

        if(defVariants == null)
            defVariants = Util.getOldFormatVariants(defTexture);
    }

    public void loadProp(InputStream stream) {
        try {
            Properties props = new Properties();
            props.load(stream);
            biomeVariants = new HashMap<>();

            for(Map.Entry<?, ?> entry : props.entrySet()) {
                String key = (String)entry.getKey();
                if(!key.startsWith("biome."))
                    continue;

                String[] variants = Util.split(entry.getValue());

                if(key.equals("biome.default"))
                    defVariants = variants;

                this.biomeVariants.put(key, variants);
            }
        } catch (IOException e) {
            RandomMob.LOGGER.error("Failed to load texture properties", e);
        }
    }

    public String getTexture(String biome, int id) {
        if(biomeVariants != null) {
            String[] variants = this.biomeVariants.getOrDefault(biome, defVariants);
            if(variants != null)
                return variants[id % variants.length];
        }
        return defTexture;
    }

    public boolean equals(Object other) {
        return other == this ||
               other instanceof TextureRule rule &&
               Objects.equals(rule.defTexture, defTexture);
    }
}
