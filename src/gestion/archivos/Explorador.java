package gestion.archivos;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class Explorador {
 
    private File ruta, portapapeles;
    boolean cortar;

    public Explorador(String ruta) {
        this.ruta = new File(ruta);
    }

    public File getRuta() {
        return this.ruta;
    }
    public void setRuta(File ruta) {
        this.ruta = ruta;
    }
    public File getPadre() {
        if (!existePadre()) return null;
        return this.ruta.getParentFile();
    }
    public File getPortapapeles() {
        return this.portapapeles;
    }
    
    // buscar un archivo por nombre
    public File getArchivoPorNombre(String nombre ) {
        for (File archivo : getArchivos()) {
            if (nombre.toLowerCase().equals(archivo.getName().toLowerCase())) {
                return archivo;
            } 
        }
        return null;
    }
    // buscar archivo por index
    public File getArchivoPorIndex(int index) {
        if (index >= 0 && index < getArchivos().length) {
            return getArchivos()[index];
        }
        return null;
    } 

    // obtener archivos en orden -> carpetas... files...
    public File[] getArchivos() {
        File[] lista = this.ruta.listFiles();
        Arrays.sort(lista, Comparator.comparing(File::getName));

        ArrayList<File> carpetas = new ArrayList<>();
        ArrayList<File> archivos = new ArrayList<>();
        if (existePadre()) {
           carpetas.add(this.ruta.getParentFile());
        }
        for (File file : lista) {
           if (file.isDirectory()) {
               carpetas.add(file);
           } else {
               archivos.add(file);
           }
        }
        carpetas.addAll(archivos);
        lista = new File[carpetas.size()];
        for (int i = 0; i < lista.length; i++) {
           lista[i] = carpetas.get(i); 
        }
        return lista;
    }

    // ir al directorio padre
    public File retroceder() {
        if (existePadre()) 
            this.ruta = this.ruta.getParentFile();
        return this.ruta;
    }
    // validar si la ruta padre existe
    public boolean existePadre() {
        return this.ruta.getParent() != null;
    }

    // lista de los detalles de cada archivo en una cadeda de texto
    public String[] getListaArchivosTexto() {
        File[] archivos = getArchivos();
        String[] lista = new String[archivos.length];
       
        for (int i = 0; i < lista.length; i++) {
           lista[i] = Utilidades.espaciosAntes(3, Integer.toString(i)) + ") " + formatoDetallesArchivo(archivos[i]); 
        }

        return lista;
    }
    public String formatoDetallesArchivo(File archivo) {
        String fecha = Utilidades.fechaArchivo(archivo);
        String tamano = Utilidades.espaciosAntes(9, Utilidades.tamanoArchivo(archivo));
        String nombre = archivo.getName();
        if (existePadre() && getPadre().compareTo(archivo)==0)
            nombre = ".. ( " + archivo.getName() + " )";

        return fecha + " " + tamano + "  " + nombre;
    }

    public String getExtensionArchivo(File archivo) {
        if (archivo.isDirectory())
            return "Directorio";
        String[] nombre = archivo.getName().split("\\.");
        if (archivo.isFile() && nombre.length > 0)
            return nombre[nombre.length-1];
        return "";
    }
    
    // borrar archivo
    public boolean borrarArchivo(File archivo) {
        return archivo.delete();
    }

    // cambiar nombre de un archivo (validar si ya existe ese nombre)
    public boolean renombrarArchivo(File archivo, String nombre) {
        if (getArchivoPorNombre(nombre).exists()) return false;
            
        return archivo.renameTo(new File(archivo.getParent()+nombre));
    }
    
    // copiar o cortar archivo (prgunta por el nuevo nombre)
    public File copiarArchivo(File archivo, boolean cortar) {
        this.portapapeles = archivo;
        this.cortar = cortar;
        return this.portapapeles;
    }

    // pegar archivo en el directorio actual
    public File pegarArchivo(String rename) {
        String nombre = rename != null ? rename : this.portapapeles.getName();
        File nuevaCopia = new File(getRuta().getAbsolutePath() + File.separator + nombre);
        try {
			nuevaCopia.createNewFile();
		} catch (Exception e) {
			return null;
		}
        if (cortar) {
            getPortapapeles().delete();
            this.cortar = false;
        }
        this.portapapeles = null;
        System.out.println(nuevaCopia.getAbsolutePath());
        return nuevaCopia;
    }

    /* Ver archivo
        txt -> mostrar contenido
        otro -> mostrar en hexadecimal
        .dat -> 
            ABC
            CBA
            41 42 43  <- MAXIMO tamaño de bytes 20
            43 42 41    
    */

    // crear un archivo con nombre

    // info archivo: nombre, ext, size, created, is hidden, read/write type
    public String[] getInfoArchivo(File archivo) {
        String[] datos = new String[6];

        datos[0] = archivo.getName();
        datos[1] = getExtensionArchivo(archivo);
        datos[2] = Utilidades.tamanoArchivo(archivo);
        datos[3] = Utilidades.fechaArchivo(archivo);
        datos[4] = archivo.isHidden() ? "Oculto" : "Visible";
        datos[5] = archivo.canWrite() ? "Escritura y lectura" : "Solo lectura";

        return datos;
    }

    // create folder

    // move filer to other directory (ask which file to move and valdate if that name already exists)

    
}
