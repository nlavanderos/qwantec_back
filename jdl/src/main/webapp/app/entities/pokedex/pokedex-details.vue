<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="pokedex">
        <h2 class="jh-entity-heading" data-cy="pokedexDetailsHeading">
          <span v-text="t$('myApp.pokedex.detail.title')"></span> {{ pokedex.id }}
        </h2>
        <dl class="row jh-entity-details"></dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link v-if="pokedex.id" :to="{ name: 'PokedexEdit', params: { pokedexId: pokedex.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./pokedex-details.component.ts"></script>
