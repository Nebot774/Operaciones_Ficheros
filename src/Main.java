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

        Path sourceDirectory = Paths.get("/tmp/niats"); // Reemplaza con la ruta de tu directorio


        Path directoryAAA = Paths.get(sourceDirectory.toString(), "aaa");
        Path directoryBBB = Paths.get(directoryAAA.toString(), "bbb");
        Path directoryCCC = Paths.get(directoryAAA.toString(), "ccc");
        Path directoryDDD = Paths.get(directoryCCC.toString(), "ddd");
        Path directoryEEE = Paths.get(directoryAAA.toString(), "eee");
        Path directoryFFF = Paths.get(sourceDirectory.toString(), "fff");

        Path fileA = Paths.get(directoryAAA.toString(), "a.txt");
        Path fileB = Paths.get(directoryBBB.toString(), "b.txt");
        Path fileC = Paths.get(directoryCCC.toString(), "c.txt");
        Path fileD = Paths.get(directoryDDD.toString(), "d.txt");
        Path fileE = Paths.get(directoryEEE.toString(), "e.txt");
        Path fileF = Paths.get(directoryFFF.toString(), "f.txt");
        Path fileN = Paths.get(sourceDirectory.toString(), "n.txt");

        Files.createDirectories(directoryAAA);
        Files.createDirectories(directoryBBB);
        Files.createDirectories(directoryCCC);
        Files.createDirectories(directoryDDD);
        Files.createDirectories(directoryEEE);
        Files.createDirectories(directoryFFF);

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



    }
}