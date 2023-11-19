import { Alimento } from "./alimento.model";
import { Bebida } from "./bebida.model";

export class CarrinhoAlimentoEBebida {

    carrinhoId?: number;
    valorTotal?: number;
    descricaoDaCompra?: string;
    carrinhoFechado?: boolean;
    dataPedido?: Date;
    opcaoPagamento?: number;

    statusPedido?: number;
    tipoCartao?: number;
    numeroCartao?: number;
    validadeCartao?: string;
    codigoCartao?: number;
    codigoDoPedido?: string;
    
    alimentos?: Alimento[];
    bebidas?: Bebida[];
    
}