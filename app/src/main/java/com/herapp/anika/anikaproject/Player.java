package com.herapp.anika.anikaproject;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

import static com.herapp.anika.anikaproject.GamePanel.drawY;

/**
 * Created by Mati on 29.12.2017.
 */

public class Player {
    PointF pos = new PointF(150, drawY / 2 - 100);
    PointF vel = new PointF(0, 0);
    static float jumpForce = -62;
    static PointF gravity = new PointF(0, 8);
    int jumps = 2;
    float radius = 27;
    Paint p = new Paint();
    Line lines[];
    int coins = 0;
    boolean dead = false;
    private String text;

    Player(Line lines[]){
        this.lines = lines;
        p.setColor(Color.CYAN);
        p.setTextSize(40);
    }

    private boolean slopeDetection(Segment s, double deltaTime){
        float d;
        float slope = (s.getLastPoint().y - s.pos.y) / (s.getLastPoint().x - s.pos.x);
        PointF point = Vec.getCopy(s.pos);
        float xDiff = pos.x - s.pos.x;
        float newY = s.pos.y + xDiff * slope - radius;
        point.x = pos.x;
        point.y = newY;
        d = Vec.distance(pos, point);
        if(d <= vel.y * deltaTime|| d <= radius / 3.f) {
            pos = point;
            return true;
        }else{
            return false;
        }
    }

    private boolean checkObstaclePoints(Segment.Obstacle o,float dist){
        if(pos.y > drawY + 150)
            return true;
        for(int i = 0; i < 8; i++) {
            if (Vec.distance(pos, o.collisionPoints[i]) < dist)
                return true;
        }
        return false;
    }

    boolean checkObstacle(Segment.Obstacle o, double deltaTime){
        float v = vel.length() * (float)deltaTime;
        o.computeCollisionPoints();
        if(v > radius)
            return checkObstaclePoints(o, v);
        return checkObstaclePoints(o, radius);
    }

   private boolean collision(Line lines[], double deltaTime){
       boolean returnValue = false;
        for (Line line : lines)
           for (Segment s : line.segments) {
               if (s.pos.x <= pos.x && s.getLastPoint().x >= pos.x) {
                   for(Segment.Obstacle o : s.obstacles){
                       if(checkObstacle(o, deltaTime))
                           dead = true;
                       if(s.checkForCoin(pos, radius)){
                           coins++;
                       }
                   }
                   if (slopeDetection(s, deltaTime))
                       returnValue = true;
               }
           }
        return returnValue;
    }

    void jump(){
        if(jumps > 0){
            jumps--;
            vel.y = jumpForce;
        }
    }

    void update(Line lines[], double deltaTime){
        text = "COINS: " + Integer.toString(coins);
        if(vel.y < 0){
            for(Line l : lines){
                for(Segment s : l.segments){
                    if (s.pos.x <= pos.x && s.getLastPoint().x >= pos.x) {
                        for(Segment.Obstacle o : s.obstacles){
                            if(checkObstacle(o, deltaTime))
                                dead = true;
                            if(s.checkForCoin(pos, radius)){
                                coins++;
                            }
                        }
                    }
                }
            }
            p.setColor(Color.RED);
            vel.y += gravity.y * deltaTime;
            pos.y += vel.y * deltaTime;
        }
        else {
            if (!collision(lines, deltaTime)) {
                p.setColor(Color.RED);
                vel.y += gravity.y * deltaTime;
                pos.y += vel.y * deltaTime;
            } else {
                vel.y = 0;
                jumps = 2;
                p.setColor(Color.CYAN);
            }
        }
    }

    void draw(Canvas canvas){
        canvas.drawText(text, 50, 50, p);
        canvas.drawCircle(pos.x, pos.y, radius, p);
    }

}
