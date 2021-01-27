/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakegame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.Timer;

/**
 *
 * @author Waruna
 */
public class Game extends javax.swing.JPanel implements ActionListener {

    /**
     * Creates new form Game
     */
    private final int dotSize = 10;
    private final int ALL_DOTS = 900;
    private final int randPos = 29;
    private final int DELAY = 140;
    
    private final int x[] = new int[ALL_DOTS];
    private final int y[] = new int[ALL_DOTS];
    
    private int dots;
    private int food_location_x;
    private int food_location_y;
    
    private boolean turnLeft = false;
    private boolean turnRight = true;
    private boolean turnUp = false;
    private boolean turnDown = false;
    private boolean isGameStarted = true;
    
    private Timer coutdown;
    private Image ball;
    private Image apple;
    private Image snakeHead;
    private boolean started = false;
    JLabel label = new JLabel();
    JButton button = new JButton("Start Game");
    Thread appleLocationChange;
    JLabel scorelbl = new JLabel();
    
//    BorderModal borderModal = new BodrerModal();

    /**
     *
     */
    public Game() {
        addKeyListener((KeyListener) new KeyMovementListent());
        setBackground(Color.blue);
        setFocusable(true);
        setPreferredSize(new Dimension(300, 400));
        button.setFocusable(false);
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                loadImages();
                started = true;
                startGame();
            }
            
        });
        label.setBounds(0, 0, 0, 0);
        label.setText("0");
        scorelbl.setText("score");
        scorelbl.setForeground(Color.white);
        label.setForeground(Color.white);
        button.setBounds(0, 0, 0, 0);
        add(button);
        add(scorelbl);
        add(label);
        
        appleLocationChange = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(RandomSingleton.getRandomNumber(10, 15) * 1000);
                    changeFoodLocation(randPos, dotSize);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        });
    }
    
    private void loadImages() {
        ball = getImage("food.png");
        apple = getImage("apple.png");
        snakeHead = getImage("snake-head.png");
    }

    /**
     *
     * @param imageName
     * @return
     */
    private Image getImage(String imageName) {
        java.net.URL imgUrl = getClass().getResource(imageName);
        return new ImageIcon(imgUrl).getImage();
    }

    /**
     *
     */
    private void startGame() {
        button.hide();
        dots = 1;
        for (int z = 0; z < dots; z++) {
            x[z] = 50 - z * 10;
            y[z] = 50;
        }
        changeFoodLocation(randPos, dotSize);
        coutdown = new Timer(DELAY, this);
        coutdown.start();
    }

    /**
     *
     * @param graphics
     */
    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        if (started) {
            populateApple(graphics);
        }
        
    }

    /**
     *
     * @param graphics
     */
    private void populateApple(Graphics graphics) {
        if (isGameStarted) {
            graphics.drawImage(apple, food_location_x, food_location_y, this);
            for (int z = 0; z < dots; z++) {
                if (z == 0) {
                    graphics.drawImage(snakeHead, x[z], y[z], this);
                } else {
                    graphics.drawImage(ball, x[z], y[z], this);
                }
            }
            Toolkit.getDefaultToolkit().sync();
        } else {
            gameOver(graphics);
        }
    }
    
    private void gameOver(Graphics g) {
        
        String msg = "Game Over";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);
        
        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (BoardSingleton.getBoardWidth()- metr.stringWidth(msg)) / 2, BoardSingleton.getBoardHeight() / 2);
    }
    
    private void checkApple() {
        
        if ((x[0] == food_location_x) && (y[0] == food_location_y)) {
            dots++;
            label.setText(Integer.toString(dots - 1));
            changeFoodLocation(randPos, dotSize);
        }
    }
    
    private void move() {
        
        for (int z = dots; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }
        
        if (turnLeft) {
            x[0] -= dotSize;
        }
        
        if (turnRight) {
            x[0] += dotSize;
        }
        
        if (turnUp) {
            y[0] -= dotSize;
        }
        
        if (turnDown) {
            y[0] += dotSize;
        }
    }
    
    private void checkCollision() {
        
        for (int c = dots; c > 0; c--) {
            if ((c > 4) && (x[0] == x[c]) && (y[0] == y[c])) {
                isGameStarted = false;
            }
        }
        
        if (y[0] >= BoardSingleton.getBoardHeight()) {
            isGameStarted = false;
        }
        
        if (y[0] < 0) {
            isGameStarted = false;
        }
        
        if (x[0] >= BoardSingleton.getBoardWidth()) {
            isGameStarted = false;
        }
        
        if (x[0] < 0) {
            isGameStarted = false;
        }
        
        if (!isGameStarted) {
            coutdown.stop();
        }
    }

    /**
     * This method changes location of the apple
     */
    private void changeFoodLocation(int randPos, int dotSize) {
        food_location_x = RandomSingleton.getFoodLocationX(randPos, dotSize);
        food_location_y = RandomSingleton.getFoodLocationY(randPos, dotSize);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if (isGameStarted) {
            checkApple();
            checkCollision();
            move();
        }
        
        repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(204, 255, 0));
        setMaximumSize(new java.awt.Dimension(300, 300));
        setPreferredSize(new java.awt.Dimension(300, 300));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private class KeyMovementListent extends KeyAdapter {
        
        @Override
        public void keyPressed(KeyEvent event) {
            int pressedKey = event.getKeyCode();
            
            switch (pressedKey) {
                case KeyEvent.VK_LEFT:
                    if (!turnRight) {
                        turnLeft = true;
                        turnUp = false;
                        turnDown = false;
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (!turnLeft) {
                        turnRight = true;
                        turnDown = false;
                        turnUp = false;
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (!turnDown) {
                        turnUp = true;
                        turnLeft = false;
                        turnRight = false;
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (!turnUp) {
                        turnDown = true;
                        turnLeft = false;
                        turnRight = false;
                    }
                    break;
                default:
                    break;
            }
        }
        
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
