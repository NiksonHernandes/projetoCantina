import { Component, ElementRef, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ModalDismissReasons, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { CarrinhoAlimentoEBebida } from 'src/app/domain/models/carrinhoAlimentoEBebida.mode';
import { CarrinhoService } from 'src/app/domain/services/carrinho.service';

@Component({
    selector: 'app-get-carrinho-open',
    templateUrl: './get-carrinho-open.component.html',
    styleUrls: ['./get-carrinho-open.component.scss']
})
export class GetCarrinhoOpenComponent {

    carrinhoList?: CarrinhoAlimentoEBebida = new CarrinhoAlimentoEBebida();
    isCarrinhoExiste: boolean = true;
    closeResult = '';

    resposta: number = 1;

    constructor(
        private router: Router,
        private route: ActivatedRoute,
        private carrinhoService: CarrinhoService,
        private modalService: NgbModal,
    ) { }

    ngOnInit(): void {
        this.getCarrinho();
    }
    
    open(content: any) {
        this.resposta = 1
        this.modalService.open(content, { ariaLabelledBy: 'modal-basic-title' }).result.then(
			(result) => {
				this.closeResult = `Closed with: ${result}`;
			}
		);
    }

    getCarrinho() {
        //const carrinhoId = this.route.snapshot.params['id'];
        const carrinhoId = 41

        if (carrinhoId) {
            this.isCarrinhoExiste = true;
            this.carrinhoService.getCarrinhoProdutos(carrinhoId).subscribe({
                next: (data) => {
                    this.carrinhoList = data;
                    console.log("Adicionado com sucesso! - getCarrinhoProdutos", data);
                },
                error: (error) => {
                    alert(error);
                    console.log("Erro - getCarrinhoProdutos", error);
                    // this.router.navigate(['/dash']);
                }
            })

        } else {
            alert("Id do carrinho não encontrado");
            this.isCarrinhoExiste = false;
        }
    }

    removerBebida(bebidaId: number) {
        let bebidaCarrinhoEQuantidade =  {
            bebidaId: bebidaId,
            carrinhoId: this.carrinhoList?.carrinhoId,
            quantidadeBebida: this.resposta
        }
        
        this.carrinhoService.removerBebida(bebidaCarrinhoEQuantidade).subscribe({
            next: (data) => {
                //alert("removido com sucesso");
                console.log("removidos com sucesso! - removerBebida", data);
                this.getCarrinho();
                this.modalService.dismissAll();
            },
            error: (error) => {
                alert(error);
                console.log("Erro - removerBebida", error);
            }
        });
    }

    removerAlimento(alimentoId: number) {
        let alimentoCarrinhoEQuantidade =  {
            alimentoId: alimentoId,
            carrinhoId: this.carrinhoList?.carrinhoId,
            quantidadeAlimento: this.resposta
        }
        
        this.carrinhoService.removerAlimento(alimentoCarrinhoEQuantidade).subscribe({
            next: (data) => {
                //alert("removido com sucesso");
                console.log("removidos com sucesso! - .removerAlimento", data);
                this.getCarrinho();
                this.modalService.dismissAll();
            },
            error: (error) => {
                alert(error);
                console.log("Erro -removerAlimento", error);
            }
        });
    }

}
