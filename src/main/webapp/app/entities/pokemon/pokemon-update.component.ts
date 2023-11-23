import { computed, defineComponent, inject, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import PokemonService from './pokemon.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import PokedexService from '@/entities/pokedex/pokedex.service';
import { type IPokedex } from '@/shared/model/pokedex.model';
import { type IPokemon, Pokemon } from '@/shared/model/pokemon.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'PokemonUpdate',
  setup() {
    const pokemonService = inject('pokemonService', () => new PokemonService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const pokemon: Ref<IPokemon> = ref(new Pokemon());

    const pokedexService = inject('pokedexService', () => new PokedexService());

    const pokedexes: Ref<IPokedex[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrievePokemon = async pokemonId => {
      try {
        const res = await pokemonService().find(pokemonId);
        pokemon.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.pokemonId) {
      retrievePokemon(route.params.pokemonId);
    }

    const initRelationships = () => {
      pokedexService()
        .retrieve()
        .then(res => {
          pokedexes.value = res.data;
        });
    };

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      nombre: {
        required: validations.required(t$('entity.validation.required').toString()),
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 25 }).toString(), 25),
      },
      tipo: {
        required: validations.required(t$('entity.validation.required').toString()),
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 15 }).toString(), 15),
      },
      elemento: {
        required: validations.required(t$('entity.validation.required').toString()),
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 15 }).toString(), 15),
      },
      region: {
        required: validations.required(t$('entity.validation.required').toString()),
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 25 }).toString(), 25),
      },
      pokemon: {},
    };
    const v$ = useVuelidate(validationRules, pokemon as any);
    v$.value.$validate();

    return {
      pokemonService,
      alertService,
      pokemon,
      previousState,
      isSaving,
      currentLanguage,
      pokedexes,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.pokemon.id) {
        this.pokemonService()
          .update(this.pokemon)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('myApp.pokemon.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.pokemonService()
          .create(this.pokemon)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('myApp.pokemon.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
