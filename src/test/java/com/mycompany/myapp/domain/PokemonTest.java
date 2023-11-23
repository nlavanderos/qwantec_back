package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.PokedexTestSamples.*;
import static com.mycompany.myapp.domain.PokemonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PokemonTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pokemon.class);
        Pokemon pokemon1 = getPokemonSample1();
        Pokemon pokemon2 = new Pokemon();
        assertThat(pokemon1).isNotEqualTo(pokemon2);

        pokemon2.setId(pokemon1.getId());
        assertThat(pokemon1).isEqualTo(pokemon2);

        pokemon2 = getPokemonSample2();
        assertThat(pokemon1).isNotEqualTo(pokemon2);
    }

    @Test
    void pokemonTest() throws Exception {
        Pokemon pokemon = getPokemonRandomSampleGenerator();
        Pokedex pokedexBack = getPokedexRandomSampleGenerator();

        pokemon.setPokemon(pokedexBack);
        assertThat(pokemon.getPokemon()).isEqualTo(pokedexBack);

        pokemon.pokemon(null);
        assertThat(pokemon.getPokemon()).isNull();
    }
}
