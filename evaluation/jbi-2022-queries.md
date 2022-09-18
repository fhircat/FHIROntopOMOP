# Evaluation Queries

## QUERY 1

- Query domain: Encounter and Demographics
- Query result: Male patients with inpatient admissions lasting >= 5 days
- Number of results: 4730 

### SQL

```sql
SELECT p.person_id, p.gender_concept_id, vo.visit_concept_id,
visit_start_datetime, visit_end_datetime
FROM omop.visit_occurrence vo JOIN omop.person p ON vo.person_id
= p.person_id
WHERE vo.visit_concept_id = 9201
and p.gender_concept_id = 8507
and DATE_PART('day', vo.visit_end_datetime - vo.visit_start_datetime) >= 5
```

### SPARQL

```sparql
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
PREFIX fhir: <http://hl7.org/fhir/>
PREFIX w5: <http://hl7.org/fhir/w5#>
PREFIX ofn: <http://www.ontotext.com/sparql/functions/>

SELECT  DISTINCT ?patient ?gender ?type ?start ?end ?diff
WHERE {	
  ?e a fhir:Encounter .  
  ?e fhir:Encounter.period  [ 
      	fhir:Period.start [ fhir:value ?start ]  ;
        fhir:Period.end   [ fhir:value ?end ] ] . 
  ?e fhir:Encounter.subject [ fhir:link ?patient ] .
  ?e fhir:Encounter.type [fhir:CodeableConcept.coding [fhir:Coding.code [fhir:value ?type] ] ].
  ?patient a fhir:Patient ;
	 fhir:Patient.gender    [   fhir:value ?gender      ] ;
	 fhir:Patient.birthDate [   fhir:value ?birthDate   ] .
  FILTER (?gender = 'male')
  FILTER (?type = 'IP')
  BIND (ofn:daysBetween(?start, ?end) as ?diff)
  FILTER (?diff >= "5"^^xsd:integer)
} 
```

## QUERY 2

- Query domain: Conditions
- Query result: Patients with Alzheimer’s disease
- Concept set: OMOP standard code for Alzheimer’s Disease (378419) including all descendants
- Number of results: 569

### SQL
```sql
SELECT DISTINCT co.person_id, co.condition_concept_id,
co.condition_start_date
FROM omop.condition_occurrence co
WHERE co.condition_concept_id IN (378419,762578,3170377,3176418,3177168,3179057,3184947,4019705,4043241,4043242,4043243,4043244,4043377,4043379,4097384,4167839,4182539,4204688,4218017,4220313,4277444,4277746,4278830,36716558,37117145,37395572,43021816,43530664,44782432,44782726,44782727,44782940,44782941,44784643)
```

### SPARQL

```sparql
PREFIX fhir: <http://hl7.org/fhir/> 
PREFIX owl: <http://www.w3.org/2002/07/owl#> 
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> 
PREFIX sct: <http://snomed.info/id/> 
PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> 
SELECT DISTINCT ?patient ?code ?cond_date
WHERE {
	?cond fhir:Condition.subject / fhir:link ?patient .
  	?cond fhir:Condition.code ?cond_code .
	?cond fhir:Condition.onsetDateTime / fhir:value ?cond_date .
  	?cond_code fhir:CodeableConcept.coding [  fhir:Coding.code    [ fhir:value ?code ] ;
                                           fhir:Coding.display [ fhir:value ?name ]  ;
                                           fhir:Coding.system  [ fhir:value ?system ]  ; ] .
	FILTER (?system = 'SNOMED') 
  FILTER(?code = '443491000000105' || ?code = '443781000000102' || ?code = '443541000000101' || ?code = '419261000000107' || ?code = '97751000119108' || ?code = '698955006' || ?code = '698954005' || ?code = '142811000119104' || ?code = '142001000119106' || ?code = '141991000119109' || ?code = '105421000119105' || ?code = '1581000119101' || ?code = '79341000119107' || ?code = '192803007' || ?code = '192802002' || ?code = '192162002' || ?code = '192161009' || ?code = '134423002' || ?code = '134422007' || ?code = '142011000119109' || ?code = '16219201000119101' || ?code = '722600006' || ?code = '66108005' || ?code = '65096006' || ?code = '6475002' || ?code = '416975007' || ?code = '416780008' || ?code = '55009008' || ?code = '54502004' || ?code = '4817008' || ?code = '26852004' || ?code = '230280008' || ?code = '230269008' || ?code = '230268000' || ?code = '230267005' || ?code = '230266001' || ?code = '230265002' || ?code = '10532003' || ?code = '429161000124103' || ?code = '26929004')
}
```

