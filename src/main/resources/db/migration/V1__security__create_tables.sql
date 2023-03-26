SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = ON;
SET check_function_bodies = FALSE;
SET client_min_messages = WARNING;
SET row_security = OFF;
CREATE SCHEMA IF NOT EXISTS security;
ALTER SCHEMA security OWNER TO market_owner;
SET search_path = security, pg_catalog;
SET default_tablespace = '';
SET default_with_oids = FALSE;

CREATE TABLE if not exists auth_user
(
    id             BIGINT                NOT NULL,
    uuid           UUID                  NOT NULL,
    create_date    TIMESTAMP             NOT NULL,
    first_name     VARCHAR(50)           NOT NULL,
    last_name      VARCHAR(50)           NOT NULL,
    birth_date     DATE                  NULL,
    image_url      TEXT                  NULL,
    provider       VARCHAR(15)           NOT NULL,
    provider_id    VARCHAR(255)          NOT NULL,
    email          VARCHAR(100)          NOT NULL,
    password       VARCHAR(255)          NOT NULL,
    login_date     TIMESTAMP             NULL,
    active         BOOLEAN DEFAULT FALSE NOT NULL,
    email_verified BOOLEAN DEFAULT FALSE NOT NULL,
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
comment on column auth_user.image_url is 'The auth_user image provider';
comment on column auth_user.provider is 'The auth_user provider';
comment on column auth_user.provider_id is 'The auth_user provider id';
comment on column auth_user.email_verified is 'The auth_user is email verified';
comment on column auth_user.email is 'The auth_user email';
comment on column auth_user.password is 'The auth_user password';
comment on column auth_user.login_date is 'The auth_user last login date';
comment on column auth_user.active is 'The auth_user status';

ALTER TABLE if exists auth_user
    OWNER TO market_owner;

CREATE SEQUENCE if not exists auth_user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE if exists auth_user_id_seq
    OWNER TO market_owner;

/**************************************************************************************************/

CREATE TABLE if not exists auth_user_role
(
    id           BIGINT      NOT NULL,
    auth_user_id BIGINT      NOT NULL,
    role         VARCHAR(10) NOT NULL,
    PRIMARY KEY (id),
    constraint auth_user_role_unique UNIQUE (auth_user_id, role),
    CONSTRAINT auth_user_role_auth_user_id_fk FOREIGN KEY (auth_user_id) REFERENCES auth_user (id)
);

ALTER TABLE if exists roles
    OWNER TO market_owner;

CREATE SEQUENCE if not exists auth_user_role_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE if exists auth_user_role_id_seq
    OWNER TO market_owner;

comment on table auth_user_role is 'users roles table';
comment on column auth_user_role.auth_user_id is 'The auth user ID';
comment on column auth_user_role.role is 'The auth user role';

/**************************************************************************************************/

CREATE TABLE if not exists token
(
    id           BIGINT                NOT NULL,
    uuid         UUID                  NOT NULL,
    auth_user_id BIGINT                NOT NULL,
    token        VARCHAR(1000)         NOT NULL,
    token_type   VARCHAR(20)           NOT NULL,
    create_date  TIMESTAMP             NOT NULL,
    is_revoked   BOOLEAN DEFAULT FALSE NOT NULL,
    PRIMARY KEY (id),
    constraint token_uuid_unique UNIQUE (uuid),
    constraint token_unique UNIQUE (token),
    CONSTRAINT token_auth_user_id_fk FOREIGN KEY (auth_user_id) REFERENCES auth_user (id)
);

ALTER TABLE if exists token
    OWNER TO market_owner;

CREATE SEQUENCE if not exists token_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE if exists token_id_seq OWNER TO market_owner;

comment on table token is 'Token table';
comment on column token.id is 'The token ID';
comment on column token.uuid is 'The token unique identifier';
comment on column token.auth_user_id is 'auth_user Id (Foreign key)';
comment on column token.create_date is 'The token creation date';
comment on column token.is_revoked is 'is token revoked';

/***************************************************************************************/
SELECT pg_catalog.setval('auth_user_id_seq', 1, FALSE);
SELECT pg_catalog.setval('auth_user_role_id_seq', 1, FALSE);
SELECT pg_catalog.setval('token_id_seq', 1, FALSE);


REVOKE ALL ON SCHEMA security FROM PUBLIC;
REVOKE ALL ON SCHEMA security FROM market_owner;
GRANT ALL ON SCHEMA security TO market_owner;
GRANT USAGE ON SCHEMA security TO market_select;
GRANT USAGE ON SCHEMA security TO market_update;


REVOKE ALL ON TABLE auth_user FROM PUBLIC;
REVOKE ALL ON TABLE auth_user FROM market_owner;
GRANT ALL ON TABLE auth_user TO market_owner;
GRANT SELECT, INSERT, DELETE, TRUNCATE, UPDATE ON TABLE auth_user TO market_update;
GRANT SELECT ON TABLE auth_user TO market_select;

REVOKE ALL ON SEQUENCE auth_user_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE auth_user_id_seq FROM market_owner;
GRANT ALL ON SEQUENCE auth_user_id_seq TO market_owner;
GRANT USAGE ON SEQUENCE auth_user_id_seq TO market_select;


REVOKE ALL ON TABLE auth_user_role FROM PUBLIC;
REVOKE ALL ON TABLE auth_user_role FROM market_owner;
GRANT ALL ON TABLE auth_user_role TO market_owner;
GRANT SELECT, INSERT, DELETE, TRUNCATE, UPDATE ON TABLE auth_user_role TO market_update;
GRANT SELECT ON TABLE auth_user_role TO market_select;

REVOKE ALL ON SEQUENCE auth_user_role_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE auth_user_role_id_seq FROM market_owner;
GRANT ALL ON SEQUENCE auth_user_role_id_seq TO market_owner;
GRANT USAGE ON SEQUENCE auth_user_role_id_seq TO market_select;


REVOKE ALL ON TABLE token FROM PUBLIC;
REVOKE ALL ON TABLE token FROM market_owner;
GRANT ALL ON TABLE token TO market_owner;
GRANT SELECT, INSERT, DELETE, TRUNCATE, UPDATE ON TABLE token TO market_update;
GRANT SELECT ON TABLE token TO market_select;

REVOKE ALL ON SEQUENCE token_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE token_id_seq FROM market_owner;
GRANT ALL ON SEQUENCE token_id_seq TO market_owner;
GRANT USAGE ON SEQUENCE token_id_seq TO market_select;


ALTER DEFAULT PRIVILEGES FOR ROLE market_owner IN SCHEMA security REVOKE ALL ON SEQUENCES FROM PUBLIC;
ALTER DEFAULT PRIVILEGES FOR ROLE market_owner IN SCHEMA security REVOKE ALL ON SEQUENCES FROM market_owner;
ALTER DEFAULT PRIVILEGES FOR ROLE market_owner IN SCHEMA security GRANT ALL ON SEQUENCES TO market_owner;
ALTER DEFAULT PRIVILEGES FOR ROLE market_owner IN SCHEMA security GRANT USAGE ON SEQUENCES TO market_select;


ALTER DEFAULT PRIVILEGES FOR ROLE market_owner IN SCHEMA security REVOKE ALL ON FUNCTIONS FROM PUBLIC;
ALTER DEFAULT PRIVILEGES FOR ROLE market_owner IN SCHEMA security REVOKE ALL ON FUNCTIONS FROM market_owner;
ALTER DEFAULT PRIVILEGES FOR ROLE market_owner IN SCHEMA security GRANT ALL ON FUNCTIONS TO market_owner;
ALTER DEFAULT PRIVILEGES FOR ROLE market_owner IN SCHEMA security GRANT ALL ON FUNCTIONS TO market_select;


ALTER DEFAULT PRIVILEGES FOR ROLE market_owner IN SCHEMA security REVOKE ALL ON TABLES FROM PUBLIC;
ALTER DEFAULT PRIVILEGES FOR ROLE market_owner IN SCHEMA security REVOKE ALL ON TABLES FROM market_owner;
ALTER DEFAULT PRIVILEGES FOR ROLE market_owner IN SCHEMA security GRANT ALL ON TABLES TO market_owner;
ALTER DEFAULT PRIVILEGES FOR ROLE market_owner IN SCHEMA security GRANT SELECT, INSERT, DELETE, TRUNCATE, UPDATE ON TABLES TO market_update;
ALTER DEFAULT PRIVILEGES FOR ROLE market_owner IN SCHEMA security GRANT SELECT ON TABLES TO market_select;
