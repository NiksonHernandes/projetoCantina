import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";

import { Observable, map } from "rxjs";
import { Cardapio } from "src/app/domain/models/cardapio.model";
import { environment } from "src/environments/environments";

@Injectable({ providedIn: 'root' })
export class CardapioRepository {

    httpOptions = {
        headers: new HttpHeaders({
            'Content-type': 'application/json'
        })
    };

    constructor(private httpClient: HttpClient) { }

    getCardapio(cardapioId: number): Observable<Cardapio> {
        return this.httpClient.get<Cardapio>(environment.apiUrl + `/cardapio/get-cardapio?cardapioId=${cardapioId}`).pipe(
            map((data: Cardapio) => {
                return data;
            })
        );
    };
}