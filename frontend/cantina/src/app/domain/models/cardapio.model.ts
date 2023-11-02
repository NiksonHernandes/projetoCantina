import { Alimento } from "./alimento.model";
import { Bebida } from "./bebida.model";

export class Cardapio {

    id?: number;
    promocoes?: string;

    alimentos?: Alimento[];
    bebidas?: Bebida[];

}