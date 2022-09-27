import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;

public class Shirokuro extends Application {
    Plocha canvas;
    BorderPane root;
    Label lvl = new Label(); // v akom si lvle
    Button next = new Button("next"); //na presun do dalsieho lvla

    /** poleBodov bude obsahovat body|gulicky z hracej plochy*/
    static ArrayList<Gulicka> poleBodov = new ArrayList<>();
    /** setClicked bude obsahovat spojene body|gulicky ako pary*/
    static HashSet<Pair<Gulicka, Gulicka>> setClicked = new HashSet<>();
    /** ci uz mate zakliknutu 1. gulicku*/
    private boolean isClicked1 = false;
    /** ci uz mate zakliknutu 2. gulicku*/
    private boolean isClicked2 = false;
    /** clickedGul1 je 1. kliknuta gulicka|bod*/
    static Gulicka clickedGul1;
    /** clickedGul2 je 2. kliknuta gulicka|bod*/
    static Gulicka clickedGul2;
    /** v akom sa nachadzate levele*/
    private int level;
    /** vyska hracej plochy plochy a platna*/
    static int vyska = 600;
    /** sirka hracej plochy plochy a platna*/
    static int sirka = 600;
    /** pocet bodov na dlzku|sirku hracej plochy*/
    static double pocetBodov = 7;
    /** boolean hodnota na kontrolu ci mozete ukoncit hru*/
    private boolean ukonci = false;

    /**
     * funkcia spusta aplikaciu
     * @param stage ,plocha
     */
    @Override
    public void start(Stage stage){
        canvas = new Plocha(sirka,vyska);
        root = new BorderPane(canvas);
        level = 1;
        lvl.setText(" level "+level);
        lvl.setStyle("" +
                "-fx-font-size: 32px;\n" +
                "-fx-font-family: \"Arial Black\";"+
                "-fx-text-fill: orange;\n"+
                "-fx-effect: innershadow( three-pass-box , rgba(0,0,0,0.7) , 6, 0.0 , 0 , 2 );");
        loadMap("lvl" + level + ".txt");
        canvas.paint();

        /** po kliknuti na plochu zisti, ci ste klikli na gulicku
         * a ak ano, tak ju bud oznaci alebo vytvori spojenie*/
        canvas.setOnMouseClicked(event -> {
            double x = event.getX();
            double y = event.getY();
            for(Gulicka g : poleBodov){
                if(Math.pow(((x-g.osX)/g.r),2) + Math.pow(((y-g.osY)/g.r),2) <= 1){
                    //System.out.println("si v gulicke");
                    if(!g.clicked){
                        //System.out.println("neni kliknuta");
                        vytvorSpojenie(g);
                    }
                    else if(g.clicked){
                        //System.out.println("je kliknuta");
                        zrusSpojenie(g);
                    }
                }
            }
        } );

        /** vypise zavola znova nakreslenie plochy po 2 sekundach */
        Timeline vystraha = new Timeline(new KeyFrame(Duration.millis(2000), e ->{
            //canvas.paintVystraha();
            canvas.paint();
        }));

        next.setStyle("" +
                "-fx-background-color: \n" +
                "        #a6b5c9,\n" +
                "        linear-gradient(#303842 0%, #3e5577 20%, #375074 100%),\n" +
                "        linear-gradient(#ffe657 0%, #f8c202 50%, #eea10b 100%);\n" +
                "    -fx-background-insets: 0 0 -1 0,0,1;\n" +
                "    -fx-background-radius: 5,5,4;\n" +
                "    -fx-padding: 7 30 7 30;\n" +
                "    -fx-text-fill: #242d35;\n" +
                "    -fx-font-family: \"Helvetica\";\n" +
                "    -fx-font-size: 12px;\n" +
                "    -fx-text-fill: black;");

        /** tlacidlo na posun do dalsieho levelu
         * alebo na ukoncenie hry */
        next.setOnAction(e ->{
            if(poleBodov.size()/2 == setClicked.size() && !ukonci) {
                System.out.println("next level");
                level++;
                if(level == 7){
                    pocetBodov = 12;
                }
                else if(level >= 8){
                    pocetBodov = 14;
                }
                else {
                    pocetBodov++;
                }
                if (level <= 10) {
                    lvl.setText("level " + level);
                    vynuluj();
                    loadMap("lvl" + level + ".txt");
                    canvas.paint();
                } else {
                    canvas.paintKoniec();
                    next.setText("exit");
                    ukonci = true;
                }
            }
            else if(ukonci){
                Platform.exit();
            }
            else{
                vystraha.play();
                canvas.paintVystraha();
                //System.out.println("nemate hotovy level");
            }
        });

        HBox topPanel = new HBox(lvl,next);
        topPanel.setAlignment(Pos.CENTER_LEFT);
        topPanel.setSpacing(20);
        root.setTop(topPanel);
        root.setCenter(canvas);

        Scene scene = new Scene(root, sirka, vyska);
        stage.setScene(scene);
        stage.setTitle("Shirokuro");
        stage.show();
    }

