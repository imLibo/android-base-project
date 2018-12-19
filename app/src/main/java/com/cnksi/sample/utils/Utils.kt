package com.cnksi.sample.utils

import android.content.Context
import com.cnksi.android.glide.progress.ProgressManager
import com.cnksi.android.https.HttpsUtil
import com.cnksi.android.https.Tls12SocketFactory
import com.cnksi.android.log.KLog
import com.qq.weixin.mp.aes.Utils
import okhttp3.OkHttpClient
import java.text.DecimalFormat
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.HttpsURLConnection

/**
 *
 * @author lyongfly
 * @copyRights 四川金信石信息技术有限公司
 * @date 2018/7/17
 * @version 1.0
 * @since 1.0
 */
object Utils {

    val BKS_FILE = "client.118.122.132.40.bks"
    val BKS_PASSWORD = "client.2019"

    //okhttp 超时时间 10 min
    val HTTP_TIMEOUT = 10
    /**
     * 验证主机名
     */
    var SafeHostnameVerifier: HostnameVerifier = HostnameVerifier { hostname, session ->
        KLog.d("hostname->$hostname")
        if (hostname.contains("118.122.132.40")) {
            true
        } else {
            val hv = HttpsURLConnection.getDefaultHostnameVerifier()
            hv.verify(hostname, session)
        }
    }

    /**
     * 初始化okgo
     */
    fun getOkHttpClient(mContext: Context): OkHttpClient {
        val builder = OkHttpClient.Builder()
        //        //超时时间设置，默认60秒
        builder.readTimeout(HTTP_TIMEOUT.toLong(), TimeUnit.MINUTES)      //全局的读取超时时间
        builder.writeTimeout(HTTP_TIMEOUT.toLong(), TimeUnit.MINUTES)     //全局的写入超时时间
        builder.connectTimeout(HTTP_TIMEOUT.toLong(), TimeUnit.MINUTES)   //全局的连接超时时间
        //初始化Https
        val sslParams = HttpsUtil.getSSLParams(mContext, BKS_FILE, BKS_PASSWORD)
        //实现只兼容 TSL v1.2 协议
        val socketFactory = Tls12SocketFactory(sslParams!!.sSLSocketFactory)
        builder.sslSocketFactory(socketFactory, sslParams.trustManager)
        builder.hostnameVerifier(SafeHostnameVerifier)
        builder.addInterceptor(ProgressManager.getProgressInterceptor())
        return builder.build()
    }

    /**
     * 保留两位小数
     * @param value
     * @return
     */
    fun keepTwoBit(value: Float): Float {
        val df = DecimalFormat("0.00")
        return java.lang.Float.parseFloat(df.format(value.toDouble()))
    }

    /**
     * 得到文件在线浏览地址
     *
     * @param folder   文件夹
     * @param fileName 文件名
     * @return 完整的加密后的地址
     */
    fun getFileUrl(ip: String, appid: String, folder: String, fileName: String): String {
        val paramStr = "appid=$appid&folder=$folder&filename=$fileName&clientid=0000"
        val url = ip + "/file/download?" + Utils.getEncryptMsg(paramStr)
        //替换掉所有的换行符
        return url.replace("\n".toRegex(), "")
    }

    /**
     * 得到文件在线浏览地址
     *
     * @param folder   文件夹
     * @param fileName 文件名
     * @return 完整的加密后的地址
     */
    fun getFileUrl(appid: String, folder: String, fileName: String): String {
        val paramStr = "appid=$appid&folder=$folder&filename=$fileName&clientid=0000"
        val url = "http://10.177.98.252/v410/file/download?" + Utils.getEncryptMsg(paramStr)
        //替换掉所有的换行符
        return url.replace("\n".toRegex(), "")
    }
}