import { defineComponent, inject, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import PokemonService from './pokemon.service';
import { type IPokemon } from '@/shared/model/pokemon.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'PokemonDetails',
  setup() {
    const pokemonService = inject('pokemonService', () => new PokemonService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const pokemon: Ref<IPokemon> = ref({});

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

    return {
      alertService,
      pokemon,

      previousState,
      t$: useI18n().t,
    };
  },
});