## QUERY 3

- Query domain: Procedures
- Query result: Patients who delivered a baby
- Concept set: OMOP standard code for Delivery procedure (4128030) including all descendants. Note that there were several terms in this set using the “OPCS4” vocabulary, which does not appear to be supported by FHIR. Thus, those codes do not appear in the SPARQL version of the query.
- Number of results: 34

### SQL

```sql
SELECT DISTINCT po.person_id, po.procedure_concept_id,
po.procedure_datetime
FROM omop.procedure_occurrence po
WHERE po.procedure_concept_id IN (1781151,1781152,2004702,2004707,2004710,2004724,2004726,2004730,2004731,2004744,2004746,2004747,2004748,2004751,2004765,2004767,2004768,2004771,2004783,2004788,2004789,2004802,2110237,2110303,2110307,2110308,2110309,2110311,2110315,2110316,2110317,2110318,2110319,2110320,2110321,2110322,2110323,2110324,2110331,2110332,2110334,2110335,2110341,2110342,2784564,2784565,2784566,2784567,2784568,2784569,2784570,2784571,2784572,2784573,2784574,2784575,2784576,2784578,3657302,3662128,3662129,4015701,4023797,4027926,4030877,4032761,4032762,4032764,4032766,4032767,4032768,4032769,4034145,4035778,4056129,4069968,4069969,4070222,4071505,4071507,4071629,4071630,4071632,4073422,4073424,4073438,4075160,4075161,4075165,4075168,4075169,4075170,4075171,4075182,4075187,4075188,4075189,4075190,4075191,4075735,4088084,4093774,4096145,4100410,4102595,4103850,4106406,4114636,4114637,4120986,4121758,4121923,4121924,4125788,4127248,4127249,4127250,4127251,4127252,4127253,4128030,4128031,4128032,4130319,4130320,4130321,4130323,4137400,4141265,4142513,4143647,4147974,4165077,4166247,4167018,4167089,4167497,4170152,4173513,4189205,4191809,4202559,4204679,4208862,4210037,4211824,4213387,4217642,4223536,4223638,4228344,4230533,4231702,4232028,4234421,4234710,4237340,4240325,4244672,4248119,4250441,4261498,4266683,4272812,4278225,4290098,4290245,4293034,4294827,4300392,4309795,4311671,4324549,4331179,37312440,40551541,42535816,42535817,42536952,42536954,42536960,42537021,42872492,42872493,42872805,43018344,43018345,44513729,44513733,44513736,44513740,44513743,44513745,44513746,44513747,44513752,44513756,44513761,44513763,44513765,44513771,44784096,44784097,44809684,46270991)
```

### SPARQL

