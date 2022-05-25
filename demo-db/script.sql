DROP TABLE IF EXISTS concept;
CREATE TABLE concept (
                         concept_id INT,
                         concept_name TEXT,
                         domain_id TEXT,
                         vocabulary_id TEXT,
                         concept_class_id TEXT,
                         standard_concept TEXT,
                         concept_code TEXT,
                         valid_start_DATE DATE,
                         valid_end_DATE DATE,
                         invalid_reason TEXT,
                         PRIMARY KEY (concept_id)
);

COPY concept(concept_id, concept_name, domain_id, vocabulary_id, concept_class_id, standard_concept, concept_code, valid_start_DATE, valid_end_DATE, invalid_reason)
    FROM '/demo-dataset/physionet.org/files/mimic-iv-demo-omop/0.9/1_omop_data_csv/2b_concept.csv'
    DELIMITER ','
    CSV HEADER;

DROP TABLE IF EXISTS concept_relationship;
CREATE TABLE concept_relationship (
                                      concept_id_1 INT,
                                      concept_id_2 INT,
                                      relationship_id TEXT,
                                      valid_start_DATE DATE,
                                      valid_end_DATE DATE,
                                      invalid_reason TEXT
);

COPY concept_relationship(concept_id_1, concept_id_2, relationship_id, valid_start_DATE, valid_end_DATE, invalid_reason)
    FROM '/demo-dataset/physionet.org/files/mimic-iv-demo-omop/0.9/1_omop_data_csv/2b_concept_relationship.csv'
    DELIMITER ','
    CSV HEADER;

DROP TABLE IF EXISTS vocabulary;
CREATE TABLE vocabulary (
                            vocabulary_id TEXT,
                            vocabulary_name TEXT,
                            vocabulary_reference TEXT,
                            vocabulary_version TEXT,
                            vocabulary_concept_id INT,
                            PRIMARY KEY (vocabulary_id)
);

COPY vocabulary(vocabulary_id, vocabulary_name, vocabulary_reference, vocabulary_version, vocabulary_concept_id)
    FROM '/demo-dataset/physionet.org/files/mimic-iv-demo-omop/0.9/1_omop_data_csv/2b_vocabulary.csv'
    DELIMITER ','
    CSV HEADER;

-- NOTE: Empty demo dataset
DROP TABLE IF EXISTS attribute_definition;
CREATE TABLE attribute_definition (
                                      attribute_definition_id INT,
                                      attribute_name TEXT,
                                      attribute_description TEXT,
                                      attribute_type_concept_id INT,
                                      attribute_syntax TEXT,
                                      PRIMARY KEY (attribute_definition_id)
);

COPY attribute_definition(attribute_definition_id, attribute_name, attribute_description, attribute_type_concept_id, attribute_syntax)
    FROM '/demo-dataset/physionet.org/files/mimic-iv-demo-omop/0.9/1_omop_data_csv/attribute_definition.csv'
    DELIMITER ','
    CSV HEADER;

DROP TABLE IF EXISTS care_site;
CREATE TABLE care_site (
                           care_site_id BIGINT,
                           care_site_name TEXT,
                           place_of_service_concept_id INT,
                           location_id INT,
                           care_site_source_value TEXT,
                           place_of_service_source_value TEXT,
                           PRIMARY KEY (care_site_id)
);

COPY care_site(care_site_id, care_site_name, place_of_service_concept_id, location_id, care_site_source_value, place_of_service_source_value)
    FROM '/demo-dataset/physionet.org/files/mimic-iv-demo-omop/0.9/1_omop_data_csv/care_site.csv'
    DELIMITER ','
    CSV HEADER;

DROP TABLE IF EXISTS cdm_source;
CREATE TABLE cdm_source (
                            cdm_source_name TEXT,
                            cdm_source_abbreviation TEXT,
                            cdm_holder TEXT,
                            source_description TEXT,
                            source_documentation_reference TEXT,
                            cdm_etl_reference TEXT,
                            source_release_date DATE,
                            cdm_release_date DATE,
                            cdm_version TEXT,
                            vocabulary_version TEXT,
                            PRIMARY KEY (cdm_source_name)
);

