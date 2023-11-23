<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="myApp.pokemon.home.createOrEditLabel"
          data-cy="PokemonCreateUpdateHeading"
          v-text="t$('myApp.pokemon.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="pokemon.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="pokemon.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('myApp.pokemon.nombre')" for="pokemon-nombre"></label>
            <input
              type="text"
              class="form-control"
              name="nombre"
              id="pokemon-nombre"
              data-cy="nombre"
              :class="{ valid: !v$.nombre.$invalid, invalid: v$.nombre.$invalid }"
              v-model="v$.nombre.$model"
              required
            />
            <div v-if="v$.nombre.$anyDirty && v$.nombre.$invalid">
              <small class="form-text text-danger" v-for="error of v$.nombre.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('myApp.pokemon.tipo')" for="pokemon-tipo"></label>
            <input
              type="text"
              class="form-control"
              name="tipo"
              id="pokemon-tipo"
              data-cy="tipo"
              :class="{ valid: !v$.tipo.$invalid, invalid: v$.tipo.$invalid }"
              v-model="v$.tipo.$model"
              required
            />
            <div v-if="v$.tipo.$anyDirty && v$.tipo.$invalid">
              <small class="form-text text-danger" v-for="error of v$.tipo.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('myApp.pokemon.elemento')" for="pokemon-elemento"></label>
            <input
              type="text"
              class="form-control"
              name="elemento"
              id="pokemon-elemento"
              data-cy="elemento"
              :class="{ valid: !v$.elemento.$invalid, invalid: v$.elemento.$invalid }"
              v-model="v$.elemento.$model"
              required
            />
            <div v-if="v$.elemento.$anyDirty && v$.elemento.$invalid">
              <small class="form-text text-danger" v-for="error of v$.elemento.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('myApp.pokemon.region')" for="pokemon-region"></label>
            <input
              type="text"
              class="form-control"
              name="region"
              id="pokemon-region"
              data-cy="region"
              :class="{ valid: !v$.region.$invalid, invalid: v$.region.$invalid }"
              v-model="v$.region.$model"
              required
            />
            <div v-if="v$.region.$anyDirty && v$.region.$invalid">
              <small class="form-text text-danger" v-for="error of v$.region.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('myApp.pokemon.pokemon')" for="pokemon-pokemon"></label>
            <select class="form-control" id="pokemon-pokemon" data-cy="pokemon" name="pokemon" v-model="pokemon.pokemon">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="pokemon.pokemon && pokedexOption.id === pokemon.pokemon.id ? pokemon.pokemon : pokedexOption"
                v-for="pokedexOption in pokedexes"
                :key="pokedexOption.id"
              >
                {{ pokedexOption.id }}
              </option>
            </select>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" v-on:click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.cancel')"></span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="v$.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.save')"></span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./pokemon-update.component.ts"></script>
