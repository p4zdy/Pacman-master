package org.example.pacman;


import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static android.R.attr.button;

public class MainActivity extends Activity {
    //reference to the main view
    GameView gameView;
    //reference to the game class.
    Game game;

    private Timer myTimer;
    public Timer ghostTimer;
    //you should put in the running in the game class
    private boolean running = false;
    public int moveDirection;
    public int ghostDirection;
    public int ghostTimerPeriod = 0;

    private static MediaPlayer player;
    private static MediaPlayer playerDeath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //saying we want the game to run in one mode only
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        gameView = findViewById(R.id.gameView);
        TextView textView = findViewById(R.id.points);

        game = new Game(this, textView);
        game.setGameView(gameView);
        gameView.setGame(game);

        game.newGame(5);

        Button buttonRight = findViewById(R.id.moveRight);
        Button buttonLeft = findViewById(R.id.moveLeft);
        Button buttonUp = findViewById(R.id.moveUp);
        Button buttonDown = findViewById(R.id.moveDown);

        //listener of our pacman, when somebody clicks it
        buttonRight.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                moveDirection = 1;
            }
        });
        buttonLeft.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                moveDirection = 2;
            }
        });
        buttonUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                moveDirection = 3;
            }
        });
        buttonDown.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                moveDirection = 4;
            }
        });

        player = MediaPlayer.create(this, R.raw.pacman_song);
        player.setVolume(20, 20);
        player.setLooping(true);

        playerDeath = MediaPlayer.create(this, R.raw.death);
        playerDeath.setVolume(20, 20);
        playerDeath.setLooping(false);

        player.start();

        if(game.ghostSpeed == 30)
            {ghostTimerPeriod = 150;}
        else if(game.ghostSpeed == 15)
            { ghostTimerPeriod = 500;}
        else if(game.ghostSpeed == 10)
            { ghostTimerPeriod = 1000;}
        else { ghostTimerPeriod = 1500;}

        //make a new timer
        myTimer = new Timer();
        ghostTimer = new Timer();

        running = true; //should the game be running?
        //We will call the timer 5 times each second
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                TimerMethod();
            }

        }, 0, 40); //0 indicates we start now, 40
        //is the number of miliseconds between each call


        ghostTimer.schedule(new TimerTask(){
            @Override
            public void run(){
                if (running) {
                    Random random = new Random();
                    ghostDirection = random.nextInt(4) + 1;
                    Log.d("GHOST", "Direction: " + ghostDirection + ".");
                }

            }
        }, 0, ghostTimerPeriod);

    }

    public static MediaPlayer getPlayer() {
        return player;
    }

    @Override
    protected void onStop() {
        super.onStop();
        //just to make sure if the app is killed, that we stop the timer.
        myTimer.cancel();
        ghostTimer.cancel();
    }

    protected void onPause() {
        super.onPause();

        running = false;
        player.pause();
    }

    private void onDeath(){

        running = false;

        player.pause();
        playerDeath.start();

    }

    protected void onResume(){
        super.onResume();

        running = true;
        player.start();
    }

    private void TimerMethod()
    {
        //This method is called directly by the timer
        //and runs in the same thread as the timer.

        //We call the method that will work with the UI
        //through the runOnUiThread method.
        this.runOnUiThread(Timer_Tick);
    }

    private Runnable Timer_Tick = new Runnable() {
        public void run() {

            //This method runs in the same thread as the UI.
            // so we can draw
            if (game.collision == true) {
                onDeath();
            }

            if (running)
            {
                game.movePacman(moveDirection);
                game.moveGhost(ghostDirection);
                game.doCollisionCheck();
            }

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_pause) {
            onPause();
        } else if (id == R.id.action_newGame) {
            game.newGame(5);
            running = true;
            moveDirection = 0;
            ghostDirection = 0;
            game.collision = false;

            player.start();
        }
        else if (id == R.id.action_play)
        {
            onResume();
        }
        else if (id == R.id.godlike)
        {
             game.ghostSpeed = 30;
            moveDirection = 0;
            ghostDirection = 0;
            game.collision = false;
        }
        else if (id == R.id.hard)
        {
            game.newGame(30);
            moveDirection = 0;
            ghostDirection = 0;
            game.collision = false;
        }
        else if (id == R.id.medium)
        {
            game.newGame(10);
            moveDirection = 0;
            ghostDirection = 0;
            game.collision = false;
        }
        else if (id == R.id.easy)
        {
            game.newGame(5);
            moveDirection = 0;
            ghostDirection = 0;
            game.collision = false;
        }
        return super.onOptionsItemSelected(item);
    }
}
