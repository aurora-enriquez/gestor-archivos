package gestion.archivos;

import java.io.File;

public class Explorador {
 
    private File ruta;

    public Explorador(String ruta) {
        this.ruta = new File(ruta);
    }

    public File getRuta() {
        return this.ruta;
    }
    
    public File[] getListaArchivos() {
        return this.ruta.listFiles();
    }
    public String[] getListaCarpetas() {
        return this.ruta.list();
    }

    public File retroceder() {
        this.ruta = ruta.getParentFile();
        return this.ruta;
    }

    // listar archivos print

    // borrar archivo

    // cambiar nombre de un archivo (validar si ya existe ese nombre)
    
    // copiar archivo (prgunta por el nuevo nombre)

    /* Ver archivo
        detectar si es TXT

    */

    
}
