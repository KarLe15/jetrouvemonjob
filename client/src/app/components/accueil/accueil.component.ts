import { Component, OnInit } from '@angular/core';
import {SearchService} from '../../services/search.service';
import {Router} from '@angular/router';
import {log} from 'util';

@Component({
  selector: 'app-accueil',
  templateUrl: './accueil.component.html',
  styleUrls: ['./accueil.component.scss']
})
export class AccueilComponent implements OnInit {

  form = {
    KEYWORD: null,
    DEPARTEMENTS: null
  };
  keyword = 'libelle';
  public appellations ;

  constructor(private service: SearchService,
              private router: Router ) {
  }

  ngOnInit() {
    console.log('get Filters init');
    this.service.getFilters('DEPARTEMENTS').then(
      response => {
        if ( response.status === 'accepted') {
          this.appellations = response.results.departements;
        }
      })
      .catch( err => console.log(err));
  }

  submit() {
    this.service.searchOnApi(this.form).then(
      response => {
        console.log('result of request ', response);
        this.router.navigate(['/result'],
          {
            queryParams: {KEYWORD: this.form.KEYWORD, DEPARTEMENTS: this.form.DEPARTEMENTS},
            state: {
              resultats: response.results.resultats,
              filtres: response.results.filtresPossibles}
          });
      }
    );
  }


  selectEvent(item) {
    this.form.DEPARTEMENTS = item.code;
  }
}
