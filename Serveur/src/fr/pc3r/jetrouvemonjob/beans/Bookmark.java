package fr.pc3r.jetrouvemonjob.beans;

import java.util.Objects;

public class Bookmark {
    String id_offre;

    public Bookmark(String id_offre) {
        this.id_offre = id_offre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bookmark bookmark = (Bookmark) o;
        return id_offre.equals(bookmark.id_offre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_offre);
    }
}
