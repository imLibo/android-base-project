package com.cnksi.android.https.glide;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.module.LibraryGlideModule;

import java.io.InputStream;

import okhttp3.OkHttpClient;

/**
 * Registers OkHttp related classes via Glide's annotation processor.
 * <p>
 * <p>For Applications that depend on this library and include an
 * {@link AppGlideModule} and Glide's annotation processor, this class
 * will be automatically included.
 */
@GlideModule
public final class OkHttpLibraryGlideModule extends LibraryGlideModule {

    public static OkHttpClient mOkHttpClient;

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        ModelLoaderFactory<GlideUrl, InputStream> factory;
        if (mOkHttpClient == null) {
            factory = new OkHttpUrlLoader.Factory();
        } else {
            factory = new OkHttpUrlLoader.Factory(mOkHttpClient);
        }
        registry.replace(GlideUrl.class, InputStream.class, factory);
    }
}