COPY cdm_source(cdm_source_name, cdm_source_abbreviation, cdm_holder, source_description, source_documentation_reference, cdm_etl_reference, source_release_date, cdm_release_date, cdm_version, vocabulary_version)
    FROM '/demo-dataset/physionet.org/files/mimic-iv-demo-omop/0.9/1_omop_data_csv/cdm_source.csv'
    DELIMITER ','
    CSV HEADER;

DROP TABLE IF EXISTS cohort;
CREATE TABLE cohort (
                        cohort_definition_id INT,
                        subject_id TEXT,
                        cohort_start_date DATE,
                        cohort_end_date DATE
);

COPY cohort(cohort_definition_id, subject_id, cohort_start_date, cohort_end_date)
    FROM '/demo-dataset/physionet.org/files/mimic-iv-demo-omop/0.9/1_omop_data_csv/cohort.csv'
    DELIMITER ','
    CSV HEADER;

DROP TABLE IF EXISTS cohort_attribute;
CREATE TABLE cohort_attribute (
                                  cohort_definition_id INT,
                                  subject_id TEXT,
                                  cohort_start_date DATE,
                                  cohort_end_date DATE,
                                  attribute_definition_id INT,
                                  value_as_number BIGINT,
                                  value_as_concept_id TEXT
);

COPY cohort_attribute(cohort_definition_id, subject_id, cohort_start_date, cohort_end_date, attribute_definition_id, value_as_number, value_as_concept_id)
    FROM '/demo-dataset/physionet.org/files/mimic-iv-demo-omop/0.9/1_omop_data_csv/cohort_attribute.csv'
    DELIMITER ','
    CSV HEADER;

DROP TABLE IF EXISTS cohort_definition;
CREATE TABLE cohort_definition (
                                   cohort_definition_id INT,
                                   cohort_definition_name TEXT,
                                   cohort_definition_description TEXT,
                                   definition_type_concept_id TEXT,
                                   cohort_definition_syntax TEXT,
                                   subject_concept_id INT,
                                   cohort_initiation_date DATE
);

COPY cohort_definition(cohort_definition_id, cohort_definition_name, cohort_definition_description, definition_type_concept_id, cohort_definition_syntax, subject_concept_id, cohort_initiation_date)
    FROM '/demo-dataset/physionet.org/files/mimic-iv-demo-omop/0.9/1_omop_data_csv/cohort_definition.csv'
    DELIMITER ','
    CSV HEADER;

DROP TABLE IF EXISTS condition_era;
CREATE TABLE condition_era (
                               condition_era_id BIGINT,
                               person_id BIGINT,
                               condition_concept_id INT,
                               condition_era_start_date DATE,
                               condition_era_end_date DATE,
                               condition_occurrence_count INT,
                               PRIMARY KEY (condition_era_id)
);

COPY condition_era(condition_era_id, person_id, condition_concept_id, condition_era_start_date, condition_era_end_date, condition_occurrence_count)
    FROM '/demo-dataset/physionet.org/files/mimic-iv-demo-omop/0.9/1_omop_data_csv/condition_era.csv'
    DELIMITER ','
    CSV HEADER;

DROP TABLE IF EXISTS condition_occurrence;
CREATE TABLE condition_occurrence (
                                      condition_occurrence_id BIGINT,
                                      person_id BIGINT,
                                      condition_concept_id INT,
                                      condition_start_date DATE,
                                      condition_start_datetime TIMESTAMP,
                                      condition_end_date DATE,
                                      condition_end_datetime TIMESTAMP,
                                      condition_type_concept_id INT,
                                      stop_reason TEXT,
                                      provider_id TEXT,
                                      visit_occurrence_id BIGINT,
                                      visit_detail_id TEXT,
                                      condition_source_value TEXT,
                                      condition_source_concept_id INT,
                                      condition_status_source_value TEXT,
                                      condition_status_concept_id TEXT,
                                      PRIMARY KEY (condition_occurrence_id)
);

