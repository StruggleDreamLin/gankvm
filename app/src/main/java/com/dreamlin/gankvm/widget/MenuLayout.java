package com.dreamlin.gankvm.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class MenuLayout extends LinearLayout {

    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    public MenuLayout(Context context) {
        super(context);
    }

    public MenuLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MenuLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        paint.setColor(Color.BLACK);
        paint.setShadowLayer(20, -20, 20, 0x888A8A8A);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        canvas.drawRect(20,20, getWidth() - 20, getHeight() - 20, paint);
    }
}
