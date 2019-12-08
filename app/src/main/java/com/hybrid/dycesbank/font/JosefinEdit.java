package com.hybrid.dycesbank.font;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by kamran on 6/28/15.
 */
@SuppressLint("AppCompatCustomView")
public class JosefinEdit extends EditText {

    public JosefinEdit(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public JosefinEdit(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public JosefinEdit(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "JosefinSans-Regular.ttf");
            setTypeface(tf);
        }
    }

}