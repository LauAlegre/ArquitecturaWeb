/*****************************************************************************************
 ⬛ ESQUEMA COMPLETO – PROYECTO MICROSERVICIOS MONOPATINES
 Incluye:
    ✔ Usuarios / Cuentas (appdb)
    ✔ Monopatines / Paradas (monopatines_db)
    ✔ Facturación (db_facturacion) → tabla correcta "facturas"
    ✔ Viajes (Mongo → SQL)
    ✔ Pausas (Mongo → SQL)
*****************************************************************************************/


/***********************************
  USUARIOS Y CUENTAS (APPDB)
***********************************/
CREATE TABLE usuario (
                         id BIGINT PRIMARY KEY AUTO_INCREMENT,
                         apellido VARCHAR(255),
                         celular VARCHAR(255),
                         email VARCHAR(255),
                         nombre VARCHAR(255),
                         rol ENUM('ADMIN','CLIENTE')
);

CREATE TABLE cuenta (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        activa BIT(1),
                        fecha_alta DATE,
                        mercado_pago_id VARCHAR(255),
                        saldo DOUBLE,
                        tipo_cuenta ENUM('BASICA','PREMIUM')
);

CREATE TABLE cuenta_usuarios (
                                 cuentas_id BIGINT,
                                 usuarios_id BIGINT,
                                 FOREIGN KEY (cuentas_id) REFERENCES cuenta(id),
                                 FOREIGN KEY (usuarios_id) REFERENCES usuario(id)
);


/***********************************
  TARIFA (COMPARTIDO)
***********************************/
CREATE TABLE tarifa (
                        id_tarifa BIGINT PRIMARY KEY AUTO_INCREMENT,
                        fecha_fin_vigencia DATE,
                        fecha_inicio_vigencia DATE,
                        precio_extra_pausa DOUBLE NOT NULL,
                        precio_minuto DOUBLE NOT NULL
);


/***********************************
  FACTURACIÓN (DB_FACTURACION)
  ✔ TABLA CORRECTA: facturas
***********************************/
CREATE TABLE facturas (
                          id BIGINT PRIMARY KEY AUTO_INCREMENT,
                          fecha_emision DATE NOT NULL,
                          id_cuenta BIGINT NOT NULL,
                          id_viaje VARCHAR(40) NOT NULL,  -- viaje de Mongo (ObjectId string)
                          monto_total DOUBLE NOT NULL
);


/***********************************
  MONOPATINES (monopatines_db)
***********************************/
CREATE TABLE parada (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        capacidad INT,
                        latitud DOUBLE,
                        longitud DOUBLE,
                        nombre VARCHAR(255)
);

CREATE TABLE monopatin (
                           id BIGINT PRIMARY KEY AUTO_INCREMENT,
                           estado ENUM('DADO_DE_BAJA','DISPONIBLE','EN_MANTENIMIENTO','EN_USO','PAUSADO'),
                           latitud DOUBLE,
                           longitud DOUBLE,
                           parada_id BIGINT,
                           total_km DOUBLE,
                           total_tiempo_uso BIGINT,
                           FOREIGN KEY (parada_id) REFERENCES parada(id)
);


/***********************************
  VIAJES (MongoDB → SQL)
  Convertido desde viajes.bson + tu código Java
***********************************/
CREATE TABLE viajes (
                        id VARCHAR(40) PRIMARY KEY,       -- ObjectId de Mongo
                        id_cuenta BIGINT,
                        id_usuario BIGINT,
                        fecha_inicio DATETIME,
                        fecha_fin DATETIME,
                        km_recorridos DOUBLE,
                        minutos_totales BIGINT
);


/***********************************
  PAUSAS (MongoDB → SQL)
***********************************/
CREATE TABLE pausas (
                        id VARCHAR(40) PRIMARY KEY,       -- ObjectId
                        viaje_id VARCHAR(40),
                        inicio DATETIME,
                        fin DATETIME,
                        FOREIGN KEY (viaje_id) REFERENCES viajes(id)
);

