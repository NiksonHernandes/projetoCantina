import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Subject } from 'rxjs';
import { Carrinho } from 'src/app/domain/models/carrinho.model';
import { CarrinhoAlimentoEBebida } from 'src/app/domain/models/carrinhoAlimentoEBebida.mode';
import { CarrinhoService } from 'src/app/domain/services/carrinho.service';
import { ToastService } from 'src/app/domain/services/toast.service';

@Component({
    selector: 'app-admin-pedidos',
    templateUrl: './admin-pedidos.component.html',
    styleUrls: ['./admin-pedidos.component.scss']
})
export class AdminPedidosComponent {

    resposta: number = 1;
    closeResult = '';

    adicionarAlimentoForm!: FormGroup;
    adicionarBebidaForm!: FormGroup;
    submitted = false;
    submittedBebida: boolean = false;

    listPedidosPendentes: Carrinho[] = [];
    listPedidosAprovados: Carrinho[] = [];
    listPedidosRecusados: Carrinho[] = [];

    pedidoCurrent?: CarrinhoAlimentoEBebida = new CarrinhoAlimentoEBebida();

    listPedidosCancelados: Carrinho[] = [];
    listPedidosEntregues: Carrinho[] = [];

    constructor(
        private formBuilder: FormBuilder,
        private carrinhoService: CarrinhoService,
        private toastService: ToastService,
        private modalService: NgbModal,

    ) { }

    async ngOnInit(): Promise<void> {
        this.getCarrinhoPedidoPendente()

        this.adicionarAlimentoForm = this.formBuilder.group({
            nomeAlimento: ['', Validators.required],
            valorAlimento: ['', Validators.required],
            descricaoAlimento: ['', Validators.required],
            qntEstoqueAlimento: ['', Validators.required],
        });

        this.adicionarBebidaForm = this.formBuilder.group({
            nomeBebida: ['', Validators.required],
            valorBebida: ['', Validators.required],
            descricaoBebida: ['', Validators.required],
            qntEstoqueBebida: ['', Validators.required],
        });
    }

    get formField() { return this.adicionarAlimentoForm.controls; }
    get formFieldBebida() { return this.adicionarBebidaForm.controls; }

    adicionarAlimento() {
        this.submitted = true;

        if (this.adicionarAlimentoForm.invalid) {
            return;
        }

        let alimentoObj = {
            nomeAlimento: this.formField['nomeAlimento'].value,
            valorAlimento: this.formField['valorAlimento'].value,
            descricaoAlimento: this.formField['descricaoAlimento'].value,
            alimentoDisponivel: true,
            qntEstoqueAlimento: this.formField['qntEstoqueAlimento'].value,
        }

        this.carrinhoService.createAlimento(alimentoObj).subscribe({
            next: (data) => {
                this.toastMessage("Alimento criado com sucesso", 1);
                console.log("sucesso! - .createAlimento", data);
                this.modalService.dismissAll();
            },
            error: (error) => {
                this.toastMessage(error, 2);
                console.log("Erro -.createAlimento", error);
                this.modalService.dismissAll();
            }
        });
        console.log("aliment", alimentoObj)
    }

