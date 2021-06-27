package com.example.catchthemouseapp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import android.graphics.Typeface;
import android.media.AudioManager;

import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Handler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.MotionEvent;
import android.view.View;

import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {


    private FlyingCatView gameView;

    private Handler handler = new Handler();
    private final static long Interval = 20;

    MediaPlayer bkgrdmsc;
    Typeface cutdeepfont;


    public class FlyingCatView extends View {
        //Sunet
        private SoundPool sound;
        private int sndmouse;
        private int sndcat;
        private int sndmilk;
        //Imagine
        private Bitmap cat[] = new Bitmap[2];
        private Bitmap mouse[] = new Bitmap[1];
        private Bitmap mouse1[] = new Bitmap[1];
        private Bitmap dog[]= new Bitmap[1];
        private Bitmap dog1[]= new Bitmap[1];
        private Bitmap milk[] = new Bitmap[1];
        private Bitmap backgroundImage;
        private Bitmap life[] = new Bitmap[2];
        //Desen
        private Paint scorePaint = new Paint();
        //Lățime și Înălțime pânză
        private  int canvasWidth, canvasHeight;
        // Poziție și viteză
        private int catX  ,catY, catSpeed;
        private int mouseX, mouseY, mouseSpeed =15;
        private int mouse1X, mouse1Y, mouse1Speed =15;
        private int dogX, dogY, dogSpeed =20;
        private int dog1X, dog1Y, dog1Speed =20;
        private int milkX, milkY, milkSpeed =16;
        //Scor și contor de viață
        private int score, lifeCounterOfCat;
        // Atingerea ecranului
        private boolean touch = false;





        public FlyingCatView(Context context) {
            super(context);

            cat[0]= BitmapFactory.decodeResource(getResources(),R.drawable.catt1);
            cat[1]= BitmapFactory.decodeResource(getResources(),R.drawable.catt2);

            backgroundImage =BitmapFactory.decodeResource(getResources(),R.drawable.bckground);

            mouse[0]= BitmapFactory.decodeResource(getResources(), R.drawable.mouse1);

            mouse1[0]= BitmapFactory.decodeResource(getResources(), R.drawable.mouse1);

            dog[0]= BitmapFactory.decodeResource(getResources(), R.drawable.doggo);
            dog1[0]= BitmapFactory.decodeResource(getResources(), R.drawable.doggo);

            milk[0]= BitmapFactory.decodeResource(getResources(), R.drawable.milk1);

            cutdeepfont= Typeface.createFromAsset(getApplicationContext().getAssets(),"font/cutdeep.otf");
            scorePaint.setColor(Color.WHITE);
            scorePaint.setTextSize(50);
            scorePaint.setTypeface(cutdeepfont);
            scorePaint.setAntiAlias(true);

            life[0] = BitmapFactory.decodeResource(getResources(),R.drawable.heart);
            life[1] = BitmapFactory.decodeResource(getResources(),R.drawable.greyheart);

            catX = 0;
            catY = 500;
            score = 0;
            lifeCounterOfCat=3;

            sound = new SoundPool(10, AudioManager.STREAM_MUSIC,0);
            sndmouse = sound.load(context, R.raw.micesound,1);
            sndcat = sound.load(context, R.raw.meowsound,1);
            sndmilk = sound.load(context, R.raw.slurpsound,1);


        }
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            canvasWidth = canvas.getWidth();
            canvasHeight = canvas.getHeight();

            canvas.drawBitmap(backgroundImage, 0,0,null);

            //cat
            int minCatY = cat[0].getHeight();
            int maxCatY = canvasHeight - cat[0].getHeight();
            catY =catY +catSpeed;
            if (catY < minCatY)
            {
                catY = minCatY;
            }
            if (catY > maxCatY)
            {
                catY = maxCatY;
            }
            catSpeed = catSpeed + 2;
            if(touch)
            {
                canvas.drawBitmap(cat[1], catX, catY, null);
                touch=false;
            }
            else
            {
                canvas.drawBitmap(cat[0], catX,catY,null);
            }
            // mouse
            mouseX=mouseX - mouseSpeed;
            if(catchTheMouse(mouseX, mouseY))
            {
                score= score+10;
                sound.play(sndmouse, 1.0f,1.0f,0,0,1.5f);
                mouseX= -100;

            }
            if(mouseX <0)
            {
                mouseX=canvasWidth+10;
                mouseY=(int) Math.floor(Math.random() * (maxCatY - minCatY)) +minCatY;
            }
            //mouse1
            mouse1X=mouse1X - mouse1Speed;
           if(catchTheMouse(mouse1X, mouse1Y))
            {
               score= score+10;
              sound.play(sndmouse, 1.0f,1.0f,0,0,1.5f);
              mouse1X= -100;

            }
           if(mouse1X <0)
            {
                mouse1X=canvasWidth+122;
                mouse1Y=(int) Math.floor(Math.random() * (maxCatY - minCatY)) +minCatY ;
           }
            //dog
            dogX=dogX - dogSpeed;
            if(catchTheMouse(dogX, dogY))
            {
                dogX= -200;
                sound.play(sndcat, 1.5f,1.5f,0,0,1.0f);


                lifeCounterOfCat--;
                if(lifeCounterOfCat == 0)
                {
                    Toast.makeText(getContext(),"Game Over", Toast.LENGTH_SHORT).show();
                    Intent gameOverIntent = new Intent(getApplicationContext(), GameOverActivity.class);
                    gameOverIntent.putExtra("SCORE", score);
                    startActivity(gameOverIntent);
                    finish();
                }
            }
            if(dogX <0)
            {
                dogX=canvasWidth+2000;
                dogY=(int) Math.floor(Math.random() * (maxCatY - minCatY)) +minCatY;
            }

            dog1X=dog1X - dog1Speed;
            if(catchTheMouse(dog1X, dog1Y))
            {
                dog1X= -200;
                sound.play(sndcat, 1.5f,1.5f,0,0,1.0f);


                lifeCounterOfCat--;
                if(lifeCounterOfCat == 0)
                {
                    Toast.makeText(getContext(),"Game Over", Toast.LENGTH_SHORT).show();
                    Intent gameOverIntent = new Intent(getApplicationContext(), GameOverActivity.class);
                    gameOverIntent.putExtra("SCORE", score);
                    startActivity(gameOverIntent);
                    finish();

                }

            }
               if(dog1X <0)
            {
                dog1X=canvasWidth+2700;
                dog1Y=(int) Math.floor(Math.random() * (maxCatY - minCatY)) +minCatY;
            }

            //milk
            milkX=milkX - milkSpeed;
            if(catchTheMouse(milkX, milkY))
            {
                milkX= -100;
                sound.play(sndmilk, 0.5f,0.5f,0,0,1.5f);
                if (lifeCounterOfCat==2 || lifeCounterOfCat==1)
                {
                    lifeCounterOfCat++;
                }
            }
            if(milkX <0)
            {
                milkX=canvasWidth+20000;
                milkY=(int) Math.floor(Math.random() * (maxCatY - minCatY)) +minCatY;
            }

            canvas.drawBitmap(mouse[0], mouseX, mouseY, null);
            canvas.drawBitmap(mouse1[0], mouse1X, mouse1Y, null);
            canvas.drawBitmap(dog[0],dogX, dogY, null);
            canvas.drawBitmap(dog1[0],dog1X, dog1Y, null);
            canvas.drawBitmap(milk[0],milkX,milkY,null);
            canvas.drawText("Score: "+ score, 20, 60 ,scorePaint);

            for (int i=0; i<3;i++)
            {
                int x = (int)(900 + life[0].getWidth() * 1.5 *i );
                int y = 30;
                if (i <lifeCounterOfCat)
                {
                    canvas.drawBitmap(life[0],x,y,null );

                }
                else
                {
                    canvas.drawBitmap(life[1], x, y,null);
                }
            }
        }


        public  boolean catchTheMouse (int x, int y)
        {
            if (catX <x  && x < (catX+cat[0].getWidth())&& catY<y && y<(catY+cat[0].getHeight()))
            {
                return true;
            }
            return false;
        }
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            if (event.getAction()== MotionEvent.ACTION_DOWN)
            {
                touch = true;

                catSpeed = -40;
            }
            return true;
        }

    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameView = new FlyingCatView(this);
        setContentView(gameView);

        bkgrdmsc = MediaPlayer.create(this, R.raw.bckgroundmusic);
        bkgrdmsc.setLooping(true);
        bkgrdmsc.start();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        gameView.invalidate();
                    }
                });
            }
        }, 0, Interval);


    }
    protected void onPause(){
        super.onPause();
        bkgrdmsc.release();
        finish();
    }

}
