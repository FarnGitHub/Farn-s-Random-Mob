package farn.randomMob.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class TextureRule {
    private Map<String, String[]> variants = null;
    private String[] defVariants;
    public final String backup;

    public TextureRule(String texture, boolean readProp) {
        this.backup = texture;

        if(!readProp) return;

        String propPath = texture.replace(".png", ".properties");
        InputStream stream = Util.getResource(propPath);
        if(stream != null) loadProp(stream);

        if(this.defVariants == null)
            this.defVariants = Util.getOldFormatVariants(backup);
    }

    public TextureRule(String texture) {
        this(texture, true);
    }

    public void loadProp(InputStream stream) {
        try {
            Properties props = new Properties();
            props.load(stream);
            variants = new HashMap<>();

            for(Map.Entry<?, ?> entry : props.entrySet()) {
                String key = (String)entry.getKey();
                if(!key.startsWith("biome.")) continue;

                String[] variants = Util.splitAndTrim((String)entry.getValue());
                if(key.equals("biome.default")) defVariants = variants;

                this.variants.put(key, variants);
            }
        } catch (IOException e) {
            System.err.println("[RandomMob] Failed to load texture properties");
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
    }

    public String getTexture(String biome, int id) {
        if(variants != null) {
            String[] variants = this.variants.getOrDefault(biome, defVariants);
            if(variants != null) {
                String texture = variants[id % variants.length];
                return Util.hasResource(texture) ? texture : backup;
            }
        }
        return backup;
    }

    public boolean equals(Object other) {
        return other == this ||
               other instanceof TextureRule rule &&
               Objects.equals(rule.backup, backup);
    }
}