COPY condition_occurrence(condition_occurrence_id, person_id, condition_concept_id, condition_start_date,
    condition_start_datetime, condition_end_date, condition_end_datetime,
    condition_type_concept_id, stop_reason, provider_id, visit_occurrence_id,
    visit_detail_id, condition_source_value, condition_source_concept_id,
    condition_status_source_value, condition_status_concept_id)
    FROM '/demo-dataset/physionet.org/files/mimic-iv-demo-omop/0.9/1_omop_data_csv/condition_occurrence.csv'
    DELIMITER ','
    CSV HEADER;

DROP TABLE IF EXISTS cost;
CREATE TABLE cost (
                      cost_id INT,
                      cost_event_id TEXT,
                      cost_domain_id TEXT,
                      cost_type_concept_id TEXT,
                      currency_concept_id TEXT,
                      total_charge DOUBLE PRECISION,
                      total_cost DOUBLE PRECISION,
                      total_paid DOUBLE PRECISION,
                      paid_by_payer TEXT,
                      paid_by_patient TEXT,
                      paid_patient_copay TEXT,
                      paid_patient_coinsurance TEXT,
                      paid_patient_deductible TEXT,
                      paid_by_primary TEXT,
                      paid_ingredient_cost DOUBLE PRECISION,
                      paid_dispensing_fee DOUBLE PRECISION,
                      payer_plan_period_id TEXT,
                      amount_allowed DOUBLE PRECISION,
                      revenue_code_concept_id TEXT,
                      revenue_code_source_value TEXT,
                      drg_concept_id TEXT,
                      drg_source_value TEXT,
                      PRIMARY KEY (cost_id)
);

COPY cost(cost_id, cost_event_id, cost_domain_id, cost_type_concept_id, currency_concept_id, total_charge, total_cost,
    total_paid, paid_by_payer, paid_by_patient, paid_patient_copay, paid_patient_coinsurance,
    paid_patient_deductible, paid_by_primary, paid_ingredient_cost, paid_dispensing_fee, payer_plan_period_id,
    amount_allowed, revenue_code_concept_id, revenue_code_source_value, drg_concept_id, drg_source_value)
    FROM '/demo-dataset/physionet.org/files/mimic-iv-demo-omop/0.9/1_omop_data_csv/cost.csv'
    DELIMITER ','
    CSV HEADER;

DROP TABLE IF EXISTS death;
CREATE TABLE death (
                       person_id BIGINT,
                       death_date DATE,
                       death_datetime TIMESTAMP,
                       death_type_concept_id INT,
                       case_concept_id INT,
                       case_source_value BIGINT,
                       case_source_concept_id INT,
                       PRIMARY KEY (person_id)
);

COPY death(person_id, death_date, death_datetime, death_type_concept_id, case_concept_id, case_source_value,
    case_source_concept_id)
    FROM '/demo-dataset/physionet.org/files/mimic-iv-demo-omop/0.9/1_omop_data_csv/death.csv'
    DELIMITER ','
    CSV HEADER;

DROP TABLE IF EXISTS device_exposure;
CREATE TABLE device_exposure (
                                 device_exposure_id BIGINT,
                                 person_id BIGINT,
                                 device_concept_id INT,
                                 device_exposure_start_date DATE,
                                 device_exposure_start_datetime TIMESTAMP,
                                 device_exposure_end_date DATE,
                                 device_exposure_end_datetime TIMESTAMP,
                                 device_type_concept_id INT,
                                 unique_device_id INT,
                                 quantity INT,
                                 provider_id INT,
                                 visit_occurrence_id BIGINT,
                                 visit_detail_id INT,
                                 device_source_value BIGINT,
                                 device_source_concept_id INT,
                                 PRIMARY KEY (device_exposure_id)
);

COPY device_exposure(device_exposure_id, person_id, device_concept_id, device_exposure_start_date,
    device_exposure_start_datetime, device_exposure_end_date, device_exposure_end_datetime,
    device_type_concept_id, unique_device_id, quantity, provider_id, visit_occurrence_id,
    visit_detail_id, device_source_value, device_source_concept_id)
    FROM '/demo-dataset/physionet.org/files/mimic-iv-demo-omop/0.9/1_omop_data_csv/device_exposure.csv'
    DELIMITER ','
    CSV HEADER;

