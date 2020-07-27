package fr.pc3r.jetrouvemonjob.beans;

import java.util.Objects;

public class Formation {
    int debut_mois;
    int debut_annee;
    int fin_mois;
    int fin_annee;
    String etablissement;
    String nom_diplome;
    String niveau_etude;

    public Formation(
        int debut_mois,
        int debut_annee,
        int fin_mois,
        int fin_annee,
        String etablissement,
        String nom_diplome,
        String niveau_etude
    ) {
        this.debut_mois = debut_mois;
        this.debut_annee = debut_annee;
        this.fin_mois = fin_mois;
        this.fin_annee = fin_annee;
        this.etablissement = etablissement;
        this.nom_diplome = nom_diplome;
        this.niveau_etude = niveau_etude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Formation formation = (Formation) o;
        return debut_mois == formation.debut_mois &&
            debut_annee == formation.debut_annee &&
            fin_mois == formation.fin_mois &&
            fin_annee == formation.fin_annee &&
            Objects.equals(etablissement, formation.etablissement) &&
            Objects.equals(nom_diplome, formation.nom_diplome) &&
            Objects.equals(niveau_etude, formation.niveau_etude);
    }

    @Override
    public int hashCode() {
        return Objects.hash(debut_mois, debut_annee, fin_mois, fin_annee, etablissement, nom_diplome, niveau_etude);
    }
}
