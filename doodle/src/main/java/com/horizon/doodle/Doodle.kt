
package com.horizon.doodle

import android.graphics.Bitmap
import android.net.Uri
import com.horizon.doodle.worker.lifecycle.LifecycleManager
import java.io.File

/**
 * Entrance of the framework
 */
object Doodle {
    /**
     * get global config object
     */
    @JvmStatic
    fun config() : Config {
        return Config
    }

    /**
     * load bitmap by file path, url, or asserts path
     *
     * @param path image path
     */
    @JvmStatic
    fun load(path: String): Request {
        return Request(path)
    }

    /**
     * load bitmap from drawable or raw resource
     *
     * @param resID drawable id or raw id
     */
    @JvmStatic
    fun load(resID: Int): Request {
        return Request(resID)
    }

    @JvmStatic
    fun load(uri: Uri): Request {
        return Request(uri)
    }

    @JvmStatic
    fun downloadOnly(url: String): File? {
        return Downloader.downloadOnly(url)
    }

    @JvmStatic
    fun getSourceCacheFile(url: String): File?{
        return Downloader.getSourceCacheFile(url)
    }

    /**
     * @param tag         identify the bitmap
     * @param bitmap      bitmap
     * @param toWeakCache cache to [WeakCache] if true,
     * otherwise cache to [LruCache]
     */
    @JvmStatic
    @JvmOverloads
    fun cacheBitmap(tag: String, bitmap: Bitmap, toWeakCache: Boolean = true) {
        MemoryCache.putBitmap(MHash.hash64(tag), bitmap, toWeakCache)
    }

    @JvmStatic
    fun getCacheBitmap(tag: String): Bitmap? {
        return MemoryCache.getBitmap(MHash.hash64(tag))
    }

    /**
     * Stop to put requests to [Worker]
     */
    @JvmStatic
    fun pauseRequest() {
        Dispatcher.pause()
    }

    /**
     * resume requests
     */
    @JvmStatic
    fun resumeRequest() {
        Dispatcher.resume()
    }

    @JvmStatic
    fun trimMemory(level: Int) {
        LruCache.trimMemory(level)
    }

    @JvmStatic
    fun clearMemory() {
        LruCache.clearMemory()
    }

    /**
     * Notify [host] destroy.
     *
     * The [host] may be one of Activity, Fragment, or Dialog (which also name 'page'), refer to [Request.host].
     * The [Worker] will auto cancel when page destroy if this method called in page's onDestroy()
     *
     * If the host is Activity, is not necessary to call this，
     * cause Doodle will do this by [Utils.registerActivityLifeCycle]
     */
    @JvmStatic
    fun notifyEvent(host: Any, event: Int) {
        LifecycleManager.notify(host, event)
    }
}
