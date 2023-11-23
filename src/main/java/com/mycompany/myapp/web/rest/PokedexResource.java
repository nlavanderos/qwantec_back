package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Pokedex;
import com.mycompany.myapp.repository.PokedexRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Pokedex}.
 */
@RestController
@RequestMapping("/api/pokedexes")
@Transactional
public class PokedexResource {

    private final Logger log = LoggerFactory.getLogger(PokedexResource.class);

    private static final String ENTITY_NAME = "pokedex";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PokedexRepository pokedexRepository;

    public PokedexResource(PokedexRepository pokedexRepository) {
        this.pokedexRepository = pokedexRepository;
    }

    /**
     * {@code POST  /pokedexes} : Create a new pokedex.
     *
     * @param pokedex the pokedex to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pokedex, or with status {@code 400 (Bad Request)} if the pokedex has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Pokedex> createPokedex(@RequestBody Pokedex pokedex) throws URISyntaxException {
        log.debug("REST request to save Pokedex : {}", pokedex);
        if (pokedex.getId() != null) {
            throw new BadRequestAlertException("A new pokedex cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Pokedex result = pokedexRepository.save(pokedex);
        return ResponseEntity
            .created(new URI("/api/pokedexes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pokedexes/:id} : Updates an existing pokedex.
     *
     * @param id the id of the pokedex to save.
     * @param pokedex the pokedex to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pokedex,
     * or with status {@code 400 (Bad Request)} if the pokedex is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pokedex couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Pokedex> updatePokedex(@PathVariable(value = "id", required = false) final Long id, @RequestBody Pokedex pokedex)
        throws URISyntaxException {
        log.debug("REST request to update Pokedex : {}, {}", id, pokedex);
        if (pokedex.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pokedex.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pokedexRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        // no save call needed as we have no fields that can be updated
        Pokedex result = pokedex;
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pokedex.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /pokedexes/:id} : Partial updates given fields of an existing pokedex, field will ignore if it is null
     *
     * @param id the id of the pokedex to save.
     * @param pokedex the pokedex to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pokedex,
     * or with status {@code 400 (Bad Request)} if the pokedex is not valid,
     * or with status {@code 404 (Not Found)} if the pokedex is not found,
     * or with status {@code 500 (Internal Server Error)} if the pokedex couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Pokedex> partialUpdatePokedex(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Pokedex pokedex
    ) throws URISyntaxException {
        log.debug("REST request to partial update Pokedex partially : {}, {}", id, pokedex);
        if (pokedex.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pokedex.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pokedexRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Pokedex> result = pokedexRepository.findById(pokedex.getId()); // .map(pokedexRepository::save)

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pokedex.getId().toString())
        );
    }

    /**
     * {@code GET  /pokedexes} : get all the pokedexes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pokedexes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Pokedex>> getAllPokedexes(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Pokedexes");
        Page<Pokedex> page = pokedexRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pokedexes/:id} : get the "id" pokedex.
     *
     * @param id the id of the pokedex to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pokedex, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Pokedex> getPokedex(@PathVariable Long id) {
        log.debug("REST request to get Pokedex : {}", id);
        Optional<Pokedex> pokedex = pokedexRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(pokedex);
    }

    /**
     * {@code DELETE  /pokedexes/:id} : delete the "id" pokedex.
     *
     * @param id the id of the pokedex to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePokedex(@PathVariable Long id) {
        log.debug("REST request to delete Pokedex : {}", id);
        pokedexRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
