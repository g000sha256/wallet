package ru.g000sha256.wallet.di

import android.content.Context
import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import java.io.File
import java.io.InputStream
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okhttp3.OkHttpClient
import ru.g000sha256.wallet.MainApplication
import ru.g000sha256.wallet.common.CoroutineDispatchers

@Module
class ApplicationModule {

    @Provides
    @Singleton
    fun provideContext(mainApplication: MainApplication): Context {
        return mainApplication
    }

    @Provides
    @Singleton
    fun provideCoroutineDispatchers(): CoroutineDispatchers {
        return CoroutineDispatchers(Dispatchers.Main.immediate, Dispatchers.IO)
    }

    @Provides
    @Singleton
    fun provideCoroutineScope(coroutineDispatchers: CoroutineDispatchers): CoroutineScope {
        return object : CoroutineScope {

            override val coroutineContext = coroutineDispatchers.mainCoroutineDispatcher + SupervisorJob()

        }
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    fun provideLocale(): Locale {
        return Locale("ru")
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val time = 20L
        return OkHttpClient.Builder()
            .callTimeout(time, TimeUnit.SECONDS)
            .connectTimeout(time, TimeUnit.SECONDS)
            .readTimeout(time, TimeUnit.SECONDS)
            .writeTimeout(time, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRequestManager(context: Context, okHttpClient: OkHttpClient): RequestManager {
        val memorySizeCalculator = MemorySizeCalculator.Builder(context)
            .build()
        val bitmapPoolSize = memorySizeCalculator.bitmapPoolSize.toLong()
        val lruBitmapPool = LruBitmapPool(bitmapPoolSize)
        val drawableTransitionOptions = DrawableTransitionOptions.withCrossFade()
        val cacheDirectoryGetter = DiskLruCacheFactory.CacheDirectoryGetter { File(context.filesDir, "images") }
        val diskLruCacheFactory = DiskLruCacheFactory(cacheDirectoryGetter, 100 * 1024 * 1024)
        val glideBuilder = GlideBuilder()
            .setBitmapPool(lruBitmapPool)
            .setDefaultTransitionOptions(Drawable::class.java, drawableTransitionOptions)
            .setDiskCache(diskLruCacheFactory)
            .setMemorySizeCalculator(memorySizeCalculator)
        Glide.init(context, glideBuilder)
        val glide = Glide.get(context)
        val okHttpUrlLoaderFactory = OkHttpUrlLoader.Factory(okHttpClient)
        glide.registry.replace(GlideUrl::class.java, InputStream::class.java, okHttpUrlLoaderFactory)
        return glide.requestManagerRetriever.get(context)
    }

}