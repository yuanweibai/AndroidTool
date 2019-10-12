package rango.tool.androidtool.wallpaper;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import rango.tool.androidtool.ToolApplication;
import rango.tool.common.utils.ScreenUtils;

public class WallpaperUtils {

    public static final int WALLPAPER_TYPE_SCROLLABLE_STANDARD = 1;
    public static final int WALLPAPER_TYPE_STATIC_STANDARD = 2;

    public static final int DEFAULT_DEVICE_SCREEN_HEIGHT = 1920;
    public static final int DEFAULT_DEVICE_SCREEN_WIDTH = 1080;

    private static final String TAG = WallpaperUtils.class.getSimpleName();

    private static final int COLOR_SAMPLE_COUNT = 6;

    public static Matrix centerCrop(int dWidth, int dHeight, ImageView imageView) {

        Matrix newMatrix = new Matrix();

        int vWidth = ScreenUtils.getScreenWidthPx();
        int vHeight = ScreenUtils.getScreenHeightPx();
        float scale;
        float dx = 0f, dy = 0f;

        if (dWidth * vHeight > vWidth * dHeight) {
            scale = (float) vHeight / (float) dHeight;
            dx = (vWidth - dWidth * scale) * 0.5f;
        } else {
            scale = (float) vWidth / (float) dWidth;
            dy = (vHeight - dHeight * scale) * 0.5f;
        }


        newMatrix.setScale(scale, scale);
        newMatrix.postTranslate(Math.round(dx), Math.round(dy));
        return newMatrix;
    }

    public static Matrix centerInside(int dWidth, int dHeight, int top, int bottom) {
        RectF bitmapRect = new RectF();
        int vWidth = ScreenUtils.getScreenWidthPx();
        int vHeight = ScreenUtils.getScreenHeightPx();

        bitmapRect.set(0, 0, dWidth, dHeight);
        RectF imgRect = new RectF(0, top, vWidth, bottom);

        Matrix matrix = new Matrix();
        matrix.setRectToRect(bitmapRect, imgRect, Matrix.ScaleToFit.CENTER);
        return matrix;
    }

    public static Bitmap centerInside(Bitmap src) {
        Rect bitmapRect = new Rect();
        Point point = getWindowSize(ToolApplication.getContext());
        int vWidth = point.x;
        int vHeight = point.y;

        bitmapRect.set(0, 0, src.getWidth(), src.getHeight());
        Rect windowRect = new Rect(0, 0, vWidth, vHeight);
        RectF windowRectF = new RectF(0, 0, vWidth, vHeight);
        RectF bitmapRectF = new RectF(bitmapRect);

        Matrix matrix = new Matrix();
        matrix.setRectToRect(windowRectF, bitmapRectF, Matrix.ScaleToFit.CENTER);
        matrix.mapRect(windowRectF);

        Bitmap bg = Bitmap.createBitmap(point.x, point.y, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bg);
        Paint paint = new Paint();

        bitmapRect.set((int) windowRectF.left, (int) windowRectF.top, (int) windowRectF.right, (int) windowRectF.bottom);

        canvas.drawBitmap(src, bitmapRect, windowRect, paint);
        return bg;
    }

    static Bitmap translateToFixedWallpaper(Bitmap src, Context context) {
        Point point = getWindowSize(context);
        if (src.getWidth() == point.x && src.getHeight() == point.y) {
            return src;
        } else {
            src = getSameRatioBitmap(src, point.x, point.y);
        }

        Bitmap bg = Bitmap.createBitmap(point.x, point.y, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bg);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        Rect dst = new Rect(0, 0, point.x, point.y);
        canvas.drawBitmap(src, null, dst, paint);
        return bg;
    }

