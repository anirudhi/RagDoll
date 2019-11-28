package com.example.ragdoll;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import androidx.core.view.MotionEventCompat;

public class DrawView extends View {

    Paint shapePaint;
    Body body;


    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        shapePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        shapePaint.setColor(0xff101010);
        shapePaint.setStyle(Paint.Style.STROKE);
        shapePaint.setStrokeWidth(3);
        body = new Body(getScreenWidth(), getScreenHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        body.draw(canvas, shapePaint);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = MotionEventCompat.getActionMasked(event);
        int index = MotionEventCompat.getActionIndex(event);

        if (event.getPointerCount() > 1) {
            float xMid = (MotionEventCompat.getX(event, index) + MotionEventCompat.getX(event, index + 1)) / 2;
            float yMid = (MotionEventCompat.getY(event, index) + MotionEventCompat.getY(event, index + 1)) / 2;
            body.setBodyPart(xMid, yMid);


        } else {

            switch (action) {
                case (MotionEvent.ACTION_DOWN):
                    body.setBodyPart(event.getX(), event.getY());
                    return true;
                case (MotionEvent.ACTION_MOVE):
                    body.doOperation(event.getX(), event.getY());
                    this.invalidate();
                    return true;
                case (MotionEvent.ACTION_UP):
                    body.releaseBodyPart();
                    this.invalidate();
                    return true;
            }
        }
        return true;
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }


}