```sparql
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
PREFIX fhir: <http://hl7.org/fhir/>
PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
PREFIX w5: <http://hl7.org/fhir/w5#>

SELECT DISTINCT ?patient ?proccode ?procdate
WHERE {
	?proc fhir:Procedure.subject / fhir:link ?patient .
  	?proc fhir:Procedure.code ?proccode .
	?proc fhir:Procedure.performedDateTime / fhir:value ?procdate .
    ?proccode fhir:CodeableConcept.coding [  fhir:Coding.code    [ fhir:value ?code ] ;
                                           fhir:Coding.display [ fhir:value ?name ]  ;
                                           fhir:Coding.system  [ fhir:value ?system ]  ; ] .
   FILTER(
     (?system='CPT4' && (?code='59899' || ?code='59898' || ?code='59856' || ?code='59855' || ?code='59851' || ?code='59850' || ?code='59622' || ?code='59620' || ?code='59618' || ?code='59614' || ?code='59612' || ?code='59610' || ?code='59525' || ?code='59515' || ?code='59514' || ?code='59510' || ?code='59414' || ?code='59410' || ?code='59409' || ?code='59400' || ?code='59300' || ?code='58611'))
     ||
     (?system='ICD10CM' && (?code='10D18Z9' || ?code='10D17Z9' || ?code='10E0XZZ' || ?code='10D28ZZ' || ?code='10D27ZZ' || ?code='10D18ZZ' || ?code='10D17ZZ' || ?code='10D07Z8' || ?code='10D07Z7' || ?code='10D07Z6' || ?code='10D07Z5' || ?code='10D07Z4' || ?code='10D07Z3' || ?code='10D00Z2' || ?code='10D00Z1' || ?code='10D00Z0' || ?code='10D24ZZ' || ?code='10D20ZZ'))     
    ||
     (?system='ICD9Proc' && (?code='73' || ?code='74.99' || ?code='74.4' || ?code='74.3' || ?code='73.99' || ?code='73.93' || ?code='73.9' || ?code='73.8' || ?code='73.59' || ?code='73.3' || ?code='73.2' || ?code='73.1' || ?code='73.09' || ?code='73.0' || ?code='72.8' || ?code='72.79' || ?code='72.54' || ?code='72.52' || ?code='72.39' || ?code='72.29' || ?code='72'))
     ||
     (?system='SNOMED' &&  (?code='709004006' || ?code='893721000000103' || ?code='700000006' || ?code='699999008' || ?code='450798003' || ?code='450484007' || ?code='450483001' || ?code='736118004' || ?code='736026009' || ?code='736020003' || ?code='736018001' || ?code='734276001' || ?code='734275002' || ?code='384730009' || ?code='788180009' || ?code='22633006' || ?code='71166009' || ?code='85548006' || ?code='85403009' || ?code='387711001' || ?code='75928003' || ?code='384729004' || ?code='69422002' || ?code='69162008' || ?code='65243006' || ?code='63407004' || ?code='62508004' || ?code='4504004' || ?code='40792007' || ?code='40704000' || ?code='38479009' || ?code='58705005' || ?code='408819007' || ?code='90438006' || ?code='359943008' || ?code='89346004' || ?code='89849000' || ?code='359940006' || ?code='89053004' || ?code='40219000' || ?code='84195007' || ?code='72492007' || ?code='417121007' || ?code='57271003' || ?code='5556001' || ?code='56620000' || ?code='54973000' || ?code='315308008' || ?code='61586001' || ?code='275168001' || ?code='275169009' || ?code='48204000' || ?code='274130007' || ?code='45718005' || ?code='416055001' || ?code='41059002' || ?code='30476003' || ?code='265639000' || ?code='265640003' || ?code='33807004' || ?code='306727001' || ?code='236994008' || ?code='236989008' || ?code='236987005' || ?code='236982004' || ?code='236991000' || ?code='236974004' || ?code='236973005' || ?code='236992007' || ?code='236985002' || ?code='236984003' || ?code='236981006' || ?code='236976002' || ?code='236975003' || ?code='288042004' || ?code='288194000' || ?code='288193006' || ?code='287977004' || ?code='302384005' || ?code='302383004' || ?code='302382009' || ?code='29613008' || ?code='28860009' || ?code='28542003' || ?code='25296001' || ?code='26313002' || ?code='25828002' || ?code='18625004' || ?code='17860005' || ?code='177174003' || ?code='177168003' || ?code='177164001' || ?code='177162002' || ?code='177161009' || ?code='177141003' || ?code='177175002' || ?code='177173009' || ?code='177167008' || ?code='177158008' || ?code='177152009' || ?code='177143000' || ?code='177142005' || ?code='177170007' || ?code='177176001' || ?code='177157003' || ?code='177185001' || ?code='177181005' || ?code='177179008' || ?code='177184002' || ?code='177180006' || ?code='177212000' || ?code='177204008' || ?code='177203002' || ?code='16819009' || ?code='15413009' || ?code='237311001' || ?code='236990004' || ?code='236988000' || ?code='236986001' || ?code='236983009' || ?code='236980007' || ?code='236978001' || ?code='236977006' || ?code='14119008' || ?code='10745001' || ?code='19390001' || ?code='11466000' || ?code='1147971000000109' || ?code='1147961000000102' || ?code='1147951000000100'))
   )
}
```

