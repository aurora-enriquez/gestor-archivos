package gestion.archivos;

import java.io.File;
import java.text.SimpleDateFormat;

public class Utilidades {
  
  public static String espaciosDespues(int longitud, String cadena) {
    int saltos = longitud-cadena.length();
    for (int i = 0; i < saltos; i++) 
      cadena += " ";
    
    return cadena;
  }
  public static String espaciosAntes(int longitud, String cadena) {
    int saltos = longitud-cadena.length();
    for (int i = 0; i < saltos; i++) 
      cadena = " " + cadena;
    
    return cadena;
  }
  public static String separador(int longitud, char caracter) {
    String separador = "";
    for (int i = 0; i < longitud; i++)
      separador += caracter; 
    return separador;
  }

  public static String tamanoArchivo(File archivo) {
    String tamano;
    long bytes = 0;
    try {
      bytes = archivo.length();
    } catch (Exception e) {
      e.printStackTrace();
    }
    long GB = 1024*1024*1024;
    long MB = 1024*1024;
    long kB = 1024;

    if (bytes >= GB) 
      tamano = ((double) (bytes/GB)) + " GB";
    else if (bytes >= MB)
      tamano = decimalCorto((double)bytes/MB, 2) + " MB";
    else if (bytes >= kB)
      tamano = (bytes/kB) + " kB";
    else
      tamano = bytes + " B";
      
    return tamano;
  }

  public static double decimalCorto(double numero, int longitud) {
    int multi = 1;
    for (int i = 0; i < longitud; i++) 
      multi *= 10;
    
    return (double) ((int) (numero*multi))/multi;
  }
  
  public static String fechaArchivo(File archivo) {
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    return sdf.format(archivo.lastModified());
  }

  public static boolean esNumero(String strNum) {
      if (strNum == null) {
          return false;
      }
      try {
          double d = Double.parseDouble(strNum);
      } catch (NumberFormatException nfe) {
          return false;
      }
      return true;
  }
}
