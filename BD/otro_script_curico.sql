--Script de base de datos

drop database "BDCurico";

create database "BDCurico" WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'C' LC_CTYPE = 'C';

alter database "BDCurico" owner to postgres;

\connect "BDCurico"

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

insert into EstacionDeServicio values ('Curico', 0.01, 400, 400, 400, 400, 400);
insert into Surtidor values ('Curico_93', '93', 400, 0, 200, 0, 'Curico'),
							('Curico_95', '95', 400, 0, 200, 0, 'Curico'),
							('Curico_97', '97', 400, 0, 200, 0, 'Curico'),
							('Curico_Diesel', 'Diesel', 400, 0, 200, 0, 'Curico'),
							('Curico_Kerosene', 'Kerosene', 400, 0, 200, 0, 'Curico');

create or replace function litros_disponibles(varchar(45), varchar(45), float) returns float as $$
	declare
		tipo_precio alias for $1;
		tipo_combustible alias for $2;
		cantidad alias for $3;
		cantidad_actual float;
		cantidad_cargada float;
		nRecargas integer;
		fin float;
		referencia_surtidor varchar(45);
		precio_venta int;
		precio_actual int;
	begin
		select LitrosDisponibles, LitrosConsumidos, CargasRealizadas, Nombre, Precio into cantidad_actual, cantidad_cargada, nRecargas, referencia_surtidor, precio_actual 
		from Surtidor where tipo_combustible = Surtidor.tipo;
		-- Se encarga de vender al precio actual, es decir, al precio que esta en la Estacion de servicio, el cual puede ser diferente que el del surtidor si es que anterior a esta
		-- venta se ha realizado un cambio de precio.
		execute 'select ' || tipo_precio || ' from EstacionDeServicio' into precio_venta;
		-- Se encarga de actualizar el precio en Surtidor si es que el precio de venta ha cambiado.
		if precio_actual <> precio_venta then
			update Surtidor set Precio = precio_venta where Tipo = tipo_combustible;
		end if;
		fin := (cantidad_actual - cantidad);
		-- Se encarga de que haya combustible disponible para cargar.
		-- El precio de ventas hace referencia al monto total, o sea, cantidad cargada (ya sea cantidad_actual o cantidad) por el precio actual.
		if cantidad_actual <> 0 then
			if fin < 0 then
				update Surtidor set LitrosDisponibles = 0, LitrosConsumidos = (cantidad_cargada + cantidad_actual), CargasRealizadas = (nRecargas + 1 ) where Tipo = tipo_combustible;
				insert into Ventas values (referencia_surtidor, (precio_venta*cantidad_actual), cantidad_actual);
				-- Devuelve la cantidad cargada de forma negativa, por razones de programacion.
				fin := (cantidad_actual*(-1));
			elseif fin >= 0 then
				update Surtidor set LitrosDisponibles = fin, LitrosConsumidos = (cantidad_cargada + cantidad), CargasRealizadas = (nRecargas + 1 ) where Tipo = tipo_combustible;
				insert into Ventas values (referencia_surtidor, (precio_venta*cantidad), cantidad);
				fin := cantidad;
			end if;
		elseif cantidad_actual = 0 then
			fin := 0;
		end if;
		return fin;
	end; $$ language plpgsql volatile cost 100;

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;