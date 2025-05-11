-- Script para criar o usuário ecotrack no banco de dados Oracle
ALTER SESSION SET CONTAINER=XEPDB1;

-- Criar o usuário com privilégios
CREATE USER ecotrack IDENTIFIED BY ecotrack
    DEFAULT TABLESPACE USERS
    TEMPORARY TABLESPACE TEMP
    QUOTA UNLIMITED ON USERS;

-- Conceder privilégios necessários
GRANT CONNECT, RESOURCE TO ecotrack;
GRANT CREATE VIEW, CREATE PROCEDURE, CREATE MATERIALIZED VIEW TO ecotrack;
GRANT CREATE SYNONYM, CREATE SEQUENCE, CREATE TRIGGER TO ecotrack;
GRANT CREATE TABLE, CREATE SESSION TO ecotrack;
GRANT SELECT ANY DICTIONARY TO ecotrack;

-- Script de verificação de integridade para healthcheck do Docker
CREATE OR REPLACE PROCEDURE healthcheck IS
BEGIN
    NULL;
END;
/

EXIT;
