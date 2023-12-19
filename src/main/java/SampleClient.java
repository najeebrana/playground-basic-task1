import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SampleClient {

    public static void main(String[] theArgs) {

        // Create a FHIR client
        FhirContext fhirContext = FhirContext.forR4();
        IGenericClient client = fhirContext.newRestfulGenericClient("http://hapi.fhir.org/baseR4");
        client.registerInterceptor(new LoggingInterceptor(false));

        // Search for Patient resources
        Bundle response = client
                .search()
                .forResource("Patient")
                .where(Patient.FAMILY.matches().value("SMITH"))
                .returnBundle(Bundle.class)
                .execute();

        // Fetch the list of patients
        List<Bundle.BundleEntryComponent> list = response.getEntry();

        // Sort the list by last name
        Collections.sort(response.getEntry(), Comparator.comparing(item -> (
                (Patient) (item.getResource())).getName().get(0).getGiven().get(0).toString())
            );

        // Finally print the sorted list
        list.forEach(pat ->
                {
                    Patient p = (Patient) pat.getResource();
                    System.out.println((p.getName().get(0).getGiven() +", "+ p.getName().get(0).getFamily() +", "+ p.getBirthDate()));
                });

    }

}
