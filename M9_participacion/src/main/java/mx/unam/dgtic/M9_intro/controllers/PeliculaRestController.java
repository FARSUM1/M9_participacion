package mx.unam.dgtic.M9_intro.controllers;

import mx.unam.dgtic.M9_intro.modelos.Pelicula;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/blockbuster")
public class PeliculaRestController {
    private HashMap<Integer, Pelicula> blockbuster;

    public PeliculaRestController() {
        blockbuster = new HashMap<>();
        blockbuster.put(1, new Pelicula(1,"Bastardos sin gloria","Comedia","2009","Quentin Tarantino"));
        blockbuster.put(2, new Pelicula(2,"Matrix","Ciencia Ficcion","1999","Wachowski"));
        blockbuster.put(3, new Pelicula(3,"Troya","Accion","2004","Wolfgang Petersen"));
    }

    @GetMapping("/ping")
    public String ping() {
        return "Ok";
    }

    @GetMapping("/creditos")
    public List<String> creditos() {
        List<String> creditos = new ArrayList<>();
        creditos.add("Diego Ignacio Nuñez Hernandez");
        creditos.add("Milner Ushuaía Flores Pérez");
        creditos.add("Ramirez Samperio Israel");
        creditos.add("Juan Antonio Callejas Sanchez");
        creditos.add("Jaramillo Hernández Jose");
        return creditos;
    }

    @GetMapping(value = "/", headers = "Accept=application/json",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<Integer, Pelicula>> getAll() {
        return new ResponseEntity<>(blockbuster, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pelicula> getPelicula(@PathVariable int id) {
        Pelicula pelicula = blockbuster.get(id);
        return pelicula != null ? ResponseEntity.ok(pelicula) : ResponseEntity.notFound().build();
    }

    @PostMapping("/")
    public ResponseEntity<Pelicula> addPelicula(@RequestBody Pelicula pelicula) {
        //Simular el auto increment de BD
        int id = 1;
        while (blockbuster.containsKey(id)) {
            id++;
        }

        pelicula.setId(id);
        blockbuster.put(id, pelicula);

        return new ResponseEntity<>(pelicula, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pelicula> updatePelicula(@PathVariable Integer id, @RequestBody Pelicula pelicula) {
        if(blockbuster.containsKey(id)) {
            pelicula.setId(id);
            blockbuster.replace(pelicula.getId(), pelicula);
            return new ResponseEntity<>(blockbuster.get(id), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Pelicula> patchPelicula(@PathVariable Integer id, @RequestBody Pelicula pelicula) {
        Pelicula peliculaDB = blockbuster.get(id);
        if(peliculaDB==null) { return new ResponseEntity<>(HttpStatus.NOT_FOUND); }
        if(pelicula.getTitulo() != null) { peliculaDB.setTitulo(pelicula.getTitulo()); }
        if(pelicula.getGenero() != null) { peliculaDB.setGenero(pelicula.getGenero()); }
        if(pelicula.getFechaLanzamiento() != null) { peliculaDB.setFechaLanzamiento(pelicula.getFechaLanzamiento()); }
        if(pelicula.getDirector() != null) { peliculaDB.setDirector(pelicula.getDirector()); }
        blockbuster.replace(id, peliculaDB);
        return ResponseEntity.ok(blockbuster.get(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Pelicula> deleteLibro(@PathVariable Integer id) {
        Pelicula pelicula = blockbuster.get(id);
        if(blockbuster.get(id) != null) {
            blockbuster.remove(id);
            return ResponseEntity.ok(pelicula);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

