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

@Mixin(targets = "net.minecraft.client.render.texture.HttpTexture$1")
public abstract class MixinHttpTextureThread extends Thread {
	@Shadow(remap = false) private String f_4140703;
	@Shadow(remap = false) private HttpImageProcessor f_3368629;

	@SuppressWarnings("all")
	@Shadow(remap = false)
	private HttpTexture f_7124740;

	@Overwrite
	public void run() {
		HttpURLConnection conn = null;
		try {
			if (f_4140703 == null || !f_4140703.startsWith("http")) return;

			URL u = new URL(f_4140703);
			conn = (HttpURLConnection) u.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(false);
			conn.connect();

			if (conn.getResponseCode() / 100 != 4) {
				BufferedImage img = ImageIO.read(conn.getInputStream());
				f_7124740.image =
					(f_3368629 == null) ? img : f_3368629.process(img);
			}
		} catch (MalformedURLException e) {
			System.err.println("[RandomMob] Bad skin URL: " + f_4140703);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) conn.disconnect();
		}
	}
}
