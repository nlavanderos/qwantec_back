import { computed, defineComponent, inject, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import PokedexService from './pokedex.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { type IPokedex, Pokedex } from '@/shared/model/pokedex.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'PokedexUpdate',
  setup() {
    const pokedexService = inject('pokedexService', () => new PokedexService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const pokedex: Ref<IPokedex> = ref(new Pokedex());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

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

    const initRelationships = () => {};

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      pokemon: {},
    };
    const v$ = useVuelidate(validationRules, pokedex as any);
    v$.value.$validate();

    return {
      pokedexService,
      alertService,
      pokedex,
      previousState,
      isSaving,
      currentLanguage,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.pokedex.id) {
        this.pokedexService()
          .update(this.pokedex)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('myApp.pokedex.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.pokedexService()
          .create(this.pokedex)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('myApp.pokedex.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