DROP TABLE IF EXISTS dose_era;
CREATE TABLE dose_era (
                          dose_era_id BIGINT,
                          person_id BIGINT,
                          drug_concept_id INT,
                          unit_concept_id INT,
                          dose_value DOUBLE PRECISION,
                          dose_era_start_date DATE,
                          dose_era_end_date DATE,
                          PRIMARY KEY (dose_era_id)
);

COPY dose_era(dose_era_id, person_id, drug_concept_id, unit_concept_id, dose_value, dose_era_start_date,
    dose_era_end_date)
    FROM '/demo-dataset/physionet.org/files/mimic-iv-demo-omop/0.9/1_omop_data_csv/dose_era.csv'
    DELIMITER ','
    CSV HEADER;

DROP TABLE IF EXISTS drug_era;
CREATE TABLE drug_era (
                          drug_era_id BIGINT,
                          person_id BIGINT,
                          drug_concept_id INT,
                          drug_era_start_date DATE,
                          drug_era_end_date DATE,
                          drug_exposure_count INT,
                          gap_days INT,
                          PRIMARY KEY (drug_era_id)
);

COPY drug_era(drug_era_id, person_id, drug_concept_id, drug_era_start_date, drug_era_end_date, drug_exposure_count,
    gap_days)
    FROM '/demo-dataset/physionet.org/files/mimic-iv-demo-omop/0.9/1_omop_data_csv/drug_era.csv'
    DELIMITER ','
    CSV HEADER;

DROP TABLE IF EXISTS drug_exposure;
CREATE TABLE drug_exposure (
                               drug_exposure_id BIGINT,
                               person_id BIGINT,
                               drug_concept_id INT,
                               drug_exposure_start_date DATE,
                               drug_exposure_start_datetime TIMESTAMP,
                               drug_exposure_end_date DATE,
                               drug_exposure_end_datetime TIMESTAMP,
                               verbatim_end_date DATE,
                               drug_type_concept_id INT,
                               stop_reason TEXT,
                               refills INT,
                               quantity DOUBLE PRECISION,
                               days_supply INT,
                               sig INT,
                               route_concept_id INT,
                               lot_number INT,
                               provider_id INT,
                               visit_occurrence_id BIGINT,
                               visit_detail_id INT,
                               drug_source_value TEXT,
                               drug_source_concept_id INT,
                               route_source_value TEXT,
                               dose_unit_source_value TEXT,
                               PRIMARY KEY (drug_exposure_id)
);

COPY drug_exposure(drug_exposure_id, person_id, drug_concept_id, drug_exposure_start_date, drug_exposure_start_datetime,
    drug_exposure_end_date, drug_exposure_end_datetime, verbatim_end_date, drug_type_concept_id,
    stop_reason, refills, quantity, days_supply, sig, route_concept_id, lot_number, provider_id,
    visit_occurrence_id ,visit_detail_id, drug_source_value, drug_source_concept_id, route_source_value,
    dose_unit_source_value)
    FROM '/demo-dataset/physionet.org/files/mimic-iv-demo-omop/0.9/1_omop_data_csv/drug_exposure.csv'
    DELIMITER ','
    CSV HEADER;

DROP TABLE IF EXISTS fact_relationship;
CREATE TABLE fact_relationship (
                                   domain_concept_id_1 INT,
                                   fact_id_1 BIGINT,
                                   domain_concept_id_2 INT,
                                   fact_id_2 BIGINT,
                                   relationship_concept_id INT
);

COPY fact_relationship(domain_concept_id_1, fact_id_1, domain_concept_id_2, fact_id_2, relationship_concept_id)
    FROM '/demo-dataset/physionet.org/files/mimic-iv-demo-omop/0.9/1_omop_data_csv/fact_relationship.csv'
    DELIMITER ','
    CSV HEADER;

DROP TABLE IF EXISTS location;
CREATE TABLE location (
                          location_id INT,
                          address_1 TEXT,
                          address_2 TEXT,
                          city TEXT,
                          state TEXT,
                          zip TEXT,
                          county TEXT,
                          location_source_value TEXT,
                          PRIMARY KEY (location_id)
);

COPY location(location_id, address_1, address_2, city, state, zip, county, location_source_value)
    FROM '/demo-dataset/physionet.org/files/mimic-iv-demo-omop/0.9/1_omop_data_csv/location.csv'
    DELIMITER ','
    CSV HEADER;

