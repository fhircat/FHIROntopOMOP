package org.fhircat.mapping.old;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.r4.model.ElementDefinition;
import org.hl7.fhir.r4.model.ElementDefinition.TypeRefComponent;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.StructureDefinition.StructureDefinitionSnapshotComponent;
import org.hl7.fhir.r4.model.ValueSet;
import org.hl7.fhir.r4.model.ValueSet.ConceptReferenceComponent;
import org.hl7.fhir.r4.model.ValueSet.ValueSetComposeComponent;

import java.io.*;
import java.util.List;

public class HandleUSCoreProfiles {

    private static String[] usCoreProfileNames = {
//            "StructureDefinition-us-core-allergyintolerance.json",
//            "StructureDefinition-us-core-careplan.json",
//            "StructureDefinition-us-core-careteam.json",
            "StructureDefinition-us-core-condition.json",
//            "StructureDefinition-us-core-diagnosticreport-lab.json",
//            "StructureDefinition-us-core-diagnosticreport-note.json",
//            "StructureDefinition-us-core-documentreference.json",
//            "StructureDefinition-us-core-encounter.json",
//            "StructureDefinition-us-core-goal.json",
//            "StructureDefinition-us-core-immunization.json",
//            "StructureDefinition-us-core-implantable-device.json",
//            "StructureDefinition-us-core-location.json",
//            "StructureDefinition-us-core-medication.json",
//            "StructureDefinition-us-core-medicationrequest.json",
//            "StructureDefinition-us-core-observation-lab.json",
//            "StructureDefinition-us-core-organization.json",
//            "StructureDefinition-us-core-patient.json",
//            "StructureDefinition-us-core-practitioner.json",
//            "StructureDefinition-us-core-practitionerrole.json",
//            "StructureDefinition-us-core-procedure.json",
//            "StructureDefinition-us-core-provenance.json",
//            "StructureDefinition-us-core-smokingstatus.json",
//            "StructureDefinition-us-core-vital-signs.json",
//            "StructureDefinition-head-occipital-frontal-circumference-percentile.json",
//            "StructureDefinition-pediatric-bmi-for-age.json",
//            "StructureDefinition-pediatric-weight-for-height.json",
//            "StructureDefinition-us-core-blood-pressure.json",
//            "StructureDefinition-us-core-bmi.json",
//            "StructureDefinition-us-core-body-height.json",
//            "StructureDefinition-us-core-body-temperature.json",
//            "StructureDefinition-us-core-body-weight.json",
//            "StructureDefinition-us-core-head-circumference.json",
//            "StructureDefinition-us-core-heart-rate.json",
//            "StructureDefinition-us-core-pulse-oximetry.json",
//            "StructureDefinition-us-core-respiratory-rate.json"
    };

    private static String[] usCoreExtensionNames = {
            "StructureDefinition-us-core-birthsex.json",
            "StructureDefinition-us-core-direct.json",
            "StructureDefinition-us-core-ethnicity.json",
            "StructureDefinition-us-core-race.json",
    };

    private static String[] usCoreValueSetNames = {
            "ValueSet-birthsex.json",
            "ValueSet-omb-race-category.json",
            "ValueSet-simple-language.json",
            "ValueSet-us-core-clinical-note-type.json",
            "ValueSet-us-core-condition-category.json",
            "ValueSet-us-core-condition-code.json",
            "ValueSet-us-core-diagnosticreport-category.json",
            "ValueSet-us-core-diagnosticreport-lab-codes.json",
            "ValueSet-us-core-diagnosticreport-report-and-note-codes.json",
            "ValueSet-us-core-documentreference-category.json",
            "ValueSet-us-core-documentreference-type.json",
            "ValueSet-us-core-encounter-type.json",
            "ValueSet-us-core-narrative-status.json",
            "ValueSet-us-core-observation-smoking-status-status.json",
            "ValueSet-us-core-observation-smokingstatus-max.json",
            "ValueSet-us-core-observation-value-codes.json",
            "ValueSet-us-core-procedure-code.json",
            "ValueSet-us-core-provenance-participant-type.json",
            "ValueSet-us-core-provider-role.json",
            "ValueSet-us-core-smoking-status-observation-codes.json",
            "ValueSet-us-core-usps-state.json",
            "ValueSet-us-core-vital-signs.json"
    };

