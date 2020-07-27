package fr.pc3r.jetrouvemonjob.beans;

import java.util.Objects;

public class Competence {
    String nom_comp;

    public Competence(String nom_comp) {
        this.nom_comp = nom_comp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Competence that = (Competence) o;
        return Objects.equals(nom_comp, that.nom_comp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nom_comp);
    }
}
