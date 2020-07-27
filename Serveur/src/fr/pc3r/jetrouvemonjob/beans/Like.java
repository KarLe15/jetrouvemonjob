package fr.pc3r.jetrouvemonjob.beans;

import java.util.Objects;

public class Like {
    String idCommentaire;

    public Like(String idCommentaire) {
        this.idCommentaire = idCommentaire;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Like like = (Like) o;
        return Objects.equals(idCommentaire, like.idCommentaire);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCommentaire);
    }
}
