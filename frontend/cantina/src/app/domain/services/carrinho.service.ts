import { Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';
import { Cardapio } from '../models/cardapio.model';
import { CarrinhoRepository } from 'src/app/data/repositories/carrinho.repository';
import { Carrinho } from '../models/carrinho.model';
import { CarrinhoAlimentoEBebida } from '../models/carrinhoAlimentoEBebida.mode';
import { Alimento } from '../models/alimento.model';
import { Bebida } from '../models/bebida.model';

@Injectable({
    providedIn: 'root'
})
export class CarrinhoService {

    constructor(private carrinhoRepository: CarrinhoRepository) { }

    adicionarAlimentoNoCarrinho(alimentoEQuantidade: any): Observable<void> {
        return this.carrinhoRepository.adicionarAlimentoNoCarrinho(alimentoEQuantidade).pipe(
            map(data => {
                return data;
            })
        );
    };

    adicionarBebidaNoCarrinho(bebidaEQuantidade: any): Observable<void> {
        return this.carrinhoRepository.adicionarBebidaNoCarrinho(bebidaEQuantidade).pipe(
            map(data => {
                return data;
            })
        );
    };

    getCarrinho(carrinhoId: number): Observable<Carrinho> {
        return this.carrinhoRepository.getCarrinho(carrinhoId).pipe(
            map(data => {
                return data;
            })
        );
    };

    getCarrinhoProdutos(carrinhoId: number): Observable<CarrinhoAlimentoEBebida> {
        return this.carrinhoRepository.getCarrinhoProdutos(carrinhoId).pipe(
            map(data => {
                return data;
            })
        );
    };

    removerAlimento(alimentoCarrinhoEQuantidade: any): Observable<void> {
        return this.carrinhoRepository.removerAlimento(alimentoCarrinhoEQuantidade).pipe(
            map(data => {
                return data;
            })
        );
    };

    removerBebida(bebidaCarrinhoEQuantidade: any): Observable<void> {
        return this.carrinhoRepository.removerBebida(bebidaCarrinhoEQuantidade).pipe(
            map(data => {
                return data;
            })
        );
    };

    verificaIsCarrinhoExiste(): Observable<Carrinho> {
        return this.carrinhoRepository.verificaIsCarrinhoExiste().pipe(
            map(data => {
                return data;
            })
        );
    };

    opcaoPagamento(carrinhoEopcao: any): Observable<void> {
        return this.carrinhoRepository.opcaoPagamento(carrinhoEopcao).pipe(
            map(data => {
                return data;
            })
        );
    };

    resetarOpcao(carrinhoId: any): Observable<void> {
        return this.carrinhoRepository.resetarOpcao(carrinhoId).pipe(
            map(data => {
                return data;
            })
        );
    };

    finalizarPedido(carrinho: any): Observable<void> {
        return this.carrinhoRepository.finalizarPedido(carrinho).pipe(
            map(data => {
                return data;
            })
        );
    };

    getCarrinhoPedidoPendente(): Observable<Carrinho[]> {
        return this.carrinhoRepository.getCarrinhoPedidoPendente().pipe(
            map(data => {
                return data;
            })
        );
    };

    getCarrinhoPedidoAprovados(): Observable<Carrinho[]> {
        return this.carrinhoRepository.getCarrinhoPedidoAprovados().pipe(
            map(data => {
                return data;
            })
        );
    };

    getCarrinhoPedidoRecusados(): Observable<Carrinho[]> {
        return this.carrinhoRepository.getCarrinhoPedidoRecusados().pipe(
            map(data => {
                return data;
            })
        );
    };

    getCarrinhoPedidoCancelados(): Observable<Carrinho[]> {
        return this.carrinhoRepository.getCarrinhoPedidoCancelados().pipe(
            map(data => {
                return data;
            })
        );
    };

    getCarrinhoPedidoEntregues(): Observable<Carrinho[]> {
        return this.carrinhoRepository.getCarrinhoPedidoEntregues().pipe(
            map(data => {
                return data;
            })
        );
    };

    aceitarPedido(carrinhoId: any): Observable<void> {
        return this.carrinhoRepository.aceitarPedido(carrinhoId).pipe(
            map(data => {
                return data;
            })
        );
    };

    recusarPedido(carrinhoId: any): Observable<void> {
        return this.carrinhoRepository.recusarPedido(carrinhoId).pipe(
            map(data => {
                return data;
            })
        );
    };
    
    cancelarPedido(carrinhoId: any): Observable<void> {
        return this.carrinhoRepository.cancelarPedido(carrinhoId).pipe(
            map(data => {
                return data;
            })
        );
    };

    fechaCarrinho(carrinhoId: any): Observable<void> {
        return this.carrinhoRepository.fechaCarrinho(carrinhoId).pipe(
            map(data => {
                return data;
            })
        );
    };

    createAlimento(alimento: any): Observable<Alimento> {
        return this.carrinhoRepository.createAlimento(alimento).pipe(
            map(data => {
                return data;
            })
        );
    };

    createbebida(bebida: any): Observable<Bebida> {
        return this.carrinhoRepository.createbebida(bebida).pipe(
            map(data => {
                return data;
            })
        );
    };

    getCurrentUser(): Observable<any> {
        return this.carrinhoRepository.getCurrentUser().pipe(
            map(data => {
                return data;
            })
        );
    };

    updateUser(user: any): Observable<any> {
        return this.carrinhoRepository.updateUser(user).pipe(
            map(data => {
                return data;
            })
        );
    };

    getCarrinhoFechados(): Observable<any> {
        return this.carrinhoRepository.getCarrinhoFechados().pipe(
            map(data => {
                return data;
            })
        );
    };

}