    /**
     * vynuluje premenne ked prechadzate do dalsieho levelu
     */
    private void vynuluj(){
        poleBodov.clear();
        setClicked.clear();
        isClicked1 = false;
        isClicked2 = false;
    }

    /**
     * Vytvara spojenia s kliknutou gulickou.
     * @param g , kliknuta gulicka
     */
    private void vytvorSpojenie(Gulicka g){
        Color w = Color.WHITE;
        Color b = Color.BLACK;
        if(isClicked1){
            if(isClicked2){
                System.out.println("CHYBA V LOGIKE SPOJENIA");
            }
            else if(!isClicked2){
                if(clickedGul1.farba == w && g.farba == b){
                    if(kontrola(clickedGul1,g)){
                        isClicked2 = true;
                        clickedGul2 = g;
                        g.clicked = true;
                        nastavSpojenie(clickedGul1,clickedGul2);
                        Pair<Gulicka, Gulicka> temp = new Pair<>(clickedGul1,clickedGul2);
                        setClicked.add(temp);
                        isClicked1 = false;
                        isClicked2 = false;
                        canvas.paint();
                        System.out.println("vytvoril si spojenie");
                    }
                }
                else if(clickedGul1.farba == b && g.farba == w){
                    if(kontrola(clickedGul1,g)){
                        isClicked2 = true;
                        clickedGul2 = g;
                        g.clicked = true;
                        nastavSpojenie(clickedGul1,clickedGul2);
                        Pair<Gulicka, Gulicka> temp = new Pair<>(clickedGul1,clickedGul2);
                        setClicked.add(temp);
                        isClicked1 = false;
                        isClicked2 = false;
                        canvas.paint();
                        System.out.println("vytvoril si spojenie");
                    }
                }
            }
        }
        else if(!isClicked1){
            clickedGul1 = g;
            isClicked1 = true;
            g.clicked = true;
            canvas.paint();
            System.out.println("oznacil si gulicku 1");
        }
        //System.out.println("vonku z kontroly");
    }

    /**
     * Odznaci gulicku, ak nema vytvorene spojenie,
     * ak ma vytvorene spojenie, zrusi to spojenie.
     * @param g , kliknuta gulicka
     */
    private void zrusSpojenie(Gulicka g){
        if(clickedGul1 == g && g.spojenie == false){
            isClicked1 = false;
            g.clicked = false;
            canvas.paint();
            System.out.println("oDznacil si gulicku 1");
            return;
        }
        for(Pair<Gulicka, Gulicka> p : Shirokuro.setClicked){
            if(p.getValue().equals(g) || p.getKey().equals(g)){
                int counter = 0;
                for(Gulicka x : poleBodov){
                    if(counter == 2){
                        break;
                    }
                    else if(x.equals(p.getKey())){
                        x.clicked = false;
                        x.spojenie = false;
                        counter++;
                    }
                    else if(x.equals(p.getValue())){
                        x.clicked = false;
                        x.spojenie = false;
                        counter++;
                    }
                }
                Shirokuro.setClicked.remove(p);
                canvas.paint();
                System.out.println("spojenie zrusene");
                break;
            }
        }
    }

    /**
     * Pre @param g1 a @param g2 nastavi ich @param spojenie na true
     * @param g1 , gulicka
     * @param g2 , gulicka
     */
    private void nastavSpojenie(Gulicka g1, Gulicka g2){
        int counter = 0;
        for(Gulicka g : poleBodov){
            if(counter == 2){
                return;
            }
            else if(g.equals(g2)){
                g.spojenie = true;
                counter++;
            }
            else if(g.equals(g1)){
                g.spojenie = true;
                counter++;
            }
        }
    }

