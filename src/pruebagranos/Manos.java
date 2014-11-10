/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pruebagranos;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Cristina Sicre & Julio Mansilla
 */
public class Manos extends Thread {

    int x;
    int y;
    boolean explotandoBarrillo = false;
    BufferedImage imagenDerecha;
    BufferedImage imagenIzquierda;
    GranosSimulator gs;
    Barrillo barrillo;
    private Semaphore semaphore = new Semaphore ( 0 );

    public Manos(int x, int y, GranosSimulator gs) {
        try {
            this.x = x;
            this.y = y;
            this.gs = gs;

            imagenDerecha = ImageIO.read(this.getClass().getResourceAsStream("/pruebagranos/manoDerecha.png"));
            imagenIzquierda = ImageIO.read(this.getClass().getResourceAsStream("/pruebagranos/manoIzquierda.png"));
            this.start();
        } catch (IOException ex) {
            Logger.getLogger(Manos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void asignarPosicion(int x, int y) {

        if (!explotandoBarrillo) {
            this.x = x;
            this.y = y;
        }
    }

    private int offsetX = -150;
    private int offsetY = -80;
    private int distanciaMaxima = 200;
    private int distanciaActual = distanciaMaxima;

    public void pintar(Graphics graphics) {
        graphics.drawImage(imagenDerecha, x - distanciaActual + offsetX, y + offsetY, 300, 300, null);
        graphics.drawImage(imagenIzquierda, x + distanciaActual + offsetX, y + offsetY, 300, 300, null);

    }

    public synchronized void push(Barrillo barrillo) {

        if (explotandoBarrillo == false) {
            
            explotandoBarrillo = true;
            this.barrillo = barrillo;
            semaphore.release();
        }
    }

    public void run() {
        while ( true )
        {
            try {
                semaphore.acquire();
            } catch (InterruptedException ex) {
                Logger.getLogger(Manos.class.getName()).log(Level.SEVERE, null, ex);
            }
            while (distanciaActual > 130) {
            try {
                distanciaActual--;
                Thread.sleep(5);
                gs.repaint();

            } catch (InterruptedException ex) {
                Logger.getLogger(Manos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (barrillo != null) {
            gs.eliminarBarrillo(barrillo);
        }
        while (distanciaActual < distanciaMaxima) {
            try {
                distanciaActual++;
                Thread.sleep(5);
                gs.repaint();
            } catch (InterruptedException ex) {
                Logger.getLogger(Manos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        explotandoBarrillo = false;
        barrillo = null;
        
            
        }
        
    }

}
