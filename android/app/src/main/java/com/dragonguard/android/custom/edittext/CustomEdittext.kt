package com.dragonguard.android.custom.edittext

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.dragonguard.android.R

class CustomEdittext : AppCompatEditText, TextWatcher, View.OnTouchListener, View.OnFocusChangeListener{

    private lateinit var clearDrawable: Drawable
    private lateinit var onfocuschangeListener: OnFocusChangeListener
    private lateinit var onTouchListener: OnTouchListener
    private var count = 0

    constructor(context: Context?):super(context!!){
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?): super(context!!, attrs){
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int): super(context!!, attrs, defStyleAttr){
        init()
    }

    override fun setOnFocusChangeListener(l: OnFocusChangeListener) {
        onFocusChangeListener = l
        count = 1
    }

    override fun setOnTouchListener(l: OnTouchListener) {
        onTouchListener = l
    }



    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun afterTextChanged(p0: Editable?) {

    }

    private fun setClearIconVisible(visible: Boolean) {
        clearDrawable.setVisible(visible, false)
        setCompoundDrawables(null, null, if(visible) clearDrawable else  null, null)
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        try {
            val x = event.x.toInt()
            if (clearDrawable.isVisible && x > width - paddingRight - clearDrawable.intrinsicWidth) {
                if (event.action == MotionEvent.ACTION_UP) {
                    error = null
                    text = null
                }
                return true
            }
            return onTouchListener.onTouch(v, event)
        }catch (e: Exception) {
            return false
        }
    }

    override fun onTextChanged(
        text: CharSequence,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        if(isFocused) setClearIconVisible(text.isNotEmpty())
    }

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        if(hasFocus && text != null) setClearIconVisible(text!!.isNotEmpty())
        else setClearIconVisible(false)

        if(count == 1) onfocuschangeListener.onFocusChange(view, hasFocus)
    }

    private fun init(){
        clearDrawable = DrawableCompat.wrap(
            (ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_clear_24,null) as Drawable)
        )
        DrawableCompat.setTintList(clearDrawable,hintTextColors)
        clearDrawable.setBounds(0,0,clearDrawable.intrinsicWidth,clearDrawable.intrinsicHeight)

        clearDrawable.colorFilter = PorterDuffColorFilter(getColor(context,R.color.black), PorterDuff.Mode.SRC_IN)
        setClearIconVisible(false)
        super.setOnTouchListener(this)
        super.setOnFocusChangeListener(this)
        addTextChangedListener(this)
    }

}