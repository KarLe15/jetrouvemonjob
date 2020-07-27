import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {SearchService} from '../../services/search.service';
import {AuthenticationService} from '../../services/authentication.service';
import {User} from '../../structures/user';
import {TokenService} from '../../services/token.service';

@Component({
  selector: 'app-result',
  templateUrl: './result.component.html',
  styleUrls: ['./result.component.scss']
})
export class ResultComponent implements OnInit {
  /* Formulaire de recherche */
  form = {
    DEPARTEMENTS: null,
    KEYWORD: null
  };

  /* Filtres possibles */
  filtresPossibles: Array<Filtre>;
  /* L'ensembles des annonces*/
  resultats: Array<Resultat>;
  /* Annonce actuelle en détail */
  pageDetail: Resultat;
  /* Correspondances entre les codes et les intitules pour nature et type de contrat */
  natureContrat: Array<any>;
  typeContrat: Array<any>;

  /* Page sauvegardée ou pas */
  pageDetailSave = false;

  /* Pour la Pagination */
  currentPage = 0;
  itemsPerPage = 5;

  /* L'utilisateur en cours */
  private currentUser: User = null;

  /* L'ensembles des commentaires d'une annonces */
  commentaires: Array<Commentaire>;

  /* formulaire d'édition d'un nouveau commentaire */
  commentText: string;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private authService: AuthenticationService,
              private service: SearchService,
              private token: TokenService) { }

  ngOnInit() {
    /* recupération des mots cles de la recherche de l'acceuil */
    this.form.KEYWORD = this.route.snapshot.queryParams.KEYWORD;
    this.form.DEPARTEMENTS = this.route.snapshot.queryParams.DEPARTEMENTS;

    /* lancer une recherche */
    this.service.searchOnApi(this.form)
      .then( (response: ResponseApi) => {
        this.filtresPossibles = response.results.filtresPossibles;
        this.resultats = response.results.resultats;
        this.initFiltre();
        console.log('response get', response.results);
      });

    /* Récupération des filtres */
    this.service.getFilters(['NATURES', 'CONTRATS'])
      .then( (response) => {
        this.natureContrat = response.results.natures;
        this.typeContrat = response.results.typecontrat;
      });

    /* Vérifier si un utilisateur est déjà connecté */
    this.token.loggedIn()
      .then(
        (respones) => {
          if (respones.status === 'accepted') {
            console.log('user récupéré ', respones);
            this.currentUser = respones.result;
          } else {
            console.log('user probleme ', respones.error);
          }
        }
      )
      .catch( err => {
        console.log(err);
      })
    ;

  }


  /* Initialisation des filtres de la recherche */
  private initFiltre() {
    for (const f of this.filtresPossibles) {
      for (const g of f.agregation) {
        g.filterBool = true;
      }
    }
  }

  /* Validation d'une recherche */
  submit() {
    this.service.searchOnApi(this.form)
      .then(
        response => {
          if (response.status === 'accepted') {
            this.resultats = response.results.resultats;
            this.filtresPossibles = response.results.filtresPossibles;
          } else {
            console.log('recherche problem ', response.error);
          }
      }
    );
  }

  /* Activation d'un filtre */
  filtrer(i, j) {
    console.log('filtrer', this.filtresPossibles[i].agregation[j]);
    this.filtresPossibles[i].agregation[j].filterBool = !this.filtresPossibles[i].agregation[j].filterBool;
  }

  /* Montrer les détails d'une annonce */
  showDetails(i: number) {
    this.pageDetail = this.resultats[i];
    if (this.currentUser) {
      this.service.getCommentaires(this.pageDetail.id)
        .then(
          response => {
            if (response.status === 'accepted') {
              console.log('commentaires', response);
              this.commentaires = response.commentaires;
            } else {
              console.log('problem commentaires', response);
            }
          }
        )
        .catch( err => console.log(err));
    }
  }

  /* Calculer le nombre de jours passés sur l'annonce */
  calculateDate(dateCreation) {
    const date1: any = new Date(dateCreation);
    const date2: any = new Date();
    return Math.floor((date2 - date1) / (1000 * 60 * 60 * 24));
  }

  /* Sauvegarder une annonces */
  save(pageDetail: Resultat) {
    this.pageDetailSave = !this.pageDetailSave;
  }

  /* Ajouter un commentaire */
  addComment(pageDetail: Resultat) {
    this.service.addComment(pageDetail.id, this.token.getUsername(), this.token.getSessionKey(), this.commentText)
      .then(response => {
        if (response.status === 'accepted') {
          console.log('commentaire', response);
          this.commentaires.push( {
            id_com: '2',
            nb_likes: 0,
            nb_dislikes: 0,
            content_com: this.commentText
          });
          this.commentText = '';
        } else {
          console.log('probleme comment', response.error);
        }
      }).catch(err => console.log(err));
  }

  /* Liker un commentaire */
  like(index) {

    this.service.like(this.commentaires[index].id_com, this.token.getUsername(), this.token.getSessionKey())
      .then(response => {
      if (response.status === 'accepted') {
        console.log('like', response);
      } else {
        console.log('probleme like', response.error);
      }
    }).catch(err => console.log(err));
    this.commentaires[index].nb_likes =  this.commentaires[index].nb_likes + 1 ;
  }

  /* Disliker un commentaire */
  dislike(index) {
    this.service.disLike(this.commentaires[index].id_com, this.token.getUsername(), this.token.getSessionKey())
      .then(response => {
      if (response.status === 'accepted') {
        console.log('dislike', response);
      } else {
        console.log('probleme dislike', response.error);
      }
    }).catch(err => console.log(err));
    this.commentaires[index].nb_dislikes =  this.commentaires[index].nb_dislikes + 1 ;
  }

  /* Remplacer les codes par des intitules dans les filtres */
  getIntituleFromValeur(valeurPossible: string, item: Filtre) {
    if ( this.natureContrat === undefined ) {
      return '';
    }
    // @ts-ignore
    switch (item.filtre) {
      case 'natureContrat' :
        return  this.natureContrat.find(e => e.code === valeurPossible).libelle;
      case 'typeContrat':
        return  this.typeContrat.find(e => e.code === valeurPossible).libelle;
      default:
        return valeurPossible;
    }
  }

  /* Vérifier si l'annonce s'affichera ou pas selon les filtres  */
  toShow(item: Resultat) {
    for (const f of this.filtresPossibles) {
      for (const a of f.agregation ) {
        if (!a.filterBool && a.valeurPossible === item[f.filtre]) {
          return false;
        }
      }
    }
    return true;
  }
}
