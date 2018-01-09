package com.herapp.anika.anikaproject;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Created by Mati on 29.12.2017.
 */

public class MainThread extends Thread{
    private int FPS = 120;
    private SurfaceHolder surfaceHolder;
    private GamePanel gamePanel;
    boolean running = true;
    private static Canvas canvas;

    MainThread(SurfaceHolder holder, GamePanel gamePanel){
        super();
        surfaceHolder = holder;
        this.gamePanel = gamePanel;
    }

    @Override
    public void run(){
        int frames = 0;
        long startTime = 0;
        long timeMilisec;
        long waitTime = 0;
        long targetTime = 1000 / FPS;
        long fixedWaitTime = 0;
        boolean set = false;

        while(running){
            if(!set)
                startTime = System.nanoTime();

            canvas = null;
            //try the canvas
            try{
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder){
                    this.gamePanel.update();
                    this.gamePanel.draw(canvas);
                }
            }catch (Exception e){}

            finally{
                if(canvas!=null)
                {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                    catch(Exception e){e.printStackTrace();}
                }
            }

            if(!set) {
                timeMilisec = (System.nanoTime() - startTime) / 1000000;
                waitTime = targetTime - timeMilisec;
            }
            fixedWaitTime += waitTime;
            if(frames < FPS)
                frames++;
            else{
                if(!set){
                    set = true;
                    fixedWaitTime /= FPS;
                }
            }
            try{
                if(!set)
                    this.sleep(waitTime);
                else
                    this.sleep(fixedWaitTime);
            }catch (Exception e){}
        }
    }
}