    static Bitmap getSameRatioBitmap(Bitmap src, int screenWidth, int screenHeight) {
        Bitmap result;
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();

        float srcRatio = srcHeight / (float) srcWidth;
        float screenRatio = screenHeight / (float) screenWidth;
        if (srcRatio == screenRatio) {
            if (srcWidth > screenWidth) {
                float scale = screenWidth / (float) srcWidth;
                result = getScaledBitmap(src, scale);
            } else {
                result = src;
            }
        } else if (srcRatio < screenRatio) {
            float scale = screenWidth / (float) srcWidth;
            src = getScaledBitmap(src, scale);
            int h = src.getHeight();
            int w = (int) (h * (screenWidth / (float) screenHeight));
            int left = (src.getWidth() - w) / 2;
            Rect rect = new Rect(left, 0, left + w, h);
            result = getAppointedRectBitmap(src, rect, w, h);
        } else {
            float scale = screenHeight / (float) srcHeight;
            src = getScaledBitmap(src, scale);
            int w = src.getWidth();
            int h = (int) (w * (screenHeight / (float) screenWidth));
            int top = (src.getHeight() - h) / 2;
            Rect rect = new Rect(0, top, w, top + h);
            result = getAppointedRectBitmap(src, rect, w, h);
        }
        return result;
    }

