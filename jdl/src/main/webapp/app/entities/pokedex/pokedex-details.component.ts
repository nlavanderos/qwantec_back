import { defineComponent, inject, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import PokedexService from './pokedex.service';
import { type IPokedex } from '@/shared/model/pokedex.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'PokedexDetails',
  setup() {
    const pokedexService = inject('pokedexService', () => new PokedexService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const pokedex: Ref<IPokedex> = ref({});

    const retrievePokedex = async pokedexId => {
      try {
        const res = await pokedexService().find(pokedexId);
        pokedex.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.pokedexId) {
      retrievePokedex(route.params.pokedexId);
    }

    return {
      alertService,
      pokedex,

      previousState,
      t$: useI18n().t,
    };
  },
});
