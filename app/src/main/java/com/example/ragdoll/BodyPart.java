package com.example.ragdoll;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;

import static java.lang.Double.NaN;


public class BodyPart extends AbstractPart {
    Matrix parentMatrix;
    Matrix rotationtMatrix;
    Matrix combinedMatrix;
    Matrix inverse;

    PointF pivot;
    float height, width, angle;
    RectF rect;
    BodyPart child;
    float constraint;
    float totalAngle;

    BodyPart(float height, float width, PointF offset, PointF pivot, Float constraint) {
        this.height = height;
        this.width = width;
        this.angle = 0;
        this.rect = new RectF(0, 0, width, height);
        this.pivot = pivot;
        this.constraint = constraint;
        this.totalAngle = 0;

        this.rotationtMatrix = new Matrix();
        this.rotationtMatrix.preTranslate(offset.x, offset.y);
        this.parentMatrix = new Matrix();
        this.combinedMatrix = new Matrix();
        this.inverse = new Matrix();
    }

    public void setChild(BodyPart child) {
        this.child = child;
    }


    public void setAngle(PointF prev, PointF event) {

        float[] points = new float[]{pivot.x, pivot.y};
        combinedMatrix.mapPoints(points);

        float angle = getAngle(new PointF(prev.x - points[0], prev.y - points[1]),
                new PointF(event.x - points[0], event.y - points[1]));

        if (angle != NaN) {
            totalAngle += angle;
            if (constraint != 0f && (totalAngle > constraint || totalAngle < (constraint * -1))) {
                totalAngle -= angle;
                return;
            }
            this.rotationtMatrix.preRotate(angle, pivot.x, pivot.y);
            this.combinedMatrix.reset();
            this.combinedMatrix.setConcat(this.parentMatrix, this.rotationtMatrix);
            if (this.child != null) {
                this.child.updateMatrix(combinedMatrix);
            }
        }
    }


    public void updateMatrix(Matrix matrix) {
        this.parentMatrix.set(matrix);
        this.combinedMatrix.reset();
        this.combinedMatrix.setConcat(this.parentMatrix, this.rotationtMatrix);
        if (this.child != null) {
            this.child.updateMatrix(combinedMatrix);
        }
    }

    public void draw(Canvas canvas, Paint paint) {
        canvas.save();
        canvas.setMatrix(combinedMatrix);
        canvas.drawRect(rect, paint);
        canvas.restore();

        if (this.child != null) {
            this.child.draw(canvas, paint);
        }
    }

    public boolean contains(float x, float y) {
        float[] points = new float[]{x, y};
        combinedMatrix.invert(inverse);
        inverse.mapPoints(points);
        return rect.contains(points[0], points[1]);
    }


    public float getAngle(PointF u, PointF v) {
        float w3 = (u.x * v.y) - (u.y * v.x);
        double u_norm = Math.sqrt((u.x * u.x) + (u.y * u.y));
        double v_norm = Math.sqrt((v.x * v.x) + (v.y * v.y));
        double sum = ((Math.asin(w3 / (u_norm * v_norm))) * 180) / Math.PI;
        return (float) sum;
    }

    public void addTranslation(float x, float y) {

    }


}
