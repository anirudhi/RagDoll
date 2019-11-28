package com.example.ragdoll;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

import java.util.ArrayList;

public class Body {
    AbstractPart curPart;
    AbstractPart prevPart;
    PointF prevPoint;
    ArrayList<AbstractPart> parts;

    Body(int screenWidth, int screenHeight) {

        // Create torso
        PointF center = new PointF((screenWidth * 0.5f) - 150, (screenHeight * 0.5f) - 400);
        Torso torso = new Torso(300, 400);

        // Create other body parts
        BodyPart head = new BodyPart(200, 150, new PointF(75, -200), new PointF(75, 200), 50.0f);
        BodyPart leftArmUpper = new BodyPart(200, 100, new PointF(-50, 0), new PointF(50, 0), 0f);
        BodyPart leftArmLower = new BodyPart(150, 80, new PointF(10, 200), new PointF(40, 0), 135.0f);
        BodyPart leftHand = new BodyPart(100, 50, new PointF(15, 150), new PointF(25, 0), 35.0f);
        BodyPart rightArmUpper = new BodyPart(200, 100, new PointF(250, 0), new PointF(50, 0), 0f);
        BodyPart rightArmLower = new BodyPart(150, 80, new PointF(10, 200), new PointF(40, 0), 135.0f);
        BodyPart rightHand = new BodyPart(100, 50, new PointF(15, 150), new PointF(25, 0), 35.0f);
        BodyPart leftLegUpper = new BodyPart(300, 110, new PointF(0, 400), new PointF(55, 0), 90.0f);
        BodyPart leftLegLower = new BodyPart(200, 90, new PointF(10, 300), new PointF(45, 0), 90.0f);
        BodyPart leftFoot = new BodyPart(75, 50, new PointF(22, 200), new PointF(25, 0), 35.0f);
        BodyPart rightLegUpper = new BodyPart(300, 110, new PointF(190, 400), new PointF(55, 0), 90.0f);
        BodyPart rightLegLower = new BodyPart(200, 90, new PointF(10, 300), new PointF(45, 0), 90.0f);
        BodyPart rightFoot = new BodyPart(75, 50, new PointF(22, 200), new PointF(25, 0), 35.0f);

        // Add torso children
        torso.addChild(leftArmUpper);
        torso.addChild(rightArmUpper);
        torso.addChild(leftLegUpper);
        torso.addChild(rightLegUpper);
        torso.addChild(head);

        // Set up children for other bodyParts
        leftArmUpper.setChild(leftArmLower);
        leftArmLower.setChild(leftHand);
        rightArmUpper.setChild(rightArmLower);
        rightArmLower.setChild(rightHand);
        leftLegUpper.setChild(leftLegLower);
        leftLegLower.setChild(leftFoot);
        rightLegUpper.setChild(rightLegLower);
        rightLegLower.setChild(rightFoot);

        // Set initial translation
        torso.addTranslation(center.x, center.y);

        parts = new ArrayList<AbstractPart>();
        parts.add(leftHand);
        parts.add(leftArmLower);
        parts.add(leftArmUpper);
        parts.add(rightHand);
        parts.add(rightArmLower);
        parts.add(rightArmUpper);
        parts.add(leftFoot);
        parts.add(leftLegLower);
        parts.add(leftLegUpper);
        parts.add(rightFoot);
        parts.add(rightLegLower);
        parts.add(rightLegUpper);
        parts.add(head);
        parts.add(torso);

        prevPoint = new PointF();
    }

    public void draw(Canvas canvas, Paint paint) {
       parts.get(parts.size() - 1).draw(canvas, paint);
    }

    public void setBodyPart(float x, float y) {
        curPart = getCurPart(x, y);
        prevPart = curPart;
        prevPoint.set(x, y);
    }

    public void doOperation(float x, float y) {
        curPart = getCurPart(x, y);
//        if (curPart != prevPart) return;
        if (curPart == null) return;
        if (curPart == parts.get(parts.size() - 1)) {

            parts.get(parts.size() - 1).addTranslation(x - prevPoint.x, y - prevPoint.y);
            prevPoint.set(x, y);

        } else {
            setRotation(x, y, curPart);
        }

        prevPart = curPart;
    }

    public void releaseBodyPart() {
        curPart = null;
    }

    public void setRotation(float x, float y, AbstractPart part) {
        part.setAngle(prevPoint, new PointF(x, y));
        prevPoint.set(x, y);
    }

    public AbstractPart getCurPart(float x, float y) {
        for (AbstractPart part : parts) {
            if (part.contains(x, y)) {
                return part;
            }
        }
        return null;
    }
}
