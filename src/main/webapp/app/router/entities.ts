import { Authority } from '@/shared/security/authority';
/* tslint:disable */
// prettier-ignore
const Entities = () => import('@/entities/entities.vue');

const Pokemon = () => import('@/entities/pokemon/pokemon.vue');
const PokemonUpdate = () => import('@/entities/pokemon/pokemon-update.vue');
const PokemonDetails = () => import('@/entities/pokemon/pokemon-details.vue');

const Pokedex = () => import('@/entities/pokedex/pokedex.vue');
const PokedexUpdate = () => import('@/entities/pokedex/pokedex-update.vue');
const PokedexDetails = () => import('@/entities/pokedex/pokedex-details.vue');

// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default {
  path: '/',
  component: Entities,
  children: [
    {
      path: 'pokemon',
      name: 'Pokemon',
      component: Pokemon,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'pokemon/new',
      name: 'PokemonCreate',
      component: PokemonUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'pokemon/:pokemonId/edit',
      name: 'PokemonEdit',
      component: PokemonUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'pokemon/:pokemonId/view',
      name: 'PokemonView',
      component: PokemonDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'pokedex',
      name: 'Pokedex',
      component: Pokedex,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'pokedex/new',
      name: 'PokedexCreate',
      component: PokedexUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'pokedex/:pokedexId/edit',
      name: 'PokedexEdit',
      component: PokedexUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'pokedex/:pokedexId/view',
      name: 'PokedexView',
      component: PokedexDetails,
      meta: { authorities: [Authority.USER] },
    },
    // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
  ],
};
