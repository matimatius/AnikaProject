package com.herapp.anika.anikaproject;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

/**
 * Created by Mati on 29.12.2017.
 */

public class Segment {
    PointF pos;
    PointF vel;
    PointF alignment;
    float width;


    Segment(PointF pos, PointF alignment, float width){
        this.width = width;
        this.pos = pos;
        this.alignment = alignment;
        float normLength = alignment.length();
        vel = new PointF(-alignment.x, -alignment.y);

        alignment.x /= normLength;
        alignment.y /= normLength;
        alignment.x *= width;
        alignment.y *= width;
    }

    PointF getLastPoint(){
        PointF last = Vec.getCopy(pos);
        Vec.addVec(last, alignment);
        return last;
    }

    private void computeVel(float speed){
        float normLength = vel.length();
        vel.x /= normLength;
        vel.y /= normLength;
        vel.x *= -speed;
        vel.y *= -speed;
    }

    void update(float speed){
        computeVel(speed);
        Vec.addVec(pos, vel);
    }

    void draw(Canvas canvas) {
        Paint p = new Paint();
        p.setColor(Color.BLUE);
        p.setTextSize(40);
        p.setStrokeWidth(10);
        canvas.drawLine(pos.x, pos.y, getLastPoint().x, getLastPoint().y, p);
    }
}
