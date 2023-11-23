import { defineComponent, provide } from 'vue';

import PokemonService from './pokemon/pokemon.service';
import PokedexService from './pokedex/pokedex.service';
import UserService from '@/entities/user/user.service';
// jhipster-needle-add-entity-service-to-entities-component-import - JHipster will import entities services here

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'Entities',
  setup() {
    provide('userService', () => new UserService());
    provide('pokemonService', () => new PokemonService());
    provide('pokedexService', () => new PokedexService());
    // jhipster-needle-add-entity-service-to-entities-component - JHipster will import entities services here
  },
});
