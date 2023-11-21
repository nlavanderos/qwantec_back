import { defineComponent, inject, onMounted, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';

import PokemonService from './pokemon.service';
import { type IPokemon } from '@/shared/model/pokemon.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'Pokemon',
  setup() {
    const { t: t$ } = useI18n();
    const pokemonService = inject('pokemonService', () => new PokemonService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const pokemon: Ref<IPokemon[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrievePokemons = async () => {
      isFetching.value = true;
      try {
        const res = await pokemonService().retrieve();
        pokemon.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrievePokemons();
    };

    onMounted(async () => {
      await retrievePokemons();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: IPokemon) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removePokemon = async () => {
      try {
        await pokemonService().delete(removeId.value);
        const message = t$('myApp.pokemon.deleted', { param: removeId.value }).toString();
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrievePokemons();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      pokemon,
      handleSyncList,
      isFetching,
      retrievePokemons,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removePokemon,
      t$,
    };
  },
});
