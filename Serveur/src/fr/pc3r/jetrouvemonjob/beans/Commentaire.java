package fr.pc3r.jetrouvemonjob.beans;

import java.util.Objects;

public class Commentaire {
    String content_com;
    int nb_likes;
    int nb_dislikes;
    int id_com;
    int id_user;

    public Commentaire(String content_com, int nb_likes, int nb_dislikes) {
        this.content_com = content_com;
        this.nb_likes = nb_likes;
        this.nb_dislikes = nb_dislikes;
    }

    public Commentaire(String content_com, int nb_likes, int nb_dislikes, int id_com, int id_user) {
        this.content_com = content_com;
        this.nb_likes = nb_likes;
        this.nb_dislikes = nb_dislikes;
        this.id_com = id_com;
        this.id_user = id_user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Commentaire that = (Commentaire) o;
        return nb_likes == that.nb_likes &&
            nb_dislikes == that.nb_dislikes &&
            Objects.equals(content_com, that.content_com);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content_com, nb_likes, nb_dislikes);
    }
}
