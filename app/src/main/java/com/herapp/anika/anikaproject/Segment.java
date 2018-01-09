package com.herapp.anika.anikaproject;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

import static com.herapp.anika.anikaproject.GamePanel.drawX;
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
        Path path = new Path();

        Obstacle(PointF pos){
            this.pos = pos;
            //initial new points vector
            PointF w = new PointF(width, 0);
            PointF h = new PointF(0, height);
            //rotation
            newW.x = w.x * cosAngle - w.y * sinAngle;
            newW.y = -(w.x * sinAngle + w.y * cosAngle);
            newH.x = h.x * cosAngle - h.y * sinAngle;
            newH.y = -(h.x * sinAngle + h.y * cosAngle);
            computeCollisionPoints();
            path.moveTo(collisionPoints[0].x, collisionPoints[0].y);
            path.lineTo(collisionPoints[1].x, collisionPoints[1].y);
            path.lineTo(collisionPoints[3].x, collisionPoints[3].y);
            path.lineTo(collisionPoints[2].x, collisionPoints[2].y);
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
            path.offset(vel.x, vel.y);
        }

        void draw(Canvas canvas){
            if(pos.x > -70 && pos.x < drawX){
//                canvas.save();
//                canvas.rotate((float)Math.toDegrees(-angle), pos.x, pos.y);
//                canvas.drawRect(pos.x,pos.y - height, pos.x + width, pos.y, obstaclePaint);
//                canvas.restore();
                canvas.drawPath(path, obstaclePaint);
            }   
        }
    }

    private float angle;
    private float sinAngle;
    private float cosAngle;
    PointF pos;
    PointF vel;
    PointF alignment;
    ArrayList <Obstacle> obstacles= new ArrayList<>();
    Paint p = new Paint();
    private Paint obstaclePaint = new Paint();

    Segment(PointF pos, PointF alignment, float width){
        p.setColor(Color.BLUE);
        obstaclePaint.setColor(Color.GREEN);
        p.setStrokeWidth(10);
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
        sinAngle = (float)Math.sin(angle);
        cosAngle = (float)Math.cos(angle);
        //initial obst at 1/4 length of the segment - to do: make this pretty much random
        PointF oPoint = Vec.getAdded(pos, Vec.getDivided(alignment, 4));
        Obstacle o = new Obstacle(oPoint);
        obstacles.add(o);
    }

    PointF getLastPoint(){
        PointF last = Vec.getCopy(pos);
        Vec.addVec(last, alignment);
        return last;
    }

    void computeVel(float speed){
        float normLength = vel.length();
        vel.x /= normLength;
        vel.y /= normLength;
        vel.x *= -speed;
        vel.y *= -speed;
    }

    void update(){
        for(Obstacle o : obstacles){
            o.update();
        }
        Vec.addVec(pos, vel);
    }

    void draw(Canvas canvas) {
        for(Obstacle o : obstacles){
            o.draw(canvas);
        }
        canvas.drawLine(pos.x, pos.y, getLastPoint().x, getLastPoint().y, p);
    }
}
