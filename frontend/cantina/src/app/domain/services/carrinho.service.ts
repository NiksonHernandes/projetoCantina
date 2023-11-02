import { Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';
import { Cardapio } from '../models/cardapio.model';
import { CarrinhoRepository } from 'src/app/data/repositories/carrinho.repository';
import { Carrinho } from '../models/carrinho.model';
import { CarrinhoAlimentoEBebida } from '../models/carrinhoAlimentoEBebida.mode';

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

}