package com.example.ragdoll;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;

import java.util.ArrayList;

public class Torso extends AbstractPart {

    RectF rect;
    ArrayList<BodyPart> children;
    Matrix mainMatrix;
    Matrix inverse;

    Torso(float width, float height) {

        // Create mainMatrix here. This stores the baseline transformations that create and move
        // the torso. This will be copied by all the children and post methods will be applied
        // to the copy. This matrix will only be modified by postTranslate() while dragging.
        this.mainMatrix = new Matrix();
        this.inverse = new Matrix();

        // Define the primary rectangle for the torso
        this.rect = new RectF(0,0, width, height);

        children = new ArrayList<BodyPart>();
    }

    public void addChild(BodyPart child) {
        this.children.add(child);
    }

    public void addTranslation(float x, float y) {
        // Concatenate a translation to the main matrix;
        this.mainMatrix.postTranslate(x, y);
        Log.d("Torso", String.valueOf(x));
        Log.d("Torso", String.valueOf(y));


        // Propagate this matrix down to its children
        for (BodyPart child : children) {
            child.updateMatrix(mainMatrix);
        }
    }

    public void draw(Canvas canvas, Paint paint) {
        // Draw current torso based on transform matrix
        canvas.save();
        canvas.setMatrix(mainMatrix);
        canvas.drawRect(rect, paint);
        canvas.restore();

        // Draw children
        for (BodyPart child : this.children) {
            child.draw(canvas, paint);
        }
    }

    public boolean contains(float x, float y) {
        float[] points = new float[] {x, y};
        mainMatrix.invert(inverse);
        inverse.mapPoints(points);
        return rect.contains(points[0], points[1]);
    }

    public void setAngle(PointF prev, PointF event) {}


}
