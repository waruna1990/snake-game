/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakegame;

import java.util.Random;

/**
 *
 * @author Waruna
 */
public class RandomSingleton {

    private static RandomSingleton random = new RandomSingleton();
    static Random rand = new Random();
    private RandomSingleton() {
    }
    
    public static int getRandomNumber(int start, int end){
        int randomNumber = rand.nextInt((end - start) + 1) + start;
        return randomNumber;
    }
    
    public static int getFoodLocationX(int randPos, int dotSize){
        int r = (int) (Math.random() * randPos);
        return ((r * dotSize));
    }
    
    public static int getFoodLocationY(int randPos, int dotSize){
        int r = (int) (Math.random() * randPos);
        return ((r * dotSize));
    }
    
}
