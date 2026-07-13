package farn.randomMob.util;

import net.modificationstation.stationapi.api.client.texture.TextureHelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Util {

    public static String[] split(Object lines) {
        return Arrays.stream(((String)lines).split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toArray(String[]::new);
    }

    public static InputStream getResource(String resource) {
        try {
            return TextureHelper.getTextureStream(resource);
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean hasResource(String resource) {
        return getResource(resource) != null;
    }

    public static String[] getOldFormatVariants(String texture) {
        int dot = texture.lastIndexOf('.');
        if (dot < 0) return new String[]{texture};

        String prefix = texture.substring(0, dot);
        String suffix = texture.substring(dot);
        List<String> list = new ArrayList<>();
        list.add(texture);

        for (int i = 2; i < 1000; i++) {
            String candidate = prefix + i + suffix;
            try (InputStream in = getResource(candidate)) {
                if (in == null) break;
                list.add(candidate);
            } catch (IOException e) {
                break;
            }
        }
        return list.toArray(new String[0]);
    }
}
