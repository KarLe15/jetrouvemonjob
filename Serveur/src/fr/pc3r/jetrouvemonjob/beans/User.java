package fr.pc3r.jetrouvemonjob.beans;

import java.util.Set;

public class User {
    public static final User EMPTY_USER = new User("Empty", "empty");
    private String username;
    private String nom;
    private String prenom;
    private String mail;
    private String passwordHash;
    private int cvID;
    private CV cv;
    private Set<Bookmark> bookmarks;
    private Set<Like> likes;
    private Set<Like> dislikes;

    public User(
        String username,
        String nom,
        String prenom,
        String mail,
        String passwordHash,
        int cvID
    ) {
        this.username = username;
        this.nom = nom;
        this.prenom = prenom;
        this.mail = mail;
        this.passwordHash = passwordHash;
        this.cvID = cvID;
        this.cv = CV.EMPTY_CV;
    }

    public User(
        String username,
        String nom,
        String prenom,
        String mail,
        CV cv,
        Set<Bookmark> bookmarks,
        Set<Like> likes,
        Set<Like> dislikes
    ) {
        this.username = username;
        this.nom = nom;
        this.prenom = prenom;
        this.mail = mail;
        this.cv = cv;
        this.bookmarks = bookmarks;
        this.likes = likes;
        this.dislikes = dislikes;
    }

    public User(String username, String nom, String prenom, String mail, String passwordHash) {
        this.username = username;
        this.nom = nom;
        this.prenom = prenom;
        this.mail = mail;
        this.passwordHash = passwordHash;
    }

    public User(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.cv = CV.EMPTY_CV;
    }

    public String getUsername() {
        return username;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getMail() {
        return mail;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public int getCvID() {
        return cvID;
    }

    public CV getCv() {
        return cv;
    }
}
