import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Pair;

class Plocha extends Canvas {

    /**
     * Nastavi velkost platna
     * @param sirka ,sirka platna, v pyxeloch
     * @param vyska ,vyska platna, v pyxeloch
     */
    public Plocha(double sirka, double vyska) {
        setWidth(sirka);
        setHeight(vyska);
    }

    /**
     * kresli hraciu plochu pocas hry
     */
    public void paint(){
        GraphicsContext gc = getGraphicsContext2D();
        paintHrany();

        /**
         * nakresli spojenia medzi gulickami z Shirokuro.setClicked
         */
        for(Pair<Gulicka, Gulicka> p : Shirokuro.setClicked){
            Gulicka g1 = p.getValue();
            Gulicka g2 = p.getKey();
            double x1 = g1.osX + (g1.r/2);
            double x2 = g2.osX + (g2.r/2);
            double y1 = g1.osY + (g1.r/2);
            double y2 = g2.osY + (g2.r/2);
            gc.setLineWidth(5);
            gc.setStroke(Color.ORANGE);
            gc.strokeLine(x1, y1, x2, y2);
        }
        /** nakresli vsetky gulicky */
        for(Gulicka g : Shirokuro.poleBodov){
            gc.setLineWidth(3);
            gc.setStroke(Color.BLACK);
            gc.strokeOval(g.osX, g.osY, g.r, g.r);
            gc.setFill(g.farba);
            gc.fillOval(g.osX, g.osY, g.r, g.r);
            /** ak je gulicka nastavena ako oznacena, obkresli ju na oranzovo*/
            if(g.clicked){
                gc.setStroke(Color.ORANGE);
                gc.strokeOval(g.osX, g.osY, g.r, g.r);
            }
        }
    }

    /**
     * Nakresli hraciu plochu po vyhrati hry
     */
    public void paintKoniec(){
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());
        gc.setFill(Color.GREEN);
        gc.setFont(new Font("Arial",30));
        gc.fillText("Gratulujem! Prešli ste všetky levely!",35, getHeight()/3);
    }

    /**
     * nakresli vystrahu
     */
    public void paintVystraha(){
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());
        gc.setFill(Color.RED);
        gc.setFont(new Font("Arial",20));
        gc.fillText("NEMÁTE DOKONČENÝ LEVEL !",35, getHeight()/3);
    }

    /**
     * Nakresli hrany hracej plochy
     */
    private void paintHrany(){
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());
        gc.setLineWidth(8);
        gc.setStroke(Color.BLACK);
        int x = 35 - 8;
        double posun2 = (Shirokuro.pocetBodov+1)*35;
        gc.strokeLine(x, x, x, posun2);
        gc.strokeLine(x, x, posun2, x);
        gc.strokeLine(posun2, x, posun2, posun2);
        gc.strokeLine(x, posun2, posun2, posun2);
    }
}
