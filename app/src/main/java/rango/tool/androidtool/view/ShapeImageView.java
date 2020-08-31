package rango.tool.androidtool.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import androidx.appcompat.widget.AppCompatImageView;
import android.util.AttributeSet;

import rango.tool.androidtool.R;
import rango.tool.common.utils.ImageUtils;

/**
 * 支持的形状：矩形、圆形、椭圆
 * 功能：
 * 1、可以在矩形上加圆角，四个圆角可以单独设置；
 * 2、可以在图形上加圈，圈的大小、颜色都可以自定义；
 * <p>
 * 优点：抗锯齿
 */
public class ShapeImageView extends AppCompatImageView {

    private static final int DEFAULT_SHAPE = 0;
    private final Matrix mShaderMatrix = new Matrix();
    protected Shape mShape = Shape.RECTANGLE; // 形状，默认为直接矩形

    private float mBorderSize = 0; // 边框大小,默认为０，即无边框
    private int mBorderColor = Color.WHITE; // 边框颜色，默认为白色
    private float mRoundRadius = 0; // 矩形的圆角半径,默认为０，即直角矩形
    private float mRoundRadiusLeftTop, mRoundRadiusLeftBottom, mRoundRadiusRightTop, mRoundRadiusRightBottom;
    private Paint mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private RectF mDrawRect = new RectF(); // 绘制区域
    private Paint mBitmapPaint = new Paint();
    private BitmapShader mBitmapShader;
    private Bitmap mBitmap;
    private Path mPath = new Path();

    public ShapeImageView(Context context) {
        this(context, null, 0);
    }

