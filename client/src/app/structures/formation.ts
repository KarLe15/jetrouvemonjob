export interface Formation {
  id_formation?: number;
  debut_mois?: number;
  debut_annee?: number;
  fin_mois?: number;
  fin_annee?: number;
  etablissement: string;
  nom_diplome: string;
  niveau_etude?: string;
  id_cv?: number;
}
