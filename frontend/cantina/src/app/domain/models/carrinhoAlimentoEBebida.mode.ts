import { Alimento } from "./alimento.model";
import { Bebida } from "./bebida.model";

export class CarrinhoAlimentoEBebida {

    valorTotal?: number;
    descricaoDaCompra?: string;
    carrinhoFechado?: boolean;
    dataPedido?: Date;
    
    alimentos?: Alimento[];
    bebidas?: Bebida[];
    
}