CREATE TABLE if not exists auth_user
(
    id          BIGINT                 NOT NULL,
    uuid        UUID                   NOT NULL,
    create_date TIMESTAMP              NOT NULL,
    first_name  CHARACTER VARYING(50)  NOT NULL,
    last_name   CHARACTER VARYING(50)  NOT NULL,
    birth_date  DATE                   NULL,
    gender      CHARACTER VARYING(6)   NULL,
    mail        CHARACTER VARYING(255) NOT NULL,
    password    CHARACTER VARYING(255)  NOT NULL,
    login_date  TIMESTAMP              NULL,
    active      BOOLEAN DEFAULT FALSE  NOT NULL,
    PRIMARY KEY (id),
    constraint auth_user_uuid_unique UNIQUE (uuid)
);

comment on table auth_user is 'auth_user main table';
comment on column auth_user.id is 'The auth_user ID';
comment on column auth_user.uuid is 'The auth_user unique identifier';
comment on column auth_user.create_date is 'The auth_user creation (insert) date';
comment on column auth_user.first_name is 'The auth_user first name';
comment on column auth_user.last_name is 'The auth_user last name';
comment on column auth_user.birth_date is 'The auth_user birth date';
comment on column auth_user.gender is 'The auth_user gender';
comment on column auth_user.mail is 'The auth_user email';
comment on column auth_user.password is 'The auth_user password';
comment on column auth_user.login_date is 'The auth_user last login date';
comment on column auth_user.active is 'The auth_user status';

ALTER TABLE if exists auth_user
    OWNER TO marketOwner;

CREATE SEQUENCE if not exists auth_user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE if exists auth_user_id_seq
    OWNER TO marketOwner;

/**************************************************************************************************/

CREATE TABLE if not exists auth_user_role
(
    id           BIGINT                NOT NULL,
    uuid         UUID                  NOT NULL,
    auth_user_id BIGINT                NOT NULL,
    role         CHARACTER VARYING(50) NOT NULL,
    create_date  TIMESTAMP             NOT NULL,
    PRIMARY KEY (id),
    constraint auth_user_role_uuid_unique UNIQUE (uuid),
    constraint auth_user_role_unique UNIQUE (auth_user_id, role),
    CONSTRAINT auth_user_role_auth_user_id_fk FOREIGN KEY (auth_user_id) REFERENCES auth_user (id)
);

ALTER TABLE if exists roles
    OWNER TO marketOwner;

CREATE SEQUENCE if not exists auth_user_role_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE if exists auth_user_role_id_seq
    OWNER TO marketOwner;

comment on table auth_user_role is 'users roles table';
comment on column auth_user_role.auth_user_id is 'The auth user ID';
comment on column auth_user_role.role is 'The auth user role';
comment on column auth_user_role.uuid is 'The auth_user unique identifier';
comment on column auth_user_role.create_date is 'The auth_user creation (insert) date';

/**************************************************************************************************/

CREATE TABLE if not exists token
(
    id           BIGINT                NOT NULL,
    uuid         UUID                  NOT NULL,
    auth_user_id BIGINT                NOT NULL,
    token        varchar(1000)                  NOT NULL,
    tokenType    CHARACTER VARYING(50) NOT NULL,
    create_date  TIMESTAMP             NOT NULL,
    is_revoked   BOOLEAN DEFAULT FALSE NOT NULL,
    PRIMARY KEY (id),
    constraint token_uuid_unique UNIQUE (uuid),
    constraint token_unique UNIQUE (token),
    CONSTRAINT token_auth_user_id_fk FOREIGN KEY (auth_user_id) REFERENCES auth_user (id)
);

ALTER TABLE if exists token
    OWNER TO marketOwner;

CREATE SEQUENCE if not exists token_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE if exists token_id_seq
    OWNER TO marketOwner;

comment on table token is 'Token table';
comment on column token.id is 'The token ID';
comment on column token.uuid is 'The token unique identifier';
comment on column token.auth_user_id is 'auth_user Id (Foreign key)';
comment on column token.create_date is 'The token creation date';
comment on column token.is_revoked is 'is token revoked';