import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable, map } from "rxjs";
import { Alimento } from "src/app/domain/models/alimento.model";
import { Bebida } from "src/app/domain/models/bebida.model";
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

    finalizarPedido(carrinho: any): Observable<void> {
        return this.httpClient.post<void>(environment.apiUrl + `/carrinho/finalizar-pedido`, carrinho, this.httpOptions).pipe(
            map(data => {
                return data;
            })
        );
    };

    
    getCarrinhoPedidoPendente(): Observable<Carrinho[]> {
        return this.httpClient.get<Carrinho[]>(environment.apiUrl + `/carrinho/get-carrinho-pedido-pendente`, this.httpOptions).pipe(
            map((data: Carrinho[]) => {
                return data;
            })
        );
    };

    getCarrinhoPedidoAprovados(): Observable<Carrinho[]> {
        return this.httpClient.get<Carrinho[]>(environment.apiUrl + `/carrinho/get-carrinho-pedido-aprovados`, this.httpOptions).pipe(
            map((data: Carrinho[]) => {
                return data;
            })
        );
    };

    getCarrinhoPedidoRecusados(): Observable<Carrinho[]> {
        return this.httpClient.get<Carrinho[]>(environment.apiUrl + `/carrinho/get-carrinho-pedido-recusados`, this.httpOptions).pipe(
            map((data: Carrinho[]) => {
                return data;
            })
        );
    };

    getCarrinhoPedidoCancelados(): Observable<Carrinho[]> {
        return this.httpClient.get<Carrinho[]>(environment.apiUrl + `/carrinho/get-carrinho-pedido-cancelados`, this.httpOptions).pipe(
            map((data: Carrinho[]) => {
                return data;
            })
        );
    };

    getCarrinhoPedidoEntregues(): Observable<Carrinho[]> {
        return this.httpClient.get<Carrinho[]>(environment.apiUrl + `/carrinho/get-carrinho-pedido-entregues`, this.httpOptions).pipe(
            map((data: Carrinho[]) => {
                return data;
            })
        );
    };

    aceitarPedido(carrinhoId: any): Observable<void> {
        return this.httpClient.post<void>(environment.apiUrl + `/carrinho/aceitar-pedido`, carrinhoId, this.httpOptions).pipe(
            map(data => {
                return data;
            })
        );
    };

    recusarPedido(carrinhoId: any): Observable<void> {
        return this.httpClient.post<void>(environment.apiUrl + `/carrinho/recusar-pedido`, carrinhoId, this.httpOptions).pipe(
            map(data => {
                return data;
            })
        );
    };

    cancelarPedido(carrinhoId: any): Observable<void> {
        return this.httpClient.post<void>(environment.apiUrl + `/carrinho/cancelar-pedido`, carrinhoId, this.httpOptions).pipe(
            map(data => {
                return data;
            })
        );
    };
    
    fechaCarrinho(carrinhoId: any): Observable<void> {
        return this.httpClient.post<void>(environment.apiUrl + `/carrinho/fechar-carrinho?carrinhoId=${carrinhoId}`, this.httpOptions).pipe(
            map(data => {
                return data;
            })
        );
    };

    createAlimento(alimento: any): Observable<Alimento> {
        return this.httpClient.post<Alimento>(environment.apiUrl + `/alimento/create-alimento`, alimento, this.httpOptions).pipe(
            map(data => {
                return data;
            })
        );
    };

    createbebida(bebida: any): Observable<Bebida> {
        return this.httpClient.post<Bebida>(environment.apiUrl + `/bebida/create-bebida`, bebida, this.httpOptions).pipe(
            map(data => {
                return data;
            })
        );
    };
    
    getCurrentUser(): Observable<any> {
        return this.httpClient.get<any>(environment.apiUrl + `/usuario/get-current-usuario`, this.httpOptions).pipe(
            map(data => {
                return data;
            })
        );
    };

    updateUser(user: any): Observable<any> {
        return this.httpClient.put<any>(environment.apiUrl + `/usuario/update-usuario`, user, this.httpOptions).pipe(
            map(data => {
                return data;
            })
        );
    };
  
    getCarrinhoFechados(): Observable<any> {
        return this.httpClient.get<any>(environment.apiUrl + `/carrinho/get-carrinho-fechados`, this.httpOptions).pipe(
            map(data => {
                return data;
            })
        );
    };

    getCarrinhoAbertos(): Observable<any> {
        return this.httpClient.get<any>(environment.apiUrl + `/carrinho/get-carrinho-abertos`, this.httpOptions).pipe(
            map(data => {
                return data;
            })
        );
    };

}