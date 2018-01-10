package com.herapp.anika.anikaproject;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

import java.util.Random;

/**
 * Created by Mati on 09.01.2018.
 */

public class Coin {
    PointF pos;
    static float radius = 15;
    private Paint paint = new Paint();
    float offset = 0;
    float maxOffset = 10;
    int dir = 1;        //1 = up, -1 = down

    Coin(PointF pos){
        Random rand = new Random();
        offset = rand.nextFloat() * maxOffset;
        this.pos = pos;
        paint.setColor(Color.YELLOW);
    }

    void update(PointF vel){
        float step = 0.6f;
        Vec.addVec(pos, vel);
        if(dir == 1){
            if(offset < maxOffset){
                offset += step;
            }else{
                dir = -1;
            }
        }else{
            if(offset > -maxOffset){
                offset -= step;
            }else{
                dir = 1;
            }
        }
    }

    void draw(Canvas canvas){
        canvas.drawCircle(pos.x, pos.y + offset, radius, paint);
    }

}
