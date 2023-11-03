import { Alimento } from "./alimento.model";
import { Bebida } from "./bebida.model";

export class CarrinhoAlimentoEBebida {

    carrinhoId?: number;
    valorTotal?: number;
    descricaoDaCompra?: string;
    carrinhoFechado?: boolean;
    dataPedido?: Date;
    
    alimentos?: Alimento[];
    bebidas?: Bebida[];
    
}