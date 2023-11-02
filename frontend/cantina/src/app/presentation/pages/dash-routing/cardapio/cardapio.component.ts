import { Component, ElementRef, ViewChild } from '@angular/core';
import { NgbCarouselConfig } from '@ng-bootstrap/ng-bootstrap';
import { Cardapio } from 'src/app/domain/models/cardapio.model';
import { CardapioService } from 'src/app/domain/services/cardapio.service';
import { CarrinhoService } from 'src/app/domain/services/carrinho.service';

@Component({
    selector: 'app-cardapio',
    templateUrl: './cardapio.component.html',
    styleUrls: ['./cardapio.component.scss']
})
export class CardapioComponent {

    cardapioList?: Cardapio = new Cardapio();
    cardapioId: number = 1;
    quantidadeAlimentos: number = 1;

    cardapioListWithQuantidade?: any;

    //valor = document.getElementById("valorQuantidade") as HTMLElement;

    constructor(config: NgbCarouselConfig, private el: ElementRef, private cardapioService: CardapioService, private carrinhoService: CarrinhoService) {
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
        this.getCardapio();    }

    getCardapio() {
        this.cardapioService.getCardapio(this.cardapioId).subscribe({
            next: (data) => {
                this.cardapioList = data;
                console.log("Retorno - cardapioGet: ", data);
            },
            error: (error) => {
                alert(error);
                console.log("Erro - cardapioGet: ", error);
               // this.router.navigate(['/dash']);
            }
        });
    }

    adicionarAlimentoCarrinho(alimentoId: number) {
        let alimentoEQuantidade =  {
            alimentoId: alimentoId,
            quantidadeAlimento: 1
        }

        this.carrinhoService.adicionarAlimentoNoCarrinho(alimentoEQuantidade).subscribe({
            next: (data) => {
                console.log("Adicionado com sucesso! - adicionarAlimentoNoCarrinho ", data);
            },
            error: (error) => {
                alert(error);
                console.log("Erro - cadicionarAlimentoNoCarrinho: ", error);
               // this.router.navigate(['/dash']);
            }
        })  
    };

    adicionarBebidaCarrinho(bebidaId: number) {
        let bebidaEQuantidade =  {
            bebidaId: bebidaId,
            quantidadeBebida: 1
        }

        this.carrinhoService.adicionarBebidaNoCarrinho(bebidaEQuantidade).subscribe({
            next: (data) => {
                console.log("Adicionado com sucesso! - adicionarBebidaNoCarrinho ", data);
            },
            error: (error) => {
                alert(error);
                console.log("Erro - adicionarBebidaNoCarrinho ", error);
               // this.router.navigate(['/dash']);
            }
        })
    };
    
}
