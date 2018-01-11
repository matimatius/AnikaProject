package com.herapp.anika.anikaproject;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

import static com.herapp.anika.anikaproject.GamePanel.drawX;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Mati on 29.12.2017.
 */

public class Segment {
    public class Obstacle{
        PointF pos; //this is rect left bottom corner
        float width = 50;
        float height = 100;
        PointF collisionPoints[] = new PointF[8]; //left top, left bot, right top, right bot middle top right bot left
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
            collisionPoints[4] = Vec.getAdded(collisionPoints[0], Vec.getDivided(newW, 2));
            collisionPoints[5] = Vec.getAdded(collisionPoints[3], Vec.getDivided(newH, 2));
            collisionPoints[6] = Vec.getAdded(pos, Vec.getDivided(newW, 2));
            collisionPoints[7] = Vec.getAdded(pos, Vec.getDivided(newH, 2));
        }

        void update(){
            Vec.addVec(pos, deltaVel);
            path.offset(deltaVel.x, deltaVel.y);
        }

        void draw(Canvas canvas){
            if(pos.x > -70 && pos.x < drawX){
                canvas.drawPath(path, obstaclePaint);
            }   
        }
    }

    private float angle;
    private float sinAngle;
    private float cosAngle;
    PointF pos;
    PointF vel;
    PointF deltaVel = new PointF();
    PointF alignment;
    ArrayList <Obstacle> obstacles= new ArrayList<>();
    Paint p = new Paint();
    private Paint obstaclePaint = new Paint();
    ArrayList <Coin> coins = new ArrayList<>();

    Segment(PointF pos, PointF alignment, float width){
        Random random = new Random();
        int randCoinAmount = random.nextInt(3); //from 0 to 3
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
        for(int i =0; i < randCoinAmount; i++){
            addCoin();
        }
    }

    boolean checkForCoin(PointF pos, float radius){
        for(Coin c : coins){
            if(Vec.distance(pos, new PointF(c.pos.x, c.pos.y + c.offset)) < radius + c.radius){
                coins.remove(c);
                return true;
            }
        }
        return false;
    }

    private void addCoin(){
        Random r = new Random();
        boolean finished = true;
        PointF coinPoint;
        int bound = 20;
        int div = r.nextInt(bound);
        coinPoint = Vec.getCopy(alignment);
        Vec.divide(coinPoint, bound);
        Vec.mult(coinPoint, div);
        Vec.addVec(coinPoint, pos);
        for(Obstacle o : obstacles){
            o.computeCollisionPoints();
            if(Vec.distance(coinPoint, o.collisionPoints[1]) < o.width + 20 && Vec.distance(coinPoint, o.collisionPoints[3]) < o.width + 20)
                finished = false;
        }
        if(finished) {
            Coin coin = new Coin(coinPoint);
            coin.pos.y -= coin.radius * 3;
            coins.add(coin);
        }
    }

    PointF getLastPoint(){
        return Vec.getAdded(pos, alignment);
    }

    void computeVel(float speed){
        float normLength = vel.length();
        vel.x /= normLength;
        vel.y /= normLength;
        vel.x *= -speed;
        vel.y *= -speed;
    }

    void update(double deltaTime){
        deltaVel = Vec.getMult(vel, (float)deltaTime);
        for(Obstacle o : obstacles){
            o.update();
        }
        for(Coin c : coins){
            c.update(deltaVel);
        }
        Vec.addVec(pos, deltaVel);
    }

    void draw(Canvas canvas) {
        for(Obstacle o : obstacles){
            o.draw(canvas);
        }
        for(Coin c : coins){
            c.draw(canvas);
        }
        canvas.drawLine(pos.x, pos.y, getLastPoint().x, getLastPoint().y, p);
    }
}
