//package com.aluracursos.desafio.principal;
//
//import com.aluracursos.desafio.model.DatosBusqueda;
//import com.aluracursos.desafio.model.DatosLibros;
//import com.aluracursos.desafio.service.ConsumoAPI;
//import com.aluracursos.desafio.service.ConvierteDatos;
//
//import java.util.Comparator;
//import java.util.DoubleSummaryStatistics;
//import java.util.Optional;
//import java.util.Scanner;
//import java.util.stream.Collectors;
//
//public class PrincipalAntiguo {
//
//    private ConsumoAPI consumoAPI = new ConsumoAPI();
//    private ConvierteDatos conversor = new ConvierteDatos();
//    private static final String URL_BASE = "https://gutendex.com/books/";
//    private Scanner teclado = new Scanner(System.in);
//
//
//
//    public void muestraMenu() {
//
//        var json = consumoAPI.obtenerDatos(URL_BASE);
//        System.out.println(json);
//
//        var datos = conversor.obtenerDatos(json, DatosBusqueda.class);
//        System.out.println(datos);
//
//        //Top 10 libros mas descargados
//        System.out.println("Top 10 libros mas descargados: ");
//        datos.resultados().stream()
//                .sorted(Comparator.comparing(DatosLibros::numeroDescargas).reversed())
//                .limit(10)
//                .map(l -> l.titulo().toUpperCase())
//                .forEach(System.out::println);
//
//        //Busqueda de libros por nombre
//        System.out.println("Ingrese el nombre del libro que desea buscar:");
//        var libroBuscar = teclado.nextLine();
//
//        json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + libroBuscar.replace(" ","+"));
//        var datosBusqueda = conversor.obtenerDatos(json, Datos.class);
//
//        Optional<DatosLibros> libroBuscado = datosBusqueda.resultados().stream()
//                .filter(l -> l.titulo().toUpperCase().contains(libroBuscar.toUpperCase()))
//                .findFirst();
//        if (libroBuscado.isPresent()) {
//            System.out.println("Libro Encontrado");
//            System.out.println(libroBuscado.get());
//        }else{
//            System.out.println("Libro no Encontrado");
//        }
//
//        //Creando Estadisticas
//
//        DoubleSummaryStatistics est = datos.resultados().stream()
//                .filter(d -> d.numeroDescargas()>0)
//                .collect(Collectors.summarizingDouble(DatosLibros::numeroDescargas));
//
//        System.out.println("Cantidad de descargas:" +est.getAverage());
//        System.out.println("Cantidad maxima de descargas:" +est.getMax());
//        System.out.println("Cantidad mininma de descargas:" +est.getMin());
//        System.out.println("Cantidad de registros parte de la muestra: " + est.getCount());
//
//
//
//    }
//}
