package com.herapp.anika.anikaproject;

import android.graphics.PointF;

/**
 * Created by Mati on 29.12.2017.
 */

public class Vec {
    static PointF getCopy(PointF p){
        PointF copy = new PointF(p.x, p.y);
        return  copy;
    }

    static PointF getDivided(PointF p, float k){
        PointF point = new PointF(p.x, p.y);
        if(k != 0) {
            point.x /= k;
            point.y /= k;
        }
        return point;
    }

    static float distance(PointF p1, PointF p2){
        return (float)Math.sqrt((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y));
    }

    static void normalize(PointF p){
        float length = p.length();
        p.x /= length;
        p.y /= length;
    }

    static PointF getNormalized(PointF p){
        PointF p2 = new PointF(p.x, p.y);
        normalize(p2);
        return p2;
    }

    static PointF getAdded(PointF p1, PointF p2){
        PointF newPoint = new PointF(p1.x, p1.y);
        newPoint.x += p2.x;
        newPoint.y += p2.y;
        return newPoint;
    }

    static PointF getSubtracted(PointF p1, PointF p2){
        PointF newPoint = new PointF(p1.x, p1.y);
        newPoint.x -= p2.x;
        newPoint.y -= p2.y;
        return newPoint;
    }

    static void divide(PointF point, float k){
        if(k != 0) {
            point.x /= k;
            point.y /= k;
        }
    }

    static void mult(PointF point, float k){
            point.x *= k;
            point.y *= k;
    }
    
    static PointF getMult(PointF p, float k){
            PointF point = new Point(p.x, p.y);
            point.x *= k;
            point.y *= k;
            return point;l
    }

    static void addVec(PointF p1, PointF p2){
        p1.x += p2.x;
        p1.y += p2.y;
    }
}
