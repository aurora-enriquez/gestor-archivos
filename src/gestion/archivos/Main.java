package gestion.archivos;

import java.io.File;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);

        Explorador explorador = new Explorador("C:\\urofile");
        
        for (File archivo : explorador.getListaArchivos()) {
            System.out.println(archivo.getName());
        }

        System.out.println("Ruta: " + explorador.getRuta().getName());
        // explorador.retroceder();
        // System.out.println(explorador.getRuta().getName());
    }
    
}
