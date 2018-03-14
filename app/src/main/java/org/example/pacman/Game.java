package org.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * This class should contain all your game logic
 */

public class Game {
    //context is a reference to the activity
    private Context context;
    private int points = 0; //points counter

    //bitmap of the Pacman
    private Bitmap pacBitmap;
    //bitmap of the Ghost
    private Bitmap ghostBitmap;
    //bitmap of the Coin
    private Bitmap coinBitmap;

    //textview reference to points
    private TextView pointsView;
    //pacman location
    private int pacx, pacy, pacdir;
    //ghost location
    private int ghostx, ghosty;
    //the list of goldcoins - initially empty
    public ArrayList<GoldCoin> coins = new ArrayList<>();
    //a reference to the gameview
    private GameView gameView;
    private int h,w; //height and width of screen
    private int moveSpeed = 8;
    public int ghostSpeed;
    public boolean collision = false;

    public Game(Context context, TextView view)
    {

        this.context = context;
        this.pointsView = view;
        pacBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.pacman);
        ghostBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ghost0);
        coinBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.coin);

    }

    public void setGameView(GameView view)
    {
        this.gameView = view;
    }

    //TODO initialize goldcoins also here
    public void newGame(int ghostSpeed)
    {
        this.ghostSpeed = ghostSpeed;

        coins.clear();

        //Pac-Man Starting Coordinates
        pacx = 150;
        pacy = 150;

        //Ghost Starting Coordinates
        Random rand1 = new Random();
        ghostx =  rand1.nextInt(1080) + 30;
        Random rand2 = new Random();
        ghosty =  rand2.nextInt(1142) + 30;
        Log.d("GHOST", "Ghost starting location generated.");
        Log.d("GHOST", "X: "+ghostx+".");
        Log.d("GHOST", "Y: "+ghosty+".");

        Random coinR = new Random();

        //goldcoins
        GoldCoin c1 = new GoldCoin(coinR.nextInt(1080)+60, coinR.nextInt(1142)+60, false);
        coins.add(c1);
        GoldCoin c2 = new GoldCoin(coinR.nextInt(1080)+60, coinR.nextInt(1142)+60, false);
        coins.add(c2);
        GoldCoin c3 = new GoldCoin(coinR.nextInt(1080)+60, coinR.nextInt(1142)+60, false);
        coins.add(c3);
        GoldCoin c4 = new GoldCoin(coinR.nextInt(1080)+60, coinR.nextInt(1142)+60, false);
        coins.add(c4);
        GoldCoin c5 = new GoldCoin(coinR.nextInt(1080)+60, coinR.nextInt(1142)+60, false);
        coins.add(c5);
        GoldCoin c6 = new GoldCoin(coinR.nextInt(1080)+60, coinR.nextInt(1142)+60, false);
        coins.add(c6);
        GoldCoin c7 = new GoldCoin(coinR.nextInt(1080)+60, coinR.nextInt(1142)+60, false);
        coins.add(c7);
        GoldCoin c8 = new GoldCoin(coinR.nextInt(1080)+60, coinR.nextInt(1142)+60, false);
        coins.add(c8);
        GoldCoin c9 = new GoldCoin(coinR.nextInt(1080)+60, coinR.nextInt(1142)+60, false);
        coins.add(c9);

        Log.d("COINS", "Coins generated.");

        for(GoldCoin coin : coins){
            Log.d("COINS", "X: "+coin.coinx+", Y: "+coin.coiny+", Taken:" +coin.taken);
        }

        //Reset the Points
        points = 0;
        pointsView.setText(context.getResources().getString(R.string.points)+" "+points);

        //ReDraw Screen
        gameView.invalidate();
    }

    public void setSize(int h, int w)
    {
        this.h = h;
        this.w = w;
    }

    //GHOST MOVEMENT
    public void moveGhost(int ghostDirection)
    {
        if (ghostDirection == 1 && ghostx+ghostSpeed+ghostBitmap.getWidth()<w)
            {ghostx = ghostx + ghostSpeed;}
        else if (ghostDirection == 2 && ghostx-ghostSpeed>0)
            {ghostx = ghostx - ghostSpeed;}
        else if (ghostDirection == 3 && ghosty-ghostSpeed>0)
            {ghosty = ghosty - ghostSpeed;}
        else if (ghostDirection == 4 && ghosty+ghostSpeed+ghostBitmap.getHeight()< h)
            {ghosty = ghosty + ghostSpeed;}

    }

    //PACMAN MOVEMENT
    public void movePacman(int moveDirection)
    {
            if (moveDirection == 1 && pacx+moveSpeed+pacBitmap.getWidth()<w)
                {pacx = pacx + moveSpeed;}
            else if (moveDirection == 2 && pacx-moveSpeed>0)
                {pacx = pacx - moveSpeed;}
            else if (moveDirection == 3 && pacy-moveSpeed>0)
                {pacy = pacy - moveSpeed;}
            else if (moveDirection == 4 && pacy+moveSpeed+pacBitmap.getHeight()< h)
                {pacy = pacy + moveSpeed;}

        gameView.invalidate();
    }

    //TODO check if the pacman touches a gold coin
    //and if yes, then update the neccesseary data
    //for the gold coins and the points
    //so you need to go through the arraylist of goldcoins and
    //check each of them for a collision with the pacman
    public void doCollisionCheck()
    {
        float centerPacmanX = getPacBitmap().getWidth() / 2 + pacx;
        float centerPacmanY = getPacBitmap().getHeight() / 2 + pacy;

        float centerGhostX = getGhostBitmap().getWidth() / 2 + ghostx;
        float centerGhostY = getGhostBitmap().getHeight() / 2 + ghosty;

        float distGX = centerPacmanX - centerGhostX;
        float distGY = centerPacmanY - centerGhostY;
        float distanceG = PithagoraFormula(distGX, distGY);

        if( distanceG < 60 ){
                Log.d("GAME", "Pacman and Ghost collision.");
                this.collision = true;
        }

        for (GoldCoin coin:coins) {
            float centerCoinX = getCoinBitmap().getWidth() / 2 + coin.coinx;
            float centerCoinY = getCoinBitmap().getHeight() / 2 + coin.coiny;
            float distX = centerPacmanX - centerCoinX;
            float distY = centerPacmanY - centerCoinY;
            float distance = PithagoraFormula(distX, distY);
            if (distance < 40)
            {
                if(!coin.taken) {points++;}
                coin.taken = true;
                // getCoins().remove(coin);  --> KEEPS CRASHING THE GAME
                Log.d("GAME", "Coin taken.");
                gameView.invalidate();
                pointsView.setText("Points: "+getPoints());
            }

        }
    }

    public float PithagoraFormula(float x, float y){
        float calculus = (float) Math.sqrt((x * x) + (y * y));
        return calculus;
    }

    public int getPacx()
    {
        return pacx;
    }

    public int getPacy()
    {
        return pacy;
    }

    //public int getPacmanDir() {return  ;}

    public int getGhostx() { return ghostx; }

    public int getGhosty() {return ghosty; }

    public int getPoints()
    {
        return points;
    }

    public ArrayList<GoldCoin> getCoins()
    {
        return coins;
    }

    public Bitmap getPacBitmap()
    {
        return pacBitmap;
    }

    public Bitmap getGhostBitmap() { return ghostBitmap; }

    public Bitmap getCoinBitmap() { return coinBitmap; }


}