DROP TABLE IF EXISTS measurement;
CREATE TABLE measurement (
                             measurement_id BIGINT,
                             person_id BIGINT,
                             measurement_concept_id INT,
                             measurement_date DATE,
                             measurement_datetime TIMESTAMP,
                             measurement_time INT,
                             measurement_type_concept_id INT,
                             operator_concept_id TEXT,
                             value_as_number DOUBLE PRECISION,
                             value_as_concept_id INT,
                             unit_concept_id INT,
                             range_low DOUBLE PRECISION,
                             range_high DOUBLE PRECISION,
                             provider_id TEXT,
                             visit_occurrence_id BIGINT,
                             visit_detail_id BIGINT,
                             measurement_source_value TEXT,
                             measurement_source_concept_id INT,
                             unit_source_value TEXT,
                             value_source_value TEXT,
                             PRIMARY KEY (measurement_id)
);

COPY measurement(measurement_id, person_id, measurement_concept_id, measurement_date, measurement_datetime,
    measurement_time, measurement_type_concept_id, operator_concept_id, value_as_number,
    value_as_concept_id, unit_concept_id, range_low, range_high, provider_id, visit_occurrence_id,
    visit_detail_id, measurement_source_value, measurement_source_concept_id, unit_source_value,
    value_source_value)
    FROM '/demo-dataset/physionet.org/files/mimic-iv-demo-omop/0.9/1_omop_data_csv/measurement.csv'
    DELIMITER ','
    CSV HEADER;

DROP TABLE IF EXISTS metadata;
CREATE TABLE metadata (
                          metadata_concept_id BIGINT,
                          metadata_type_concept_id BIGINT,
                          name TEXT,
                          value_as_string TEXT,
                          value_as_concept_id TEXT,
                          metadata_date DATE,
                          metadata_datetime TIMESTAMP,
                          PRIMARY KEY (metadata_concept_id)
);

COPY metadata(metadata_concept_id, metadata_type_concept_id, name, value_as_string, value_as_concept_id,
    metadata_date, metadata_datetime)
    FROM '/demo-dataset/physionet.org/files/mimic-iv-demo-omop/0.9/1_omop_data_csv/metadata.csv'
    DELIMITER ','
    CSV HEADER;

DROP TABLE IF EXISTS note;
CREATE TABLE note (
                      note_id BIGINT,
                      person_id BIGINT,
                      note_date DATE,
                      note_datetime TIMESTAMP,
                      note_type_concept_id TEXT,
                      note_title TEXT,
                      note_text TEXT,
                      encoding_concept_id TEXT,
                      language_concept_id TEXT,
                      provider_id INT,
                      visit_occurrence_id BIGINT,
                      visit_detail_id BIGINT,
                      note_source_value TEXT,
                      PRIMARY KEY (note_id)
);

COPY note(note_id, person_id, note_date, note_datetime, note_type_concept_id, note_title, note_text, encoding_concept_id,
    language_concept_id, provider_id, visit_occurrence_id, visit_detail_id, note_source_value)
    FROM '/demo-dataset/physionet.org/files/mimic-iv-demo-omop/0.9/1_omop_data_csv/note.csv'
    DELIMITER ','
    CSV HEADER;

DROP TABLE IF EXISTS note_nlp;
CREATE TABLE note_nlp (
                          note_nlp_id BIGINT,
                          note_id BIGINT,
                          section_concept_id INT,
                          snippet TEXT,
                          "offset" TEXT,
                          lexical_variant TEXT,
                          note_nlp_concept_id INT,
                          nlp_system TEXT,
                          nlp_date DATE,
                          nlp_datetime TIMESTAMP,
                          term_exists TEXT,
                          term_temporal TEXT,
                          term_modifiers TEXT,
                          PRIMARY KEY (note_nlp_id)
);

COPY note_nlp(note_nlp_id , note_id, section_concept_id, snippet, "offset", lexical_variant, note_nlp_concept_id, nlp_system,
    nlp_date, nlp_datetime, term_exists, term_temporal, term_modifiers)
    FROM '/demo-dataset/physionet.org/files/mimic-iv-demo-omop/0.9/1_omop_data_csv/note_nlp.csv'
    DELIMITER ','
    CSV HEADER;

