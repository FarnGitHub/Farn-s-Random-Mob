package farn.farnRandomMob.mixin;

import net.minecraft.client.render.texture.HttpImageProcessor;
import net.minecraft.client.render.texture.HttpTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@Mixin(targets = "net.minecraft.client.render.texture.HttpTexture$1", priority = 900)
public abstract class MixinHttpTextureThread extends Thread {
	@Shadow(remap = false) private String f_4140703;
	@Shadow(remap = false) private HttpImageProcessor f_3368629;

	@SuppressWarnings("all")
	@Shadow(remap = false)
	private HttpTexture f_7124740;

	@Overwrite
	public void run() {
		HttpURLConnection conn = null;
		String location = this.f_4140703; // will need @Shadow

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
				if (f_3368629 == null) {
					f_7124740.image = ImageIO.read(conn.getInputStream());
				} else {
					f_7124740.image =
						f_3368629.process(ImageIO.read(conn.getInputStream()));
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
}
