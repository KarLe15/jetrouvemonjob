import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {LoginComponent} from './components/login/login.component';
import {SignupComponent} from './components/signup/signup.component';
import {ProfileComponent} from './components/profile/profile.component';
import {AccueilComponent} from './components/accueil/accueil.component';
import {ResultComponent} from './components/result/result.component';
import {SaveSpaceComponent} from './components/save-space/save-space.component';
import {AuthGuard} from './services/auth-guard.service';


const routes: Routes = [
  {
    path: '',
    component: AccueilComponent
  },
  {
    path: 'login',
    component: LoginComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'signup',
    component: SignupComponent
  },
  {
    path: 'profile',
    component: ProfileComponent,
  },
  {
    path: 'result',
    component: ResultComponent
  },
  {
    path: 'save',
    component: SaveSpaceComponent,
    canActivate: [AuthGuard]
  },

  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes)
  ],
  exports: [
    RouterModule
  ]
})
export class AppRoutingModule { }
