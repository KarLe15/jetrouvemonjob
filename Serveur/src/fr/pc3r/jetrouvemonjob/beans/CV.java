package fr.pc3r.jetrouvemonjob.beans;

import java.util.Set;

public class CV {
    public static final CV EMPTY_CV = new CV(null, null, null);
    // this class represents a Curriculum Vitae

    private Set<Competence> competences;
    private Set<Experience> experiences;
    private Set<Formation> formations;

    public CV(Set competences, Set experiences, Set formations) {
        this.competences = competences;
        this.experiences = experiences;
        this.formations = formations;
    }

}