DROP TABLE IF EXISTS observation;
CREATE TABLE observation (
                             observation_id BIGINT,
                             person_id BIGINT,
                             observation_concept_id INT,
                             observation_date DATE,
                             observation_datetime TIMESTAMP,
                             observation_type_concept_id INT,
                             value_as_number INT,
                             value_as_string TEXT,
                             value_as_concept_id INT,
                             qualifier_concept_id INT,
                             unit_concept_id INT,
                             provider_id INT,
                             visit_occurrence_id BIGINT,
                             visit_detail_id INT,
                             observation_source_value TEXT,
                             observation_source_concept_id INT,
                             unit_source_value TEXT,
                             qualifier_source_value TEXT,
                             PRIMARY KEY (observation_id)
);

COPY observation(observation_id ,person_id, observation_concept_id, observation_date, observation_datetime,
    observation_type_concept_id, value_as_number, value_as_string, value_as_concept_id,
    qualifier_concept_id, unit_concept_id, provider_id, visit_occurrence_id, visit_detail_id,
    observation_source_value, observation_source_concept_id, unit_source_value, qualifier_source_value)
    FROM '/demo-dataset/physionet.org/files/mimic-iv-demo-omop/0.9/1_omop_data_csv/observation.csv'
    DELIMITER ','
    CSV HEADER;


DROP TABLE IF EXISTS observation_period;
CREATE TABLE observation_period (
                                    observation_period_id BIGINT,
                                    person_id BIGINT,
                                    observation_period_start_date DATE,
                                    observation_period_end_date DATE,
                                    period_type_concept_id INT,
                                    PRIMARY KEY (observation_period_id)
);

COPY observation_period(observation_period_id ,person_id, observation_period_start_date, observation_period_end_date,
    period_type_concept_id)
    FROM '/demo-dataset/physionet.org/files/mimic-iv-demo-omop/0.9/1_omop_data_csv/observation_period.csv'
    DELIMITER ','
    CSV HEADER;

DROP TABLE IF EXISTS payer_plan_period;
CREATE TABLE payer_plan_period (
                                   payer_plan_period_id BIGINT,
                                   person_id BIGINT,
                                   payer_plan_period_start_date DATE,
                                   payer_plan_period_end_date DATE,
                                   payer_concept_id INT,
                                   payer_source_value TEXT,
                                   payer_source_concept_id INT,
                                   plan_concept_id INT,
                                   plan_source_value TEXT,
                                   plan_source_concept_id INT,
                                   sponsor_concept_id INT,
                                   sponsor_source_value TEXT,
                                   sponsor_source_concept_id INT,
                                   family_source_value TEXT,
                                   stop_reason_concept_id INT,
                                   stop_reason_source_value TEXT,
                                   stop_reason_source_concept_id INT,
                                   PRIMARY KEY (payer_plan_period_id)
);

COPY payer_plan_period(payer_plan_period_id ,person_id, payer_plan_period_start_date, payer_plan_period_end_date,
    payer_concept_id, payer_source_value, payer_source_concept_id, plan_concept_id,
    plan_source_value, plan_source_concept_id, sponsor_concept_id, sponsor_source_value,
    sponsor_source_concept_id, family_source_value, stop_reason_concept_id, stop_reason_source_value,
    stop_reason_source_concept_id)
    FROM '/demo-dataset/physionet.org/files/mimic-iv-demo-omop/0.9/1_omop_data_csv/payer_plan_period.csv'
    DELIMITER ','
    CSV HEADER;

DROP TABLE IF EXISTS person;
CREATE TABLE person (
                        person_id BIGINT,
                        gender_concept_id INT,
                        year_of_birth INT,
                        month_of_birth INT,
                        day_of_birth INT,
                        birth_datetime TIMESTAMP,
                        race_concept_id INT,
                        ethnicity_concept_id INT,
                        location_id INT,
                        provider_id INT,
                        care_site_id INT,
                        person_source_value TEXT,
                        gender_source_value TEXT,
                        gender_source_concept_id INT,
                        race_source_value TEXT,
                        race_source_concept_id INT,
                        ethnicity_source_value TEXT,
                        ethnicity_source_concept_id INT,
                        PRIMARY KEY (person_id)
);

