# FHIROntopOMOP

This project exposes
the [Observational Medical Outcomes Partnership (OMOP)](https://www.ohdsi.org/data-standardization/the-common-data-model/)
data as a queryable Knowledge Graph compliant with the [HL7 FHIR standard](https://www.hl7.org/fhir/) using
the [Ontop](https://ontop-vkg.org/)
Virtual Knowledge Graph engine.

## Perquisite: 
a working connection to the OMOP PostgreSQL database. If the database and the Docker engine are running on the same machine, some additional configuration might be needed (see the end of this document)   

## Docker-based Installation Instruction

* install Docker (<https://www.docker.com/>)
* create a `.env` file from the example `.env.example` file 
```
cp .env.example .env
```
and modify the `.env` file to configure the connection to the database
* run `docker-compose up`
* open a browser at <http://localhost:8080/> to see the Ontop SPARQL endpoint as below:

![Endpoint](images/endpoint.png)

## Additional Database configuration

If the PostgreSQL DB and the Docker engine are on the same machine, we need to make sure the Docker container can access the DB. 

1. Edit the file `postgresql.conf` (located at e.g. `/etc/postgresql/11/main/postgresql.conf`). This configures which IP addresses the DB engine is listening on.

```config
#listen_addresses = 'localhost'         # what IP address(es) to listen on
listen_addresses = '*'          # PostgreSQL listen on all the IP address(es), including the address for Docker
```
2. Edit the file `pg_hba.conf` (located at e.g. `/etc/postgresql/11/main/pg_hba.conf`). This configures the addresses of the clients, which can access the DB. Add the following block to this file.

```
# Docker
host    all             all             172.17.0.0/16           md5
host    all             all             172.18.0.0/16           md5
host    all             all             172.19.0.0/16           md5
```

