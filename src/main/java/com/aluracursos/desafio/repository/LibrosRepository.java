package com.aluracursos.desafio.repository;

import com.aluracursos.desafio.model.Libros;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface LibrosRepository extends JpaRepository<Libros, Long> {
    Libros findByTitulo(String titulo);

    Optional<Libros> findByTituloContains(String titulo);

    @Query("""
            SELECT l
            FROM Libros
            l WHERE l.idioma = :idioma
            """)
    List<Libros>buscarPorIdioma(String idioma);

    @Query("""
            SELECT l
            FROM Libros
            l ORDER BY l.numeroDeDescargas DESC LIMIT 10
            """)
    List<Libros>top10Libros();
}