COPY person(person_id, gender_concept_id, year_of_birth, month_of_birth, day_of_birth, birth_datetime, race_concept_id,
    ethnicity_concept_id, location_id, provider_id, care_site_id, person_source_value, gender_source_value,
    gender_source_concept_id, race_source_value, race_source_concept_id, ethnicity_source_value,
    ethnicity_source_concept_id)
    FROM '/demo-dataset/physionet.org/files/mimic-iv-demo-omop/0.9/1_omop_data_csv/person.csv'
    DELIMITER ','
    CSV HEADER;

DROP TABLE IF EXISTS procedure_occurrence;
CREATE TABLE procedure_occurrence (
                                      procedure_occurrence_id BIGINT,
                                      person_id BIGINT,
                                      procedure_concept_id INT,
                                      procedure_date DATE,
                                      procedure_datetime TIMESTAMP,
                                      procedure_type_concept_id INT,
                                      modifier_concept_id INT,
                                      quantity INT,
                                      provider_id INT,
                                      visit_occurrence_id BIGINT,
                                      visit_detail_id INT,
                                      procedure_source_value TEXT,
                                      procedure_source_concept_id INT,
                                      modifier_source_value TEXT,
                                      PRIMARY KEY (procedure_occurrence_id)
);

COPY procedure_occurrence(procedure_occurrence_id ,person_id, procedure_concept_id, procedure_date, procedure_datetime,
    procedure_type_concept_id, modifier_concept_id, quantity, provider_id, visit_occurrence_id,
    visit_detail_id, procedure_source_value, procedure_source_concept_id, modifier_source_value)
    FROM '/demo-dataset/physionet.org/files/mimic-iv-demo-omop/0.9/1_omop_data_csv/procedure_occurrence.csv'
    DELIMITER ','
    CSV HEADER;


DROP TABLE IF EXISTS provider;
CREATE TABLE provider (
                          provider_id INT,
                          provider_name TEXT,
                          npi TEXT,
                          dea TEXT,
                          specialty_concept_id INT,
                          care_site_id INT,
                          modifier_concept_id INT,
                          year_of_birth INT,
                          gender_concept_id INT,
                          provider_source_value TEXT,
                          speciality_source_value TEXT,
                          speciality_source_concept_id INT,
                          gender_source_value TEXT,
                          gender_source_concept_id INT,
                          PRIMARY KEY (provider_id)
);

COPY provider(provider_id, provider_name, npi, dea, specialty_concept_id, care_site_id, modifier_concept_id,
    year_of_birth, gender_concept_id, provider_source_value, speciality_source_value,
    speciality_source_concept_id, gender_source_value, gender_source_concept_id)
    FROM '/demo-dataset/physionet.org/files/mimic-iv-demo-omop/0.9/1_omop_data_csv/provider.csv'
    DELIMITER ','
    CSV HEADER;

DROP TABLE IF EXISTS specimen;
CREATE TABLE specimen (
                          specimen_id BIGINT,
                          person_id BIGINT,
                          specimen_concept_id INT,
                          specimen_type_concept_id INT,
                          specimen_date DATE,
                          specimen_datetime TIMESTAMP,
                          quantity INT,
                          unit_concept_id INT,
                          anatomic_site_concept_id INT,
                          disease_status_concept_id INT,
                          specimen_source_id TEXT,
                          specimen_source_value TEXT,
                          unit_source_value TEXT,
                          anatomic_site_source_value TEXT,
                          disease_status_source_value TEXT,
                          PRIMARY KEY (specimen_id)
);

COPY specimen(specimen_id, person_id, specimen_concept_id, specimen_type_concept_id, specimen_date, specimen_datetime,
    quantity, unit_concept_id, anatomic_site_concept_id, disease_status_concept_id, specimen_source_id,
    specimen_source_value, unit_source_value, anatomic_site_source_value, disease_status_source_value)
    FROM '/demo-dataset/physionet.org/files/mimic-iv-demo-omop/0.9/1_omop_data_csv/specimen.csv'
    DELIMITER ','
    CSV HEADER;

