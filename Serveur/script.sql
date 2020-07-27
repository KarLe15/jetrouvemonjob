create table cv
(
    id_cv int auto_increment
        primary key,
    titre varchar(64) null
);

create table competences
(
    id_comp  int auto_increment
        primary key,
    nom_comp varchar(64) null,
    id_cv    int         null,
    constraint competences_cv_id_cv_fk
        foreign key (id_cv) references cv (id_cv)
);

create table experiences
(
    id_exp           int auto_increment
        primary key,
    debut_mois       int          null,
    debut_annee      int          null,
    fin_mois         int          null,
    fin_annee        int          null,
    entreprise       varchar(256) null,
    post_description text         null,
    intitule         varchar(256) null,
    id_cv            int          null,
    constraint experiences_cv_id_cv_fk
        foreign key (id_cv) references cv (id_cv)
);

create table formations
(
    id_formation  int auto_increment
        primary key,
    debut_mois    int          null,
    debut_annee   int          null,
    fin_mois      int          null,
    fin_annee     int          null,
    etablissement varchar(32)  null,
    nom_diplome   varchar(256) null,
    niveau_etude  varchar(16)  null,
    id_cv         int          null,
    constraint formations_cv_id_cv_fk
        foreign key (id_cv) references cv (id_cv)
);

create table users
(
    id       int auto_increment
        primary key,
    username varchar(256) not null,
    nom      varchar(256) not null,
    prenom   varchar(256) not null,
    mail     varchar(256) not null,
    id_cv    int          null,
    password varchar(256) null,
    constraint users_cv_users_id_cv_fk
        foreign key (id_cv) references cv (id_cv)
);

create table commentaires
(
    id_com      int auto_increment
        primary key,
    id_user     int           null,
    id_offre    varchar(16)   not null,
    content_com text          not null,
    nb_likes    int default 0 null,
    nb_dislikes int default 0 null,
    constraint commentaires_users_id_fk
        foreign key (id_user) references users (id)
);

create table user_connexion
(
    id_user        int         not null,
    session_key    varchar(32) not null,
    last_connexion mediumtext  null,
    primary key (id_user, session_key),
    constraint user_connexion_users_id_fk
        foreign key (id_user) references users (id)
);

create table user_dislike
(
    id_user        int null,
    id_commentaire int null,
    constraint user_dislike_commentaires_id_com_fk
        foreign key (id_commentaire) references commentaires (id_com),
    constraint user_dislike_users_id_fk
        foreign key (id_user) references users (id)
);

create table user_like
(
    id_user        int null,
    id_commentaire int null,
    constraint user_like_commentaires_id_com_fk
        foreign key (id_commentaire) references commentaires (id_com),
    constraint user_like_users_id_fk
        foreign key (id_user) references users (id)
);

create table users_bookmark
(
    id_user  int         null,
    id_offre varchar(16) null
);

create index users_bookmark_users_id_fk
    on users_bookmark (id_user);


