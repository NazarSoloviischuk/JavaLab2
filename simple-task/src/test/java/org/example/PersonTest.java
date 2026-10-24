package org.example;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

class PersonTest {

    @Test
    void equalsAndHashCodeContract() {
        EqualsVerifier.forClass(Person.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .verify();
    }
}