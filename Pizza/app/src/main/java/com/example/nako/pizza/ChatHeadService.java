package com.example.nako.pizza;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static android.R.attr.process;
import static android.media.AudioManager.STREAM_MUSIC;
import static android.media.AudioManager.STREAM_SYSTEM;

/**
 * Created by Nako on 2017/2/2.
 */

public class ChatHeadService extends Service {

    private WindowManager windowManager;
    private ImageView chatHead;
    private WindowManager.LayoutParams params;
    private int i=0;
    private int t=0;
    private Context c;
    private Timer timer = new Timer(true);
    private MediaPlayer mp;
    @Override public IBinder onBind(Intent intent) {
        // Not used
        return null;
    }

    @Override public void onCreate() {
        super.onCreate();
        c=this;
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        chatHead = new ImageView(this);
        chatHead.setImageResource(R.drawable.pizza_slice);
        timer.schedule(new MyTimerTask(), 3000, 3000);
        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_TOAST,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 100;


        final AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

        chatHead.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        audioManager.setStreamVolume(STREAM_MUSIC,audioManager.getStreamMaxVolume(STREAM_MUSIC),0);
                        i++;
                        String []str=getResources().getStringArray(R.array.pizza_name);
                        Random ran = new Random();
                        int num=ran.nextInt(str.length);
                        String s="s"+(num+1);
                        Log.wtf("s","/="+(num+1));
                        int mp3Resource = getResources().getIdentifier(s, "raw" , "com.example.nako.pizza");
                        mp = MediaPlayer.create(c,mp3Resource);
                        mp.start();

                        Toast.makeText(c,str[num],Toast.LENGTH_SHORT).show();

                        mp.start();
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        if(i==10||t>30)
                            onDestroy();
                        return true;
                    case MotionEvent.ACTION_UP:
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        windowManager.updateViewLayout(chatHead, params);
                        return true;
                }
                return false;
            }
        });

        windowManager.addView(chatHead, params);

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        this.stopSelf();
        timer.cancel();
        if (chatHead != null)
            windowManager.removeView(chatHead);

    }

    public class MyTimerTask extends TimerTask
    {
        public void run()
        {
            i=0;
            t++;
            Log.wtf("?","/"+t);
        }
    };
}
