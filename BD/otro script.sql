--Script de base de datos

drop database "BDSantiago";

create database "BDSantiago" WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'C' LC_CTYPE = 'C';

alter database "BDSantiago" owner to postgres;

\connect "BDSantiago"

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

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

create table EstacionDeServicio(
	Nombre varchar(45) primary key,
	FactorUtilidad float default 0.01,
	Precio93 int not null,
	Precio95 int not null,
	Precio97 int not null,
	PrecioDiesel int not null,
	PrecioKerosene int not null
);

alter table EstacionDeServicio owner to postgres;

create table Surtidor(
	Nombre varchar(45) primary key,
	Tipo varchar(45) not null,
	Precio int not null,
	LitrosConsumidos float not null,
	LitrosDisponibles float not null,
	CargasRealizadas int not null,
	RefEstacion varchar(45) not null,
	Foreign key (RefEstacion) References EstacionDeServicio(Nombre) on delete cascade
);

alter table Surtidor owner to postgres;

create table Ventas(
	RefSurtidor varchar(45) not null,
	Precio int not null,
	Litros float not null,
	Foreign key (RefSurtidor) References Surtidor(Nombre)
);

alter table Ventas owner to postgres;

insert into EstacionDeServicio values ('Santiago', 0.01, 400, 400, 400, 400, 400);
insert into Surtidor values ('Hi', '93', 400, 0, 200, 0, 'Santiago');

create or replace function litros_disponibles(varchar(45), float) returns float as $$
	declare
		tipo_combustible alias for $1;
		cantidad alias for $2;
		cantidad_actual float;
		cantidad_cargada float;
		nRecargas integer;
		fin float;
		referencia_surtidor varchar(45);
		precio_venta int;
	begin
		select LitrosDisponibles, LitrosConsumidos, CargasRealizadas, Nombre, Precio into cantidad_actual, cantidad_cargada, nRecargas, referencia_surtidor, precio_venta 
		from Surtidor where tipo_combustible = Surtidor.tipo;
		fin := (cantidad_actual - cantidad);
		if fin < 0 then
			update Surtidor set LitrosDisponibles = 0, LitrosConsumidos = (cantidad_cargada + cantidad_actual), CargasRealizadas = (nRecargas + 1 ) where Tipo = tipo_combustible;
			insert into Ventas values (referencia_surtidor, precio_venta, cantidad_actual);
		elseif fin >= 0 then
			update Surtidor set LitrosDisponibles = fin, LitrosConsumidos = (cantidad_cargada + cantidad), CargasRealizadas = (nRecargas + 1 ) where Tipo = tipo_combustible;
			insert into Ventas values (referencia_surtidor, precio_venta, cantidad);
		end if;
		return fin;
	end; $$ language plpgsql volatile cost 100;

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;