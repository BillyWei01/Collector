package com.horizon.doodle

import com.horizon.doodle.worker.LogProxy
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.ByteString
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.util.concurrent.TimeUnit

internal object Downloader {
    private const val TAG = "Downloader"
    private const val IMAGE_SUFFIX = ".img"

    private val cacheDirPath: String by lazy {
        Utils.cacheDir + "/doodle/source/"
    }

    private val client: OkHttpClient by lazy {
        val capacity = Config.sourceCacheCapacity
        val agent = Config.userAgent
        val maxSize = if (capacity > 0) capacity else (256L shl 20)
        val builder = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .cache(Cache(File(cacheDirPath), maxSize))
        if (agent.isNotEmpty()) {
            builder.addNetworkInterceptor { chain ->
                chain.proceed(chain.request().newBuilder().header("User-Agent", agent).build())
            }
        }
        builder.build()
    }

    @Throws(IOException::class)
    fun getStream(request: Request): InputStream? {
        val response = client.newCall(request).execute()
        if (response.isSuccessful) {
            return response.body()?.byteStream()
        } else {
            throw IOException("request failed, " + response.code())
        }
    }

    fun downloadFile(request: Request, onlyIfCached: Boolean): File? {
        var tmpFile: File? = null
        try {
            val fileName = Cache.key(request.url()) + IMAGE_SUFFIX
            val cachedFile = File(cacheDirPath, fileName)
            if (cachedFile.exists()) {
                return cachedFile
            }
            if (onlyIfCached) {
                return null
            }

            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val inputStream = response.body()!!.byteStream()
                tmpFile = File(cacheDirPath, "tmp_$fileName")
                if (Utils.streamToFile(inputStream, tmpFile) && tmpFile.renameTo(cachedFile)) {
                    return cachedFile
                }
            }
        } catch (e: Exception) {
            LogProxy.e(TAG, e)
        } finally {
            deleteFileQuietly(tmpFile)
        }
        return null
    }

    fun downloadOnly(url: String, desFile: File? = null): File? {
        var tmpFile: File? = null
        try {
            if (desFile != null && desFile.exists()) {
                return desFile
            }

            val request = Request.Builder().url(url)
                .cacheControl(CacheControl.Builder().noStore().build())
                .build()

            val fileName = Cache.key(request.url()) + IMAGE_SUFFIX
            tmpFile = File(cacheDirPath, "tmp_$fileName")
            val cachedFile = File(cacheDirPath, fileName)
            if (cachedFile.exists()) {
                if (desFile == null) {
                    return cachedFile
                }
                if (Utils.copyFile(cachedFile, tmpFile) && tmpFile.renameTo(desFile)) {
                    return desFile
                }
            }

            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val inputStream = response.body()!!.byteStream()
                val targetFile = desFile ?: cachedFile
                if (Utils.streamToFile(inputStream, tmpFile) && tmpFile.renameTo(targetFile)) {
                    return targetFile
                }
            }
        } catch (e: Exception) {
            LogProxy.e(TAG, e)
        } finally {
            deleteFileQuietly(tmpFile)
        }
        return null
    }

    private fun deleteFileQuietly(file: File?) {
        try {
            if (file != null && file.exists()) {
                file.delete()
            }
        } catch (ignore: Exception) {
        }
    }

    fun getSourceCacheFile(url: String): File? {
        val cacheFile = File(cacheDirPath + ByteString.encodeUtf8(url).md5().hex() + IMAGE_SUFFIX)
        return if (cacheFile.exists()) cacheFile else null
    }
}

