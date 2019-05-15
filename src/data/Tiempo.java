/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;
import guitetris.TetrisFrame;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 *
 * @author jes19
 */
public class Tiempo implements Runnable{
    private long timeInit = 0;
    private long timeEnd = 0;
    private TetrisFrame frame;
    private int segundos;
    private int minutos;
    private boolean continuar;
    private final int delay = 1000;
    private String cadena;
    
    public Tiempo(TetrisFrame frame) {
        this.frame = frame;
        continuar = true;
    }
    @Override
    public void run(){
        timeInit = System.currentTimeMillis();
        
        while(continuar){
            try{
              Thread.sleep(delay);
              
              timeEnd = System.currentTimeMillis();
              segundos = (int) (timeEnd-timeInit) /1000;
              minutos = segundos /60;
              segundos %= 60;
              
              if(minutos < 10 && segundos < 10){
                  cadena = "0"+minutos+":0"+segundos;
              } else if(minutos < 10){
                  cadena = "0"+minutos+":"+segundos;
              } else if(segundos < 10){
                  cadena = minutos+":0"+segundos;
              } else {
                  cadena = minutos+":0"+segundos;
              }
              frame.setTimeLabel(cadena);
            } catch (InterruptedException ex) {
                Logger.getLogger(Tiempo.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }
    }
    
    public void terminar(){
        continuar = false;
    }
    
    
}
