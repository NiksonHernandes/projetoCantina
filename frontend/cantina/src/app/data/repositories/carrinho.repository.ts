import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable, map } from "rxjs";
import { Carrinho } from "src/app/domain/models/carrinho.model";
import { CarrinhoAlimentoEBebida } from "src/app/domain/models/carrinhoAlimentoEBebida.mode";
import { environment } from "src/environments/environments";


@Injectable({ providedIn: 'root' })
export class CarrinhoRepository {

    httpOptions = {
        headers: new HttpHeaders({
            'Content-type': 'application/json'
        })
    };

    constructor(private httpClient: HttpClient) { }

    adicionarAlimentoNoCarrinho(alimentoEQuantidade: any): Observable<void> {
        return this.httpClient.post<void>(environment.apiUrl + `/carrinho/adicionar-alimento`, alimentoEQuantidade, this.httpOptions).pipe(
            map(data => {
                return data;
            })
        );
    };

    adicionarBebidaNoCarrinho(bebidaEQuantidade: any): Observable<void> {
        return this.httpClient.post<void>(environment.apiUrl + `/carrinho/adicionar-bebida`, bebidaEQuantidade, this.httpOptions).pipe(
            map(data => {
                return data;
            })
        );
    };

    getCarrinho(carrinhoId: number): Observable<Carrinho> {
        return this.httpClient.get<Carrinho>(environment.apiUrl + `/carrinho/get-carrinho?carrinhoId=${carrinhoId}`, this.httpOptions).pipe(
            map((data: Carrinho) => {
                return data;
            })
        );
    };

    getCarrinhoProdutos(carrinhoId: number): Observable<CarrinhoAlimentoEBebida> {
        return this.httpClient.get<CarrinhoAlimentoEBebida>(environment.apiUrl + `/carrinho/get-carrinho-produtos?carrinhoId=${carrinhoId}`, this.httpOptions).pipe(
            map((data: CarrinhoAlimentoEBebida) => {
                return data;
            })
        );
    };

    removerAlimento(alimentoCarrinhoEQuantidade: any): Observable<void> {
        return this.httpClient.post<void>(environment.apiUrl + `/carrinho/remover-alimento`, alimentoCarrinhoEQuantidade, this.httpOptions).pipe(
            map(data => {
                return data;
            })
        );
    };

    removerBebida(bebidaCarrinhoEQuantidade: any): Observable<void> {
        return this.httpClient.post<void>(environment.apiUrl + `/carrinho/remover-bebida`, bebidaCarrinhoEQuantidade, this.httpOptions).pipe(
            map(data => {
                return data;
            })
        );
    };

    verificaIsCarrinhoExiste(): Observable<Carrinho> {
        return this.httpClient.get<Carrinho>(environment.apiUrl + `/carrinho/verifica-is-carrinho-existe`, this.httpOptions).pipe(
            map((data: Carrinho) => {
                return data;
            })
        );
    };

    opcaoPagamento(carrinhoEopcao: any): Observable<void> {
        return this.httpClient.post<void>(environment.apiUrl + `/carrinho/opcao-pagamento`, carrinhoEopcao, this.httpOptions).pipe(
            map(data => {
                return data;
            })
        );
    };

    resetarOpcao(carrinhoId: any): Observable<void> {
        return this.httpClient.post<void>(environment.apiUrl + `/carrinho/resetar-opcao`, carrinhoId, this.httpOptions).pipe(
            map(data => {
                return data;
            })
        );
    };

}