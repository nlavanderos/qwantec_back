/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import PokedexUpdate from './pokedex-update.vue';
import PokedexService from './pokedex.service';
import AlertService from '@/shared/alert/alert.service';

type PokedexUpdateComponentType = InstanceType<typeof PokedexUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const pokedexSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<PokedexUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Pokedex Management Update Component', () => {
    let comp: PokedexUpdateComponentType;
    let pokedexServiceStub: SinonStubbedInstance<PokedexService>;

    beforeEach(() => {
      route = {};
      pokedexServiceStub = sinon.createStubInstance<PokedexService>(PokedexService);
      pokedexServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          pokedexService: () => pokedexServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(PokedexUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.pokedex = pokedexSample;
        pokedexServiceStub.update.resolves(pokedexSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(pokedexServiceStub.update.calledWith(pokedexSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        pokedexServiceStub.create.resolves(entity);
        const wrapper = shallowMount(PokedexUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.pokedex = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(pokedexServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        pokedexServiceStub.find.resolves(pokedexSample);
        pokedexServiceStub.retrieve.resolves([pokedexSample]);

        // WHEN
        route = {
          params: {
            pokedexId: '' + pokedexSample.id,
          },
        };
        const wrapper = shallowMount(PokedexUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.pokedex).toMatchObject(pokedexSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        pokedexServiceStub.find.resolves(pokedexSample);
        const wrapper = shallowMount(PokedexUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
