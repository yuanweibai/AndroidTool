package rango.tool.androidtool.wallpaper;

import android.app.WallpaperInfo;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.IBinder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import rango.tool.androidtool.BuildConfig;
import rango.tool.androidtool.ToolApplication;

/**
 * A proxy that delegates calls to {@link WallpaperManager} after applying appropriate size transformations
 * when setting wallpaper.
 */
public class WallpaperManagerProxy {

    private static volatile WallpaperManagerProxy sInstance;

    private WallpaperManager mWm;

    private Boolean mScrollable;

    public static WallpaperManagerProxy getInstance() {
        if (sInstance == null) {
            synchronized (WallpaperManagerProxy.class) {
                if (sInstance == null) {
                    sInstance = new WallpaperManagerProxy();
                }
            }
        }
        return sInstance;
    }

    private WallpaperManagerProxy() {
        mWm = WallpaperManager.getInstance(ToolApplication.getContext());
    }

    public void setSystemBitmap(Context context, Bitmap wallpaper) throws IOException {
        setSystemBitmap(context, wallpaper, WallpaperUtils.canScroll(context, wallpaper));
    }

    public void setSystemBitmap(Context context, Bitmap wallpaper, boolean scrollable) throws IOException {
        if (wallpaper == null || wallpaper.getHeight() == 0 || wallpaper.getWidth() == 0) {
            if (BuildConfig.DEBUG) {
                throw new NullPointerException("Wallpaper bitmap can't be null");
            }
            return;
        }
        mScrollable = scrollable;
        Point point = WallpaperUtils.getWindowSize(context);

        try {
            if (scrollable) {
                wallpaper = WallpaperUtils.translateToScrollWallpaper(wallpaper, context);
            } else {
                wallpaper = WallpaperUtils.translateToFixedWallpaper(wallpaper, context);
            }
        } catch (OutOfMemoryError error) {
        }

        if (wallpaper != null) {
            try {
                try {
                    ByteArrayInputStream stream = bitmapToStream(wallpaper);
                    mWm.setStream(stream);
                    if (scrollable) {
                        mWm.suggestDesiredDimensions(point.x * 2, point.y);
                    } else {
                        mWm.suggestDesiredDimensions(point.x, point.y);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (OutOfMemoryError error) {
                try {
                    mWm.setBitmap(wallpaper);
                    if (scrollable) {
                        mWm.suggestDesiredDimensions(point.x * 2, point.y);
                    } else {
                        mWm.suggestDesiredDimensions(point.x, point.y);
                    }
                } catch (OutOfMemoryError | Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static ByteArrayInputStream bitmapToStream(Bitmap bitmap) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream(2048);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return new ByteArrayInputStream(stream.toByteArray());
    }

    public BitmapDrawable getSystemDrawable() {
        BitmapDrawable bitmapDrawable = null;
        try {
            bitmapDrawable = (BitmapDrawable) mWm.getDrawable();
        } catch (Exception e) {
            // Multiple crashes are reported to occur due to internal exceptions:
            // (1) IllegalStateException on Huawei Android 4.4.2 (API 19) devices
            // (2) IOException on Meizu M2 and alps devices
            e.printStackTrace();
        }
        return bitmapDrawable;
    }

    public Bitmap getSystemBitmap() {
        Bitmap result = null;
        BitmapDrawable drawable = getSystemDrawable();
        if (drawable != null) {
            result = drawable.getBitmap();
        }
        return result;
    }

    public void restore(IBinder windowToken) {
        if (windowToken != null) {
            try {
                mWm.setWallpaperOffsets(windowToken, 0, 0.5f);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public WallpaperInfo getWallpaperInfo() {
        return mWm.getWallpaperInfo();
    }

    public void sendWallpaperCommand(IBinder windowToken, String action,
                                     int x, int y, int z, Bundle extras) {
        try {
            mWm.sendWallpaperCommand(windowToken, action, x, y, z, extras);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setWallpaperOffsets(IBinder windowToken, float xOffset, float yOffset) {
        try {
            mWm.setWallpaperOffsets(windowToken, xOffset, yOffset);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getDesiredMinimumWidth() {
        return mWm.getDesiredMinimumWidth();
    }

    public int getDesiredMinimumHeight() {
        return mWm.getDesiredMinimumHeight();
    }

    public void suggestDesiredDimensions(int minimumWidth, int minimumHeight) {
        try {
            mWm.suggestDesiredDimensions(minimumWidth, minimumHeight);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
