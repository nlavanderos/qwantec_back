/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import PokemonUpdate from './pokemon-update.vue';
import PokemonService from './pokemon.service';
import AlertService from '@/shared/alert/alert.service';

import PokedexService from '@/entities/pokedex/pokedex.service';

type PokemonUpdateComponentType = InstanceType<typeof PokemonUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const pokemonSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<PokemonUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Pokemon Management Update Component', () => {
    let comp: PokemonUpdateComponentType;
    let pokemonServiceStub: SinonStubbedInstance<PokemonService>;

    beforeEach(() => {
      route = {};
      pokemonServiceStub = sinon.createStubInstance<PokemonService>(PokemonService);
      pokemonServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'b-input-group': true,
          'b-input-group-prepend': true,
          'b-form-datepicker': true,
          'b-form-input': true,
        },
        provide: {
          alertService,
          pokemonService: () => pokemonServiceStub,
          pokedexService: () =>
            sinon.createStubInstance<PokedexService>(PokedexService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(PokemonUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.pokemon = pokemonSample;
        pokemonServiceStub.update.resolves(pokemonSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(pokemonServiceStub.update.calledWith(pokemonSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        pokemonServiceStub.create.resolves(entity);
        const wrapper = shallowMount(PokemonUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.pokemon = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(pokemonServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        pokemonServiceStub.find.resolves(pokemonSample);
        pokemonServiceStub.retrieve.resolves([pokemonSample]);

        // WHEN
        route = {
          params: {
            pokemonId: '' + pokemonSample.id,
          },
        };
        const wrapper = shallowMount(PokemonUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.pokemon).toMatchObject(pokemonSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        pokemonServiceStub.find.resolves(pokemonSample);
        const wrapper = shallowMount(PokemonUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
