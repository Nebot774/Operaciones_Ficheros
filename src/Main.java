import java.io.IOException;
import java.net.StandardSocketOptions;
import java.nio.file.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.Collectors;


public class Main {
    public static void main(String[] args) throws IOException {

        /**
         *Haz un programa que reciba como argumento la ruta de un directorio y “aplane” (flatten) su contenido, es
         * decir, que mueva a este mismo directorio todos los ficheros que tenga anidados. También tendrá que borrar
         * todos los directorios anidados. Por ejemplo, si ejecutamos el programa con este directorio "niats":
         *
         *
         *
         * niats
         * │
         * ├─ aaa
         * │  ├─ a.txt
         * │  ├─ bbb
         * │  │  ├─ b.txt
         * │  ├─ ccc
         * │  │  ├─ c.txt
         * │  │  ├─ ddd
         * │  │  │  ├─ d.txt
         * │  ├─ eee
         * │  │  ├─ e.txt
         * │
         * ├─ fff
         * │  ├─ f.txt
         * │
         * └─ n.txt
         *
         *
         *
         *
         *
         */


        // Definición de la ruta principal del directorio 'niats' en '/tmp'
        Path sourceDirectory = Paths.get("/tmp/niats"); // Reemplaza con la ruta de tu directorio

        // Definición de subdirectorios dentro de 'niats' o de otros subdirectorios
        Path directoryAAA = Paths.get(sourceDirectory.toString(), "aaa");
        Path directoryBBB = Paths.get(directoryAAA.toString(), "bbb");
        Path directoryCCC = Paths.get(directoryAAA.toString(), "ccc");
        Path directoryDDD = Paths.get(directoryCCC.toString(), "ddd");
        Path directoryEEE = Paths.get(directoryAAA.toString(), "eee");
        Path directoryFFF = Paths.get(sourceDirectory.toString(), "fff");

        // Definición de rutas para archivos dentro de los directorios especificados
        Path fileA = Paths.get(directoryAAA.toString(), "a.txt");
        Path fileB = Paths.get(directoryBBB.toString(), "b.txt");
        Path fileC = Paths.get(directoryCCC.toString(), "c.txt");
        Path fileD = Paths.get(directoryDDD.toString(), "d.txt");
        Path fileE = Paths.get(directoryEEE.toString(), "e.txt");
        Path fileF = Paths.get(directoryFFF.toString(), "f.txt");
        Path fileN = Paths.get(sourceDirectory.toString(), "n.txt");

        // Creación de directorios. Si el directorio ya existe, no hará nada.
        Files.createDirectories(directoryAAA);
        Files.createDirectories(directoryBBB);
        Files.createDirectories(directoryCCC);
        Files.createDirectories(directoryDDD);
        Files.createDirectories(directoryEEE);
        Files.createDirectories(directoryFFF);

        // Creación de archivos. Si el archivo ya existe, lanzará una excepción.
        Files.createFile(fileA);
        Files.createFile(fileB);
        Files.createFile(fileC);
        Files.createFile(fileD);
        Files.createFile(fileE);
        Files.createFile(fileF);
        Files.createFile(fileN);

        //despues de crear los directorios

        //creamos un objeto path que respresenta la ruta del rirectorio que le pasamos con todo lo que contie
        List<Path> list = Files.walk(Paths.get("/tmp/niats"))
                .collect(Collectors.toList());

        //definimos el destino de los archivos de texto
        Path destino=Paths.get("/tmp/niats");

        //recorremos la lista path i movemos los ficheros de texto al directorio niats
        for(Path path:list){
            if(Files.isRegularFile(path)){
                Path destinoFichero=destino.resolve(path.getFileName());
                try{
                    Files.move(path,destinoFichero, StandardCopyOption.REPLACE_EXISTING);
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }

        //borramos directorios de manera recursiva
        try (Stream<Path> list1 = Files.walk(Paths.get("/tmp/niats"))) {
            list1.sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(file -> {
                        if(file.isDirectory()){
                            if(file.list().length==0){
                                file.delete();
                            }
                        }
                    });
        }

        /**
         * APUNTES Y CONCEPTOS CLAVE:
         *
         * 1. Java NIO (New I/O) y java.nio.file:
         * - java.nio.file.Path: Representa una ruta en el sistema de archivos. Es una alternativa moderna y flexible a la clase File tradicional.
         * - java.nio.file.Paths: Proporciona métodos estáticos para obtener objetos Path.
         * - java.nio.file.Files: Proporciona métodos para operaciones de archivos y directorios.
         *
         * 2. Uso de Paths.get():
         * - Se utiliza para crear un objeto Path, que puede representar un archivo o un directorio.
         *
         * 3. Creación de directorios y archivos:
         * - Files.createDirectories(path): Crea todos los directorios en la ruta especificada. Si ya existen, no hace nada.
         * - Files.createFile(path): Crea un nuevo archivo vacío en la ruta especificada.
         *
         * 4. Recorrer directorios y subdirectorios:
         * - Files.walk(path): Devuelve un Stream que es una vista de todos los archivos y directorios en un directorio y subdirectorios.
         *
         * 5. Java Streams:
         * - Se utiliza para procesar colecciones de datos de manera funcional.
         *
         * 6. Recopilación en una lista:
         * - collect(Collectors.toList()): Convierte el Stream en una lista.
         *
         * 7. Verificar si un Path es un archivo regular:
         * - Files.isRegularFile(path): Retorna true si el Path representa un archivo regular (no un directorio).
         *
         * 8. Mover archivos:
         * - Files.move(source, destination, options): Mueve o renombra un archivo a una ruta de destino, con opciones como StandardCopyOption.REPLACE_EXISTING para reemplazar si el archivo ya existe.
         *
         * 9. Borrar directorios y archivos de manera recursiva:
         * - Para borrar directorios anidados de forma segura, es necesario ordenarlos en orden inverso (primero borrar los directorios más profundos) y asegurarse de que el directorio esté vacío antes de borrarlo.
         *
         * 10. Manejo de excepciones:
         * - Las operaciones de archivo pueden lanzar excepciones, en particular IOException, que debe ser capturada o declarada en el método.
         *
         * OBJETIVO:
         * - El programa tiene como objetivo "aplanar" un directorio, es decir, mover todos los archivos de los subdirectorios al directorio principal y luego eliminar todos los subdirectorios.
         *
         * PROCESO:
         * 1. Crea una estructura de directorios y archivos para probar el proceso de aplanamiento.
         * 2. Utiliza Files.walk() para obtener una lista de todos los archivos y directorios dentro del directorio principal.
         * 3. Mueve todos los archivos al directorio principal.
         * 4. Elimina todos los subdirectorios de manera recursiva.
         */




    }
}