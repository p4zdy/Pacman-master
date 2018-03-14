package org.example.pacman;

/**
 * This class should contain information about a single GoldCoin.
 * such as x and y coordinates (int) and whether or not the goldcoin
 * has been taken (boolean)
 */

public class GoldCoin {
    public float coinx;
    public float coiny;
    public boolean taken;

    public GoldCoin (float coinx, float coiny, boolean taken)
    {
        this.coinx = coinx;
        this.coiny = coiny;
        this.taken = taken;
    }
}
