import { Component } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CardapioService } from 'src/app/domain/services/cardapio.service';
import { CarrinhoService } from 'src/app/domain/services/carrinho.service';
import { ToastService } from 'src/app/domain/services/toast.service';

@Component({
    selector: 'app-historico',
    templateUrl: './historico.component.html',
    styleUrls: ['./historico.component.scss']
})
export class HistoricoComponent {

    pedidosFechados?: any
    pedidoCurrent?: any;
    
    resposta: number = 1;
    closeResult = '';

    constructor(private carrinhoService: CarrinhoService, private toastService: ToastService, private modalService: NgbModal,) { }

    async ngOnInit() {
        await this.getCarrinhoFechado();
    }

    getCarrinhoFechado() {
        return new Promise<void>((resolve, reject) => {
            this.carrinhoService.getCarrinhoFechados().subscribe({
                next: (data) => {
                    this.pedidosFechados = data;
                    console.log("Retorno - getCarrinhoFechados ", data);
                    resolve(); // Resolva a Promise quando a chamada for concluída com sucesso
                },
                error: (error) => {
                    this.toastMessage(error, 2);
                    console.log("Erro - cardapioGet: ", error);
                    // this.router.navigate(['/dash']);
                    reject(error); // Rejeite a Promise em caso de erro
                }
            });

        });
    }

    getPedido(carrinhoId: number) {
        return new Promise<void>((resolve, reject) => {
            this.carrinhoService.getCarrinhoProdutos(carrinhoId).subscribe({
                next: (data) => {
                    this.pedidoCurrent = data;

                    console.log("sucesso! - getCarrinho", data);
                    resolve(); // Resolva a Promise quando a ch
                },
                error: (error) => {
                    this.toastMessage(error, 2);
                    console.log("Erro - getCarrinho", error);
                    reject(error);
                }
            });
        });
    }
    
    transformarData(dataHora: any): string {
        const data = new Date(dataHora);
        const dia = data.getDate();
        const mes = data.getMonth() + 1;
        const ano = data.getFullYear();
        const hora = data.getHours();
        const minutos = data.getMinutes();

        const diaFormatado = dia < 10 ? `0${dia}` : dia.toString();
        const mesFormatado = mes < 10 ? `0${mes}` : mes.toString();
        const horaFormatada = hora < 10 ? `0${hora}` : hora.toString();
        const minutosFormatados = minutos < 10 ? `0${minutos}` : minutos.toString();

        const dataFormatada = `${diaFormatado}/${mesFormatado}/${ano} às ${horaFormatada}:${minutosFormatados}`;
        return dataFormatada;
    }


    async openScrollableContent(longContent: any, carrinhoId: number) {
        await this.getPedido(carrinhoId);
        this.modalService.open(longContent, { scrollable: true });
    }

    toastMessage(message: string, type: number) {
        const successToast = 1;
        const dangerToast = 2;

        if (type == dangerToast)
            this.toastService.show(message, { classname: 'bg-danger text-light', delay: 3000 });

        else if (type == successToast)
            this.toastService.show(message, { classname: 'bg-success text-light', delay: 2000 });
    }

}
