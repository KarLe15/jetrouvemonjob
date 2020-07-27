package fr.pc3r.jetrouvemonjob.beans;

import java.util.Objects;

public class Experience {
    int debut_mois;
    int debut_annee;
    int fin_mois;
    int fin_annee;
    String entreprise;
    String post_description;
    String intitule;

    public Experience(
        int debut_mois,
        int debut_annee,
        int fin_mois,
        int fin_annee,
        String entreprise,
        String post_description,
        String intitule
    ) {
        this.debut_mois = debut_mois;
        this.debut_annee = debut_annee;
        this.fin_mois = fin_mois;
        this.fin_annee = fin_annee;
        this.entreprise = entreprise;
        this.post_description = post_description;
        this.intitule = intitule;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Experience that = (Experience) o;
        return debut_mois == that.debut_mois &&
            debut_annee == that.debut_annee &&
            fin_mois == that.fin_mois &&
            fin_annee == that.fin_annee &&
            Objects.equals(entreprise, that.entreprise) &&
            Objects.equals(post_description, that.post_description) &&
            Objects.equals(intitule, that.intitule);
    }

    @Override
    public int hashCode() {
        return Objects.hash(debut_mois, debut_annee, fin_mois, fin_annee, entreprise, post_description, intitule);
    }
}
