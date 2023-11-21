package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.PokedexTestSamples.*;
import static com.mycompany.myapp.domain.PokemonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PokedexTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pokedex.class);
        Pokedex pokedex1 = getPokedexSample1();
        Pokedex pokedex2 = new Pokedex();
        assertThat(pokedex1).isNotEqualTo(pokedex2);

        pokedex2.setId(pokedex1.getId());
        assertThat(pokedex1).isEqualTo(pokedex2);

        pokedex2 = getPokedexSample2();
        assertThat(pokedex1).isNotEqualTo(pokedex2);
    }

    @Test
    void pokemonTest() throws Exception {
        Pokedex pokedex = getPokedexRandomSampleGenerator();
        Pokemon pokemonBack = getPokemonRandomSampleGenerator();

        pokedex.addPokemon(pokemonBack);
        assertThat(pokedex.getPokemon()).containsOnly(pokemonBack);
        assertThat(pokemonBack.getPokemon()).isEqualTo(pokedex);

        pokedex.removePokemon(pokemonBack);
        assertThat(pokedex.getPokemon()).doesNotContain(pokemonBack);
        assertThat(pokemonBack.getPokemon()).isNull();

        pokedex.pokemon(new HashSet<>(Set.of(pokemonBack)));
        assertThat(pokedex.getPokemon()).containsOnly(pokemonBack);
        assertThat(pokemonBack.getPokemon()).isEqualTo(pokedex);

        pokedex.setPokemon(new HashSet<>());
        assertThat(pokedex.getPokemon()).doesNotContain(pokemonBack);
        assertThat(pokemonBack.getPokemon()).isNull();
    }
}
