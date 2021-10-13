## Get min and max ids of 1000 patients

```sparql
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
PREFIX fhir: <http://hl7.org/fhir/>
PREFIX w5: <http://hl7.org/fhir/w5#>

SELECT (MIN(?id) AS ?min_id) (MAX(?id) AS ?max_id) WHERE {
?sub a fhir:Patient .
?sub fhir:Resource.id [ fhir:value ?id ]  .
?sub fhir:Patient.gender    [   fhir:value ?gender      ] .
?sub fhir:Patient.birthDate [   fhir:value ?birthDate   ] .
#  	FILTER (?gender = 'male')
}
LIMIT 100
```
ORDER BY is needed
NOT CORRECT!!

min: 392775850
max: 392822369

## Patents

```sparql
PREFIX owl: <http://www.w3.org/2002/07/owl#>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX xml: <http://www.w3.org/XML/1998/namespace>
PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
PREFIX fhir: <http://hl7.org/fhir/>
PREFIX : <http://hl7.org/fhir/>
PREFIX w5: <http://hl7.org/fhir/w5#>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>

CONSTRUCT {
?p a fhir:Patient  .
?p                             :Resource.id                 ?id . ?id :value  ?id_value.
?p                            :Patient.gender              ?gender . ?gender :value ?gender_value .
?p                            :Patient.birthDate           ?birthDate1 . ?birthDate1  :value ?birthDate1_value . 
}
WHERE {
?p a fhir:Patient  .
?p                            :Resource.id                 ?id . ?id :value  ?id_value.
?p                            :Patient.gender              ?gender . ?gender :value ?gender_value .
?p                            :Patient.birthDate           ?birthDate1 . ?birthDate1  :value ?birthDate1_value .
#?p                            :Patient.birthDate           ?birthDate2 . ?birthDate2  :value ?birthDate2_value .
OPITONAL {?p                            :Patient.address             ?address .} 
#?p                            :Patient.generalPractitioner ?generalPractitioner . 
##?p                            :Patient.generalPractitioner [ rr:template "http://hl7.org/fhir/PractitionerRole/{provider_id}" ] ;
FILTER (?id_value >= '392775850' && ?id_value <= '392822369')
}
```
## Encounter

```sparql
PREFIX owl: <http://www.w3.org/2002/07/owl#>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX xml: <http://www.w3.org/XML/1998/namespace>
PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
PREFIX fhir: <http://hl7.org/fhir/>
PREFIX : <http://hl7.org/fhir/>
PREFIX w5: <http://hl7.org/fhir/w5#>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>

CONSTRUCT {
?p a                  :Encounter . 
?p :Resource.id         ?id . ?id :value ?id_value .
?p :Encounter.status    ?status . ?status :value ?status_value .
?p :Encounter.type    ?type .
?p :Encounter.class   ?class .
?p :Encounter.subject ?subject . ?subject :link ?subjectLink .
?p :Encounter.period  ?period. ?period :Period.start  ?start . ?start :value ?start_value .
    ?period :Period.end ?end .?end  :value ?end_value .
?p :Encounter.performer ?performer .                                       
?p :Encounter.partOf ?partOf .
}
WHERE
{
?p a                  :Encounter . 
?p :Resource.id         ?id . ?id :value ?id_value .
?p :Encounter.status    ?status . ?status :value ?status_value .
?p :Encounter.type    ?type .
?p :Encounter.class   ?class .
?p :Encounter.subject ?subject . ?subject :link ?subjectLink .
?p :Encounter.period  ?period. ?period :Period.start  ?start . ?start :value ?start_value .
    ?period :Period.end ?end .?end  :value ?end_value .
  OPTIONAL {?p :Encounter.performer ?performer . }
?p :Encounter.partOf ?partOf .
#
?subjectLink  :Resource.id   [ :value ?subject_id_value  ]  .  
FILTER (?subject_id_value >= '392775850' && ?id_value <= '392822369')
# rr:template "http://hl7.org/fhir/Encounter/{preceding_visit_occurrence_id}" ] ; # to be implemented
}
```

## Condition

NB: This query returns 651000 results, and may kill the browser

```sparql
PREFIX owl: <http://www.w3.org/2002/07/owl#>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX xml: <http://www.w3.org/XML/1998/namespace>
PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
PREFIX fhir: <http://hl7.org/fhir/>
PREFIX : <http://hl7.org/fhir/>
PREFIX w5: <http://hl7.org/fhir/w5#>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>

CONSTRUCT {
  ?x a  :Condition .
?x :Resource.id   ?id . ?id :value ?id_value .
?x :Condition.code   ?code .
?x :Condition.category ?category  .
?x :Condition.subject ?link . ?link :link ?subject_link .
?x :Condition.encounter  ?encounter .
?x :Condition.onsetDateTime ?onsetDateTime . ?onsetDateTime :value ?onsetDateTime_value .
?x :Condition.abatementDateTime ?abatementDateTime. ?abatementDateTime :value ?abatementDateTime_value. 
?x :Condition.asserter ?asserter .}
  WHERE
#SELECT (COUNT(*) AS ?count) 
{
?x a  :Condition .
?x :Resource.id   ?id . ?id :value ?id_value .
?x :Condition.code   ?code .
?x :Condition.category ?category  .
?x :Condition.subject ?link . ?link :link ?subject_link .
?x :Condition.encounter  ?encounter .
?x :Condition.onsetDateTime ?onsetDateTime . ?onsetDateTime :value ?onsetDateTime_value .
?x :Condition.abatementDateTime ?abatementDateTime. ?abatementDateTime :value ?abatementDateTime_value. 
OPTIONAL {?x :Condition.asserter ?asserter .}
?subject_link  :Resource.id   [ :value ?subject_id_value  ]  .  
FILTER (?subject_id_value >= '392775850' && ?id_value <= '392822369')
#  FILTER (?subject_id_value = '392775850')
}
#LIMIT 100
```

