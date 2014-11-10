/**
 * Titulo del proyecto: Simulador de granos Clase: Barrillo > Clase que almacena
 * parametros con las propiedades necesarias para la creación de los granos.
 * Esta clase extiende de Thread.
 *
 * @version 1.5
 */
package pruebagranos;

import java.awt.Rectangle;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Cristina Sicre & Julio Mansilla
 */
public class Barrillo extends Thread {

    int x;
    int y;
    int alto;
    int ancho;
    String tipo;
    int maxAncho;
    int maxAlto;
    GranosSimulator gs;
    boolean mascarillaAplicada;

    /**
     * Metodo run de Thread Mientras el @ancho y el @alto sean menores del
     * @maxAncho y el @maxAlto, va aumentando su tamaño si la mascarilla no está
     * aplicada. duerme el hilo un tiempo aleatorio.
     *
     * @see gs.repaint() repinta el grano con cada cambio de propiedades. Una
     * vez alcanza el tamaño maximo posible, se mantiene 5 segundos con esas
     * dimensiones antes de volver a entrar en un bucle donde se vaya reduciendo
     * poco a poco hasta llegar a 0.
     */
    public void run() {
        Random ran = new Random();
        while (ancho < maxAncho && alto < maxAlto) {
            try {
                if (mascarillaAplicada == false) {
                    Thread.sleep(500 + ran.nextInt(3000));
                    ancho++;
                    alto++;
                    gs.repaint();
                } else {
                    break;
                }

            } catch (InterruptedException ex) {
                Logger.getLogger(Barrillo.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        try {
            Thread.sleep(5000);

            while (ancho > 0 && alto > 0) {
                Thread.sleep(500 + ran.nextInt(3000));
                ancho--;
                alto--;
                gs.repaint();
            }
            gs.eliminarBarrillo(this);

        } catch (InterruptedException ex) {
            Logger.getLogger(Barrillo.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Constructor de la clase
     *
     * @param x
     * @param y
     * @param tipo
     * @param maxAncho
     * @param maxAlto
     * @param gs @see GranosSimulator.class > recibe un objeto de la clase
     * GranosSimulator
     */
    public Barrillo(int x, int y, String tipo, int maxAncho, int maxAlto, GranosSimulator gs) {
        this.x = x;
        this.y = y;
        this.alto = alto;
        this.ancho = ancho;
        this.tipo = tipo;
        this.maxAncho = maxAncho;
        this.maxAlto = maxAlto;
        this.gs = gs;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getMaxAncho() {
        return maxAncho;
    }

    public void setMaxAncho(int maxAncho) {
        this.maxAncho = maxAncho;
    }

    public int getMaxAlto() {
        return maxAlto;
    }

    public void setMaxAlto(int maxAlto) {
        this.maxAlto = maxAlto;
    }

    public int getAlto() {
        return alto;
    }

    public void setAlto(int alto) {
        this.alto = alto;
    }

    public int getAncho() {
        return ancho;
    }

    public void setAncho(int ancho) {
        this.ancho = ancho;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    /**
     * Crea un nuevo objeto Rectangle, le asigna unas propiedades y despues lo
     * devuelve.
     *
     * @return Rectangle
     */
    public Rectangle devolverRectangle() {

        Rectangle rect = new Rectangle(x, y, ancho, alto);
        return rect;

    }
}
