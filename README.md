# FHIROntopOMOP
FHIR Ontop OMOP Implementation

== Installation Instruction ==

* download the Ontop Command Line Interface at https://github.com/ontop/ontop/releases/download/ontop-4.1.0/ontop-cli-4.1.0.zip
* unzip (into the folder of ontop-cli-4.1.0)
* copy the the folders of input and jdbc into the folder of ontop-cli-4.1.0
* run $./ontop endpoint --ontology=input/fhir.ttl --mapping=input/fhir.obda --properties=input/fhir.properties
* open browser http://localhost:8080/ see the Ontop SPARQL Endpoint
