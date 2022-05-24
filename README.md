# FHIROntopOMOP

This project exposes
the [Observational Medical Outcomes Partnership (OMOP)](https://www.ohdsi.org/data-standardization/the-common-data-model/)
data as a queryable Knowledge Graph compliant with the [HL7 FHIR standard](https://www.hl7.org/fhir/) using
the [Ontop](https://ontop-vkg.org/)
Virtual Knowledge Graph engine.

## Prerequisite: 
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

## Docker run with sample data
For this tutorial, we assume that the ports 7777 (used for database) and 8080 (used by Ontop) are free. If you need to use different ports, please edit the file ```.env.demo```.
Run the docker-compose commands in 2 separate terminals.
```
cp .env.demo .env
sudo docker-compose -f docker-compose.demo.yml up demo-db
sudo docker-compose -f docker-compose.demo.yml up ontop
```
Data from a sample physionet project https://physionet.org/content/mimic-iv-demo-omop/0.9/ is run locally for this demo.

References:

Kallfelz, M., Tsvetkova, A., Pollard, T., Kwong, M., Lipori, G., Huser, V., Osborn, J., Hao, S., & Williams, A. (2021). MIMIC-IV demo data in the OMOP Common Data Model (version 0.9). PhysioNet. https://doi.org/10.13026/p1f5-7x35.

Goldberger, A., Amaral, L., Glass, L., Hausdorff, J., Ivanov, P. C., Mark, R., ... & Stanley, H. E. (2000). PhysioBank, PhysioToolkit, and PhysioNet: Components of a new research resource for complex physiologic signals. Circulation [Online]. 101 (23), pp. e215–e220.