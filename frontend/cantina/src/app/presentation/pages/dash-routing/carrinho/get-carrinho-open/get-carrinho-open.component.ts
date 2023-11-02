import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CarrinhoAlimentoEBebida } from 'src/app/domain/models/carrinhoAlimentoEBebida.mode';
import { CarrinhoService } from 'src/app/domain/services/carrinho.service';

@Component({
    selector: 'app-get-carrinho-open',
    templateUrl: './get-carrinho-open.component.html',
    styleUrls: ['./get-carrinho-open.component.scss']
})
export class GetCarrinhoOpenComponent {

    carrinhoList?: CarrinhoAlimentoEBebida = new CarrinhoAlimentoEBebida();

    constructor(
        private router: Router,
        private route: ActivatedRoute,
        private carrinhoService: CarrinhoService
    ) { }

    ngOnInit(): void {
        this.getCarrinho();
    }

    getCarrinho() {
        //const carrinhoId = this.route.snapshot.params['id'];
        const carrinhoId = 41

        if (carrinhoId) {
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
            
        }
    }
}
