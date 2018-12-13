package com.cnksi.android.glide.widget;

import android.content.Context;
import android.graphics.Point;

import com.cnksi.android.glide.util.Utils;

import java.util.List;

/**
 * @author sunfusheng on 2018/6/19.
 */
public class GridLayoutHelper implements LayoutHelper {
    private int spanCount;
    private int cellWidth;
    private int cellHeight;
    private int margin;

    public GridLayoutHelper(int spanCount, int cellWidth, int cellHeight, int margin) {
        this.spanCount = spanCount;
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
        this.margin = margin;
    }

    @Override
    public Point getCoordinate(int position) {
        Point point = new Point();
        point.x = position % spanCount * (cellWidth + margin);
        point.y = position / spanCount * (cellHeight + margin);
        return point;
    }

    @Override
    public Point getSize(int position) {
        Point point = new Point();
        point.x = cellWidth;
        point.y = cellHeight;
        return point;
    }

    /**
     * 获取默认的LayoutHelper
     *
     * @param list
     * @return
     */
    public static GridLayoutHelper getDefaultLayoutHelper(Context context, List<ImageData> list) {
        int margin, maxImgWidth, maxImgHeight, cellWidth, cellHeight, minImgWidth, minImgHeight;
        margin = Utils.dp2px(context, 3);
        maxImgHeight = maxImgWidth = (Utils.getWindowWidth(context) - Utils.dp2px(context, 16) * 2) * 3 / 4;
        cellHeight = cellWidth = (maxImgWidth - margin * 3) / 3;
        minImgHeight = minImgWidth = cellWidth;
        int spanCount = Utils.getSize(list);
        if (spanCount == 1) {
            int width = list.get(0).realWidth;
            int height = list.get(0).realHeight;
            if (width > 0 && height > 0) {
                float whRatio = width * 1f / height;
                if (width > height) {
                    width = Math.max(minImgWidth, Math.min(width, maxImgWidth));
                    height = Math.max(minImgHeight, (int) (width / whRatio));
                } else {
                    height = Math.max(minImgHeight, Math.min(height, maxImgHeight));
                    width = Math.max(minImgWidth, (int) (height * whRatio));
                }
            } else {
                width = cellWidth;
                height = cellHeight;
            }
            return new GridLayoutHelper(spanCount, width, height, margin);
        }

        if (spanCount > 3) {
            spanCount = (int) Math.ceil(Math.sqrt(spanCount));
        }

        if (spanCount > 3) {
            spanCount = 3;
        }
        return new GridLayoutHelper(spanCount, cellWidth, cellHeight, margin);
    }
}
