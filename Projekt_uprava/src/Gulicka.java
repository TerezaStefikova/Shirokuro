import javafx.scene.paint.Color;

class Gulicka{
    public double osX;
    public double osY;
    public double r;
    public Color farba;
    boolean clicked;
    boolean spojenie;

    /**
     * @param osX ,suradnice pre os X, v pyxeloch
     * @param osY ,suradnice pre os Y, v pyxeloch
     * @param r ,velkost polomeru gulicky, v pyxeloch
     * @param farba ,je farba gulicky
     * @param clicked ,boolean hodnota ci je gulicka oznacena
     * @param spojenie ,boolean hodnota ci uz je gulicka spojenia s nejako dalsou
     */

    public Gulicka(double osX, double osY, double r, Color farba, boolean clicked, boolean spojenie) {
        this.osX = osX;
        this.osY = osY;
        this.r = r;
        this.farba = farba;
        this.clicked = clicked;
        this.spojenie = spojenie;
    }
}
