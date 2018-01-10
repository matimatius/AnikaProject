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
    long timeMilisec = 0;

    MainThread(SurfaceHolder holder, GamePanel gamePanel){
        super();
        surfaceHolder = holder;
        this.gamePanel = gamePanel;
    }

    @Override
    public void run(){
        long startTime = 0;

        while(running){
            startTime = System.nanoTime();

            canvas = null;
            //try the canvas
            try{
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder){
                    this.gamePanel.update(timeMilisec);
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
            //to do check if milisec or sec
            timeMilisec = (System.nanoTime() - startTime) / 1000000000;
        }
     }
}
