package com.herapp.anika.anikaproject;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

import java.util.ArrayList;

/**
 * Created by Mati on 29.12.2017.
 */

public class Segment {
    public class Obstacle{
        PointF pos; //this is rect left bottom corner
        float width = 50;
        float height = 100;
        PointF collisionPoints[] = new PointF[4]; //left top, left bot, right top, right bot
        PointF newW = new PointF();    //used for computing new collision points
        PointF newH = new PointF();

        Obstacle(PointF pos){
            this.pos = pos;
            //initial new points vector
            PointF w = new PointF(width, 0);
            PointF h = new PointF(0, height);
            //rotation
            newW.x = (float)(w.x * Math.cos(angle) - w.y * Math.sin(angle));
            newW.y = (float)-(w.x * Math.sin(angle) + w.y * Math.cos(angle));
            newH.x = (float)(h.x * Math.cos(angle) - h.y * Math.sin(angle));
            newH.y = (float)-(h.x * Math.sin(angle) + h.y * Math.cos(angle));
        }

        void computeCollisionPoints(){
            //assignment
            collisionPoints[0] = Vec.getAdded(pos, newH);
            collisionPoints[1] = Vec.getCopy(pos);
            collisionPoints[2] = Vec.getAdded(pos, Vec.getAdded(newW, newH));
            collisionPoints[3] = Vec.getAdded(pos, newW);
        }

        void update(){
            Vec.addVec(pos, vel);
            computeCollisionPoints();
        }

        void draw(Canvas canvas){
            Paint p = new Paint();
            p.setColor(Color.GREEN);
            canvas.save();
            canvas.rotate((float)Math.toDegrees(-angle), pos.x, pos.y);
            canvas.drawRect(pos.x,pos.y - height, pos.x + width, pos.y, p);
            canvas.restore();
        }
    }

    float angle;
    PointF pos;
    PointF vel;
    PointF alignment;
    float width;
    ArrayList <Obstacle> obstacles= new ArrayList<>();

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

        angle = alignment.y / alignment.x;
        angle = (float)Math.atan(angle);
        angle = -angle;
        //test
        PointF oPoint = Vec.getAdded(pos, Vec.getDivided(alignment, 4));
        Obstacle o = new Obstacle(oPoint);
        obstacles.add(o);
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
        for(Obstacle o : obstacles){
            o.update();
        }
        Vec.addVec(pos, vel);
    }

    void draw(Canvas canvas) {
        Paint p = new Paint();
        p.setColor(Color.BLUE);
        p.setTextSize(40);
        p.setStrokeWidth(10);
        for(Obstacle o : obstacles){
            o.draw(canvas);
        }
        canvas.drawLine(pos.x, pos.y, getLastPoint().x, getLastPoint().y, p);
    }
}
