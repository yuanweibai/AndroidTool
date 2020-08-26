package rango.kotlin.views.custom

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class TestView(context: Context, attributeSet: AttributeSet?, defStyle: Int) : View(context, attributeSet, defStyle) {

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context) : this(context, null, 0)

    val path = Path()
    val paint = Paint()

    val paint1 = Paint()
    val paint2 = Paint()
    val paint3 = Paint()
    val paint4 = Paint()

    val rectF = RectF()

    init {
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


    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawColor(Color.RED)

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

        rectF.left = 150f
        rectF.top = 550f
        rectF.right = 450f
        rectF.bottom = 850f



        canvas?.drawArc(rectF.left + 10f, rectF.top + 10f, rectF.right + 10f, rectF.bottom + 10f, 0f, 90f, true, paint)

        canvas?.drawArc(rectF.left, rectF.top + 10f, rectF.right, rectF.bottom + 10f, 90f, 120f, true, paint1)

        canvas?.drawArc(rectF, 210f, 30f, true, paint2)

        canvas?.drawArc(rectF, 240f, 110f, true, paint3)

        canvas?.drawArc(rectF, 350f, 10f, true, paint4)


    }
}