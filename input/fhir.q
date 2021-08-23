[QueryItem="person"]
PREFIX owl: <http://www.w3.org/2002/07/owl#>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX xml: <http://www.w3.org/XML/1998/namespace>
PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
PREFIX fhir: <http://hl7.org/fhir/>
PREFIX obda: <https://w3id.org/obda/vocabulary#>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>

SELECT * {
  ?p a fhir:Patient ; 
    fhir:Resource.id [ fhir:value ?id ]  ;
    fhir:Patient.birthDate [ fhir:value ?birthDate ]  ;
    fhir:Patient.gender [ fhir:value ?gender ]
}
[QueryItem="condition"]
PREFIX owl: <http://www.w3.org/2002/07/owl#>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX xml: <http://www.w3.org/XML/1998/namespace>
PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
PREFIX fhir: <http://hl7.org/fhir/>
PREFIX obda: <https://w3id.org/obda/vocabulary#>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>

SELECT * {
  ?cond a fhir:Condition ; 
    fhir:Resource.id [ fhir:value ?id ]  ;
}
[QueryItem="condition-patient"]
PREFIX owl: <http://www.w3.org/2002/07/owl#>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
PREFIX fhir: <http://hl7.org/fhir/>

SELECT DISTINCT ?patient ?code ?system ?display WHERE {

    ?condition a fhir:Condition .
 	?condition	fhir:Condition.subject	?patient .
	?condition	fhir:Condition.code	[fhir:CodeableConcept.coding [ fhir:Coding.code	[ fhir:value	?code  ];  fhir:Coding.system	[ fhir:value	?system  ]; fhir:Coding.display	[ fhir:value	?display  ] ] ].
	FILTER (str(?code) = '129721000119106' || str(?code) = '140031000119103' || str(?code) = '145681000119101' || str(?code) = '14669001' || str(?code) = '200118004' || str(?code) = '236428007' || str(?code) = '236429004' || str(?code) = '236432001' || str(?code) = '307309005' || str(?code) = '36225005' || str(?code) = '423533009' || str(?code) = '429224003' || str(?code) = '429489008' || str(?code) = '430535006' || str(?code) = '722095005' || str(?code) = '722096006' || str(?code) = '722278006'  )
	FILTER (str(?system) = 'SNOMED' )

}
[QueryItem="describe a patient"]
PREFIX owl: <http://www.w3.org/2002/07/owl#>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
PREFIX fhir: <http://hl7.org/fhir/>

DESCRIBE ?p WHERE {
    ?p a   fhir:Encounter .
	?p  fhir:Resource.id  [ fhir:value "541" ]  .
}