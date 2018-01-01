package com.herapp.anika.anikaproject;

import android.graphics.Canvas;
import android.graphics.PointF;

import java.util.ArrayList;
import java.util.Random;

import static com.herapp.anika.anikaproject.GamePanel.drawX;
import static com.herapp.anika.anikaproject.Player.gravity;
import static com.herapp.anika.anikaproject.Player.jumpForce;

/**
 * Created by Mati on 29.12.2017.
 */

public class Line {
    PointF pos;
    PointF endPoint;
    PointF alignment;
    float speed;
    ArrayList <Segment> segments =  new ArrayList<>();
    private float gap;

    private void computeGap(){
        float time = -jumpForce / gravity.y;    //t = Vymax / a
        gap = -speed * 2 * time;    //x = Vx * 2t
        Random r = new Random();
        gap = gap + r.nextFloat() * gap; //random gap between gap and 2 * gap
    }

    private void computeSpeed(){
        speed = (pos.y - endPoint.y) / 80.f * 1.4f;
    }

    Line(PointF pos, PointF endPoint){
        this.endPoint = endPoint;
        this.pos = pos;
        this.alignment = Vec.getSubstracted(endPoint, pos);
        computeSpeed();
        computeGap();
        Segment startSegment = new Segment(Vec.getCopy(pos), Vec.getCopy(alignment), alignment.length());
        addSegment(startSegment);
    }

    void addSegment(float width){
        Segment s = new Segment(Vec.getCopy(endPoint), Vec.getCopy(alignment), width);
        segments.add(s);
    }

    boolean add(){
        if(segments.size() > 0) {
            if (segments.get(segments.size() - 1).getLastPoint().x < drawX - gap){
                return true;
            }else{
                return false;
            }
        }else{
            return true;
        }
    }

    void update(float offsetChange){
        computeGap();
        this.endPoint.y += offsetChange;
        this.alignment = Vec.getSubstracted(endPoint, pos);
        computeSpeed();
        for(Segment s : segments){
            s.update(speed);
            if(s.getLastPoint().x < 0){
                segments.remove(s);
            }
        }
    }

    void addSegment(Segment s){
        computeGap();
        segments.add(s);
    }

    void draw(Canvas canvas){
        for(Segment s : segments){
            s.draw(canvas);
        }
    }

}
