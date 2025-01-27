package Projekt;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

class Kierowca {
    int x = 0;
    int y = 0;
    int speed = 32;
    ArrayList<ArrayList<String>> pakaKierowca = new ArrayList<>();
    public Kierowca(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
class Sklep{
    int x;
    int y;
    String zapotrzebowanie;
    ArrayList<ArrayList<String>> inwentarzSklep = new ArrayList<>();
    public Sklep(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public boolean isOnSklep(Kierowca kierowca) {
        return kierowca.x == this.x && kierowca.y == this.y;
    }
    public void losujZapotrzebowanie(ArrayList kategorie){
        Random rand = new Random();
        int index = rand.nextInt(kategorie.size());
        this.zapotrzebowanie = kategorie.get(index).toString();
        kategorie.remove(index);
    }
}
class Magazyn{
    int x;
    int y;
    static ArrayList<ArrayList<String>> inwentarzMagazyn = new ArrayList<>();
    public Magazyn(int x, int y) {
        this.x = x;
        this.y = y;
    };
    public boolean isOnMagazyn(Kierowca kierowca) {
        return kierowca.x == this.x && kierowca.y == this.y;
    }
    public void losujInwentarz(ArrayList<ArrayList<String>> wszystkieProdukty){
        Random rand = new Random();
        int randIndex;
        for(int i = 0; i<wszystkieProdukty.size(); i++){
            inwentarzMagazyn.add(new ArrayList<>());
            inwentarzMagazyn.get(i).add(wszystkieProdukty.get(i).getFirst());
            for(int j=0; j<10; j++){
                randIndex = rand.nextInt(wszystkieProdukty.get(i).size()-1)+1;
                inwentarzMagazyn.get(i).add(wszystkieProdukty.get(i).get(randIndex));
                wszystkieProdukty.get(i).remove(randIndex);
            }
        }
    }
}
class Init{
    static String sciezka = "Projekt/resources/produkty.dat";
    private static Sklep[] sklepy;
    private static Magazyn magazyn;
    private static Kierowca gracz;
    public static Sklep[] getSklepy() {return sklepy;}
    public static Magazyn getMagazyn() {return magazyn;}
    public static Kierowca getGracz() {return gracz;}
    static ArrayList<ArrayList<String>> ladowanieDanych(){
        try{
            BufferedReader reader = new BufferedReader(new FileReader(sciezka));
            StringBuilder daneBuilder = new StringBuilder();
            StringBuilder kat = new StringBuilder();
            String wiersz;
            while((wiersz = reader.readLine()) != null) {
                wiersz = wiersz.trim();
                if (wiersz.endsWith(";")) {
                    wiersz = wiersz.substring(0, wiersz.length() - 1);
                }
                if (!wiersz.isEmpty()) {
                    if (!wiersz.startsWith(" ") && wiersz.equals(wiersz.toUpperCase())) {
                        if (!kat.isEmpty()) {
                            daneBuilder.append(kat).append(";");
                            kat.setLength(0);
                        }
                    }
                    kat.append(wiersz).append(",");
                }
            }
            if(!kat.isEmpty())daneBuilder.append(kat);
            reader.close();
            ArrayList<ArrayList<String>> daneNowe = new ArrayList<>();
            for (String item : daneBuilder.toString().split(";")) {
                ArrayList<String> kategoria = new ArrayList<>();
                for (String element : item.split(",")) {
                    if (!element.isEmpty())kategoria.add(element.trim());
                }
                daneNowe.add(kategoria);
            }
            return daneNowe;
        }catch(IOException e) {
            System.err.println("Błąd odczytu pliku: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    public static void inicjujObiektyGry() {
        ArrayList<ArrayList<String>> wszystkieProdukty = ladowanieDanych();
        ArrayList<String> kategorie = new ArrayList<>();
        for(ArrayList<String> kategoria: wszystkieProdukty){
            kategorie.add(kategoria.getFirst());
        }
        magazyn = new Magazyn(10*64,9*64);
        magazyn.losujInwentarz(wszystkieProdukty);
        sklepy = new Sklep[]{
                new Sklep(64, 4 * 64),
                new Sklep(64, 9 * 64),
                new Sklep(6 * 64, 8 * 64),
                new Sklep(7 * 64, 5 * 64),
                new Sklep(10 * 64, 5 * 64)
        };
        for(Sklep sklep: sklepy){
            sklep.losujZapotrzebowanie(kategorie);
        }
        gracz = new Kierowca(128, 704);
    }
}
public class Main {
    static boolean onPath(int kx, int ky){
        int[][] droga = new int[][]{
                {2*64,9*64,2*64}, {64,2*64,4*64}, {2*64,6*64,5*64},
                {9*64,10*64,5*64}, {6*64,9*64,6*64}, {64,9*64,9*64},
                {9*64,10*64,9*64},{2*64,11*64,2*64}, {2*64,6*64,6*64},
                {8*64,9*64,6*64},{5*64,6*64,7*64}, {2*64,9*64,9*64}
        };
        for(int i=0; i<7; i++){
            if(kx >= droga[i][0] && kx <= droga[i][1] && ky == droga[i][2])return true;
        }
        for(int i=6; i<12; i++){
            if(ky >= droga[i][0] && ky <= droga[i][1] && kx == droga[i][2])return true;
        }
        return false;
    }
    public static void main(String[] args) {
        Init.inicjujObiektyGry();
        Magazyn magazyn = Init.getMagazyn();
        Kierowca gracz1 = Init.getGracz();
        Sklep[] sklepy = Init.getSklepy();

        System.out.println(magazyn.inwentarzMagazyn.get(0).get(0));
        System.out.println(magazyn.inwentarzMagazyn.get(0).get(1));
        System.out.println(magazyn.inwentarzMagazyn.get(1).get(0));
        System.out.println(magazyn.inwentarzMagazyn.get(1).get(1));

        Image mapa = new ImageIcon("Projekt/resources/map.png").getImage();
        Image pojazd = new ImageIcon("Projekt/resources/kierowcaTestowy.png").getImage();

        JFrame MainOkno = new JFrame("Projekt Dostawca");
        MainOkno.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MainOkno.setSize(768, 798);
        MainOkno.setResizable(false);
        JFrame MagazynOkno = new JFrame("Magazyn");
        MagazynOkno.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        MagazynOkno.setSize(500,600);
        MagazynOkno.setResizable(false);
        JFrame SklepOkno = new JFrame("Sklep");
        SklepOkno.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        SklepOkno.setSize(500,600);
        SklepOkno.setResizable(false);

        JPanel gra = new JPanel() {
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(mapa, 0, 0, this);
                g.drawImage(pojazd, gracz1.x, gracz1.y, this);
            }
        };
        JPanel panelSklepZapotrzebowanie = new JPanel();
        JScrollPane panelSklepInwentarz = new JScrollPane();
        JScrollPane panelMagazyn = new JScrollPane();
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
                        boolean onSklep = false;
                        boolean onMagazyn = false;
                        for(Sklep sklep: sklepy){
                            if(sklep.isOnSklep(gracz1))onSklep = true;
                        }
                        if (magazyn.isOnMagazyn(gracz1))onMagazyn = true;
                        if(onSklep){
                            System.out.println("Jesteś w sklepie");
                            SklepOkno.setLocationRelativeTo(MainOkno);
                            SklepOkno.setVisible(true);
                        }
                        if(onMagazyn){
                            System.out.println("Jesteś w magazynie");
                            MagazynOkno.setLocationRelativeTo(MainOkno);
                            MagazynOkno.setVisible(true);
                        }
                    }
                }
                gra.repaint();
            }
        }
        );
        SklepOkno.add(panelSklepZapotrzebowanie);
        MainOkno.add(gra);
        MainOkno.setLocationRelativeTo(null);
        MainOkno.setVisible(true);
    }
}
