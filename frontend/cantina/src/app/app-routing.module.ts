import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashRoutingComponent } from './presentation/pages/dash-routing/dash-routing.component';
import { AuthGuard } from './core/security/auth.guard';
import { LoginComponent } from './presentation/pages/public-routing/login/login.component';
import { HomeComponent } from './presentation/pages/dash-routing/home/home/home.component';

const routes: Routes = [
  // { path: '', component: HomeComponent }

  // Authentication Required
  {
    path: 'dash', component: DashRoutingComponent, children: [
      {path: '', component: HomeComponent}
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
