--Script de base de datos

drop database "estacion_de_servicio";

create database "estacion_de_servicio" WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'C' LC_CTYPE = 'C';

alter database "estacion_de_servicio" owner to postgres;

\connect "estacion_de_servicio"

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

create schema public;

alter schema public owner to postgres;

--
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON SCHEMA public IS 'standard public schema';


--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

create table estacion(
	nombre_estacion text not null,
	factor_utilidad float not null
);

alter table estacion owner to postgres;

create table dispensadores(
	nombre text not null,
	precio integer not null,
	cantidad_total integer not null,
	cantidad_disponible integer not null
);

alter table dispensadores owner to postgres;

create table listaDeCompra(
	nombre_dispensador text not null,
	cantidad integer not null,
	precio integer not null
);

alter table listaDeCompra owner to postgres;

insert into dispensadores (nombre, precio, cantidad_total, cantidad_disponible)
values ('93', 600, 100, 100), ('97', 610, 120, 100), ('95', 605, 200, 190), ('diesel', 400, 300, 150), ('kerosene', 300, 500, 420);

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;
