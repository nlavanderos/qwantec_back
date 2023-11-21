<template>
  <div>
    <h2 id="page-heading" data-cy="PokemonHeading">
      <span v-text="t$('myApp.pokemon.home.title')" id="pokemon-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('myApp.pokemon.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'PokemonCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-pokemon"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('myApp.pokemon.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && pokemon && pokemon.length === 0">
      <span v-text="t$('myApp.pokemon.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="pokemon && pokemon.length > 0">
      <table class="table table-striped" aria-describedby="pokemon">
        <thead>
          <tr>
            <th scope="row"><span v-text="t$('global.field.id')"></span></th>
            <th scope="row"><span v-text="t$('myApp.pokemon.nombre')"></span></th>
            <th scope="row"><span v-text="t$('myApp.pokemon.tipo')"></span></th>
            <th scope="row"><span v-text="t$('myApp.pokemon.elemento')"></span></th>
            <th scope="row"><span v-text="t$('myApp.pokemon.region')"></span></th>
            <th scope="row"><span v-text="t$('myApp.pokemon.pokemon')"></span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="pokemon in pokemon" :key="pokemon.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'PokemonView', params: { pokemonId: pokemon.id } }">{{ pokemon.id }}</router-link>
            </td>
            <td>{{ pokemon.nombre }}</td>
            <td>{{ pokemon.tipo }}</td>
            <td>{{ pokemon.elemento }}</td>
            <td>{{ pokemon.region }}</td>
            <td>
              <div v-if="pokemon.pokemon">
                <router-link :to="{ name: 'PokedexView', params: { pokedexId: pokemon.pokemon.id } }">{{ pokemon.pokemon.id }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'PokemonView', params: { pokemonId: pokemon.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'PokemonEdit', params: { pokemonId: pokemon.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(pokemon)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="t$('entity.action.delete')"></span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <template #modal-title>
        <span id="myApp.pokemon.delete.question" data-cy="pokemonDeleteDialogHeading" v-text="t$('entity.delete.title')"></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-pokemon-heading" v-text="t$('myApp.pokemon.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" v-on:click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-pokemon"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            v-on:click="removePokemon()"
          ></button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./pokemon.component.ts"></script>
