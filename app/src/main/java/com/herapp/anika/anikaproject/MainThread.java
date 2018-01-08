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
        long startTime;
        long timeMilisec;
        long waitTime;
        long targetTime = 1000 / FPS;

        while(running){
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

            timeMilisec = (System.nanoTime() - startTime) / 1000000;
            waitTime = targetTime - timeMilisec;

            try{
                this.sleep(waitTime);
            }catch (Exception e){}
        }
    }
}
