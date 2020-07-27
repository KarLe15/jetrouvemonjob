import {Formation} from './formation';
import {Experience} from './experience';
import {Adresse} from './adresse';

export interface User {
  mail: string;
  nom: string;
  prenom: string;
  username: string;
  cv?: {
    formations?: Array<Formation>;
    experiences?: Array<Experience>;
    competences?: Array<{
      id_comp?: number;
      nom_comp: string;
      id_cv?: number;
    }>
  };
  cvID?: number;
  password?: string;
  adresse?: Adresse;
  confirmedPassword?: string;
}


