package gestion.archivos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Scanner;

public class Main {
    static Explorador explorador;
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        explorador = new Explorador(System.getProperty("user.home"));

        // imprimir comandos
        System.out.println(comandosAyuda() + Utilidades.separador(80, '-'));

        System.out.println("Enter para continuar");
        sc.nextLine();
        System.out.println();

        do {
            System.out.println("Directorio: " + explorador.getRuta().getAbsolutePath() + "\n");

            for (String string : explorador.getListaArchivosTexto()) {
                System.out.println(string);
            }
            if (explorador.getRuta().listFiles().length < 1) {
               System.out.println("\n     Directorio vacio"); 
            }
            System.out.println();
    
            try {
                String portapapeles = explorador.getPortapapeles().getName();
                System.out.println("Portapapeles: " + portapapeles);
            } catch(Exception e) {} 
    
            System.out.print("> ");
            String[] comando = leerComando();
            ejecutarComando(comando);

            System.out.println(Utilidades.separador(80, '-'));
        } while (true);
    }

    public static String[] leerComando() {
        String leer = sc.nextLine();
        String[] comando = leer.split("\\s+");
        // for (String key: comando) 
        //     System.out.println(key);
        return comando;
    }

    public static void ejecutarComando(String[] comando) {
        File select;
        switch (comando[0]) {
            case "..":
                explorador.retroceder();
                break;
            case "ayuda":
                System.out.println(comandosAyuda() + "enter");
                sc.nextLine();
                break;
            case "abrir":
                select = selecionarArchivo(comando[1]); 
                if (select != null) {
                    if (select.isDirectory()) {
                        explorador.setRuta(select);
                    } else if (explorador.getExtensionArchivo(select).toLowerCase().equals("txt")){
                        String contenido;
                        try {
                            contenido = new String(Files.readAllBytes(select.toPath()));
                            System.out.println(
                                "\nArchivo: " + select.getAbsolutePath() + "\n \n"  
                                + Utilidades.separador(20, '_') + "\n"
                                + contenido 
                                + Utilidades.separador(20, '_')
                                + "\n \nenter"
                            );
                            sc.nextLine();
                        } catch (Exception e) {
                            System.err.println("No se pudo leer el archivo txt");
                        }
                    } else {
                        InputStream stream = null;
						try {
							stream = new FileInputStream(select);
						} catch (FileNotFoundException e) {
                            System.err.println("No se pudo imprimir el contenido hexadecimal");
                        }
                        System.out.println(
                            "\nArchivo: " + select.getAbsolutePath() + "\n \n"  
                            + Utilidades.separador(20, '_') + "\n"
                        );
                        imprimirHexStream(stream, 20);
                        System.out.println(
                            "\n" + Utilidades.separador(20, '_')
                            + "\n \nenter"
                        );
                        sc.nextLine();
                    }
                }
                break; 
            case "info":
                select = selecionarArchivo(comando[1]); 
                if (select != null) {
                    System.out.println(
                        
                        "\nArchivo: " + select.getAbsolutePath() + "\n \n  " +
                        Utilidades.espaciosDespues(20, "Nombre:") + explorador.getInfoArchivo(select)[0] + "\n  " +
                        Utilidades.espaciosDespues(20, "Extension:") + explorador.getInfoArchivo(select)[1] + "\n  " +
                        Utilidades.espaciosDespues(20, "Tamaño:") + explorador.getInfoArchivo(select)[2] + "\n  " +
                        Utilidades.espaciosDespues(20, "Modificado:") + explorador.getInfoArchivo(select)[3] + "\n  " +
                        Utilidades.espaciosDespues(20, "Estado:") + explorador.getInfoArchivo(select)[4] + "\n  " +
                        Utilidades.espaciosDespues(20, "Tipo:") + explorador.getInfoArchivo(select)[5] + "\n  " +
                    "\nenter");
                    sc.nextLine();
                }
                break; 
            case "crear":
                if (comando.length > 2) {
                    String tipoArchivo = comando[1].toLowerCase();
                    File nuevoArchivo = new File(explorador.getRuta().getAbsolutePath() + File.separator + comando[2]);
                    if (tipoArchivo.equals("carpeta")) {
                        try {
                            Files.createDirectories(nuevoArchivo.toPath());
                            System.out.println("Directorio creado!");
                            System.out.println(nuevoArchivo.getAbsolutePath());
                        } catch (Exception e) {
                            System.out.println("No se pudo crear el directorio.");
                        }
                    } else if (tipoArchivo.equals("archivo")) {
                        try {
                            nuevoArchivo.createNewFile();
                            System.out.println("Archivo creado!");
                            System.out.println(nuevoArchivo.getAbsolutePath());
                        } catch (Exception e) {
                            System.out.println("No se pudo crear el archivo.");
                        } 
                    } else {
                        System.out.println("Tipo de archivo no definido.");
                    }
                } else {
                    System.out.println("Comando 'crear' incorrecto. \n Escribir comando 'ayuda' para apoyarte.");
                }
                System.out.println("\nenter");
                sc.nextLine();
                break;
            case "borrar":
                select = selecionarArchivo(comando[1]); 
                if (select != null) {
                    boolean success = explorador.borrarArchivo(select);
                    System.out.println();
                    if (success) {
                       System.out.println("Se elimino el archivo: "+ select.getName()); 
                    } else {
                       System.out.println("No se pudo eliminar el archivo: "+ select.getName()); 
                    }
                    System.out.println("\nenter");
                    sc.nextLine();
                }
                break;
           
            case "copiar":
                select = selecionarArchivo(comando[1]); 
                if (select != null) {
                    explorador.copiarArchivo(select, false);
                    System.out.println("\nArchivo copiado en el portapapeles.");
                    System.out.println("\nenter");
                    sc.nextLine();
                }
                break; 
            case "cortar":
                select = selecionarArchivo(comando[1]); 
                if (select != null) {
                    explorador.copiarArchivo(select, true);
                    System.out.println("\nArchivo cortado en el portapapeles.");
                    System.out.println("\nenter");
                    sc.nextLine();
                }
                break;
            case "pegar":
                String nuevoNombre = null;
                while (!validarPegarArchivo(nuevoNombre)) {
                    System.out.println("Existe un archivo con el mismo nombre."); 
                    System.out.print("Renombrar ("+explorador.getPortapapeles().getName()+"): ");
                    nuevoNombre = sc.nextLine();
                };
                break;
            default:
                System.out.println("Comando no detectado. \n Escribir comando 'ayuda' para apoyarte.");
                break;
        }
    }

    public static boolean validarPegarArchivo(String nombre) {
        File nombreMath = explorador.getArchivoPorNombre(nombre != null ? nombre : explorador.getPortapapeles().getName());
        if (nombreMath == null) {
            explorador.pegarArchivo(nombre);
            return true;      
        }
        return false;
    }

    public static File selecionarArchivo(String regex) {
        try {
            File archivo = null;
            if (Utilidades.esNumero(regex)) {
                int index = Integer.parseInt(regex);
                archivo = explorador.getArchivoPorIndex(index);
                return archivo;
            } else {
                archivo = explorador.getArchivoPorNombre(regex);
                if (archivo.exists()) return archivo;
            }
            return archivo;
        } catch(Exception e) {
            System.err.println("Archivo definido no existe.");
            return null;
        }    
    }
    
    public static String comandosAyuda() {
        String ayuda = 
        Utilidades.separador(80, '-') + "\n\n" +
        "Comandos del Programa: \n" +
        "\n" +
        "..         Ir al directorio padre (retroceder).\n" +
        "\n" +
        "ayuda      Imprimir los comandos disponibles.\n" +
        "\n" +
        "abrir      Abrir archivo o carpeta (abrir directorio). Ejemplo:\n" +
        "           abrir <nombre o id>\n" +
        "\n" +
        "info       Ver informacion del archivo selecionado. Ejemplo:\n" +
        "           info <nombre o id>\n" +
        "\n" +
        "crear      Crear archivo o carpeta. Ejemplo:\n" +
        "           crear <archivo|carpeta> <nombre>\n" +
        "\n" +
        "borrar     Borrar un archivo o carpeta. Ejemplo:\n" +
        "           borrar <nombre o id>\n" +
        "\n" +
        "copiar     Copia el archivo al portapapeles. Ejemplo:\n" +
        "           copiar <nombre o id>\n" +
        "\n" +
        "cortar     Corta el archivo al portapapeles (mover archivo). Ejemplo:\n" +
        "           cortar <nombre o id>\n" +
        "\n" +
        "pegar      Pega el archivo del portapapeles en la ruta actual. Ejemplo:\n" +
        "           pegar\n" +
        "\n";
        
        return ayuda;
    }

    public static void imprimirHexStream(final InputStream inputStream, final int numberOfColumns) {
        long streamPtr=0;
        try {
            while (inputStream.available() > 0) { 
                final long col = streamPtr++ % numberOfColumns;
                System.out.printf("%02x ",inputStream.read());
                if (col == (numberOfColumns-1)) {
                    System.out.printf("\n");
                }
            }
        } catch (Exception e) {
            System.err.println("No se pudo imprimir el contenido hexadecimal");
        }
    }
}
