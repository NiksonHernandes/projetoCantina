import { Component, ElementRef, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
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

    opcaoPagamento?: number;
    tipoCartao?: string;

    resposta: number = 1;

    cartaoForm!: FormGroup;
    submitted = false;

    qrCode?: boolean;
    cartao?: boolean;
    pgHora?: boolean;

    randomPixKey?: string;

    constructor(
        private formBuilder: FormBuilder,
        private router: Router,
        private route: ActivatedRoute,
        private carrinhoService: CarrinhoService,
        private modalService: NgbModal,
    ) {
        this.tipoCartao = '0';
    }

    async ngOnInit(): Promise<void> {
        await this.getCarrinho();

        this.cartaoForm = this.formBuilder.group({
            numeroCartao: [this.carrinhoList?.numeroCartao || '', Validators.required],
            validadeCartao: [this.carrinhoList?.validadeCartao || '', Validators.required],
            codigoCartao: [this.carrinhoList?.codigoCartao || '', Validators.required],
        });

    }

    get formField() { return this.cartaoForm.controls; }

    onSubmit() {
        this.submitted = true;

        if (this.cartaoForm.invalid) {
            return;
        }

        let carrinhoDTO = {
            carrinhoId: this.carrinhoList?.carrinhoId,
            opcaoPagamento: this.opcaoPagamento,
            tipoCartao: this.tipoCartao,
            numeroCartao: this.formField['numeroCartao'].value,
            validadeCartao: this.formField['validadeCartao'].value,
            codigoCartao: this.formField['codigoCartao'].value,
        }

        this.carrinhoService.finalizarPedido(carrinhoDTO).subscribe({
            next: (data) => {
                //alert("removido com sucesso");
                console.log("sucesso! -finalizarPedid", data);
                this.getCarrinho();
                this.modalService.dismissAll();
            },
            error: (error) => {
                alert(error);
                console.log("Erro - removerBebida", error);
            }
        });

        console.log("valor", carrinhoDTO)
    }

    comprarQrCode() {
        let carrinhoDTO = {
            carrinhoId: this.carrinhoList?.carrinhoId,
            opcaoPagamento: this.opcaoPagamento
        }

        this.carrinhoService.finalizarPedido(carrinhoDTO).subscribe({
            next: (data) => {
                //alert("removido com sucesso");
                console.log("sucesso! -finalizarPedid", data);
                this.getCarrinho();
                this.modalService.dismissAll();
            },
            error: (error) => {
                alert(error);
                console.log("Erro - removerBebida", error);
            }
        });
    }

    comprarPgHora() {
        let carrinhoDTO = {
            carrinhoId: this.carrinhoList?.carrinhoId,
            opcaoPagamento: this.opcaoPagamento
        }

        this.carrinhoService.finalizarPedido(carrinhoDTO).subscribe({
            next: (data) => {
                //alert("removido com sucesso");
                console.log("sucesso! - finalizarPedid", data);
                this.getCarrinho();
                this.modalService.dismissAll();
            },
            error: (error) => {
                alert(error);
                console.log("Erro - removerBebida", error);
            }
        });
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

                        console.log("sucesso! - getCarrinhoProdutos", data);
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
    }

    opcaoCartao() {
        this.definirPagamento(1);
    }

    opcaoPagarHora() {
        this.definirPagamento(2);
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
    }

}
