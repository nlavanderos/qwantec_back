/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import PokedexDetails from './pokedex-details.vue';
import PokedexService from './pokedex.service';
import AlertService from '@/shared/alert/alert.service';

type PokedexDetailsComponentType = InstanceType<typeof PokedexDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const pokedexSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Pokedex Management Detail Component', () => {
    let pokedexServiceStub: SinonStubbedInstance<PokedexService>;
    let mountOptions: MountingOptions<PokedexDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      pokedexServiceStub = sinon.createStubInstance<PokedexService>(PokedexService);

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'router-link': true,
        },
        provide: {
          alertService,
          pokedexService: () => pokedexServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        pokedexServiceStub.find.resolves(pokedexSample);
        route = {
          params: {
            pokedexId: '' + 123,
          },
        };
        const wrapper = shallowMount(PokedexDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.pokedex).toMatchObject(pokedexSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        pokedexServiceStub.find.resolves(pokedexSample);
        const wrapper = shallowMount(PokedexDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
