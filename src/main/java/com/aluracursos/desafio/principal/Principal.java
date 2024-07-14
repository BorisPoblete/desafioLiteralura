package com.aluracursos.desafio.principal;

import com.aluracursos.desafio.model.*;
import com.aluracursos.desafio.repository.AutoresRepository;
import com.aluracursos.desafio.repository.LibrosRepository;
import com.aluracursos.desafio.service.ConsumoAPI;
import com.aluracursos.desafio.service.ConvierteDatos;

import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_Base = "https://gutendex.com/books/?search=";
    private ConvierteDatos convierte = new ConvierteDatos();
    private LibrosRepository librosRepository;
    private AutoresRepository autoresRepository;

    public Principal(LibrosRepository librosRepository, AutoresRepository autoresRepository){
        this.librosRepository = librosRepository;
        this.autoresRepository = autoresRepository;
    }

    public void miMenu() {

        var opcion = -1;
        while (opcion != 0){
            var menu = """
                    -------------------------------------------------
                    Elija la opción a través de su número:
                    1 - Buscar libro por título
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un determinado año
                    5 - Listar libros por idioma
                                  
                    0 - Salir
                    -------------------------------------------------
                    """;
            System.out.println(menu);
            opcion = Integer.parseInt(teclado.nextLine());
            switch (opcion){
                case 1:
                    buscarLibroPorTitulo();
                    break;
                case 2:
                    listaLibrosRegistrados();
                    break;
                case 3:
                    listaAutoresRegistrados();
                    break;
                case 4:
                    autoresVivosDate();
                    break;
                case 5:
                    listaLibrosPorIdioma();
                    break;
                case 6:
                    top10LibrosDescargados();
                    break;
                case 0:
                    System.out.println("Saliendo de la aplicación...");
                    break;
                default:
                    System.out.println("opción invalida");
                    break;
            }
        }
    }


    private void buscarLibroPorTitulo(){
        System.out.println("Escriber el titulo del libro que deseas buscar:");
        String buscarTitulo = teclado.nextLine();
        var json =consumoApi.obtenerDatos(URL_Base + buscarTitulo.replace(" ", "%20"));
        System.out.println(json);
        var datos =convierte.obtenerDatos(json,DatosBusqueda.class);
        System.out.println(datos);
        if(datos.resultadoLibros().isEmpty()){
            System.out.println("Libro " + buscarTitulo + " no encontrado");
        }else {
            DatosLibros datosLibros = datos.resultadoLibros().get(0);
            System.out.println(datosLibros);
            Libros libros = new Libros(datosLibros);
            System.out.println(libros);
            Autores autores = new Autores().getPrimerAutor(datosLibros);
            System.out.println(autores);

            guardarDatos(libros, autores);
        }
    }

    private  void guardarDatos(Libros libros, Autores autores) {
        Optional<Libros> registroLibro = librosRepository.findByTituloContains(libros.getTitulo());

        if (registroLibro.isPresent()) {
            System.out.println("Este libro ya fue registrado anteriormente...");
        }else {
            try {
                librosRepository.save(libros);
                System.out.println("El libro se ha registrado!");
            } catch (Exception e) {
                System.out.println("Mensaje de error: " + e.getMessage());
            }
        }

        Optional<Autores> encontrarAutor = autoresRepository.findByNombreContains(autores.getNombre());

        if (encontrarAutor.isPresent()) {
            System.out.println("Este autor ya fue registrado anteriormente...");
        }else {
            try {
                autoresRepository.save(autores);
                System.out.println("Se ha registrado el autor!");
            }catch (Exception e) {
                System.out.println("Mensaje de error: " + e.getMessage());
            }
        }
    }
    private void listaLibrosRegistrados(){
        System.out.println("\n------------Libros Registrados---------");
        List<Libros> librosRegistados = librosRepository.findAll();
        librosRegistados.stream()
                .sorted(Comparator.comparing(Libros::getTitulo))
                .forEach(System.out::println);
    }

    private void listaAutoresRegistrados() {
        System.out.println("\n----------- Autores Registrados-----------");
        List<Autores> autoresRegistrados = autoresRepository.findAll();
        autoresRegistrados.stream()
                .sorted(Comparator.comparing(Autores::getNombre))
                .forEach(System.out::println);
    }

    private void autoresVivosDate() {
        System.out.println("\n-------------- Autores vivos durante el Año ------------\n");
        System.out.println("Escriba el año que desea consultar:");
        Integer consulta = Integer.valueOf(teclado.nextLine());
        List<Autores> autores = autoresRepository.buscarAutorPorDeterminadoAño(consulta);
        if(autores.isEmpty()){
            System.out.println("No hay registros...");
        } else {
            autores.stream()
                    .sorted(Comparator.comparing(Autores::getNombre))
                    .forEach(System.out::println);
        }
    }

    private void listaLibrosPorIdioma() {
        System.out.println("""
                    -------------------------------------------------
                    Consultar libros por idioma:
                    
                    [es] - Español
                    [en] - Inglés
                    [fr] - Francés
                    [jp] - Japonés
                    [pt] - Portugués
                    [de] - Alemán
                    
                    Digite el id del idioma:
                """);
        String idioma = teclado.nextLine();
        List<Libros> libros = librosRepository.buscarPorIdioma(idioma);
        if (!libros.isEmpty()){
            System.out.println(libros.toString());
        } else {
            System.out.println("No hay libros registrados en este idioma...");
        }
    }

    private void top10LibrosDescargados() {
        List<Libros> libros = librosRepository.findAll();
        if(libros.isEmpty()) {
            System.out.println("No hay datos...");
        }else{
            System.out.println("\n------------Top 10 Libros mas descargados -------------\n");
            System.out.println(librosRepository.top10Libros().toString());
        }

    }






}



