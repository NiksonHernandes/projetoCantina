import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { LoginComponent } from './presentation/pages/public-routing/login/login.component';
import { DashRoutingComponent } from './presentation/pages/dash-routing/dash-routing.component';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ErrorInterceptor, JwtInterceptor } from './core';
import { NavbarComponent } from './presentation/shared/navbar/navbar.component';
import { HomeComponent } from './presentation/pages/dash-routing/home/home/home.component';
import { CardapioComponent } from './presentation/pages/dash-routing/cardapio/cardapio.component';
import { GetCarrinhoOpenComponent } from './presentation/pages/dash-routing/carrinho/get-carrinho-open/get-carrinho-open.component';
import { DeleteAlimentoComponent } from './presentation/pages/dash-routing/carrinho/delete-alimento/delete-alimento.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    DashRoutingComponent,
    NavbarComponent,
    HomeComponent,
    CardapioComponent,
    GetCarrinhoOpenComponent,
    DeleteAlimentoComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgbModule,
    HttpClientModule,
    ReactiveFormsModule,
    FormsModule,
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true },
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