## QUERY 4
- 
- Query domain: Drugs
- Query result: Patients taking trazodone
- Concept set: Descendants of the OMOP concept for trazodone (RxNorm ingredient level) (OMOP concept 703547)
  (OMOP code: 40163473, RxNorm code: 856377)
- Number of results: 6737

### SQL

```sql
SELECT DISTINCT de.person_id, de.drug_concept_id, de.drug_exposure_start_datetime
FROM omop.drug_exposure de
WHERE de.drug_concept_id = 40163473 AND de.drug_exposure_start_datetime IS NOT NULL;
```
### SPARQL

```sparql
PREFIX owl: <http://www.w3.org/2002/07/owl#> 
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> 
PREFIX sct: <http://snomed.info/id/> 
PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> 
PREFIX fhir: <http://hl7.org/fhir/>

SELECT DISTINCT ?patient ?medcode ?meddate
WHERE {
  ?med fhir:MedicationStatement.subject / fhir:link ?patient .
  ?med fhir:MedicationStatement.medicationCodeableConcept ?med_concept. 
  ?med_concept fhir:CodeableConcept.coding [  fhir:Coding.code    / fhir:value ?med_code   ;
                                          fhir:Coding.display / fhir:value ?med_name   ;
                                          fhir:Coding.system  / fhir:value ?med_system ; ] .
  ?med fhir:MedicationStatement.effectiveDateTime / fhir:value ?med_date .
  FILTER (?med_system = 'RxNorm')
  FILTER (?med_code = '856377')
}
```

## QUERY 5

- Query domain: Measurement
- Query result: Patients with an HbA1c >= 10%
- Concept set: Three LOINC codes for A1c tests (OMOP concept IDs 3005673,3004410,3003309)
- Number of results: 944

### SQL

```sql
SELECT DISTINCT mea.person_id, mea.measurement_concept_id, mea.value_as_number, mea.measurement_date
FROM omop.measurement mea
WHERE mea.measurement_concept_id in (3005673,3004410,3003309) and mea.value_as_number >= 10
```

### SPARQL

```sparql
PREFIX owl: <http://www.w3.org/2002/07/owl#> 
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> 
PREFIX sct: <http://snomed.info/id/> 
PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> 
PREFIX fhir: <http://hl7.org/fhir/>

SELECT DISTINCT ?patient ?code ?result ?year ?month ?day
WHERE {
	?meas fhir:Observation.subject / fhir:link ?patient .
	?meas fhir:Observation.code / fhir:CodeableConcept.coding [ 
    fhir:Coding.code / fhir:value ?code ;
	  fhir:Coding.system / fhir:value ?system ].
  ?meas fhir:Observation.valueQuantity / fhir:Quantity.value / fhir:value ?result .
  ?meas fhir:Observation.effectivePeriod / fhir:Period.start / fhir:value ?testdatetime .
  BIND(YEAR(?testdatetime) AS ?year)
  BIND(MONTH(?testdatetime) AS ?month)
  BIND(DAY(?testdatetime) AS ?day)  
  FILTER(?system = 'LOINC') 
  FILTER(?code = '17856-6' || ?code = '4548-4' || ?code = '4549-2')
  FILTER(?result >= 10)
}
```




