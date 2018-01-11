package com.herapp.anika.anikaproject;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Created by Mati on 29.12.2017.
 */

public class MainThread extends Thread{
    private SurfaceHolder surfaceHolder;
    private GamePanel gamePanel;
    boolean running = true;
    private static Canvas canvas;
    double prevTime;
    double curTime = 0;

    MainThread(SurfaceHolder holder, GamePanel gamePanel){
        super();
        surfaceHolder = holder;
        this.gamePanel = gamePanel;
    }

    @Override
    public void run(){
        curTime = System.currentTimeMillis();

        while(running){
            prevTime = curTime;
            curTime = System.nanoTime();

            double dt = (curTime - prevTime) / 100;
            if(dt > 0.22)
                dt = 0.22;

            canvas = null;
            //try the canvas
            try{
                canvas = this.surfaceHolder.lockCanvas();
                this.gamePanel.update(dt);
                this.gamePanel.draw(canvas);

            }catch (Exception e){}

            finally{
                if(canvas!=null)
                {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
     }
}