## Procedure

573146

```sparql
PREFIX owl: <http://www.w3.org/2002/07/owl#>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX xml: <http://www.w3.org/XML/1998/namespace>
PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
PREFIX fhir: <http://hl7.org/fhir/>
PREFIX : <http://hl7.org/fhir/>
PREFIX w5: <http://hl7.org/fhir/w5#>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>

CONSTRUCT {
?x a                            :Procedure .
?x :Resource.id                 ?id . ?id :value ?id_value .
?x :Procedure.code              ?code .
?x :Procedure.category          ?category .
?x :Procedure.subject           ?subject . ?subject :link ?subject_link .
?x :Procedure.encounter         ?encounter .
?x :Procedure.performedDateTime ?performedDateTime . ?performedDateTime :value ?performedDateTime_value . 
}
# SELECT * {
WHERE {
?x a                            :Procedure .
?x :Resource.id                 ?id . ?id :value ?id_value .
?x :Procedure.code              ?code .
?x :Procedure.category          ?category .
?x :Procedure.subject           ?subject . ?subject :link ?subject_link .
?x :Procedure.encounter         ?encounter .
?x :Procedure.performedDateTime ?performedDateTime . ?performedDateTime :value ?performedDateTime_value . 
#?x :Procedure.performer ?performer .
?subject_link  :Resource.id   [ :value ?subject_id_value  ]  .  
FILTER (?subject_id_value >= '392775850' && ?id_value <= '392822369')
}
#LIMIT 100
```

## MedicationStatement

3250366

```sparql
PREFIX owl: <http://www.w3.org/2002/07/owl#>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX xml: <http://www.w3.org/XML/1998/namespace>
PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
PREFIX fhir: <http://hl7.org/fhir/>
PREFIX : <http://hl7.org/fhir/>
PREFIX w5: <http://hl7.org/fhir/w5#>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>

CONSTRUCT {
  ?x a                                              :MedicationStatement .
?x :Resource.id                                   ?id . ?id :value ?id_value .
?x :MedicationStatement.status                    ?status . ?status :value ?status_value . 
?x :MedicationStatement.statusReason              ?statusReason . ?statusReason :value ?statusReason_value .
?x :MedicationStatement.medicationCodeableConcept ?medicationCodeableConcept .
?x :MedicationStatement.category                  ?category .
?x :MedicationStatement.subject                   ?subject . ?subject :link ?subject_link .
?x :MedicationStatement.context                   ?context .
?x :MedicationStatement.effectivePeriod           ?period . ?period :Period.start ?start . ?start :value ?start_value .
                                                ?period :Period.end ?end . ?end :value ?end_value .
}
WHERE
# SELECT (COUNT(*) AS ?count) 
{
?x a                                              :MedicationStatement .
?x :Resource.id                                   ?id . ?id :value ?id_value .
?x :MedicationStatement.status                    ?status . ?status :value ?status_value . 
?x :MedicationStatement.statusReason              ?statusReason . ?statusReason :value ?statusReason_value .
?x :MedicationStatement.medicationCodeableConcept ?medicationCodeableConcept .
?x :MedicationStatement.category                  ?category .
?x :MedicationStatement.subject                   ?subject . ?subject :link ?subject_link .
?x :MedicationStatement.context                   ?context .
?x :MedicationStatement.effectivePeriod           ?period . ?period :Period.start ?start . ?start :value ?start_value .
                                                ?period :Period.end ?end . ?end :value ?end_value .
?subject_link  :Resource.id   [ :value ?subject_id_value  ]  .  
FILTER (?subject_id_value >= '392775850' && ?id_value <= '392822369')
}
#LIMIT 100
```

## Observation

```sparql
PREFIX owl: <http://www.w3.org/2002/07/owl#>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX xml: <http://www.w3.org/XML/1998/namespace>
PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
PREFIX fhir: <http://hl7.org/fhir/>
PREFIX : <http://hl7.org/fhir/>
PREFIX w5: <http://hl7.org/fhir/w5#>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>

SELECT 
{
?x a                                 :Observation .
?x :Resource.id                      ?id . ?id :value ?id_value .
?x :Observation.code                 ?code .
?x :Observation.category             ?category ;
?x :Observation.subject              ?subject;
?x :Observation.encounter            ?encounter ;
?x :Observation.effectiveDateTime    ?effectiveDateTime . ?effectiveDateTime :value ?effectiveDateTime_value .
?x :Observation.valueCodeableConcept [ :CodeableConcept.coding ?coding ] ;
?x :Observation.valueString          [ :value ?value_as_number ] ;
?x :Observation.referenceRange       [ :Observation.referenceRange.low  [ :value ?range_low ] ] ;
                                    :Observation.referenceRange.high [ :value ?range_high ] ] ] ;
?x :Observation.performer            [ :link ?performer ]
} 
```