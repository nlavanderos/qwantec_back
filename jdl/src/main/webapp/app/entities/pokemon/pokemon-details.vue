<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="pokemon">
        <h2 class="jh-entity-heading" data-cy="pokemonDetailsHeading">
          <span v-text="t$('myApp.pokemon.detail.title')"></span> {{ pokemon.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('myApp.pokemon.nombre')"></span>
          </dt>
          <dd>
            <span>{{ pokemon.nombre }}</span>
          </dd>
          <dt>
            <span v-text="t$('myApp.pokemon.tipo')"></span>
          </dt>
          <dd>
            <span>{{ pokemon.tipo }}</span>
          </dd>
          <dt>
            <span v-text="t$('myApp.pokemon.elemento')"></span>
          </dt>
          <dd>
            <span>{{ pokemon.elemento }}</span>
          </dd>
          <dt>
            <span v-text="t$('myApp.pokemon.region')"></span>
          </dt>
          <dd>
            <span>{{ pokemon.region }}</span>
          </dd>
          <dt>
            <span v-text="t$('myApp.pokemon.pokemon')"></span>
          </dt>
          <dd>
            <div v-if="pokemon.pokemon">
              <router-link :to="{ name: 'PokedexView', params: { pokedexId: pokemon.pokemon.id } }">{{ pokemon.pokemon.id }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link v-if="pokemon.id" :to="{ name: 'PokemonEdit', params: { pokemonId: pokemon.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./pokemon-details.component.ts"></script>
