package data;
import guitetris.TetrisFrame;

/**
 * Esta clase implementa una hebra que hace que se mueva continuamente hacia abajo la Figura actual.
 * La hebra se encarga también de ir refrescando la pantalla
 * dónde se dibuja todo. Además controla si la Figura
 * choca contra el fond o contra las piezas ya colocadas. 
 */
public class Mueve implements Runnable{
    private int delay;
    private boolean continuar;
    private boolean suspendFlag;
    private TetrisFrame frame;

       
    /**
     * Constructor de la clase, que inicializa la referencia utilizadas por
     * la hebra al TetrisMidlet, establece el retardo en milisegundos
     * entre movimiento y movimiento de la Figura actual, y comienza a ejecutar
     * la hebra. 
     */
    public Mueve(TetrisFrame fr,int nivel){
        frame=fr;
        delay= actualizaRetardo(nivel);
        continuar=true;
        suspendFlag=true;
        
    }
    
    /**
     * Código que constituye las sentencias de la tarea. Se encarga
     * de hacer que se mueva hacia abajo continuamente la Figura que cae en el tablero.
     * También se encarga de ir refrescando la pantalla dónde se dibuja todo. 
     * Además controla si la Figura ha terminado
     * de caer (se ha chocado con el fondo). En ese caso se copian sus Elementos
     * (celdas) en la Rejilla y se genera una nueva Figura.
     */
    public void run(){
        
        try{
            while(continuar){
                
                
                synchronized(this){
                    while(suspendFlag){
                        wait();
                    }
                }
                Thread.sleep(delay);
                if(!frame.getRejilla().seChoca(frame.getFigura(),Figura.ABAJO)){
                    frame.getFigura().mueve(Figura.ABAJO);
                    if(frame.getPanel()!=null)
                        frame.getPanel().repaint();
                }
                else{
                    boolean valor=frame.getRejilla().copiaFiguraEnRejilla(frame.getFigura());
                    frame.getRejilla().eliminarFilasLlenas();
                    if(frame.getPanel()!=null)
                        frame.getPanel().repaint();
                    if(!valor)
                        frame.nuevaFigura();
                    else{
                        System.out.println("He llegado al final");
                        continuar=false;
                    }
                }
            }// end while(continuar)
        } catch(InterruptedException e){
            System.out.println("Hebra MueveFigura interrumpida");
        }
    }
    

    
    /**
     * Detiene momentaneamente la ejecución de la hebra, haciendo que la Figura actual
     * quede parada.
     */
    synchronized public void suspender(){
        frame.getPanel().repaint();
        suspendFlag=true;
    }
    
    /**
     * Reanuda el movimiento de la hebra. La Figura actual vuelve  a moverse.
     */
    public synchronized void reanudar(){
        if(frame.getPanel()!=null)
            frame.getPanel().repaint();
        suspendFlag = false;
        notify();
    }
    
    /**
     * Termina la ejecución de la hebra.
     */
    public void terminar(){
        continuar=false;
    }
    
    /**
     * Devuelve si la hebra ha terminado su ejecución
     * @return true si la hebra ha terminado su ejecución
     */
    public boolean getTerminado(){
        return !continuar;
    }
    
    /**
     * Nos dice si la hebra está o no parada.
     * @return true si la hebra de movimiento está parada, false en otro caso
     */
    synchronized public boolean getParado(){
        return suspendFlag;
    }
    
    /**
     * La siguiente función actualiza el retardo que espera la hebra
     * para mover la Figura actual. El nivel más lento será
     * el 0 (retardo 700) y el más rápido el 10 (retardo 50)
     */
    private int actualizaRetardo(int nivel) {
        if (nivel>10) nivel=10;
        else if (nivel<0) nivel=0;
        return ( 400-(nivel*35) );
    }
}
