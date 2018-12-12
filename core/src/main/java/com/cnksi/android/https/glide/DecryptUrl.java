package com.cnksi.android.https.glide;

import android.text.TextUtils;

import com.bumptech.glide.load.model.GlideUrl;
import com.cnksi.android.encrypt.weixin.aes.WxAes;

/**
 * Glide 解密加密url 用明文url作为缓存key
 *
 * @author lyongfly
 * @version 1.0
 * @copyRights 四川金信石信息技术有限公司
 * @date 2018/12/10
 * @since 1.0
 */
public class DecryptUrl extends GlideUrl {

    private String mUrl;

    public DecryptUrl(String url) {
        super(url);
        this.mUrl = url;
    }

    @Override
    public String getCacheKey() {
        return getDecryptUrl();
    }

    private String getDecryptUrl() {
        if (mUrl.contains("?")) {
            String[] urls = mUrl.split("\\?");
            if (urls.length == 2) {
                String decryptUrl = WxAes.getDecryptMsg(urls[1]);
                if (!TextUtils.isEmpty(decryptUrl)) {
                    return urls[0] + "?" + decryptUrl;
                }
            }
        }
        return mUrl;
    }
}
