import { type IPokemon } from '@/shared/model/pokemon.model';

export interface IPokedex {
  id?: number;
  pokemon?: IPokemon[] | null;
}

export class Pokedex implements IPokedex {
  constructor(
    public id?: number,
    public pokemon?: IPokemon[] | null,
  ) {}
}
