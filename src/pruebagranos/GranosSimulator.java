/**
 * Titulo del proyecto: Simulador de granos Clase : GranosSimulator > Esta clase
 * extiende JComponent , implementa MouseListener , MouseMotionListener y
 * Runnable Esta es la clase que va a controlar la creacion de granos, de que
 * tipo son estos, pintará la imagen del personaje granudo y los propios granos,
 * controlará donde salen y donde no pueden salir los granos, realizara
 * distintas acciones con los listener que se han añadido, y por ultimo aplicará
 * la mascarilla al personaje.
 *
 * En esta clase emplearemos semaforos.
 *
 * @version 1.5
 */
package pruebagranos;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JComponent;

/**
 *
 * @author Cristina Sicre & Julio Mansilla
 */
public class GranosSimulator extends JComponent implements MouseListener, MouseMotionListener, Runnable {

    private int velocidadGranos;
    ArrayList<Barrillo> barrillos = new ArrayList<Barrillo>();
    BufferedImage imagen;
    BufferedImage granoImage;
    BufferedImage grano2Image;
    boolean mascarillaAplicada;
    Manos manos = new Manos(0, 0, this);
    Semaphore semaforo = new Semaphore(1);

    /**
     * Constructor de la clase > añade a la clase dos Listeners. define imagenes
     * para los dos tipos de granos.
     */
    public GranosSimulator() {
        addMouseListener(this);
        addMouseMotionListener(this);
        try {

            granoImage = ImageIO.read(this.getClass().getResourceAsStream("/pruebagranos/grano.png"));
            grano2Image = ImageIO.read(this.getClass().getResourceAsStream("/pruebagranos/grano2.png"));
            for (int i = 0; i < cara.length; i++) {

                cara[i] = ImageIO.read(this.getClass().getResourceAsStream("/pruebagranos/RodolfoCaraPestañeo" + i + ".png"));

            }
            mascarilla = ImageIO.read(this.getClass().getResourceAsStream("/pruebagranos/MascarillaRodolfo.png"));

        } catch (IOException ex) {
            Logger.getLogger(GranosSimulator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setVelocidadGranos(String v) {
        this.velocidadGranos = Integer.parseInt(v);
    }

    public int getVelocidadGranos() {
        return velocidadGranos;
    }

    private BufferedImage[] cara = new BufferedImage[6];
    private BufferedImage mascarilla;

    /**
     * Metodo paint > Encargado de pintar y repintar el rostro del personaje,
     * los granos y la mascarilla facial.
     *
     * @param graphics dibuja la imagen en el JComponent Dependiendo del tipo de
     * grano pinta una imagen u otra de granos sobre el JComponent Por ultimo
     * comprueba si la mascarilla está aplicada y si es así dibuja la mascarilla
     * sobre el JComponent.
     */
    public void paint(Graphics graphics) {

        graphics.drawImage(cara[imagenActual], 0, 0, getWidth(), getHeight(), this);

        Barrillo[] barrillo = getBarrillos();
        for (int o = 0; o < barrillo.length; o++) {
            Barrillo b = barrillo[o];
            if (b == null) {
                continue;
            }
            if (b.tipo.equals("grano")) {
                graphics.drawImage(granoImage, b.getX(), b.getY(), b.getAncho(), b.getAlto(), this);
            } else {
                graphics.drawImage(grano2Image, b.getX(), b.getY(), b.getAncho(), b.getAlto(), this);
            }
        }

        if (mascarillaAplicada) {

            graphics.drawImage(mascarilla, 0, 0, getWidth(), getHeight(), this);

        }

        manos.pintar(graphics);
    }

    /**
     * Metodo generarGrano > Se encarga de crear granos, para ello empleamos la
     * clase random, asignando de esta forma valores aleatorios para cada
     * objeto. También hay que destacar que se asignará aleatoriamente un
     * maxAlto y maxAncho diferente para cada grano, algunos crecerán más que
     * otros, dando más realismo a la simulación. Después de dar valores al
     * grano será añadido al ArrayList, repintamos y iniciamos el hilo de ese
     * grano.
     *
     * @see #comprobarRectangulos(int, int) si hay un rectangulo dibujado en esa
     * misma posicion se vuelve a generar una nueva posicion para ese grano
     * hasta que deje de coincidir con la x e y del rectangulo.
     * @return Barrillo
     */
    public Barrillo generarGrano() {

        Random rand = new Random();

        int x = (int) (0.28 * getWidth()) + rand.nextInt((int) (0.45 * getWidth()));
        int y = (int) (0.34 * getHeight()) + rand.nextInt((int) (0.55 * getHeight()));
        int maxAncho = rand.nextInt(20);
        int maxAlto = maxAncho;
        while (comprobarRectangulos(x, y) == true) {
            x = (int) (0.28 * getWidth()) + rand.nextInt((int) (0.45 * getWidth()));
            y = (int) (0.34 * getHeight()) + rand.nextInt((int) (0.55 * getHeight()));
        }
        Barrillo barrillo;
        if (rand.nextInt(100) <= 10) {
            barrillo = new Barrillo(x, y, "grano2", maxAncho, maxAlto, this);
        } else {
            barrillo = new Barrillo(x, y, "grano", maxAncho, maxAlto, this);
        }
        barrillos.add(barrillo);
        repaint(); //vuelve a pintar todos los componentes
        barrillo.start();
        return barrillo;

    }

    /**
     * Metodo comprobarRectangulos > ComprobarRectangulos se encarga de crear
     * rectangulos , y si ese rectagulo coincide la x y la y pasadas por
     * parametros de entrada entonces devuelve true.
     *
     * @param x hace referencia a la posicion x del grano @see #generarGrano()
     * @param y hace referencia a la posicion y del grano @see #generarGrano()
     * @return true si la posicion de x e y coinciden con las pasadas por
     * parametros de entrada.
     */
    public boolean comprobarRectangulos(int x, int y) {
        Rectangle[] rectangulos = new Rectangle[3];
        Rectangle rectanguloOjoIzq = new Rectangle((int) (0.31 * getWidth()), (int) (0.52 * getHeight()), (int) (0.135 * getWidth()), (int) (0.15 * getHeight()));
        Rectangle rectanguloOjoDerch = new Rectangle((int) (0.55 * getWidth()), (int) (0.52 * getHeight()), (int) (0.13 * getWidth()), (int) (0.15 * getHeight()));
        Rectangle rectanguloBoca = new Rectangle((int) (0.40 * getWidth()), (int) (0.70 * getHeight()), (int) (0.20 * getWidth()), (int) (0.13 * getHeight()));
        rectangulos[0] = rectanguloOjoIzq;
        rectangulos[1] = rectanguloOjoDerch;
        rectangulos[2] = rectanguloBoca;
        for (Rectangle r : rectangulos) {
            if (r.contains(x, y)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    /**
     * Metodo mouseReleased > Se activa cuando sueltas el click Izquierdo del
     * ratón, elimina el grano de la posicion cliqueada y despues llama a
     * repaint.
     *
     * @param e
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        Rectangle rect = new Rectangle(e.getX(), e.getY(), 1, 1);
        Barrillo[] prebarrillo = barrillos.toArray(new Barrillo[0]);
        Barrillo barrilloAExplotar = null;
        for (Barrillo bar : prebarrillo) {
            if (bar.devolverRectangle().intersects(rect)) {
                barrilloAExplotar = bar;
                repaint();
            }
        }
        mouseMoved(e);
        manos.push(barrilloAExplotar);
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        manos.asignarPosicion(e.getX(), e.getY());
        repaint();

    }

    private int tiempoParaParpadeo = 10;
    private int intervaloParpadeo = 0;

    private int velocidadParpadeo = 150;
    private int imagenActual;

    /**
     * Metodo run de Thread duerme al hilo un tiempo aleatorio usando
     * Random.class adquiere el semaforo, llama a generar grano y luego suelta
     * el semaforo. Si el semaforo está adquirido por otro hilo, entonces espera
     * a que pueda adquirir los permisos para crear un nuevo grano.
     */
    @Override
    public void run() {
        Random rand = new Random();
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    if (intervaloParpadeo <= 0) {

                        if (imagenActual < 6) {
                            try {
                                Thread.sleep(velocidadParpadeo);
                                imagenActual++;
                                repaint();
                            } catch (InterruptedException ex) {
                                Logger.getLogger(GranosSimulator.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } else {
                            imagenActual = 0;
                            intervaloParpadeo = tiempoParaParpadeo;
                        }
                    } else {
                        intervaloParpadeo--;
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(GranosSimulator.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        }.start();
        try {
            while (true) {
                Thread.sleep(rand.nextInt(velocidadGranos));
                semaforo.acquire();
                generarGrano();
                semaforo.release();
            }

        } catch (InterruptedException ex) {
            Logger.getLogger(GranosSimulator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Metodo aplicarMascarilla > sí la mascarila no está aplicada, recorre el
     * ArrayList y va combiando el valor de la variable mascarillaAplicada a
     * true en cada grano. más tarde pone la variable booleana
     * MascarillaAplicada de esta clase a true y llama al metodo repaint. se
     * adquiere el semaforo, crea un nuevo Thread como clase anonima y en su
     * metodo run lo dormimos durante 5 segundos despues de ese tiempo se quita
     * la mascarilla, y se desotorgan las permisos del semaforo. inciamos el
     * Thread .
     */
    public void aplicarMascarilla() throws InterruptedException {

        if (!mascarillaAplicada) {
            for (Barrillo barrillo : barrillos) {
                barrillo.mascarillaAplicada = true;
            }
            mascarillaAplicada = true;
            repaint();

            semaforo.acquire();
            Thread th = new Thread() {
                public void run() {
                    try {
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(GranosSimulator.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        Thread.sleep(4000);
                        semaforo.release();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(GranosSimulator.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    mascarillaAplicada = false;
                }

            };
            th.start();
        }

    }

    public synchronized Barrillo[] getBarrillos() {
        return barrillos.toArray(new Barrillo[0]);
    }

    public synchronized void eliminarBarrillo(Barrillo barrillo) {
        barrillos.remove(barrillo);
    }

}
