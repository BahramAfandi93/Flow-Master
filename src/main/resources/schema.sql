 CREATE SCHEMA IF NOT EXISTS FlowMaster;

CREATE TABLE IF NOT EXISTS FlowMaster.CulvertEntity
(
    id              SERIAL PRIMARY KEY,
    projectName     VARCHAR(255),
    projectLocation VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS FlowMaster.Culvert_Entity
(
    id                serial primary key,
    location          varchar(255),
    projectName       varchar(255),
    chainage          varchar(255),
    material          varchar(255),
    flowHeight        INT,
    rainIntensity     INT,
    calculationArea   INT,
    slope             DOUBLE PRECISION,
    shape             varchar(255),
    structureDiameter DOUBLE PRECISION,
    structureWidth    DOUBLE PRECISION,
    structureHeight   DOUBLE PRECISION,
    waterSpeed        DOUBLE PRECISION,
    wettedPerimeter   DOUBLE PRECISION,
    flowArea          DOUBLE PRECISION,
    hydraulicRadius   DOUBLE PRECISION,
    minAllowedSlope   DOUBLE PRECISION,
    roughness         DOUBLE PRECISION,
    flowRate          DOUBLE PRECISION,
    requiredFlowRate  DOUBLE PRECISION,
    pipePostDate      varchar(255),
    result            varchar(255),
    project_entity_id INT
);