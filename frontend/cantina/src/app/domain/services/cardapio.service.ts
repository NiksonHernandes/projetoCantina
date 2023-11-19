import { Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';
import { CardapioRepository } from 'src/app/data/repositories/cardapio.repository';
import { Cardapio } from '../models/cardapio.model';

@Injectable({
    providedIn: 'root'
})
export class CardapioService {

    constructor(private cardapioRepository: CardapioRepository) { }

    getCardapio(cardapioId: number): Observable<Cardapio> {
        return this.cardapioRepository.getCardapio(cardapioId).pipe(
            map((data: Cardapio)=> {
                return data;
            })
        );
    };
}
