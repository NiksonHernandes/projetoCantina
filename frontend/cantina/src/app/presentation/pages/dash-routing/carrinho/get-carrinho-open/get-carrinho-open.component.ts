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
    isFinalizarCompra?: boolean;

    resposta: number = 1;

    qrCode?: boolean;
    cartao?: boolean;
    pgHora?: boolean;

    randomPixKey?: string;

    constructor(
        private router: Router,
        private route: ActivatedRoute,
        private carrinhoService: CarrinhoService,
        private modalService: NgbModal,
    ) { }

    async ngOnInit(): Promise<void> {
        await this.getCarrinho();
        this.capturaLocalStorage();
    }

    capturaLocalStorage() {
        // const valorLocalStorage = localStorage.getItem('opcaoPagamento');

        // if (valorLocalStorage) {
        //     this.isFinalizarCompra = true;
        // }

        // if (valorLocalStorage == "qrcode") {
        //     this.qrCode = true;
        //     document.addEventListener("DOMContentLoaded", function () {
        //         const qrCodeCor = document.querySelector('.corCode') as HTMLElement;
        //         qrCodeCor.style.border = '2px solid green';
        //     });

        // } else if (valorLocalStorage == "cartao") {
        //     this.cartao = true;
        //     document.addEventListener("DOMContentLoaded", function () {
        //         const corCartao = document.querySelector('.corCartao') as HTMLElement;
        //         corCartao.style.border = '2px solid green';
        //     });

        // } else if (valorLocalStorage == "pgHora") {
        //     this.pgHora = true;

        //     document.addEventListener("DOMContentLoaded", function () {
        //         const corRetirada = document.querySelector('.corRetirada') as HTMLElement;
        //         corRetirada.style.border = '2px solid green';
        //     });
        // } else {
        //     console.log("valor", valorLocalStorage);
        // }


    }

    copyPixKey() {
        // Copiar o valor da chave Pix para a área de transferência
        const pixKeyInput = document.getElementById('pixKey') as HTMLInputElement;
        pixKeyInput.select();
        document.execCommand('copy');
        alert("CHAVE COPIADA")
    }

    open(content: any) {
        this.resposta = 1
        this.modalService.open(content, { ariaLabelledBy: 'modal-basic-title' }).result.then(
            (result) => {
                this.closeResult = `Closed with: ${result}`;
            }
        );
    }

    openScrollableContent(longContent: any) {
        this.modalService.open(longContent, { scrollable: true });
    }

    // getCarrinho() {
    //     const carrinhoId = this.route.snapshot.params['id'];
    //     //const carrinhoId = 41

    //     if (carrinhoId) {
    //         this.isCarrinhoExiste = true;
    //         this.carrinhoService.getCarrinhoProdutos(carrinhoId).subscribe({
    //             next: (data) => {
    //                 this.carrinhoList = data;

    //                 if (this.carrinhoList?.opcaoPagamento == 0 || this.carrinhoList?.opcaoPagamento == 1 || this.carrinhoList?.opcaoPagamento == 2) {
    //                     this.isFinalizarCompra = true;
    //                 }
    //                 console.log("Adicionado com sucesso! - getCarrinhoProdutos", data);
    //             },
    //             error: (error) => {
    //                 alert(error);
    //                 console.log("Erro - getCarrinhoProdutos", error);
    //                 // this.router.navigate(['/dash']);
    //             }
    //         })

    //     } else {
    //         alert("Id do carrinho não encontrado");
    //         this.isCarrinhoExiste = false;
    //     }
    // }

    getCarrinho() {
        return new Promise<void>((resolve, reject) => {
            const carrinhoId = this.route.snapshot.params['id'];

            if (carrinhoId) {
                this.isCarrinhoExiste = true;
                this.carrinhoService.getCarrinhoProdutos(carrinhoId).subscribe({
                    next: (data) => {
                        this.carrinhoList = data;

                        if (this.carrinhoList?.opcaoPagamento == 0 || this.carrinhoList?.opcaoPagamento == 1 || this.carrinhoList?.opcaoPagamento == 2) {
                            this.isFinalizarCompra = true;
                        }
                        console.log("Adicionado com sucesso! - getCarrinhoProdutos", data);
                        resolve(); // Resolva a Promise quando a chamada for concluída com sucesso
                    },
                    error: (error) => {
                        alert(error);
                        console.log("Erro - getCarrinhoProdutos", error);
                        reject(error); // Rejeite a Promise em caso de erro
                    }
                });
            } else {
                alert("Id do carrinho não encontrado");
                this.isCarrinhoExiste = false;
                resolve(); // Resolva a Promise em caso de sucesso sem chamada
            }
        });
    }

    removerBebida(bebidaId: number) {
        let bebidaCarrinhoEQuantidade = {
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
        let alimentoCarrinhoEQuantidade = {
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

    finalizarCompra() {
        this.isFinalizarCompra = true;
    }

    opcaoQRcode() {
        this.definirPagamento(0);

        const qrCodeCor = document.querySelector('.corCode') as HTMLElement;
        qrCodeCor.style.border = '2px solid green';
    }

    opcaoCartao() {
        this.definirPagamento(1);

        const corCartao = document.querySelector('.corCartao') as HTMLElement;
        corCartao.style.border = '2px solid green';
    }

    opcaoPagarHora() {
        this.definirPagamento(2);

        const corRetirada = document.querySelector('.corRetirada') as HTMLElement;
        corRetirada.style.border = '2px solid green';
    }

    definirPagamento(opcao: number) {
        let carrinhoEOpcaoObject = {
            carrinhoId: this.carrinhoList?.carrinhoId,
            opcao: opcao
        };

        this.carrinhoService.opcaoPagamento(carrinhoEOpcaoObject).subscribe({
            next: (data) => {
                console.log("opcaoPagamento", data);
                this.getCarrinho();
                this.isFinalizarCompra = true;
            },
            error: (error) => {
                alert(error);
                console.log("Erro - opcaoPagamento", error);
            }
        })
    }

    redefinirOpcaoes() {
        let carrinhoIdObject = {
            carrinhoId: this.carrinhoList?.carrinhoId
        };

        this.carrinhoService.resetarOpcao(carrinhoIdObject).subscribe({
            next: (data) => {
                console.log("resetarOpca", data);
                this.getCarrinho();
                this.isFinalizarCompra = true;
            },
            error: (error) => {
                alert(error);
                console.log("Erro - resetarOpca", error);
            }
        })

        const qrCodeCor = document.querySelector('.corCode') as HTMLElement;
        qrCodeCor.style.border = '1px solid #ddd';
        const corCartao = document.querySelector('.corCartao') as HTMLElement;
        corCartao.style.border = '1px solid #ddd';
    }

}
