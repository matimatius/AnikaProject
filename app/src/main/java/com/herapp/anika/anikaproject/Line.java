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
        speed = (pos.y - endPoint.y) / 40.f;
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
        computeGap();
        computeSpeed();
        Segment s = new Segment(Vec.getCopy(endPoint), Vec.getCopy(alignment), width);
        s.computeVel(speed);
        segments.add(s);
    }

    boolean add(){
        if(segments.size() > 0) {
            return segments.get(segments.size() - 1).getLastPoint().x < drawX - gap;
        }else{
            return true;
        }
    }

    void update(float offsetChange){
        this.endPoint.y += offsetChange;
        this.alignment = Vec.getSubstracted(endPoint, pos);
        for(Segment s : segments){
            s.update();
            if(s.getLastPoint().x < 0){
                segments.remove(s);
            }
        }
    }

    void addSegment(Segment s){
        s.computeVel(speed);
        segments.add(s);
    }

    void draw(Canvas canvas){
        for(Segment s : segments){
            s.draw(canvas);
        }
    }

}