    /**
     * Kontroluje ci moze vytvorit spojenie medzi @param g1 a @param g2
     * @param g1 ,vybrana gulicka
     * @param g2 ,vybrana gulicka
     * @return hodnotu true, ak moze a false, ak nemoze
     */
    private boolean kontrola(Gulicka g1, Gulicka g2){
        if(!(g1.osX == g2.osX || g1.osY == g2.osY)){
            return false;
        }
        /** prehlada vsetky gulicky na ploche a zisti ci sa nejaky z nich nachadza
         * medzi @param g1 a @param g2
         * ak sa nachadza, @return false */
        for (Gulicka g : poleBodov){
            if(!(g.equals(g1)) && !(g.equals(g2))){
                if(g.osX == g1.osX && g.osX == g2.osX){
                    if(g2.osY > g1.osY){
                        if(g.osY > g1.osY && g.osY < g2.osY){
                            System.out.println("nemozes vytvorit spojenie cez body");
                            return false;
                        }
                    }
                    else if(g2.osY < g1.osY){
                        if(g.osY < g1.osY && g.osY > g2.osY){
                            System.out.println("nemozes vytvorit spojenie cez body");
                            return false;
                        }
                    }
                }
                else if(g.osY == g1.osY && g.osY == g2.osY){
                    if(g2.osX > g1.osX){
                        if(g.osX > g1.osX && g.osX < g2.osX){
                            System.out.println("nemozes vytvorit spojenie cez body");
                            return false;
                        }
                    }
                    else if(g2.osX < g1.osX){
                        if(g.osX < g1.osX && g.osX > g2.osX){
                            System.out.println("nemozes vytvorit spojenie cez body");
                            return false;
                        }
                    }
                }
            }
        }
        /** zisti ci by sa spojenie medzi @param g1 a @param g2 pretinalo s uz vytvorenym spojenim,
         * ak ano, tak @return false*/
        if (g1.osX == g2.osX){ /** ak su @param g1 a @param g2 vodorovne*/
            for(Pair<Gulicka, Gulicka> p : Shirokuro.setClicked){
                if(p.getKey().osY == p.getValue().osY){ //spojenie je kolmo
                    double[] intervalY = new double[]{g1.osY, g2.osY};
                    Arrays.sort(intervalY);
                    if(p.getKey().osY > intervalY[0] && p.getKey().osY < intervalY[1]){ //nachadza v intervale ich osY
                        double[] intervalX = new double[]{p.getKey().osX, p.getValue().osX};
                        Arrays.sort(intervalX);
                        if(intervalX[0] < g1.osX && intervalX[1] > g1.osX){ //pretinaju sa
                            System.out.println("spojenia sa pretinaju");
                            return false;
                        }
                    }
                }
            }
        }
        else if (g1.osY == g2.osY){ /** ak su @param g1 a @param g2 zvyslo*/
            for(Pair<Gulicka, Gulicka> p : Shirokuro.setClicked){
                if(p.getKey().osX == p.getValue().osX){ //spojenie je kolmo
                    double[] intervalX = new double[]{g1.osX, g2.osX};
                    Arrays.sort(intervalX);
                    if(p.getKey().osX > intervalX[0] && p.getKey().osX < intervalX[1]){ //nachadza v intervale ich osX
                        double[] intervalY = new double[]{p.getKey().osY, p.getValue().osY};
                        Arrays.sort(intervalY);
                        if(intervalY[0] < g1.osY && intervalY[1] > g1.osY){ //pretinaju sa
                            System.out.println("spojenia sa pretinaju");
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * Nacitava mapu zo suboru @param filePath
     * @param filePath ,nazov subory
     */
    private void loadMap(String filePath) {
        int counter = 0;
        int r = 20;
        int p = 35;
        try {
            File myObj = new File(filePath);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                counter++;
                for(int i=1 ; i < data.length()+1; i++){
                    if (data.charAt(i-1) == 'B'){
                       Gulicka g = new Gulicka(i*p,counter*p,r,Color.BLACK,false, false);
                       poleBodov.add(g);
                       System.out.println("B");
                    }
                    else  if (data.charAt(i-1) == 'W'){
                        Gulicka g = new Gulicka(i*p,counter*p,r,Color.WHITE,false, false);
                        poleBodov.add(g);
                        System.out.println("W");
                    }
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File neexistuje");
            //e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

