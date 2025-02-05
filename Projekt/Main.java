package Projekt;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

class Timer{
    boolean isRunning = false;
    long czasStartu;
    long minelo;
    long mineloSekund;
    int sekundy;
    int mineloMinut;
    public void start() {
        if(this.isRunning)return;
        this.czasStartu = System.currentTimeMillis();
        this.isRunning = true;
    }
    public int[] stop() {
        this.minelo = System.currentTimeMillis() - this.czasStartu;
        this.mineloSekund = minelo / 1000;
        this.sekundy = (int)mineloSekund % 60;
        this.mineloMinut = (int)mineloSekund / 60;s
        if(mineloMinut < 1){
            this.mineloMinut = 0;
        }
        return new int[]{mineloMinut, sekundy};
    }
}
class Kierowca {
    int x;
    int y;
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
class SklepUI{
    public static void otworzSklep(Sklep sklep, Kierowca gracz, JFrame parentFrame) {
        //Okno oraz panele sklepu i ekwipunku
        JFrame sklepOkno = new JFrame("Sklep");
        JPanel sklepLista = new JPanel();
        JPanel pakaLista = new JPanel();
        //paski przewijania dla sklepu i ekwipunku
        JScrollPane scrollSklep = new JScrollPane(sklepLista);
        JScrollPane scrollEkwipunek = new JScrollPane(pakaLista);
        //etykiety z zapotrzebowaniem dla sklepów
        JLabel zapotrzebowanieLabel = new JLabel();
        //panel przycisków oraz same przyciski
        JPanel przyciskiPanel = new JPanel();
        JButton przeniesDoEkwipunku = new JButton("→");
        JButton przeniesDoSklepu = new JButton("←");
        JButton zatwierdz = new JButton("Zatwierdź");
        //Konfiguracja elementów
        sklepOkno.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        sklepOkno.setSize(800, 600);
        sklepOkno.setLayout(new BorderLayout());
        sklepOkno.setResizable(false);
        sklepLista.setLayout(new BoxLayout(sklepLista, BoxLayout.Y_AXIS));
        pakaLista.setLayout(new BoxLayout(pakaLista, BoxLayout.Y_AXIS));
        przyciskiPanel.setLayout(new BoxLayout(przyciskiPanel, BoxLayout.Y_AXIS));
        przyciskiPanel.add(Box.createVerticalGlue());
        //Stylowanie przycisków
        Dimension rozmiar = new Dimension(100, 30);
        przeniesDoEkwipunku.setMaximumSize(rozmiar);
        przeniesDoEkwipunku.setFont(new Font("Arial", Font.PLAIN, 16));
        przeniesDoSklepu.setMaximumSize(rozmiar);
        przeniesDoSklepu.setFont(new Font("Arial", Font.PLAIN, 16));
        zatwierdz.setMaximumSize(rozmiar);
        zatwierdz.setFont(new Font("Arial", Font.PLAIN, 12));
        //Dodanie przycisków do panelu
        przyciskiPanel.add(przeniesDoEkwipunku);
        przyciskiPanel.add(przeniesDoSklepu);
        przyciskiPanel.add(zatwierdz);
        przyciskiPanel.add(Box.createVerticalGlue());
        //konfiguracja etykiety
        zapotrzebowanieLabel.setText(sklep.zapotrzebowanie);
        zapotrzebowanieLabel.setHorizontalAlignment(JLabel.CENTER);
        //Dodawanie elementów sklepu do panelu
        for (ArrayList<String> kategoria : sklep.inwentarzSklep) {
            String nazwaKategorii = kategoria.getFirst();
            for (int i = 1; i < kategoria.size(); i++) {
                String przedmiot = kategoria.get(i);
                JCheckBox checkBox = new JCheckBox(nazwaKategorii + ": " + przedmiot);
                sklepLista.add(checkBox);
            }
        }
        //Dodawanie elementów ekwipunku gracza do panelu
        for (ArrayList<String> kategoria : gracz.pakaKierowca) {
            String nazwaKategorii = kategoria.getFirst();
            for (int i = 1; i < kategoria.size(); i++) {
                String przedmiot = kategoria.get(i);
                JCheckBox checkBox = new JCheckBox(nazwaKategorii + ": " + przedmiot);
                pakaLista.add(checkBox);
            }
        }
        //Zastąpienie pustego panelu placeholderem, jeśli brak elementów
        if (pakaLista.getComponentCount() == 0) {
            JLabel placeholder = new JLabel("Ekwipunek pusty");
            pakaLista.add(placeholder);
        }
        if (sklepLista.getComponentCount() == 0) {
            JLabel placeholder = new JLabel("Sklep pusty");
            sklepLista.add(placeholder);
        }

        // Obsługa przenoszenia elementów do ekwipunku gracza
        przeniesDoEkwipunku.addActionListener(e -> {
            Component[] checkBoxy = sklepLista.getComponents();
            for (Component komponent : checkBoxy) {
                if (pakaLista.getComponentCount() > 0 && pakaLista.getComponent(0) instanceof JLabel) {
                    pakaLista.remove(0);
                }
                if (komponent instanceof JCheckBox checkBox && checkBox.isSelected()) {
                    sklepLista.remove(checkBox);
                    pakaLista.add(checkBox);
                    checkBox.setSelected(false);
                }
                if (sklepLista.getComponentCount() == 0) {
                    JLabel placeholder = new JLabel("Sklep pusty");
                    sklepLista.add(placeholder);
                }
            }
            sklepLista.revalidate();
            sklepLista.repaint();
            pakaLista.revalidate();
            pakaLista.repaint();
        });
        //Obsługa przenoszenia elementów do magazynu
        przeniesDoSklepu.addActionListener(e -> {
            Component[] checkBoxy = pakaLista.getComponents();
            for (Component komponent : checkBoxy) {
                if (sklepLista.getComponentCount() > 0 && sklepLista.getComponent(0) instanceof JLabel) {
                    sklepLista.remove(0);
                }
                if (komponent instanceof JCheckBox checkBox && checkBox.isSelected()) {
                    pakaLista.remove(checkBox);
                    sklepLista.add(checkBox);
                    checkBox.setSelected(false);
                }
                if (pakaLista.getComponentCount() == 0) {
                    JLabel placeholder = new JLabel("Ekwipunek pusty");
                    pakaLista.add(placeholder);
                }
            }
            sklepLista.revalidate();
            sklepLista.repaint();
            pakaLista.revalidate();
            pakaLista.repaint();
        });
        //Zatwierdzanie zmian w magazynie i ekwipunku
        zatwierdz.addActionListener(e -> {
            sklep.inwentarzSklep.clear();
            for (Component komponent : sklepLista.getComponents()) {
                if (komponent instanceof JCheckBox checkBox) {
                    String item = checkBox.getText();
                    String kategoria = item.split(":")[0].trim();
                    String przedmiot = item.split(":")[1].trim();
                    boolean dodano = false;
                    for (ArrayList<String> kategoriaLista : sklep.inwentarzSklep) {
                        if (kategoriaLista.getFirst().equals(kategoria)) {
                            kategoriaLista.add(przedmiot);
                            dodano = true;
                            break;
                        }
                    }
                    if (!dodano) {
                        ArrayList<String> nowaKategoria = new ArrayList<>();
                        nowaKategoria.add(kategoria);
                        nowaKategoria.add(przedmiot);
                        sklep.inwentarzSklep.add(nowaKategoria);
                    }
                }
            }
            gracz.pakaKierowca.clear();
            for (Component komponent : pakaLista.getComponents()) {
                if (komponent instanceof JCheckBox checkBox) {
                    String item = checkBox.getText();
                    String kategoria = item.split(":")[0].trim();
                    String przedmiot = item.split(":")[1].trim();

                    boolean dodano = false;
                    for (ArrayList<String> kategoriaLista : gracz.pakaKierowca) {
                        if (kategoriaLista.getFirst().equals(kategoria)) {
                            kategoriaLista.add(przedmiot);
                            dodano = true;
                            break;
                        }
                    }
                    if (!dodano) {
                        ArrayList<String> nowaKategoria = new ArrayList<>();
                        nowaKategoria.add(kategoria);
                        nowaKategoria.add(przedmiot);
                        gracz.pakaKierowca.add(nowaKategoria);
                    }
                }
            }
            sklepOkno.dispose();
        });
        sklepOkno.add(zapotrzebowanieLabel, BorderLayout.NORTH);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        scrollSklep.setPreferredSize(new Dimension(100, 200));
        mainPanel.add(scrollSklep);
        przyciskiPanel.setPreferredSize(new Dimension(100, 200));
        mainPanel.add(przyciskiPanel);
        scrollEkwipunek.setPreferredSize(new Dimension(100, 200));
        mainPanel.add(scrollEkwipunek);

        sklepOkno.add(mainPanel);
        sklepOkno.setLocationRelativeTo(parentFrame);
        sklepOkno.setVisible(true);
    }
}
class Magazyn{
    int x;
    int y;
    static ArrayList<ArrayList<String>> inwentarzMagazyn = new ArrayList<>();
    public Magazyn(int x, int y) {
        this.x = x;
        this.y = y;
    }
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
class MagazynUI {
    public static void otworzMagazyn(Magazyn magazyn, Kierowca gracz, JFrame parentFrame) {
        Init.timer.start();
        JFrame magazynOkno = new JFrame("Magazyn");
        magazynOkno.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        magazynOkno.setSize(800, 600);
        magazynOkno.setLayout(new BorderLayout());
        magazynOkno.setResizable(false);
        //Tworzenie paneli magazynu i ekwipunku
        JPanel magazynLista = new JPanel();
        JPanel pakaLista = new JPanel();
        magazynLista.setLayout(new BoxLayout(magazynLista, BoxLayout.Y_AXIS));
        pakaLista.setLayout(new BoxLayout(pakaLista, BoxLayout.Y_AXIS));
        //Dodawanie elementów magazynu do panelu
        for (ArrayList<String> kategoria : magazyn.inwentarzMagazyn) {
            String nazwaKategorii = kategoria.getFirst();
            for (int i = 1; i < kategoria.size(); i++) {
                String przedmiot = kategoria.get(i);
                JCheckBox checkBox = new JCheckBox(nazwaKategorii + ": " + przedmiot);
                magazynLista.add(checkBox);
            }
        }
        //Dodawanie elementów ekwipunku gracza do panelu
        for (ArrayList<String> kategoria : gracz.pakaKierowca) {
            String nazwaKategorii = kategoria.getFirst();
            for (int i = 1; i < kategoria.size(); i++) {
                String przedmiot = kategoria.get(i);
                JCheckBox checkBox = new JCheckBox(nazwaKategorii + ": " + przedmiot);
                pakaLista.add(checkBox);
            }
        }
        //Zastąpienie pustego panelu placeholderem, jeśli brak elementów
        if (pakaLista.getComponentCount() == 0) {
            JLabel placeholder = new JLabel("Ekwipunek pusty");
            pakaLista.add(placeholder);
        }
        if (magazynLista.getComponentCount() == 0) {
            JLabel placeholder = new JLabel("Magazyn pusty");
            magazynLista.add(placeholder);
        }
        //Dodanie pasków przewijania dla magazynu i ekwipunku
        JScrollPane scrollMagazyn = new JScrollPane(magazynLista);
        JScrollPane scrollEkwipunek = new JScrollPane(pakaLista);
        //Tworzenie panelu przycisków
        JPanel przyciskiPanel = new JPanel();
        przyciskiPanel.setLayout(new BoxLayout(przyciskiPanel, BoxLayout.Y_AXIS));
        przyciskiPanel.add(Box.createVerticalGlue());
        //Tworzenie przycisków
        JButton przeniesDoEkwipunku = new JButton("→");
        JButton przeniesDoMagazynu = new JButton("←");
        JButton zatwierdz = new JButton("Zatwierdź");
        //Stylowanie przycisków
        Dimension rozmiar = new Dimension(100, 30);
        przeniesDoEkwipunku.setMaximumSize(rozmiar);
        przeniesDoEkwipunku.setFont(new Font("Arial", Font.PLAIN, 16));

        przeniesDoMagazynu.setMaximumSize(rozmiar);
        przeniesDoMagazynu.setFont(new Font("Arial", Font.PLAIN, 16));

        zatwierdz.setMaximumSize(rozmiar);
        zatwierdz.setFont(new Font("Arial", Font.PLAIN, 12));

        // Obsługa przenoszenia elementów do ekwipunku gracza
        przeniesDoEkwipunku.addActionListener(e -> {
            Component[] checkBoxy = magazynLista.getComponents();
            for (Component komponent : checkBoxy) {
                if (pakaLista.getComponentCount() > 0 && pakaLista.getComponent(0) instanceof JLabel) {
                    pakaLista.remove(0);
                }
                if (komponent instanceof JCheckBox checkBox && checkBox.isSelected()) {
                    magazynLista.remove(checkBox);
                    pakaLista.add(checkBox);
                    checkBox.setSelected(false);
                }
                if (magazynLista.getComponentCount() == 0) {
                    JLabel placeholder = new JLabel("Magazyn pusty");
                    magazynLista.add(placeholder);
                }
            }
            magazynLista.revalidate();
            magazynLista.repaint();
            pakaLista.revalidate();
            pakaLista.repaint();
        });
        //Obsługa przenoszenia elementów do magazynu
        przeniesDoMagazynu.addActionListener(e -> {
            Component[] checkBoxy = pakaLista.getComponents();
            for (Component komponent : checkBoxy) {
                if (magazynLista.getComponentCount() > 0 && magazynLista.getComponent(0) instanceof JLabel) {
                    magazynLista.remove(0);
                }
                if (komponent instanceof JCheckBox checkBox && checkBox.isSelected()) {
                    pakaLista.remove(checkBox);
                    magazynLista.add(checkBox);
                    checkBox.setSelected(false);
                }
                if (pakaLista.getComponentCount() == 0) {
                    JLabel placeholder = new JLabel("Ekwipunek pusty");
                    pakaLista.add(placeholder);
                }
            }
            magazynLista.revalidate();
            magazynLista.repaint();
            pakaLista.revalidate();
            pakaLista.repaint();
        });
        //Zatwierdzanie zmian w magazynie i ekwipunku
        zatwierdz.addActionListener(e -> {
            magazyn.inwentarzMagazyn.clear();
            for (Component komponent : magazynLista.getComponents()) {
                if (komponent instanceof JCheckBox checkBox) {
                    String item = checkBox.getText();
                    String kategoria = item.split(":")[0].trim();
                    String przedmiot = item.split(":")[1].trim();
                    boolean dodano = false;
                    for (ArrayList<String> kategoriaLista : magazyn.inwentarzMagazyn) {
                        if (kategoriaLista.getFirst().equals(kategoria)) {
                            kategoriaLista.add(przedmiot);
                            dodano = true;
                            break;
                        }
                    }
                    if (!dodano) {
                        ArrayList<String> nowaKategoria = new ArrayList<>();
                        nowaKategoria.add(kategoria);
                        nowaKategoria.add(przedmiot);
                        magazyn.inwentarzMagazyn.add(nowaKategoria);
                    }
                }
            }
            gracz.pakaKierowca.clear();
            for (Component komponent : pakaLista.getComponents()) {
                if (komponent instanceof JCheckBox checkBox) {
                    String item = checkBox.getText();
                    String kategoria = item.split(":")[0].trim();
                    String przedmiot = item.split(":")[1].trim();

                    boolean dodano = false;
                    for (ArrayList<String> kategoriaLista : gracz.pakaKierowca) {
                        if (kategoriaLista.getFirst().equals(kategoria)) {
                            kategoriaLista.add(przedmiot);
                            dodano = true;
                            break;
                        }
                    }
                    if (!dodano) {
                        ArrayList<String> nowaKategoria = new ArrayList<>();
                        nowaKategoria.add(kategoria);
                        nowaKategoria.add(przedmiot);
                        gracz.pakaKierowca.add(nowaKategoria);
                    }
                }
            }
            magazynOkno.dispose();
        });
        //Dodanie przycisków do panelu
        przyciskiPanel.add(przeniesDoEkwipunku);
        przyciskiPanel.add(przeniesDoMagazynu);
        przyciskiPanel.add(zatwierdz);
        przyciskiPanel.add(Box.createVerticalGlue());
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        scrollMagazyn.setPreferredSize(new Dimension(100, 200));
        mainPanel.add(scrollMagazyn);
        przyciskiPanel.setPreferredSize(new Dimension(100, 200));
        mainPanel.add(przyciskiPanel);
        scrollEkwipunek.setPreferredSize(new Dimension(100, 200));
        mainPanel.add(scrollEkwipunek);

        magazynOkno.add(mainPanel);
        magazynOkno.setLocationRelativeTo(parentFrame);
        magazynOkno.setVisible(true);
    }
}
class EndGame{
    public static void Zakoncz(JFrame parentFrame, Sklep[] sklepy, Kierowca gracz){
        int przyciskYesNo = JOptionPane.YES_NO_OPTION;
        int odpowiedz = JOptionPane.showConfirmDialog(parentFrame, "Czy chcesz zakończyć grę?", "Koniec gry", przyciskYesNo);
        int wynik = 0;
        if(odpowiedz == 0 && gracz.pakaKierowca.isEmpty()) {
            int[] czas = new int[2];
            for(Sklep sklep: sklepy){
                int poprawny = 0;
                for(ArrayList<String> kategoria : sklep.inwentarzSklep){
                    if(kategoria.getFirst().equals(sklep.zapotrzebowanie)){
                        for(int i = 1; i < kategoria.size(); i++){
                            poprawny++;
                        }
                    }
                }
                if(poprawny == 10)wynik++;
            }
            czas = Init.timer.stop();
            String rezultat="";
            if(wynik==0)rezultat="Oj słabo, słabo,...\nZawiodłeś swoich klientów i zostałeś zwolniony w "+czas[0]+" minut i "+czas[1]+" sekund";
            if(wynik>0)rezultat="Mogło pójść lepiej...\nPoprawnie dostarczyłeś towary do "+wynik+" na "+ sklepy.length+" sklepach w "+czas[0]+" minut i "+czas[1]+" sekund!";
            if(wynik==5)rezultat="Brawo!\nPoprawnie dostarczyłeś towary do wszystkich sklepów w "+czas[0]+" minut i "+czas[1]+" sekund!";

            int e = JOptionPane.showOptionDialog(parentFrame,
                    rezultat,
                    "Wynik końcowy",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    new Object[]{"Zamknij"},
                    "Zamknij"
                    );
            if(e==0)System.exit(0);
        }else if(!gracz.pakaKierowca.isEmpty()){
            JOptionPane.showMessageDialog(parentFrame,"Aby poznać wynik końcowy opróżnij pojazd!","Opróżnij pakę!",JOptionPane.ERROR_MESSAGE);
        }
    }
}
class Rules{
    public static void Zasady(JFrame parentFrame){
        JFrame oknoZasady = new JFrame("Zasady");
        JLabel tytul = new JLabel("ZASADY GRY:");
        JTextArea opis = new JTextArea(
                        "\n" +
                        "    Twoim zadaniem jest rozwieźć przedmioty z magazynu\n" +
                        "    do sklepów według zapotrzebowania.\n" +
                        "    W każdym sklepie powinno znaleźć się 10 przedmiotów\n" +
                        "    z kategorii widocznej na górze okna otwartego sklepu.\n" +
                        "    Aby sprawdzić jak Ci poszło musisz mieć pusty pojazd\n" +
                        "    i nacisnąć ESC."
        );
        JTextArea poruszanie = new JTextArea(
                "    Poruszanie: \n" +
                "    ↑ - jedź w górę\n" +
                "    ← - jedź w lewo\n" +
                "    → - jedź w prawo\n" +
                "    ↓ - jedź w dół\n" +
                "    spacja - interakcja\n" +
                "    ESC - sprawdzenie wyniku"
        );

        tytul.setFont(new Font("Sans", Font.BOLD, 24));
        tytul.setAlignmentX(Component.CENTER_ALIGNMENT);
        opis.setFont(new Font("Sans", Font.PLAIN, 20));
        opis.setLineWrap(true);
        opis.setEditable(false);
        poruszanie.setFont(new Font("Sans", Font.PLAIN, 20));
        poruszanie.setAlignmentX(Component.CENTER_ALIGNMENT);
        poruszanie.setLayout(new BoxLayout(poruszanie, BoxLayout.Y_AXIS));
        poruszanie.setEditable(false);
        oknoZasady.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        oknoZasady.setSize(600, 500);
        oknoZasady.setLayout(new BorderLayout());
        oknoZasady.setResizable(false);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(tytul);
        panel.add(opis);
        panel.add(poruszanie);

        oknoZasady.add(panel);
        oknoZasady.setLocationRelativeTo(parentFrame);
        oknoZasady.setVisible(true);
    }
}
class Init{
    static String sciezka = "Projekt/resources/produkty.dat";
    static Timer timer;
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
        timer = new Timer();
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
                {9*64,10*64,9*64}, {2*64,11*64,2*64}, {2*64,6*64,6*64},
                {8*64,9*64,6*64}, {5*64,6*64,7*64}, {2*64,9*64,9*64}
        };
        for(int i=0; i<7; i++){//Sprawdzenie, czy gracz znajduje się na drogach poziomych
            if(kx >= droga[i][0] && kx <= droga[i][1] && ky == droga[i][2])return true;
        }
        for(int i=7; i<12; i++){//Sprawdzenie, czy gracz znajduje się na drogach pionowych
            if(ky >= droga[i][0] && ky <= droga[i][1] && kx == droga[i][2])return true;
        }
        return false;
    }
    public static void main(String[] args) {
        Init.inicjujObiektyGry();
        Magazyn magazyn = Init.getMagazyn();
        Kierowca gracz1 = Init.getGracz();
        Sklep[] sklepy = Init.getSklepy();

        Image mapa = new ImageIcon("Projekt/resources/map.png").getImage();
        Image pojazd = new ImageIcon("Projekt/resources/kierowcaTestowy.png").getImage();

        JFrame MainOkno = new JFrame("Projekt Dostawca");
        MainOkno.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MainOkno.setSize(768, 798);
        MainOkno.setResizable(false);

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
                            for(Sklep sklep: sklepy){
                                if(sklep.isOnSklep(gracz1)){
                                    SklepUI.otworzSklep(sklep,gracz1,MainOkno);
                                    for(ArrayList<String> kategoria : sklep.inwentarzSklep){
                                        System.out.println(kategoria);
                                    }
                                }
                            }
                            if (magazyn.isOnMagazyn(gracz1)){
                                MagazynUI.otworzMagazyn(magazyn, gracz1, MainOkno);
                            }
                        }
                        case KeyEvent.VK_ESCAPE -> {
                            EndGame.Zakoncz(MainOkno, sklepy, gracz1);
                        }
                    }
                    gra.repaint();
                }
            }
        );
        MainOkno.add(gra);
        MainOkno.setLocationRelativeTo(null);
        MainOkno.setVisible(true);
        Rules.Zasady(MainOkno);
    }
}
