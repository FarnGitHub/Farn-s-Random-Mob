package net.minecraft.src;

import net.lenni0451.classtransform.annotations.CShadow;
import net.lenni0451.classtransform.annotations.CTransformer;
import net.lenni0451.classtransform.annotations.injection.COverride;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@CTransformer(ThreadDownloadImage.class)
public class RandomMobTransformDownloadImage {
    @COverride
    public void run() {
        HttpURLConnection conn = null;
        String location = this.location; // will need @Shadow

        try {
            if (location == null || !location.startsWith("http")) {
                return; // skip bad values
            }

            URL url = new URL(location);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(false);
            conn.connect();

            if (conn.getResponseCode() / 100 != 4) {
                if (this.buffer == null) {
                    this.imageData.image = ImageIO.read(conn.getInputStream());
                } else {
                    this.imageData.image =
                            this.buffer.parseUserSkin(ImageIO.read(conn.getInputStream()));
                }
            }
        } catch (MalformedURLException e) {
            // Quietly ignore or log once
            System.err.println("[RandomMob] Bad URL: " + location);
        } catch (IOException e) {
            e.printStackTrace(); // keep real IO errors
        } finally {
            if (conn != null) conn.disconnect();
        }
    }

    @CShadow
    private ThreadDownloadImageData imageData;
    @CShadow
    private String location;
    @CShadow
    private ImageBuffer buffer;
}