    public ShapeImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShapeImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle); // 虽然此处会调用setImageDrawable，但此时成员变量还未被正确初始化
        init(attrs);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setStrokeWidth(mBorderSize);
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setAntiAlias(true);

        mBitmapPaint.setAntiAlias(true);
        super.setScaleType(ScaleType.CENTER_CROP); // 固定为CENTER_CROP，其他不生效
    }

    private void init(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.ShapeImageView);
        int value = a.getInt(R.styleable.ShapeImageView_shape, DEFAULT_SHAPE);
        mShape = getShapeFromAttrs(value);
        mRoundRadius = a.getDimension(R.styleable.ShapeImageView_radius, mRoundRadius);
        mBorderSize = a.getDimension(R.styleable.ShapeImageView_border_size, mBorderSize);
        mBorderColor = a.getColor(R.styleable.ShapeImageView_border_color, mBorderColor);

        mRoundRadiusLeftBottom = a.getDimension(R.styleable.ShapeImageView_radius_leftBottom, mRoundRadius);
        mRoundRadiusLeftTop = a.getDimension(R.styleable.ShapeImageView_radius_leftTop, mRoundRadius);
        mRoundRadiusRightBottom = a.getDimension(R.styleable.ShapeImageView_radius_rightBottom, mRoundRadius);
        mRoundRadiusRightTop = a.getDimension(R.styleable.ShapeImageView_radius_rightTop, mRoundRadius);

        a.recycle();
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        mBitmap = ImageUtils.getBitmapFromDrawable(getDrawable());
        setupBitmapShader();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        mBitmap = ImageUtils.getBitmapFromDrawable(drawable);
        setupBitmapShader();
    }

    @Override
    public void setScaleType(ScaleType scaleType) {
        if (scaleType != ScaleType.CENTER_CROP) {
            throw new IllegalArgumentException(String.format("ScaleType %s not supported.", scaleType));
        }
    }

    private Shape getShapeFromAttrs(int value) {
        switch (value) {
            case 1:
                return Shape.RECTANGLE;
            case 2:
                return Shape.CIRCLE;
            case 3:
                return Shape.OVAL;
            default:
                return Shape.RECTANGLE;
        }
    }

    /**
     * 对于普通的view,在执行到onDraw()时，背景图已绘制完成
     * <p>
     * 对于ViewGroup,当它没有背景时直接调用的是dispatchDraw()方法, 而绕过了draw()方法，
     * 当它有背景的时候就调用draw()方法，而draw()方法里包含了dispatchDraw()方法的调用，
     */
    @Override
    public void onDraw(Canvas canvas) {

        if (mBitmap != null) {
            if (mShape == Shape.CIRCLE) {
                canvas.drawCircle(mDrawRect.right / 2f, mDrawRect.bottom / 2f,
                        Math.min(mDrawRect.right, mDrawRect.bottom) / 2f, mBitmapPaint);
            } else if (mShape == Shape.OVAL) {
                canvas.drawOval(mDrawRect, mBitmapPaint);
            } else {
//                canvas.drawRoundRect(mDrawRect, mRoundRadius, mRoundRadius, mBitmapPaint);
                mPath.reset();
                mPath.addRoundRect(mDrawRect, new float[]{
                        mRoundRadiusLeftTop, mRoundRadiusLeftTop,
                        mRoundRadiusRightTop, mRoundRadiusRightTop,
                        mRoundRadiusRightBottom, mRoundRadiusRightBottom,
                        mRoundRadiusLeftBottom, mRoundRadiusLeftBottom,
                }, Path.Direction.CW);
                canvas.drawPath(mPath, mBitmapPaint);
            }
        }

        if (mBorderSize > 0) { // 绘制边框
            if (mShape == Shape.CIRCLE) {
                canvas.drawCircle(mDrawRect.right / 2f, mDrawRect.bottom / 2f,
                        Math.min(mDrawRect.right, mDrawRect.bottom) / 2f - mBorderSize / 2f, mBorderPaint);
            } else if (mShape == Shape.OVAL) {
                canvas.drawOval(mDrawRect, mBorderPaint);
            } else {
//                canvas.drawRoundRect(mDrawRect, mRoundRadius, mRoundRadius, mBorderPaint);
                mPath.reset();
                mPath.addRoundRect(mDrawRect, new float[]{
                        mRoundRadiusLeftTop, mRoundRadiusLeftTop,
                        mRoundRadiusRightTop, mRoundRadiusRightTop,
                        mRoundRadiusRightBottom, mRoundRadiusRightBottom,
                        mRoundRadiusLeftBottom, mRoundRadiusLeftBottom,
                }, Path.Direction.CW);
                canvas.drawPath(mPath, mBorderPaint);
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initRect();
        setupBitmapShader();
    }

    // 不能在onLayout()调用invalidate()，否则导致绘制异常。（setupBitmapShader()中调用了invalidate()）
    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);
//        initRect();
//        setupBitmapShader();
    }

    private void setupBitmapShader() {
        // super(context, attrs, defStyle)调用setImageDrawable时,成员变量还未被正确初始化
        if (mBitmapPaint == null) {
            return;
        }
        if (mBitmap == null) {
            return;
        }
        mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mBitmapPaint.setShader(mBitmapShader);

        // 固定为CENTER_CROP,使图片在ｖｉｅｗ中居中并裁剪
        mShaderMatrix.set(null);
        // 缩放到高或宽　与view的高或宽　匹配
        float scale = Math.max(getWidth() * 1f / mBitmap.getWidth(), getHeight() * 1f / mBitmap.getHeight());
        // 由于BitmapShader默认是从画布的左上角开始绘制，所以把其平移到画布中间，即居中
        float dx = (getWidth() - mBitmap.getWidth() * scale) / 2f;
        float dy = (getHeight() - mBitmap.getHeight() * scale) / 2f;
        mShaderMatrix.setScale(scale, scale);
        mShaderMatrix.postTranslate(dx, dy);
        mBitmapShader.setLocalMatrix(mShaderMatrix);
        invalidate();
    }

    private void initRect() {
        float space = mBorderSize / 2f;
        mDrawRect.top = space;
        mDrawRect.left = space;
        mDrawRect.right = getWidth() - space;
        mDrawRect.bottom = getHeight() - space;
    }

    public float getBorderSize() {
        return mBorderSize;
    }

    public void setBorderSize(int mBorderSize) {
        this.mBorderSize = mBorderSize;
        mBorderPaint.setStrokeWidth(mBorderSize);
        initRect();
        invalidate();
    }

    public int getBorderColor() {
        return mBorderColor;
    }

    public void setBorderColor(int mBorderColor) {
        this.mBorderColor = mBorderColor;
        mBorderPaint.setColor(mBorderColor);
        invalidate();
    }

    public float getRoundRadius() {
        return mRoundRadius;
    }

    public void setRoundRadius(float mRoundRadius) {
        this.mRoundRadius = mRoundRadius;
        invalidate();
    }

    public void setRoundRadiis(float roundRadiusLeftBottom, float roundRadiusLeftTop, float roundRadiusRightBottom, float roundRadiusRightTop) {
        mRoundRadiusLeftBottom = roundRadiusLeftBottom;
        mRoundRadiusLeftTop = roundRadiusLeftTop;
        mRoundRadiusRightBottom = roundRadiusRightBottom;
        mRoundRadiusRightTop = roundRadiusRightTop;
        invalidate();
    }

    public float[] getRoundRadiis() {
        return new float[]{mRoundRadiusLeftBottom, mRoundRadiusLeftTop, mRoundRadiusRightBottom, mRoundRadiusRightTop};
    }

    public enum Shape {
        RECTANGLE,
        CIRCLE,
        OVAL
    }
}
