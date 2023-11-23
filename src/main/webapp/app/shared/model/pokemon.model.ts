import { type IPokedex } from '@/shared/model/pokedex.model';

export interface IPokemon {
  id?: number;
  nombre?: string;
  tipo?: string;
  elemento?: string;
  region?: string;
  pokemon?: IPokedex | null;
}

export class Pokemon implements IPokemon {
  constructor(
    public id?: number,
    public nombre?: string,
    public tipo?: string,
    public elemento?: string,
    public region?: string,
    public pokemon?: IPokedex | null,
  ) {}
}
