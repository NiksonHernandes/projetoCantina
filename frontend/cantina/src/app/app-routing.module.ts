import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashRoutingComponent } from './presentation/pages/dash-routing/dash-routing.component';
import { AuthGuard } from './core/security/auth.guard';
import { LoginComponent } from './presentation/pages/public-routing/login/login.component';
import { HomeComponent } from './presentation/pages/dash-routing/home/home/home.component';
import { CardapioComponent } from './presentation/pages/dash-routing/cardapio/cardapio.component';
import { GetCarrinhoOpenComponent } from './presentation/pages/dash-routing/carrinho/get-carrinho-open/get-carrinho-open.component';
import { AdminPedidosComponent } from './presentation/pages/dash-routing/admin/admin-pedidos/admin-pedidos.component';
import { PerfilComponent } from './presentation/pages/dash-routing/perfil/perfil.component';
import { HistoricoComponent } from './presentation/pages/dash-routing/historico/historico.component';

const routes: Routes = [
  // Authentication Required
  {
    path: 'dash', component: DashRoutingComponent, children: [
      {path: '', component: HomeComponent},
      {path: 'cardapio', component: CardapioComponent},
      {path: 'carrinho-open/:id', component: GetCarrinhoOpenComponent},
      {path: 'admin-pedidos', component: AdminPedidosComponent},
      {path: 'perfil', component: PerfilComponent},
      {path: 'historico', component: HistoricoComponent},
    ],
    canActivate: [AuthGuard]
  },

  // Public
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent }

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
