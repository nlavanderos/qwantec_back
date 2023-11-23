package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Pokedex;
import com.mycompany.myapp.repository.PokedexRepository;
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
 * Integration tests for the {@link PokedexResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PokedexResourceIT {

    private static final String ENTITY_API_URL = "/api/pokedexes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PokedexRepository pokedexRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPokedexMockMvc;

    private Pokedex pokedex;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pokedex createEntity(EntityManager em) {
        Pokedex pokedex = new Pokedex();
        return pokedex;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pokedex createUpdatedEntity(EntityManager em) {
        Pokedex pokedex = new Pokedex();
        return pokedex;
    }

    @BeforeEach
    public void initTest() {
        pokedex = createEntity(em);
    }

    @Test
    @Transactional
    void createPokedex() throws Exception {
        int databaseSizeBeforeCreate = pokedexRepository.findAll().size();
        // Create the Pokedex
        restPokedexMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pokedex)))
            .andExpect(status().isCreated());

        // Validate the Pokedex in the database
        List<Pokedex> pokedexList = pokedexRepository.findAll();
        assertThat(pokedexList).hasSize(databaseSizeBeforeCreate + 1);
        Pokedex testPokedex = pokedexList.get(pokedexList.size() - 1);
    }

    @Test
    @Transactional
    void createPokedexWithExistingId() throws Exception {
        // Create the Pokedex with an existing ID
        pokedex.setId(1L);

        int databaseSizeBeforeCreate = pokedexRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPokedexMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pokedex)))
            .andExpect(status().isBadRequest());

        // Validate the Pokedex in the database
        List<Pokedex> pokedexList = pokedexRepository.findAll();
        assertThat(pokedexList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPokedexes() throws Exception {
        // Initialize the database
        pokedexRepository.saveAndFlush(pokedex);

        // Get all the pokedexList
        restPokedexMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pokedex.getId().intValue())));
    }

    @Test
    @Transactional
    void getPokedex() throws Exception {
        // Initialize the database
        pokedexRepository.saveAndFlush(pokedex);

        // Get the pokedex
        restPokedexMockMvc
            .perform(get(ENTITY_API_URL_ID, pokedex.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pokedex.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingPokedex() throws Exception {
        // Get the pokedex
        restPokedexMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPokedex() throws Exception {
        // Initialize the database
        pokedexRepository.saveAndFlush(pokedex);

        int databaseSizeBeforeUpdate = pokedexRepository.findAll().size();

        // Update the pokedex
        Pokedex updatedPokedex = pokedexRepository.findById(pokedex.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPokedex are not directly saved in db
        em.detach(updatedPokedex);

        restPokedexMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPokedex.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPokedex))
            )
            .andExpect(status().isOk());

        // Validate the Pokedex in the database
        List<Pokedex> pokedexList = pokedexRepository.findAll();
        assertThat(pokedexList).hasSize(databaseSizeBeforeUpdate);
        Pokedex testPokedex = pokedexList.get(pokedexList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingPokedex() throws Exception {
        int databaseSizeBeforeUpdate = pokedexRepository.findAll().size();
        pokedex.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPokedexMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pokedex.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pokedex))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pokedex in the database
        List<Pokedex> pokedexList = pokedexRepository.findAll();
        assertThat(pokedexList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPokedex() throws Exception {
        int databaseSizeBeforeUpdate = pokedexRepository.findAll().size();
        pokedex.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPokedexMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pokedex))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pokedex in the database
        List<Pokedex> pokedexList = pokedexRepository.findAll();
        assertThat(pokedexList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPokedex() throws Exception {
        int databaseSizeBeforeUpdate = pokedexRepository.findAll().size();
        pokedex.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPokedexMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pokedex)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pokedex in the database
        List<Pokedex> pokedexList = pokedexRepository.findAll();
        assertThat(pokedexList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePokedexWithPatch() throws Exception {
        // Initialize the database
        pokedexRepository.saveAndFlush(pokedex);

        int databaseSizeBeforeUpdate = pokedexRepository.findAll().size();

        // Update the pokedex using partial update
        Pokedex partialUpdatedPokedex = new Pokedex();
        partialUpdatedPokedex.setId(pokedex.getId());

        restPokedexMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPokedex.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPokedex))
            )
            .andExpect(status().isOk());

        // Validate the Pokedex in the database
        List<Pokedex> pokedexList = pokedexRepository.findAll();
        assertThat(pokedexList).hasSize(databaseSizeBeforeUpdate);
        Pokedex testPokedex = pokedexList.get(pokedexList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdatePokedexWithPatch() throws Exception {
        // Initialize the database
        pokedexRepository.saveAndFlush(pokedex);

        int databaseSizeBeforeUpdate = pokedexRepository.findAll().size();

        // Update the pokedex using partial update
        Pokedex partialUpdatedPokedex = new Pokedex();
        partialUpdatedPokedex.setId(pokedex.getId());

        restPokedexMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPokedex.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPokedex))
            )
            .andExpect(status().isOk());

        // Validate the Pokedex in the database
        List<Pokedex> pokedexList = pokedexRepository.findAll();
        assertThat(pokedexList).hasSize(databaseSizeBeforeUpdate);
        Pokedex testPokedex = pokedexList.get(pokedexList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingPokedex() throws Exception {
        int databaseSizeBeforeUpdate = pokedexRepository.findAll().size();
        pokedex.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPokedexMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pokedex.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pokedex))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pokedex in the database
        List<Pokedex> pokedexList = pokedexRepository.findAll();
        assertThat(pokedexList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPokedex() throws Exception {
        int databaseSizeBeforeUpdate = pokedexRepository.findAll().size();
        pokedex.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPokedexMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pokedex))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pokedex in the database
        List<Pokedex> pokedexList = pokedexRepository.findAll();
        assertThat(pokedexList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPokedex() throws Exception {
        int databaseSizeBeforeUpdate = pokedexRepository.findAll().size();
        pokedex.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPokedexMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(pokedex)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pokedex in the database
        List<Pokedex> pokedexList = pokedexRepository.findAll();
        assertThat(pokedexList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePokedex() throws Exception {
        // Initialize the database
        pokedexRepository.saveAndFlush(pokedex);

        int databaseSizeBeforeDelete = pokedexRepository.findAll().size();

        // Delete the pokedex
        restPokedexMockMvc
            .perform(delete(ENTITY_API_URL_ID, pokedex.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Pokedex> pokedexList = pokedexRepository.findAll();
        assertThat(pokedexList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
