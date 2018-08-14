package com.cnksi.amaploc.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * 高德定位
 *
 * @author lyongfly
 * @version 1.0
 * @copyRights 四川金信石信息技术有限公司
 * @date 2018/8/13
 * @since 1.0
 */
public class AMapLocService extends Service {

    public AMapLocService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
