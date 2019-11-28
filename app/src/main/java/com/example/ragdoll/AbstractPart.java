package com.example.ragdoll;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;

abstract public class AbstractPart {
    abstract boolean contains(float x, float y);
    abstract void draw(Canvas canvas, Paint paint);
    abstract void addTranslation(float x, float y);
    abstract void setAngle(PointF prev, PointF event);
}
