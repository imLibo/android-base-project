package com.cnksi.android.utils;

/**
 * @author lyongfly
 * @version 1.0
 * @copyRights 四川金信石信息技术有限公司
 * @date 2018/10/18
 * @since 1.0
 */
public class TestUtil {

    public static void main() {
        String[] latlng = LatLngKcodeUtil.ltm2Latlng("6091-3339-8340");
        System.out.println(latlng[0] + "-" + latlng[1]);

        latlng = LatLngKcodeUtil.ltm2Latlng("6091-3239-2340");
        System.out.println(latlng[0] + "-" + latlng[1]);
    }


}