    private static String[] usCoreCodeSystemNames = {
            "CodeSystem-careplan-category.json",
            "CodeSystem-condition-category.json",
            "CodeSystem-us-core-documentreference-category.json",
            "CodeSystem-us-core-provenance-participant-type.json"
    };


    private static FhirContext ctx = FhirContext.forR4();


    //private static String profilePath = "/Users/m005994/Documents/cd2h-n3c/FHIR2OMOP/full-ig/site/";
    private static String profilePath = "/Users/xiao/Development/fhircat/full-ig/site/";

    //private static String outputPath = "/Users/m005994/Documents/cd2h-n3c/FHIR2OMOP/";
    private static String outputPath = "/Users/xiao/Development/fhircat/FHIROntopOMOP/";


    public static String readUSCoreProfileFromFile(String fileName) {

        StringBuffer sb = new StringBuffer();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(fileName));
            //bw = new BufferedWriter(new FileWriter(outputFile));
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
            br.close();
        } catch (IOException ie) {
            ie.printStackTrace();
        }

        return sb.toString();

    }

    public static void saveStringIntoFile(String content, String fileName) {

        BufferedWriter bw = null;

        try {
            bw = new BufferedWriter(new FileWriter(fileName));
            bw.write(content);
            bw.close();
        } catch (IOException ie) {
            ie.printStackTrace();
        }

    }

    public static String handleUSCoreProfileFromFile(String fileName) {

        StringBuffer sb = new StringBuffer();

        String profileJson = readUSCoreProfileFromFile(fileName);
        StructureDefinition profile = ctx.newJsonParser().parseResource(StructureDefinition.class, profileJson);
        
        String profileName = profile.getId();
        profileName = profileName.substring(20);

        StructureDefinitionSnapshotComponent snapshot = profile.getSnapshot();
        List<ElementDefinition> elementDefinitions = snapshot.getElement();
        for (ElementDefinition element : elementDefinitions) {
            String id = element.getId();
            String definition = element.getDefinition();
            definition = definition.replaceAll("\n", " ");
            definition = definition.replaceAll("\\|", "\\/");

            int min = element.getMin();
            String max = element.getMax();

            sb.append(profileName + "|");

            sb.append(id + "|");
            sb.append(min + "~" + max + "|");

            List<TypeRefComponent> typeRefs = element.getType();
            for (TypeRefComponent typeRef : typeRefs) {
                String code = typeRef.getCode();

                if (typeRefs.size() > 1) {
                    sb.append(code + "/");
                } else {
                    sb.append(code);
                }
            }

            sb.append("|");
            sb.append(definition + "\n");
        }
        
        System.out.println(sb.toString());

        return sb.toString();

    }

    public static String handleUSCoreValueSetromFile(String fileName) {

        StringBuffer sb = new StringBuffer();
        String valuesetJson = readUSCoreProfileFromFile(fileName);
        ValueSet valueset = ctx.newJsonParser().parseResource(ValueSet.class, valuesetJson);
        String vsName = valueset.getName();

        //sb.append(vsName + "|");


        ValueSetComposeComponent compose = valueset.getCompose();
        List<ValueSet.ConceptSetComponent> conceptsets = compose.getInclude();
        for (ValueSet.ConceptSetComponent conceptset : conceptsets) {

            String system = conceptset.getSystem();
            List<ConceptReferenceComponent> conceptRefs = conceptset.getConcept();
            for (ConceptReferenceComponent conceptRef : conceptRefs) {
                String code = conceptRef.getCode();
                String display = conceptRef.getDisplay();
                sb.append(vsName + "|" + system + "|" + code + "|" + display + "\n");
            }


        }


        System.out.println(sb.toString());


        return sb.toString();

    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub

        //String fileName = "/Users/m005994/Documents/cd2h-n3c/FHIR2OMOP/full-ig/site/ValueSet-birthsex.json";
        //handleUSCoreProfileFromFile(fileName);

        StringBuffer sb = new StringBuffer();

//        for (int i = 0; i < usCoreCodeSystemNames.length; i++) {
        for (int i = 0; i < usCoreProfileNames.length; i++) {

            String fileName = profilePath + usCoreProfileNames[i];
            String content = handleUSCoreProfileFromFile(fileName);

            sb.append(content);
        }

        saveStringIntoFile(sb.toString(), outputPath + "deProfiles.txt");

    }

}
