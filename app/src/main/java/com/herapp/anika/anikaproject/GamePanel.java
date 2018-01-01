package com.herapp.anika.anikaproject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.support.v4.view.GestureDetectorCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.io.FileOutputStream;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Random;

import static com.herapp.anika.anikaproject.GamePanel.drawX;
import static com.herapp.anika.anikaproject.GamePanel.drawY;

class LineManager{
    Line lines[] = new Line[3];
    private static float alignOffset = 200;
    private float alignOffsetChange = 0.1f;

    LineManager(){
        PointF[] endPoints = new PointF[3];
        endPoints[0] = new PointF(drawX, (float)drawY / 4.f + alignOffset);
        endPoints[1] = new PointF(drawX, (float)drawY / 2.f + alignOffset);
        endPoints[2] = new PointF(drawX, (float)drawY * 3.f / 4.f + alignOffset);
        PointF[] startPoints = new PointF[3];
        startPoints[0] = new PointF(0, (float)drawY / 4.f);
        startPoints[1] = new PointF(0, (float)drawY / 2.f);
        startPoints[2] = new PointF(0, (float)drawY  * 3.f / 4.f);

        lines[0] = new Line(startPoints[0], endPoints[0]);
        lines[1] = new Line(startPoints[1], endPoints[1]);
        lines[2] = new Line(startPoints[2], endPoints[2]);
    }

    float computeWidth(){
        Random r = new Random();
        return drawX + drawX * r.nextFloat();
    }

    void update(){
        boolean add[] = {false, false, false};

        for(int i =0; i < 3; i++){
            lines[i].update(alignOffsetChange);
            add[i] = lines[i].add();
        }

        for(int i =0; i < 3; i++){
            if(add[i]){
                float width = computeWidth();
                lines[i].addSegment(width);
            }
        }
    }

    void draw(Canvas canvas){
        for(Line l : lines){
            l.draw(canvas);
        }
    }
}


public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    class MyGestureListener implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

        @Override
        public boolean onDown(MotionEvent motionEvent) {
            player.jump();
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
        {
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e)
        {
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e){

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return true;
        }
    }
    LineManager manager;
    Player player;
    Context context;
    MainThread thread;
    GestureDetectorCompat gestureDetector;
    MyGestureListener listener;
    static int drawY = 1184;
    static int drawX = 720;
    float scaleX;
    float scaleY;
    public GamePanel(Context con) {
        super(con);
        context = con;
        listener = new MyGestureListener();
        gestureDetector = new GestureDetectorCompat(context, listener);
        gestureDetector.setOnDoubleTapListener(listener);
        //add callback to surface to intercept events
        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry){
            try{
                thread.running = false;
                thread.join();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            retry = false;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)  {
        manager = new LineManager();
        player = new Player();

        Canvas c;
        c = getHolder().lockCanvas();
        scaleX = c.getWidth() / (float)drawX;
        scaleY = c.getHeight() / (float)drawY;
        //here you can pass canvas
        getHolder().unlockCanvasAndPost(c);
        //start the thread
        thread.running = true;
        thread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gestureDetector.onTouchEvent(event);
        if(event.getAction() == MotionEvent.ACTION_UP){

        }
        return true;
    }

    public void update() {
        manager.update();
        player.update(manager.lines);
    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);
        if(canvas != null){
            canvas.save();
            canvas.scale(scaleX, scaleY);
            manager.draw(canvas);
            player.draw(canvas);
            canvas.restore();
        }
    }

}
