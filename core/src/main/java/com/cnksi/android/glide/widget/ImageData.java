package com.cnksi.android.glide.widget;

import android.graphics.Point;

import java.io.Serializable;

/**
 * @author sunfusheng on 2018/6/19.
 */
public class ImageData implements Serializable {
    public Object url;
    public String text;

    public int realWidth;
    public int realHeight;

    public int startX;
    public int startY;
    public int width;
    public int height;

    public ImageData(Object url) {
        this.url = url;
    }

    public ImageData(Object url, int realWidth, int realHeight) {
        this.url = url;
        this.realWidth = realWidth;
        this.realHeight = realHeight;
    }

    public ImageData from(ImageData imageData, LayoutHelper layoutHelper, int position) {
        if (imageData != null && layoutHelper != null) {
            Point coordinate = layoutHelper.getCoordinate(position);
            if (coordinate != null) {
                imageData.startX = coordinate.x;
                imageData.startY = coordinate.y;
            }

            Point size = layoutHelper.getSize(position);
            if (size != null) {
                imageData.width = size.x;
                imageData.height = size.y;
            }
        }
        return imageData;
    }
}