    adicionarBebida() {
        this.submittedBebida = true;

        if (this.adicionarBebidaForm.invalid) {
            return;
        }

        let bebidaObj = {
            nomeBebida: this.formFieldBebida['nomeBebida'].value,
            valorBebida: this.formFieldBebida['valorBebida'].value,
            descricaoBebida: this.formFieldBebida['descricaoBebida'].value,
            bebidaDisponivel: true,
            qntEstoqueBebida: this.formFieldBebida['qntEstoqueBebida'].value,
        }

        this.carrinhoService.createbebida(bebidaObj).subscribe({
            next: (data) => {
                this.toastMessage("Bebida criada com sucesso", 1);
                console.log("sucesso! - createbebida", data);
                this.modalService.dismissAll();
            },
            error: (error) => {
                this.toastMessage(error, 2);
                console.log("Erro - createbebida", error);
                this.modalService.dismissAll();
            }
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

    open(content: any) {
        this.resposta = 1
        this.modalService.open(content, { ariaLabelledBy: 'modal-basic-title' }).result.then(
            (result) => {
                this.closeResult = `Closed with: ${result}`;
            }
        );
    }

    async openScrollableContent(longContent: any, carrinhoId: number) {
        await this.getPedido(carrinhoId);
        this.modalService.open(longContent, { scrollable: true });
    }

    async openScrollableAlimento(adidicionarAlimento: any) {
        this.modalService.open(adidicionarAlimento, { scrollable: true });
    }

    async openScrollableBebida(adidicionarBebida: any) {
        this.modalService.open(adidicionarBebida, { scrollable: true });
    }

    getCarrinhoPedidoPendente() {
        return new Promise<void>((resolve, reject) => {
            this.carrinhoService.getCarrinhoPedidoPendente().subscribe({
                next: (data) => {
                    this.listPedidosPendentes = data;
                    console.log("sucesso! - getCarrinhoPedidoPendente", data);
                    resolve(); // Resolva a Promise quando a chamada for concluída com sucesso
                },
                error: (error) => {
                    this.toastMessage(error, 2);
                    console.log("Erro - getCarrinhoPedidoPendente", error);
                    reject(error); // Rejeite a Promise em caso de erro
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

    getCarrinhoPedidoAprovados() {
        this.carrinhoService.getCarrinhoPedidoAprovados().subscribe({
            next: (data) => {
                this.listPedidosAprovados = data;
                console.log("sucesso! - getCarrinhoPedidoAprovados()", data);
                this.getCarrinhoPedidoPendente()
            },
            error: (error) => {
                this.toastMessage(error, 2);
                console.log("Erro - getCarrinhoPedidoAprovados()", error);
            }
        });
    }

    getCarrinhoPedidoRecusados() {
        this.carrinhoService.getCarrinhoPedidoRecusados().subscribe({
            next: (data) => {
                this.listPedidosRecusados = data;
                console.log("sucesso! - getCarrinhoPedidoRecusados()", data);
                this.getCarrinhoPedidoPendente()
            },
            error: (error) => {
                this.toastMessage(error, 2);
                console.log("Erro - getCarrinhoPedidoRecusados()", error);
            }
        });
    }

    getCarrinhoPedidoCancelados() {
        this.carrinhoService.getCarrinhoPedidoCancelados().subscribe({
            next: (data) => {
                this.listPedidosCancelados = data;
                console.log("sucesso! -  getCarrinhoPedidoCancelados()", data);
                this.getCarrinhoPedidoPendente()
            },
            error: (error) => {
                this.toastMessage(error, 2);
                console.log("Erro -  getCarrinhoPedidoCancelados()", error);
            }
        });
    }

    getCarrinhoPedidoEntregues() {
        this.carrinhoService.getCarrinhoPedidoEntregues().subscribe({
            next: (data) => {
                this.listPedidosEntregues = data;
                console.log("sucesso! - listPedidosEntregues", data);
                this.getCarrinhoPedidoPendente()
            },
            error: (error) => {
                this.toastMessage(error, 2);
                console.log("Erro - listPedidosEntregues", error);
            }
        });
    }

    aceitarPedido(carrinhoId: number) {
        let carrinhoIdObj = {
            carrinhoId: carrinhoId
        };

        this.carrinhoService.aceitarPedido(carrinhoIdObj).subscribe({
            next: (data) => {
                this.toastMessage("Pedido aceito com sucesso!", 1);
                console.log("sucesso! - aceitarPedid", data);
                this.getCarrinhoPedidoPendente()
            },
            error: (error) => {
                this.toastMessage(error, 2);
                console.log("Erro - aceitarPedid", error);
            }
        });
    };

    recusarPedido(carrinhoId: number) {
        let carrinhoIdObj = {
            carrinhoId: carrinhoId
        };

        this.carrinhoService.recusarPedido(carrinhoIdObj).subscribe({
            next: (data) => {
                this.toastMessage("Pedido recusado com sucesso!", 1);
                console.log("sucesso! - aceitarPedid", data);
                this.getCarrinhoPedidoPendente()
            },
            error: (error) => {
                this.toastMessage(error, 2);
                console.log("Erro - aceitarPedid", error);
            }
        });
    };

    fecharPedido(carrinhoId: number) {
        this.carrinhoService.fechaCarrinho(carrinhoId).subscribe({
            next: (data) => {
                this.toastMessage("Pedido fechado com sucesso!", 1);
                console.log("sucesso! - fechaCarrinho", data);
                this.getCarrinhoPedidoAprovados();
            },
            error: (error) => {
                this.toastMessage(error, 2);
                console.log("Erro - fechaCarrinho", error);
            }
        });
    }

    cancelarPedido(carrinhoId: number) {
        this.carrinhoService.cancelarPedido(carrinhoId).subscribe({
            next: (data) => {
                this.toastMessage("Pedido cancelado com sucesso!", 1);
                console.log("sucesso! - fechaCarrinho", data);
                this.getCarrinhoPedidoAprovados();
            },
            error: (error) => {
                this.toastMessage(error, 2);
                console.log("Erro - fechaCarrinho", error);
            }
        });
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
