/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import Pokemon from './pokemon.vue';
import PokemonService from './pokemon.service';
import AlertService from '@/shared/alert/alert.service';

type PokemonComponentType = InstanceType<typeof Pokemon>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('Pokemon Management Component', () => {
    let pokemonServiceStub: SinonStubbedInstance<PokemonService>;
    let mountOptions: MountingOptions<PokemonComponentType>['global'];

    beforeEach(() => {
      pokemonServiceStub = sinon.createStubInstance<PokemonService>(PokemonService);
      pokemonServiceStub.retrieve.resolves({ headers: {} });

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          bModal: bModalStub as any,
          'font-awesome-icon': true,
          'b-badge': true,
          'b-button': true,
          'router-link': true,
        },
        directives: {
          'b-modal': {},
        },
        provide: {
          alertService,
          pokemonService: () => pokemonServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        pokemonServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(Pokemon, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(pokemonServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.pokemon[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: PokemonComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(Pokemon, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        pokemonServiceStub.retrieve.reset();
        pokemonServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        pokemonServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removePokemon();
        await comp.$nextTick(); // clear components

        // THEN
        expect(pokemonServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(pokemonServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
