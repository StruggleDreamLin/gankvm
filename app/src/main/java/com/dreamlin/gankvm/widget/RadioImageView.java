package com.dreamlin.gankvm.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * Created by dreamlin on 2017/12/20.
 */

public class RadioImageView extends AppCompatImageView {

    private int originalWidth;
    private int originalHeight;

    private int measureWidth;
    private int measureHeight;

    public RadioImageView(Context context) {
        super(context);
    }

    public RadioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RadioImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOriginalSize(int originalWidth, int originalHeight){
        this.originalWidth = originalWidth;
        this.originalHeight = originalHeight;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (originalHeight >0 && originalWidth > 0) {
            float radio = (originalWidth) / (float) originalHeight;

            measureWidth = View.MeasureSpec.getSize(widthMeasureSpec);
            measureHeight = View.MeasureSpec.getSize(heightMeasureSpec);

            if (measureWidth > 0){
                measureHeight = (int) ((float) measureWidth / radio);
            }
            setMeasuredDimension(measureWidth, measureHeight);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

    }
}
