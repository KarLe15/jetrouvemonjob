import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { LoginComponent } from './components/login/login.component';
import { SignupComponent } from './components/signup/signup.component';
import { ProfileComponent } from './components/profile/profile.component';
import {FormsModule} from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';
import { AccueilComponent } from './components/accueil/accueil.component';
import { ModalFormComponent } from './components/modal-form/modal-form.component';
import { FooterComponent } from './components/footer/footer.component';
import { ResultComponent } from './components/result/result.component';
import {UiSwitchModule} from 'ngx-ui-switch';
import {AngularPaginatorModule} from 'angular-paginator';
import { SaveSpaceComponent } from './components/save-space/save-space.component';
import { SummaryPipe } from './pipes/summary.pipe';
import {AutocompleteLibModule} from 'angular-ng-autocomplete';

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    LoginComponent,
    SignupComponent,
    ProfileComponent,
    AccueilComponent,
    ModalFormComponent,
    FooterComponent,
    ResultComponent,
    SaveSpaceComponent,
    SummaryPipe,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    UiSwitchModule,
    AngularPaginatorModule,
    AutocompleteLibModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