    static Bitmap getScaledBitmap(Bitmap bitmap, float scale) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
    }

    static Bitmap getAppointedRectBitmap(Bitmap bitmap, Rect src, int width, int height) {
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        Rect dst = new Rect(0, 0, width, height);
        canvas.drawBitmap(bitmap, src, dst, paint);
        return result;
    }

    static Bitmap translateToScrollWallpaper(Bitmap src, Context context) {
        Point point = getWindowSize(context);
        if (src.getWidth() == 2 * point.x && src.getHeight() == point.y) {
            return src;
        }
        // avoid crash
        if (point.x <= 0 || point.y <= 0) {
            point.x = DEFAULT_DEVICE_SCREEN_WIDTH;
            point.y = DEFAULT_DEVICE_SCREEN_HEIGHT;
        }
        Bitmap bg = Bitmap.createBitmap(2 * point.x, point.y, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bg);
        PaintFlagsDrawFilter pfd = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        canvas.setDrawFilter(pfd);
        Rect dst = new Rect(0, 0, 2 * point.x, point.y);
        canvas.drawBitmap(src, null, dst, paint);
        return bg;
    }

    public static boolean canScroll(Context context, Bitmap wallpaper) {
        if (wallpaper == null || wallpaper.isRecycled()) {
            return false;
        }
        int width = wallpaper.getWidth();
        int height = wallpaper.getHeight();
        float wallpaperRatio = (float) width / (float) height;
        Point size = WallpaperUtils.getWindowSize(context);
        float windowRatio = (float) size.x / (float) size.y;
        float detla = wallpaperRatio / windowRatio;

        /**
         * 1.125 = 1440/1280 Our wallpaper's size is 1440*1280
         */
        if (Math.abs(detla - 2) <= 0.05 || Math.abs(wallpaperRatio - 1.125) <= 0.05) {
            return true;
        } else {
            return false;
        }
    }

    // TODO: 13/12/2016 remove this method

    /**
     * use Activity context
     */
    private static Point sCachePoint;

    public static Point getWindowSize(Context context) {
        Point point = new Point();
        if (sCachePoint != null) {
            point.set(sCachePoint.x, sCachePoint.y);
            return point;
        }
        int screenTotalHeight = 0;
        int screenTotalWidth = 0;
        if (context instanceof Activity) {
            View rootView = ((Activity) context).getWindow().getDecorView();
            screenTotalHeight = rootView.getHeight();
            screenTotalWidth = rootView.getWidth();
        }
        if (screenTotalWidth != 0 && screenTotalHeight != 0) {
            point.x = screenTotalWidth;
            point.y = screenTotalHeight;
            sCachePoint = new Point();
            sCachePoint.x = screenTotalWidth;
            sCachePoint.y = screenTotalHeight;
        } else {
            point.x = ScreenUtils.getScreenWidthPx();
            point.y = ScreenUtils.getScreenHeightPx();
        }
        return point;
    }

    public static final float WALLPAPER_SCREENS_SPAN = 2f;

    public static void suggestWallpaperDimension(Resources res,
                                                 WindowManager windowManager,
                                                 final WallpaperManagerProxy wallpaperManager, boolean fallBackToDefaults) {
        final Point defaultWallpaperSize = WallpaperUtils.getDefaultWallpaperSize(res, windowManager);

        int savedWidth;
        int savedHeight;

        if (!fallBackToDefaults) {
            return;
        } else {
            savedWidth = defaultWallpaperSize.x;
            savedHeight = defaultWallpaperSize.y;
        }

        if (savedWidth != wallpaperManager.getDesiredMinimumWidth() ||
                savedHeight != wallpaperManager.getDesiredMinimumHeight()) {
            wallpaperManager.suggestDesiredDimensions(savedWidth, savedHeight);
        }
    }

    /**
     * As a ratio of screen height, the total distance we want the parallax effect to span
     * horizontally
     */
    public static float wallpaperTravelToScreenWidthRatio(int width, int height) {
        float aspectRatio = width / (float) height;

        // At an aspect ratio of 16/10, the wallpaper parallax effect should span 1.5 * screen width
        // At an aspect ratio of 10/16, the wallpaper parallax effect should span 1.2 * screen width
        // We will use these two data points to extrapolate how much the wallpaper parallax effect
        // to span (ie travel) at any aspect ratio:

        final float ASPECT_RATIO_LANDSCAPE = 16 / 10f;
        final float ASPECT_RATIO_PORTRAIT = 10 / 16f;
        final float WALLPAPER_WIDTH_TO_SCREEN_RATIO_LANDSCAPE = 1.5f;
        final float WALLPAPER_WIDTH_TO_SCREEN_RATIO_PORTRAIT = 1.2f;

        // To find out the desired width at different aspect ratios, we use the following two
        // formulas, where the coefficient on x is the aspect ratio (width/height):
        //   (16/10)x + y = 1.5
        //   (10/16)x + y = 1.2
        // We solve for x and y and end up with a final formula:
        final float x =
                (WALLPAPER_WIDTH_TO_SCREEN_RATIO_LANDSCAPE - WALLPAPER_WIDTH_TO_SCREEN_RATIO_PORTRAIT) /
                        (ASPECT_RATIO_LANDSCAPE - ASPECT_RATIO_PORTRAIT);
        final float y = WALLPAPER_WIDTH_TO_SCREEN_RATIO_PORTRAIT - x * ASPECT_RATIO_PORTRAIT;
        return x * aspectRatio + y;
    }

    private static Point sDefaultWallpaperSize;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static Point getDefaultWallpaperSize(Resources res, WindowManager windowManager) {
        if (sDefaultWallpaperSize == null) {
            Point minDims = new Point();
            Point maxDims = new Point();
            windowManager.getDefaultDisplay().getCurrentSizeRange(minDims, maxDims);

            int maxDim = Math.max(maxDims.x, maxDims.y);
            int minDim = Math.max(minDims.x, minDims.y);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                Point realSize = new Point();
                windowManager.getDefaultDisplay().getRealSize(realSize);
                maxDim = Math.max(realSize.x, realSize.y);
                minDim = Math.min(realSize.x, realSize.y);
            }

            // We need to ensure that there is enough extra space in the wallpaper
            // for the intended parallax effects
            final int defaultWidth, defaultHeight;
            if (res.getConfiguration().smallestScreenWidthDp >= 720) {
                defaultWidth = (int) (maxDim * wallpaperTravelToScreenWidthRatio(maxDim, minDim));
                defaultHeight = maxDim;
            } else {
                defaultWidth = Math.max((int) (minDim * WALLPAPER_SCREENS_SPAN), maxDim);
                defaultHeight = maxDim;
            }
            sDefaultWallpaperSize = new Point(defaultWidth, defaultHeight);
        }
        return sDefaultWallpaperSize;
    }
}