DROP TABLE IF EXISTS visit_detail;
CREATE TABLE visit_detail (
                              visit_detail_id BIGINT,
                              person_id BIGINT,
                              visit_detail_concept_id INT,
                              visit_detail_start_date DATE,
                              visit_detail_start_datetime TIMESTAMP,
                              visit_detail_end_date DATE,
                              visit_detail_end_datetime TIMESTAMP,
                              visit_detail_type_concept_id INT,
                              provider_id INT,
                              care_site_id BIGINT,
                              admitting_source_concept_id INT,
                              discharge_to_concept_id INT,
                              preceding_visit_detail_id BIGINT,
                              visit_detail_source_value TEXT,
                              visit_detail_source_concept_id INT,
                              admitting_source_value TEXT,
                              discharge_to_source_value TEXT,
                              visit_detail_parent_id INT,
                              visit_occurrence_id BIGINT,
                              PRIMARY KEY (visit_detail_id)
);

COPY visit_detail(visit_detail_id, person_id, visit_detail_concept_id, visit_detail_start_date,
    visit_detail_start_datetime, visit_detail_end_date, visit_detail_end_datetime,
    visit_detail_type_concept_id, provider_id, care_site_id, admitting_source_concept_id,
    discharge_to_concept_id, preceding_visit_detail_id, visit_detail_source_value,
    visit_detail_source_concept_id, admitting_source_value, discharge_to_source_value,
    visit_detail_parent_id, visit_occurrence_id)
    FROM '/demo-dataset/physionet.org/files/mimic-iv-demo-omop/0.9/1_omop_data_csv/visit_detail.csv'
    DELIMITER ','
    CSV HEADER;


DROP TABLE IF EXISTS visit_occurrence;
CREATE TABLE visit_occurrence (
                                  visit_occurrence_id BIGINT,
                                  person_id BIGINT,
                                  visit_concept_id INT,
                                  visit_start_date DATE,
                                  visit_start_datetime TIMESTAMP,
                                  visit_end_date DATE,
                                  visit_end_datetime TIMESTAMP,
                                  visit_type_concept_id INT,
                                  provider_id INT,
                                  care_site_id BIGINT,
                                  visit_source_value TEXT,
                                  visit_source_concept_id INT,
                                  admitting_source_concept_id INT,
                                  admitting_source_value TEXT,
                                  discharge_to_concept_id INT,
                                  discharge_to_source_value TEXT,
                                  preceding_visit_occurrence_id BIGINT,
                                  PRIMARY KEY (visit_occurrence_id)
);

COPY visit_occurrence(visit_occurrence_id, person_id, visit_concept_id, visit_start_date, visit_start_datetime,
    visit_end_date, visit_end_datetime, visit_type_concept_id, provider_id, care_site_id,
    visit_source_value, visit_source_concept_id, admitting_source_concept_id, admitting_source_value,
    discharge_to_concept_id, discharge_to_source_value, preceding_visit_occurrence_id)
    FROM '/demo-dataset/physionet.org/files/mimic-iv-demo-omop/0.9/1_omop_data_csv/visit_occurrence.csv'
    DELIMITER ','
    CSV HEADER;

-- No demo data. Empty table.
DROP TABLE IF EXISTS concept_ancestor;
CREATE TABLE concept_ancestor (
                                  ancestor_concept_id BIGINT,
                                  descendant_concept_id BIGINT
);

-- Add concepts for Geneder
INSERT INTO
    concept ("concept_id","concept_name","domain_id","vocabulary_id","concept_class_id","standard_concept","concept_code","valid_start_date","valid_end_date","invalid_reason")
VALUES
('8507','MALE','Gender','Gender','Gender','S','M','1970-01-01','2099-12-31',''),
('8521','OTHER','Gender','Gender','Gender','','O','1970-01-01','2014-07-31','D'),
('8532','FEMALE','Gender','Gender','Gender','S','F','1970-01-01','2099-12-31',''),
('8551','UNKNOWN','Gender','Gender','Gender','','U','1970-01-01','2014-07-31','D'),
('8570','AMBIGUOUS','Gender','Gender','Gender','','A','1970-01-01','2014-07-31','D');
