package com.rubabe.shopapp.custom

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.EditText
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import com.rubabe.shopapp.R

class CustomEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.appcompat.R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr) {

    private var drawableStart: Drawable? = null
    private var drawableEnd: Drawable? = null
    private var drawableTop: Drawable? = null
    private var drawableBottom: Drawable? = null

    private var actionX: Int = 0
    private var actionY: Int = 0

    private var clickListener: DrawableClickListener? = null

    private var passwordVisible: Boolean = false

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.CustomEditText)
        drawableStart = a.getDrawable(R.styleable.CustomEditText_drawableStartCompat)
        drawableEnd = a.getDrawable(R.styleable.CustomEditText_drawableEndCompat)
        drawableTop = a.getDrawable(R.styleable.CustomEditText_drawableTopCompat)
        drawableBottom = a.getDrawable(R.styleable.CustomEditText_drawableBottomCompat)
        val customTextAppearance =
            a.getResourceId(R.styleable.CustomEditText_customTextAppearance, 0)
        drawableStart = ContextCompat.getDrawable(context, R.drawable.password_custom_privacy)
        drawableEnd = ContextCompat.getDrawable(context, R.drawable.password_visibility_off_icon)
        if (customTextAppearance != 0) {
            TextViewCompat.setTextAppearance(this, customTextAppearance)
        }
        val typedArray = context.obtainStyledAttributes(attrs, intArrayOf(R.color.black))
        val colorHint = typedArray.getResourceId(0, R.color.black)
        val colorText = typedArray.getResourceId(0, R.color.black)
        typedArray.recycle()


        setHintTextColor(ContextCompat.getColor(context, colorHint))
        setTextColor(ContextCompat.getColor(context, colorText))
        a.recycle()
        updateCompoundDrawables()

        val editText = findViewById<EditText>(R.id.password_Sign_In)


        editText.transformationMethod = AsteriskPasswordTransformationMethod()

        // Set click listener for the drawableEnd
        setDrawableClickListener(object : DrawableClickListener {
            override fun onClick(position: DrawablePosition) {
                if (position == DrawablePosition.RIGHT) {
                    togglePasswordVisibility()
                }
            }
        })
    }

    private fun togglePasswordVisibility() {
        passwordVisible = !passwordVisible

        if (passwordVisible) {
            drawableEnd = ContextCompat.getDrawable(context, R.drawable.password_visibility_icon)
            transformationMethod = null
        } else {
            drawableEnd =
                ContextCompat.getDrawable(context, R.drawable.password_visibility_off_icon)
            transformationMethod = AsteriskPasswordTransformationMethod()
        }

        updateCompoundDrawables()
    }

    override fun setCompoundDrawablesWithIntrinsicBounds(
        left: Drawable?, top: Drawable?, right: Drawable?, bottom: Drawable?
    ) {
        super.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom)
        drawableStart = left
        drawableTop = top
        drawableEnd = right
        drawableBottom = bottom
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        var bounds: Rect
        if (event.action == MotionEvent.ACTION_DOWN) {
            actionX = event.x.toInt()
            actionY = event.y.toInt()

            if (drawableBottom != null && drawableBottom!!.bounds.contains(actionX, actionY)) {
                clickListener?.onClick(DrawablePosition.BOTTOM)
                return super.onTouchEvent(event)
            }

            if (drawableTop != null && drawableTop!!.bounds.contains(actionX, actionY)) {
                clickListener?.onClick(DrawablePosition.TOP)
                return super.onTouchEvent(event)
            }

            if (drawableStart != null) {
                bounds = drawableStart!!.bounds
                val extraTapArea = (13 * resources.displayMetrics.density + 0.5).toInt()
                var x = actionX
                var y = actionY

                if (!bounds.contains(actionX, actionY)) {
                    x = actionX - extraTapArea
                    y = actionY - extraTapArea

                    if (x <= 0) x = actionX
                    if (y <= 0) y = actionY

                    if (x < y) y = x
                }

                if (bounds.contains(x, y) && clickListener != null) {
                    clickListener!!.onClick(DrawablePosition.LEFT)
                    event.action = MotionEvent.ACTION_CANCEL
                    return false
                }
            }

            if (drawableEnd != null) {
                bounds = drawableEnd!!.bounds
                val extraTapArea = (13 * resources.displayMetrics.density + 0.5).toInt()
                val x = actionX + extraTapArea
                var y = actionY - extraTapArea

                val rightBoundsX = width - x

                if (rightBoundsX <= 0) {
                    y += extraTapArea
                }

                if (y <= 0) y = actionY

                if (bounds.contains(rightBoundsX, y) && clickListener != null) {
                    clickListener!!.onClick(DrawablePosition.RIGHT)
                    event.action = MotionEvent.ACTION_CANCEL
                    return false
                }
            }
        }

        return super.onTouchEvent(event)
    }

    private fun updateCompoundDrawables() {
        setCompoundDrawablesWithIntrinsicBounds(
            drawableStart,
            drawableTop,
            drawableEnd,
            drawableBottom
        )
    }

    private fun setDrawableClickListener(listener: DrawableClickListener) {
        clickListener = listener
    }

    enum class DrawablePosition {
        LEFT, TOP, RIGHT, BOTTOM
    }

    interface DrawableClickListener {
        fun onClick(position: DrawablePosition)
    }

}