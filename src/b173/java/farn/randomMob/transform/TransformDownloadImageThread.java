package farn.randomMob.transform;

import net.lenni0451.classtransform.InjectionCallback;
import net.lenni0451.classtransform.annotations.CShadow;
import net.lenni0451.classtransform.annotations.CTarget;
import net.lenni0451.classtransform.annotations.CTransformer;
import net.lenni0451.classtransform.annotations.injection.CInject;
import net.lenni0451.classtransform.annotations.injection.COverride;
import net.minecraft.src.ImageBuffer;
import net.minecraft.src.ThreadDownloadImageData;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@CTransformer(name = "net.minecraft.src.ThreadDownloadImage")
public class TransformDownloadImageThread {

    //don't make it flood console log with bunch of MalformedURLException
    @CInject(method="run", target = @CTarget("HEAD"), cancellable = true)
    public void randommob_run(InjectionCallback callback) {
        if (location == null || !location.startsWith("http")) {
            callback.setCancelled(true);
        }

    }

    @CShadow
    private String location;
}
