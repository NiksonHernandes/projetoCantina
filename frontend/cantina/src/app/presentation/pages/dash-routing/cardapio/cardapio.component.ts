import { Component, ElementRef, ViewChild } from '@angular/core';
import { NgbCarouselConfig, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Cardapio } from 'src/app/domain/models/cardapio.model';
import { CardapioService } from 'src/app/domain/services/cardapio.service';
import { CarrinhoService } from 'src/app/domain/services/carrinho.service';
import { ToastService } from 'src/app/domain/services/toast.service';

@Component({
    selector: 'app-cardapio',
    templateUrl: './cardapio.component.html',
    styleUrls: ['./cardapio.component.scss']
})
export class CardapioComponent {

    cardapioList?: Cardapio = new Cardapio();
    cardapioId: number = 1;
    quantidadeAlimentos: number = 1;

    currentCarrinho?: any;

    linkImg = "../../../../../assets/imagens/bebeidas.jpg"

    resposta: number = 1;
    closeResult = '';

    cardapioListWithQuantidade?: any;

    constructor(config: NgbCarouselConfig, private toastService: ToastService, private el: ElementRef, private cardapioService: CardapioService, private carrinhoService: CarrinhoService, private modalService: NgbModal) {
        config.interval = 10000;
        config.wrap = false;
        config.keyboard = false;
        config.pauseOnHover = false;
    }

    scrollToSection(sectionId: string): void {
        const element = this.el.nativeElement.querySelector(`#${sectionId}`);
        if (element) {
            element.scrollIntoView({ behavior: 'smooth' });
        }
    }

    async ngOnInit() {
        this.getCardapio();
        this.getCarrinho();
    }

    getCardapio() {
        this.cardapioService.getCardapio(this.cardapioId).subscribe({
            next: (data) => {
                this.cardapioList = data;
                console.log("Retorno - cardapioGet: ", data);
            },
            error: (error) => {
                this.toastMessage(error, 2);
                console.log("Erro - cardapioGet: ", error);
                // this.router.navigate(['/dash']);
            }
        });
    }

    getCarrinho() {
        this.carrinhoService.getCarrinhoAbertos().subscribe({
            next: (data) => {
                this.currentCarrinho = data;
                console.log("Retorno - getCarrinhoAbertos: ", data);
            },
            error: (error) => {
                this.toastMessage(error, 2);
                console.log("Erro - getCarrinhoAbertos ", error);
                // this.router.navigate(['/dash']);
            }
        });
    }

    open(content: any) {
        this.resposta = 1
        this.modalService.open(content, { ariaLabelledBy: 'modal-basic-title' }).result.then(
            (result) => {
                this.closeResult = `Closed with: ${result}`;
            }
        );
    }

    openBebida(contentBebida: any) {
        this.resposta = 1
        this.modalService.open(contentBebida, { ariaLabelledBy: 'modal-basic-title' }).result.then(
            (result) => {
                this.closeResult = `Closed with: ${result}`;
            }
        );
    }

    adicionarAlimentoCarrinho(alimentoId: number) {
        let alimentoEQuantidade = {
            alimentoId: alimentoId,
            quantidadeAlimento: this.resposta
        }


        this.carrinhoService.adicionarAlimentoNoCarrinho(alimentoEQuantidade).subscribe({
            next: (data) => {
                this.toastMessage("Alimento adicionada no carrinho", 1);
                console.log("Adicionado com sucesso! - adicionarAlimentoNoCarrinho ", data);
                this.modalService.dismissAll();
            },
            error: (error) => {
                this.toastMessage(error, 2);
                console.log("Erro - cadicionarAlimentoNoCarrinho: ", error);
                // this.router.navigate(['/dash']);
            }
        })


        // if (this.currentCarrinho.statusPedido != null) {

        // } 
        // else {
        //     this.toastMessage("Ops. Aguardando a resposta do carrinho anterior. Faça um novo pedido após a finalização do atual.", 2);
        // }
    };

    adicionarBebidaCarrinho(bebidaId: number) {
        let bebidaEQuantidade = {
            bebidaId: bebidaId,
            quantidadeBebida: this.resposta
        }
        console.log("bebidasEQuantidade:", bebidaEQuantidade);


        this.carrinhoService.adicionarBebidaNoCarrinho(bebidaEQuantidade).subscribe({
            next: (data) => {
                this.toastMessage("Bebida adicionada no carrinho", 1);
                this.modalService.dismissAll();
                console.log("Adicionado com sucesso! - adicionarBebidaNoCarrinho ", data);
            },
            error: (error) => {
                this.toastMessage(error, 2);
                console.log("Erro - adicionarBebidaNoCarrinho ", error);
            }
        })
        // if (this.currentCarrinho.statusPedido != null) {
        //     console.log("Carrinho é diferente de nyll")
        // }


        // if (this.currentCarrinho.statusPedido != null) {
        //     this.carrinhoService.adicionarBebidaNoCarrinho(bebidaEQuantidade).subscribe({
        //         next: (data) => {
        //             this.toastMessage("Bebida adicionada no carrinho", 1);
        //             this.modalService.dismissAll();
        //             console.log("Adicionado com sucesso! - adicionarBebidaNoCarrinho ", data);
        //         },
        //         error: (error) => {
        //             this.toastMessage(error, 2);
        //             console.log("Erro - adicionarBebidaNoCarrinho ", error);
        //         }
        //     })
        // }
        // else {
        //     this.toastMessage("Ops. Aguardando a resposta do carrinho anterior. Faça um novo pedido após a finalização do atual.", 2);
        // }
    };

    toastMessage(message: string, type: number) {
        const successToast = 1;
        const dangerToast = 2;

        if (type == dangerToast)
            this.toastService.show(message, { classname: 'bg-danger text-light', delay: 3000 });

        else if (type == successToast)
            this.toastService.show(message, { classname: 'bg-success text-light', delay: 2000 });
    };

}
