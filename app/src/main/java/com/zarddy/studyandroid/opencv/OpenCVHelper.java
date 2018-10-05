package com.zarddy.studyandroid.opencv;

import android.graphics.Bitmap;

import com.zarddy.studyandroid.opencv.entity.PointMargin;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OpenCVHelper {

    public final static int LT_LOCATION = 0;
    public final static int LB_LOCATION = 1;
    public final static int RB_LOCATION = 2;
    public final static int RT_LOCATION = 3;

    /**
     * 透视扭曲
     */
    public static Bitmap warpPerspective(Bitmap srcBitmap, ArrayList<PointMargin> pointsMargin) {
        if (srcBitmap == null) {
            return null;
        }

        Mat src = new Mat();
        Utils.bitmapToMat(srcBitmap, src);//convert original bitmap to Mat, R G B.
        if(src.empty()){
            return srcBitmap;
        }

        Mat dst = new Mat();
        Bitmap dstBitmap = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), Bitmap.Config.ARGB_8888);

        int xMin = 0;
        int yMin = 0;
        int xMax = src.cols();
        int yMax = src.rows();

        List<Point> listSrcs = Arrays.asList(
                new Point(xMin,yMin), // 左上
                new Point(xMin,yMax), // 左下
                new Point(xMax,yMax), // 右下
                new Point(xMax,yMin) // 右上
        );

        Mat srcPoints = Converters.vector_Point_to_Mat(listSrcs, CvType.CV_32F);
        for (int i = 0; i < pointsMargin.size(); i++) {
            PointMargin margin = pointsMargin.get(i);
            Point point = listSrcs.get(i);
            point.x += margin.getxMargin();
            point.y += margin.getyMargin();
        }

        List<Point> listDsts = Arrays.asList(
                listSrcs.get(LT_LOCATION),
                listSrcs.get(LB_LOCATION),
                listSrcs.get(RB_LOCATION),
                listSrcs.get(RT_LOCATION));

        Mat dstPoints = Converters.vector_Point_to_Mat(listDsts, CvType.CV_32F);
        Mat perspectiveMat = Imgproc.getPerspectiveTransform(srcPoints, dstPoints);
        Imgproc.warpPerspective(src, dst, perspectiveMat, src.size(), Imgproc.INTER_LINEAR);

        Utils.matToBitmap(dst, dstBitmap);
        return dstBitmap;
    }
}
