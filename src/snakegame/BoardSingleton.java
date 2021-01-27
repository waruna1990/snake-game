/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakegame;

/**
 *
 * @author Waruna
 */
public class BoardSingleton {

    private static int boardWidth;
    private static int boardHeight;

    private static BoardSingleton bs = new BoardSingleton();

    private BoardSingleton() {
        this.boardWidth = 300;
        this.boardHeight = 300;
    }

    public static int getBoardWidth() {
        return bs.boardWidth;
    }

    public static int getBoardHeight() {
        return bs.boardHeight;
    }
}
