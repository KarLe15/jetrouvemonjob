// interface ResultData {
//
// }


interface Commentaire {
  id_com: string;
  id_user?: string;
  content_com: string;
  nb_likes: number;
  nb_dislikes: number;
}

interface ResponseApiUser {
  status: string;
  results?: string;
  error?: {
    code: number,
    message: string
  };
}



interface ResponseApi {
  status: string;
  results: {
    filtresPossibles: Array<Filtre>,
    resultats: Array<Resultat>;
  };
}

interface Filtre {
  agregation: Array<{nbResultats: number, valeurPossible: string, filterBool?: boolean}>;
  filtre: string;
}

interface Formation {
  codeFormation: string;
  commentaire: string;
  exigence: string;
  niveauLibelle: string;
}

interface Resultat {
  id: string;
  appellationlibelle: string;
  description: string;
  entreprise: {nom: string };
  lieuTravail: {
    codePostal: string;
    commune: string;
    libelle: string;
  };
  dureeTravailLibelle: string;
  dureeTravailLibelleConverti: string;
  qualificationLibelle: string;
  natureContrat: string;
  typeContrat: string;
  alternance: boolean;
  typeContratLibelle: string;
  dateCreation: string;
  intitule: string;
  competences: Array<Competence>;
  formations: Array<Formation>;
  contact: {
    commentaire: string;
    courriel: string;
    nom: string;
  };
}

interface Competence {
  code: string;
  exigence: string;
  libelle: string;
}
