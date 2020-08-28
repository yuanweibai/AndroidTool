package rango.kotlin.views.custom

import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import rango.tool.androidtool.R

class TestView(context: Context, attributeSet: AttributeSet?, defStyle: Int) : View(context, attributeSet, defStyle) {

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context) : this(context, null, 0)

    private var drawIndex = 1

    val path = Path()
    val paint = Paint()

    private val paint1 = Paint()
    private val paint2 = Paint()
    private val paint3 = Paint()
    private val paint4 = Paint()

    private val bitmapShader: BitmapShader
    private val bitmapPaint = Paint()
    private val bitmap: Bitmap
    private val otherBitmap: Bitmap

    private val textPaint = TextPaint()

    private val composeShader: ComposeShader

    val rectF = RectF()


    private val xfermodePaint = Paint()
    private val xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
    private val xfermodeRectF = RectF()
    val rect = Rect()

    private val linePaint = Paint()

    private val bPaint = Paint()

    init {

        drawIndex = 2

        paint.isAntiAlias = true
        paint.color = Color.BLACK
        paint.style = Paint.Style.FILL
        paint.strokeWidth = 3f

        paint1.isAntiAlias = true
        paint1.color = Color.BLUE
        paint1.style = Paint.Style.FILL
        paint1.strokeWidth = 0f

        paint2.isAntiAlias = true
        paint2.color = Color.WHITE
        paint2.style = Paint.Style.FILL
        paint2.strokeWidth = 0f

        paint3.isAntiAlias = true
        paint3.color = Color.YELLOW
        paint3.style = Paint.Style.FILL
        paint3.strokeWidth = 0f

        paint4.isAntiAlias = true
        paint4.color = Color.WHITE
        paint4.style = Paint.Style.FILL
        paint4.strokeWidth = 0f

        bitmap = BitmapFactory.decodeResource(resources, R.drawable.solon)

        // 从该 View 的左上角开始绘制
        bitmapShader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        val matrix = Matrix()
        val scaleX = 400f / bitmap.width
        val scaleY = 400f / bitmap.height
        val scale = if (scaleX > scaleY) {
            scaleX
        } else {
            scaleY
        }

        val dx: Float = (400 - bitmap.width * scale) / 2f + 100f
        val dy: Float = (400 - bitmap.height * scale) / 2f + 400f
        matrix.setScale(scale, scale)
        matrix.postTranslate(dx, dy)
        bitmapShader.setLocalMatrix(matrix)


        otherBitmap = BitmapFactory.decodeResource(resources, R.drawable.guide_profile_icon)
        val otherShader = BitmapShader(otherBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)

        val otherMatrix = Matrix()
        val otherScaleX = 400f / otherBitmap.width
        val otherScaleY = 400f / otherBitmap.height
        val otherScale = if (otherScaleX > otherScaleY) {
            otherScaleX
        } else {
            otherScaleY
        }

        val otherDx: Float = (400 - otherBitmap.width * otherScale) / 2f + 100f
        val otherDy: Float = (400 - otherBitmap.height * otherScale) / 2f + 400f
        otherMatrix.setScale(otherScale, otherScale)
        otherMatrix.postTranslate(otherDx, otherDy)
        otherShader.setLocalMatrix(otherMatrix)

        // 硬件加速不支持两个相同的 shader，因此必须关掉硬件加速，才能看到 ComposeShader 的效果
        composeShader = ComposeShader(bitmapShader, otherShader, PorterDuff.Mode.SRC_OVER)
        setLayerType(LAYER_TYPE_SOFTWARE, null)

        bitmapPaint.shader = composeShader

        linePaint.strokeWidth = 30f
        linePaint.color = Color.BLACK
        linePaint.style = Paint.Style.STROKE
        linePaint.strokeCap = Paint.Cap.ROUND
        linePaint.strokeJoin = Paint.Join.ROUND

        textPaint.textSize = 50f
        textPaint.color = Color.BLACK
        textPaint.setShadowLayer(10f, 0f, 0f, Color.RED)

        bPaint.maskFilter = BlurMaskFilter(100f, BlurMaskFilter.Blur.NORMAL)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (drawIndex == 1) {
            drawFirstIndex(canvas)
        } else if (drawIndex == 2) {
            drawSecondIndex(canvas)
        }
    }

    private fun drawFirstIndex(canvas: Canvas?) {

        canvas?.drawColor(Color.WHITE)

        canvas?.drawCircle(100f, 100f, 50f, paint)

        // Path#reset() 一定要调用，否则绘制不出来
        path.reset()
        path.fillType = Path.FillType.EVEN_ODD
        path.moveTo(100f, 200f)
        path.addCircle(100f, 250f, 50f, Path.Direction.CW)
        path.addCircle(160f, 250f, 50f, Path.Direction.CW)

        path.moveTo(100f, 350f)
        path.addCircle(100f, 400f, 50f, Path.Direction.CW)
        path.addCircle(100f, 400f, 20f, Path.Direction.CW)
        canvas?.drawPath(path, paint)

        rectF.left = 400f
        rectF.top = 100f
        rectF.right = 700f
        rectF.bottom = 400f



        canvas?.drawArc(rectF.left + 10f, rectF.top + 10f, rectF.right + 10f, rectF.bottom + 10f, 0f, 90f, true, paint)

        canvas?.drawArc(rectF.left, rectF.top + 10f, rectF.right, rectF.bottom + 10f, 90f, 120f, true, paint1)

        canvas?.drawArc(rectF, 210f, 30f, true, paint2)

        canvas?.drawArc(rectF, 240f, 110f, true, paint3)

        canvas?.drawArc(rectF, 350f, 10f, true, paint4)

        canvas?.drawCircle(300f, 600f, 200f, bitmapPaint)


        xfermodeRectF.left = 100f
        xfermodeRectF.top = 900f
        xfermodeRectF.right = 500f
        xfermodeRectF.bottom = 1300f


        rect.left = 50
        rect.top = 50
        rect.right = bitmap.width - 50
        rect.bottom = bitmap.height - 50

        val saved = canvas?.saveLayer(null, null, Canvas.ALL_SAVE_FLAG)
        canvas?.drawBitmap(bitmap, rect, xfermodeRectF, xfermodePaint)
        xfermodePaint.xfermode = xfermode
        canvas?.drawBitmap(otherBitmap, null, xfermodeRectF, xfermodePaint)
        xfermodePaint.xfermode = null
        canvas?.restoreToCount(saved!!)

        path.reset()
        path.moveTo(600f, 500f)
        path.lineTo(800f, 500f)
        path.lineTo(700f, 800f)
        canvas?.drawPath(path, linePaint)

        canvas?.drawText("Hello HenCoder", 100f, 1400f, textPaint)
    }

    private fun drawSecondIndex(canvas: Canvas?) {
        canvas?.drawColor(Color.WHITE)

        canvas?.drawBitmap(bitmap, 100f, 100f, bPaint)

    }
}