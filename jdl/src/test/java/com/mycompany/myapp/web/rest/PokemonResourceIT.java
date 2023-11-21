package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Pokemon;
import com.mycompany.myapp.repository.PokemonRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PokemonResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PokemonResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_TIPO = "AAAAAAAAAA";
    private static final String UPDATED_TIPO = "BBBBBBBBBB";

    private static final String DEFAULT_ELEMENTO = "AAAAAAAAAA";
    private static final String UPDATED_ELEMENTO = "BBBBBBBBBB";

    private static final String DEFAULT_REGION = "AAAAAAAAAA";
    private static final String UPDATED_REGION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/pokemon";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PokemonRepository pokemonRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPokemonMockMvc;

    private Pokemon pokemon;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pokemon createEntity(EntityManager em) {
        Pokemon pokemon = new Pokemon().nombre(DEFAULT_NOMBRE).tipo(DEFAULT_TIPO).elemento(DEFAULT_ELEMENTO).region(DEFAULT_REGION);
        return pokemon;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pokemon createUpdatedEntity(EntityManager em) {
        Pokemon pokemon = new Pokemon().nombre(UPDATED_NOMBRE).tipo(UPDATED_TIPO).elemento(UPDATED_ELEMENTO).region(UPDATED_REGION);
        return pokemon;
    }

    @BeforeEach
    public void initTest() {
        pokemon = createEntity(em);
    }

    @Test
    @Transactional
    void createPokemon() throws Exception {
        int databaseSizeBeforeCreate = pokemonRepository.findAll().size();
        // Create the Pokemon
        restPokemonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pokemon)))
            .andExpect(status().isCreated());

        // Validate the Pokemon in the database
        List<Pokemon> pokemonList = pokemonRepository.findAll();
        assertThat(pokemonList).hasSize(databaseSizeBeforeCreate + 1);
        Pokemon testPokemon = pokemonList.get(pokemonList.size() - 1);
        assertThat(testPokemon.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testPokemon.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testPokemon.getElemento()).isEqualTo(DEFAULT_ELEMENTO);
        assertThat(testPokemon.getRegion()).isEqualTo(DEFAULT_REGION);
    }

    @Test
    @Transactional
    void createPokemonWithExistingId() throws Exception {
        // Create the Pokemon with an existing ID
        pokemon.setId(1L);

        int databaseSizeBeforeCreate = pokemonRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPokemonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pokemon)))
            .andExpect(status().isBadRequest());

        // Validate the Pokemon in the database
        List<Pokemon> pokemonList = pokemonRepository.findAll();
        assertThat(pokemonList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = pokemonRepository.findAll().size();
        // set the field null
        pokemon.setNombre(null);

        // Create the Pokemon, which fails.

        restPokemonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pokemon)))
            .andExpect(status().isBadRequest());

        List<Pokemon> pokemonList = pokemonRepository.findAll();
        assertThat(pokemonList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTipoIsRequired() throws Exception {
        int databaseSizeBeforeTest = pokemonRepository.findAll().size();
        // set the field null
        pokemon.setTipo(null);

        // Create the Pokemon, which fails.

        restPokemonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pokemon)))
            .andExpect(status().isBadRequest());

        List<Pokemon> pokemonList = pokemonRepository.findAll();
        assertThat(pokemonList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkElementoIsRequired() throws Exception {
        int databaseSizeBeforeTest = pokemonRepository.findAll().size();
        // set the field null
        pokemon.setElemento(null);

        // Create the Pokemon, which fails.

        restPokemonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pokemon)))
            .andExpect(status().isBadRequest());

        List<Pokemon> pokemonList = pokemonRepository.findAll();
        assertThat(pokemonList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRegionIsRequired() throws Exception {
        int databaseSizeBeforeTest = pokemonRepository.findAll().size();
        // set the field null
        pokemon.setRegion(null);

        // Create the Pokemon, which fails.

        restPokemonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pokemon)))
            .andExpect(status().isBadRequest());

        List<Pokemon> pokemonList = pokemonRepository.findAll();
        assertThat(pokemonList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPokemon() throws Exception {
        // Initialize the database
        pokemonRepository.saveAndFlush(pokemon);

        // Get all the pokemonList
        restPokemonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pokemon.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO)))
            .andExpect(jsonPath("$.[*].elemento").value(hasItem(DEFAULT_ELEMENTO)))
            .andExpect(jsonPath("$.[*].region").value(hasItem(DEFAULT_REGION)));
    }

    @Test
    @Transactional
    void getPokemon() throws Exception {
        // Initialize the database
        pokemonRepository.saveAndFlush(pokemon);

        // Get the pokemon
        restPokemonMockMvc
            .perform(get(ENTITY_API_URL_ID, pokemon.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pokemon.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO))
            .andExpect(jsonPath("$.elemento").value(DEFAULT_ELEMENTO))
            .andExpect(jsonPath("$.region").value(DEFAULT_REGION));
    }

    @Test
    @Transactional
    void getNonExistingPokemon() throws Exception {
        // Get the pokemon
        restPokemonMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPokemon() throws Exception {
        // Initialize the database
        pokemonRepository.saveAndFlush(pokemon);

        int databaseSizeBeforeUpdate = pokemonRepository.findAll().size();

        // Update the pokemon
        Pokemon updatedPokemon = pokemonRepository.findById(pokemon.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPokemon are not directly saved in db
        em.detach(updatedPokemon);
        updatedPokemon.nombre(UPDATED_NOMBRE).tipo(UPDATED_TIPO).elemento(UPDATED_ELEMENTO).region(UPDATED_REGION);

        restPokemonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPokemon.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPokemon))
            )
            .andExpect(status().isOk());

        // Validate the Pokemon in the database
        List<Pokemon> pokemonList = pokemonRepository.findAll();
        assertThat(pokemonList).hasSize(databaseSizeBeforeUpdate);
        Pokemon testPokemon = pokemonList.get(pokemonList.size() - 1);
        assertThat(testPokemon.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testPokemon.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testPokemon.getElemento()).isEqualTo(UPDATED_ELEMENTO);
        assertThat(testPokemon.getRegion()).isEqualTo(UPDATED_REGION);
    }

    @Test
    @Transactional
    void putNonExistingPokemon() throws Exception {
        int databaseSizeBeforeUpdate = pokemonRepository.findAll().size();
        pokemon.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPokemonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pokemon.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pokemon))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pokemon in the database
        List<Pokemon> pokemonList = pokemonRepository.findAll();
        assertThat(pokemonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPokemon() throws Exception {
        int databaseSizeBeforeUpdate = pokemonRepository.findAll().size();
        pokemon.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPokemonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pokemon))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pokemon in the database
        List<Pokemon> pokemonList = pokemonRepository.findAll();
        assertThat(pokemonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPokemon() throws Exception {
        int databaseSizeBeforeUpdate = pokemonRepository.findAll().size();
        pokemon.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPokemonMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pokemon)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pokemon in the database
        List<Pokemon> pokemonList = pokemonRepository.findAll();
        assertThat(pokemonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePokemonWithPatch() throws Exception {
        // Initialize the database
        pokemonRepository.saveAndFlush(pokemon);

        int databaseSizeBeforeUpdate = pokemonRepository.findAll().size();

        // Update the pokemon using partial update
        Pokemon partialUpdatedPokemon = new Pokemon();
        partialUpdatedPokemon.setId(pokemon.getId());

        partialUpdatedPokemon.nombre(UPDATED_NOMBRE).tipo(UPDATED_TIPO).elemento(UPDATED_ELEMENTO).region(UPDATED_REGION);

        restPokemonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPokemon.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPokemon))
            )
            .andExpect(status().isOk());

        // Validate the Pokemon in the database
        List<Pokemon> pokemonList = pokemonRepository.findAll();
        assertThat(pokemonList).hasSize(databaseSizeBeforeUpdate);
        Pokemon testPokemon = pokemonList.get(pokemonList.size() - 1);
        assertThat(testPokemon.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testPokemon.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testPokemon.getElemento()).isEqualTo(UPDATED_ELEMENTO);
        assertThat(testPokemon.getRegion()).isEqualTo(UPDATED_REGION);
    }

    @Test
    @Transactional
    void fullUpdatePokemonWithPatch() throws Exception {
        // Initialize the database
        pokemonRepository.saveAndFlush(pokemon);

        int databaseSizeBeforeUpdate = pokemonRepository.findAll().size();

        // Update the pokemon using partial update
        Pokemon partialUpdatedPokemon = new Pokemon();
        partialUpdatedPokemon.setId(pokemon.getId());

        partialUpdatedPokemon.nombre(UPDATED_NOMBRE).tipo(UPDATED_TIPO).elemento(UPDATED_ELEMENTO).region(UPDATED_REGION);

        restPokemonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPokemon.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPokemon))
            )
            .andExpect(status().isOk());

        // Validate the Pokemon in the database
        List<Pokemon> pokemonList = pokemonRepository.findAll();
        assertThat(pokemonList).hasSize(databaseSizeBeforeUpdate);
        Pokemon testPokemon = pokemonList.get(pokemonList.size() - 1);
        assertThat(testPokemon.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testPokemon.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testPokemon.getElemento()).isEqualTo(UPDATED_ELEMENTO);
        assertThat(testPokemon.getRegion()).isEqualTo(UPDATED_REGION);
    }

    @Test
    @Transactional
    void patchNonExistingPokemon() throws Exception {
        int databaseSizeBeforeUpdate = pokemonRepository.findAll().size();
        pokemon.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPokemonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pokemon.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pokemon))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pokemon in the database
        List<Pokemon> pokemonList = pokemonRepository.findAll();
        assertThat(pokemonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPokemon() throws Exception {
        int databaseSizeBeforeUpdate = pokemonRepository.findAll().size();
        pokemon.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPokemonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pokemon))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pokemon in the database
        List<Pokemon> pokemonList = pokemonRepository.findAll();
        assertThat(pokemonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPokemon() throws Exception {
        int databaseSizeBeforeUpdate = pokemonRepository.findAll().size();
        pokemon.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPokemonMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(pokemon)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pokemon in the database
        List<Pokemon> pokemonList = pokemonRepository.findAll();
        assertThat(pokemonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePokemon() throws Exception {
        // Initialize the database
        pokemonRepository.saveAndFlush(pokemon);

        int databaseSizeBeforeDelete = pokemonRepository.findAll().size();

        // Delete the pokemon
        restPokemonMockMvc
            .perform(delete(ENTITY_API_URL_ID, pokemon.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Pokemon> pokemonList = pokemonRepository.findAll();
        assertThat(pokemonList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
