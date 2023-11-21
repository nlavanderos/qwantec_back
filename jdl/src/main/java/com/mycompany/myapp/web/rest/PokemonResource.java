package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Pokemon;
import com.mycompany.myapp.repository.PokemonRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Pokemon}.
 */
@RestController
@RequestMapping("/api/pokemon")
@Transactional
public class PokemonResource {

    private final Logger log = LoggerFactory.getLogger(PokemonResource.class);

    private static final String ENTITY_NAME = "pokemon";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PokemonRepository pokemonRepository;

    public PokemonResource(PokemonRepository pokemonRepository) {
        this.pokemonRepository = pokemonRepository;
    }

    /**
     * {@code POST  /pokemon} : Create a new pokemon.
     *
     * @param pokemon the pokemon to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pokemon, or with status {@code 400 (Bad Request)} if the pokemon has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Pokemon> createPokemon(@Valid @RequestBody Pokemon pokemon) throws URISyntaxException {
        log.debug("REST request to save Pokemon : {}", pokemon);
        if (pokemon.getId() != null) {
            throw new BadRequestAlertException("A new pokemon cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Pokemon result = pokemonRepository.save(pokemon);
        return ResponseEntity
            .created(new URI("/api/pokemon/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pokemon/:id} : Updates an existing pokemon.
     *
     * @param id the id of the pokemon to save.
     * @param pokemon the pokemon to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pokemon,
     * or with status {@code 400 (Bad Request)} if the pokemon is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pokemon couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Pokemon> updatePokemon(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Pokemon pokemon
    ) throws URISyntaxException {
        log.debug("REST request to update Pokemon : {}, {}", id, pokemon);
        if (pokemon.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pokemon.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pokemonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Pokemon result = pokemonRepository.save(pokemon);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pokemon.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /pokemon/:id} : Partial updates given fields of an existing pokemon, field will ignore if it is null
     *
     * @param id the id of the pokemon to save.
     * @param pokemon the pokemon to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pokemon,
     * or with status {@code 400 (Bad Request)} if the pokemon is not valid,
     * or with status {@code 404 (Not Found)} if the pokemon is not found,
     * or with status {@code 500 (Internal Server Error)} if the pokemon couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Pokemon> partialUpdatePokemon(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Pokemon pokemon
    ) throws URISyntaxException {
        log.debug("REST request to partial update Pokemon partially : {}, {}", id, pokemon);
        if (pokemon.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pokemon.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pokemonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Pokemon> result = pokemonRepository
            .findById(pokemon.getId())
            .map(existingPokemon -> {
                if (pokemon.getNombre() != null) {
                    existingPokemon.setNombre(pokemon.getNombre());
                }
                if (pokemon.getTipo() != null) {
                    existingPokemon.setTipo(pokemon.getTipo());
                }
                if (pokemon.getElemento() != null) {
                    existingPokemon.setElemento(pokemon.getElemento());
                }
                if (pokemon.getRegion() != null) {
                    existingPokemon.setRegion(pokemon.getRegion());
                }

                return existingPokemon;
            })
            .map(pokemonRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pokemon.getId().toString())
        );
    }

    /**
     * {@code GET  /pokemon} : get all the pokemon.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pokemon in body.
     */
    @GetMapping("")
    public List<Pokemon> getAllPokemon() {
        log.debug("REST request to get all Pokemon");
        return pokemonRepository.findAll();
    }

    /**
     * {@code GET  /pokemon/:id} : get the "id" pokemon.
     *
     * @param id the id of the pokemon to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pokemon, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Pokemon> getPokemon(@PathVariable Long id) {
        log.debug("REST request to get Pokemon : {}", id);
        Optional<Pokemon> pokemon = pokemonRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(pokemon);
    }

    /**
     * {@code DELETE  /pokemon/:id} : delete the "id" pokemon.
     *
     * @param id the id of the pokemon to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePokemon(@PathVariable Long id) {
        log.debug("REST request to delete Pokemon : {}", id);
        pokemonRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
