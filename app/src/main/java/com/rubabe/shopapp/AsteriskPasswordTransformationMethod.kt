package com.rubabe.shopapp


import android.graphics.Rect
import android.text.method.TransformationMethod
import android.view.View

class AsteriskPasswordTransformationMethod : TransformationMethod {
    override fun getTransformation(source: CharSequence, view: View): CharSequence {
        return PasswordCharSequence(source)
    }

    override fun onFocusChanged(
        view: View,
        sourceText: CharSequence,
        focused: Boolean,
        direction: Int,
        previouslyFocusedRect: Rect?
    ) {
        // No implementation needed
    }

    private inner class PasswordCharSequence(private val source: CharSequence) : CharSequence {
        override val length: Int
            get() = source.length

        override fun get(index: Int): Char {
            return '*'
        }

        override fun subSequence(startIndex: Int, endIndex: Int): CharSequence {
            return source.subSequence(
                startIndex,
                endIndex
            ) // Return the original text for copying, cutting, etc.
        }
    }
}