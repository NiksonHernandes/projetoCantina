import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './presentation/pages/home/home.component';
import { AuthGuard } from './core/security/auth.guard';
import { LoginComponent } from './presentation/public-routing/login/login/login.component';

const routes: Routes = [
   // Authentication Required

   { path: '', component: HomeComponent, canActivate: [AuthGuard] },

   // Public

   { path: '', redirectTo: 'login', pathMatch: 'full' },
   { path: 'login', component: LoginComponent }
];

@NgModule({
   imports: [RouterModule.forRoot(routes)],
   exports: [RouterModule]
})
export class AppRoutingModule { }
