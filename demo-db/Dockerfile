FROM postgres:14.2
RUN apt-get update && apt-get  install -y postgresql-14-postgis-3
COPY demo-dataset demo-dataset
COPY script.sql /docker-entrypoint-initdb.d/script.sql
