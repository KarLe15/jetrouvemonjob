import { Component, OnInit } from '@angular/core';
import {User} from '../../structures/user';
import {Formation} from '../../structures/formation';
import {Experience} from '../../structures/experience';
import {TokenService} from '../../services/token.service';
import {log} from 'util';
import {UserService} from '../../services/user.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
  private updateForm = false;
  urlImgProfile = '../assets/pictures/avatar.jpg';

  private user: User = null;

  private saveUser: User;
  skill = '';
  constructor(
    private token: TokenService,
    private userService: UserService
  ) { }

  ngOnInit() {
    this.token.loggedIn()
      .then(
        (respones) => {
          if (respones.status === 'accepted') {
            console.log('Profile ', respones);
            this.user = respones.result;
          } else {
            console.log('Profile probleme ', respones.error);
          }
        }
      )
      .catch( err => {
        console.log(err);
      });
  }

  update() {
    console.log('UPDATE');
    this.updateForm = true;
  }
  valideUpdate() {
    this.updateForm = false;
    console.log('validdd ', JSON.stringify(this.user));
    this.userService.updateCV(this.user, this.token.getUsername(), this.token.getSessionKey())
      .then(
        response => {
          if (response.status === 'accepted' ) {
            console.log('update cv ', response);
          } else {
            console.log('err update cv ', response.error);
          }
        }
      )
      .catch(err => console.log(err));
  }
  cancelUpdate() {
    console.log('Cancel UPDATE');
    this.updateForm = false;
    this.token.loggedIn()
      .then(
        (respones) => {
          if (respones.status === 'accepted') {
            console.log('Profile ', respones);
            this.user = respones.result;
          } else {
            console.log('Profile probleme ', respones.error);
          }
        }
      )
      .catch( err => {
        console.log(err);
      });
  }


  /* --------------- l'ajout --------------- */
  addEtude() {
    console.log('ADD etudes');
  }

  addExprience() {
    console.log('ADD experience');
  }

  addcompetence() {
    console.log('add Skill');
    this.user.cv.competences.push({
      nom_comp: this.skill
    });
    this.skill = '';
  }

  /* ------------------- Update --------------------*/

  updateFormation(e) {
    if (e.id !== this.user.cv.formations.length) {
      this.user.cv.formations[e.id].etablissement = e.name;
      this.user.cv.formations[e.id].nom_diplome = e.description;
    } else {
      const f: Formation = {
        etablissement: e.name,
        nom_diplome: e.description,
      };
      this.user.cv.formations.push(f);
    }
  }

  removeFormation(index: number) {
    this.user.cv.formations[index].nom_diplome = '';
    this.user.cv.formations[index].etablissement = '';
    this.user.cv.formations.splice(index, 1);
    document.getElementById('formation' + index).remove();
  }

  updateExperience(e: { id: number; name: string; description: string }) {
    console.log('ud  ', e);
    if (e.id !== this.user.cv.experiences.length) {
      this.user.cv.experiences[e.id].intitule = e.name;
      this.user.cv.experiences[e.id].entreprise = e.description;
    } else {
      const f: Experience = {
        intitule: e.name,
        entreprise: e.description,
      };
      this.user.cv.experiences.push(f);
    }
  }

  removeExperience(index: number) {
    this.user.cv.experiences[index].intitule = '';
    this.user.cv.experiences[index].entreprise = '';
    this.user.cv.experiences.splice(index, 1);
    document.getElementById('experience' + index).remove();
  }

  removeSkills(index: number) {
    this.user.cv.competences.splice(index, 1);
  }
}
