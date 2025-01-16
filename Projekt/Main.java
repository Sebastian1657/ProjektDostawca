package Projekt;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

class Kierowca {
    int x = 0;
    int y = 0;
    int speed = 32;
    public Kierowca(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
class Sklep{
    int x;
    int y;
    public Sklep(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public boolean isOnSklep(Kierowca kierowca) {
        return kierowca.x == this.x && kierowca.y == this.y;
    }
}
class Magazyn{
    int x;
    int y;
    public Magazyn(int x, int y) {
        this.x = x;
        this.y = y;
    };
}
public class Main {
    static Sklep sklep1 = new Sklep(64,4*64);
    static Sklep sklep2 = new Sklep(64,9*64);
    static Sklep sklep3 = new Sklep(6*64,8*64);
    static Sklep sklep4 = new Sklep(7*64,5*64);
    static Sklep sklep5 = new Sklep(10*64,5*64);
    static Sklep[] sklepy = new Sklep[]{
            sklep1,sklep2,sklep3,sklep4,sklep5
    };
    static Kierowca gracz1 = new Kierowca(128, 704);
    static boolean onPath(int kx, int ky){
        int[][] droga = new int[][]{
                {2*64,9*64,2*64}, {64,2*64,4*64}, {2*64,6*64,5*64},
                {9*64,10*64,5*64}, {6*64,9*64,6*64}, {64,9*64,9*64},
                {2*64,11*64,2*64}, {2*64,6*64,6*64}, {8*64,9*64,6*64},
                {5*64,6*64,7*64}, {2*64,9*64,9*64}
        };
        for(int i=0; i<6; i++){
            if(kx >= droga[i][0] && kx <= droga[i][1] && ky == droga[i][2])return true;
        }
        for(int i=6; i<11; i++){
            if(ky >= droga[i][0] && ky <= droga[i][1] && kx == droga[i][2])return true;
        }
        return false;
    }

    public static void main(String[] args) {
        JFrame MainOkno = new JFrame("Projekt Dostawca");
        MainOkno.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MainOkno.setSize(768, 798);
        MainOkno.setResizable(false);

        Image mapa = new ImageIcon("Projekt/resources/map.png").getImage();
        Image pojazd = new ImageIcon("Projekt/resources/kierowcaTestowy.png").getImage();

        JPanel gra = new JPanel() {
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(mapa, 0, 0, this);
                g.drawImage(pojazd, gracz1.x, gracz1.y, this);
            }
        };
        MainOkno.addKeyListener(
            new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP -> {
                        gracz1.y -= gracz1.speed;
                        if (!onPath(gracz1.x, gracz1.y)){
                            gracz1.y += gracz1.speed;
                        }
                    }
                    case KeyEvent.VK_DOWN -> {
                        gracz1.y += gracz1.speed;
                        if (!onPath(gracz1.x, gracz1.y)){
                            gracz1.y -= gracz1.speed;
                        }
                    }
                    case KeyEvent.VK_LEFT -> {
                        gracz1.x -= gracz1.speed;
                        if (!onPath(gracz1.x, gracz1.y)){
                            gracz1.x += gracz1.speed;
                        }
                    }
                    case KeyEvent.VK_RIGHT -> {
                        gracz1.x += gracz1.speed;
                        if (!onPath(gracz1.x, gracz1.y)){
                            gracz1.x -= gracz1.speed;
                        }
                    }
                    case KeyEvent.VK_SPACE -> {
                        Boolean onSklep = false;
                        for(Sklep sklep: sklepy){
                            if(sklep.isOnSklep(gracz1))onSklep = true;
                        }
                        if(onSklep){
                            System.out.println("Jesteś w sklepie");
                        }
                    }
                }
                gra.repaint();
            }
        }
        );

        MainOkno.add(gra);
        MainOkno.setLocationRelativeTo(null);
        MainOkno.setVisible(true);
    }
}
