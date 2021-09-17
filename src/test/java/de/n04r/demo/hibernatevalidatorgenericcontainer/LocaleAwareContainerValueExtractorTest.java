package de.n04r.demo.hibernatevalidatorgenericcontainer;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.junit.jupiter.api.Test;

import java.util.Locale;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class LocaleAwareContainerValueExtractorTest {

    private final Validator VALIDATOR = Validation.byDefaultProvider()
            .configure()
            .addValueExtractor(new LocaleAwareContainerValueExtractor())
            .messageInterpolator(new ParameterMessageInterpolator())
            .buildValidatorFactory()
            .getValidator();

    @Test
    void validateWithInnerClass() {
        var instance = SomeClassWithInnerClass.builder()
                .container(LocaleAwareContainer.<SomeClassWithInnerClass.InnerClass>builder()
                        .defaultValue(SomeClassWithInnerClass.InnerClass.builder().value1("").build())
                        .valuesPerLocale(Map.of(Locale.GERMAN, SomeClassWithInnerClass.InnerClass.builder().value1("").build()))
                        .build())
                .build();

        var violations = VALIDATOR.validate(instance);

        assertThat(violations).extracting(ConstraintViolation::getMessage, o -> o.getPropertyPath().toString())
                .containsExactlyInAnyOrder(
                        tuple("must not be blank", "container.defaultValue.value1"),
                        tuple("must not be blank", "container.valuesPerLocale[de].value1")
                );
    }

    @Test
    void validateWithString() {
        var instance = SomeClassWithString.builder()
                .container(LocaleAwareContainer.<String>builder()
                        .defaultValue("")
                        .valuesPerLocale(Map.of(Locale.GERMAN, ""))
                        .build())
                .build();

        var violations = VALIDATOR.validate(instance);

        assertThat(violations).extracting(ConstraintViolation::getMessage, o -> o.getPropertyPath().toString())
                .containsExactlyInAnyOrder(
                        tuple("must not be blank", "container.defaultValue"),
                        tuple("must not be blank", "container[de].valuesPerLocale")
                );
    }

    @Builder
    @Getter
    @EqualsAndHashCode
    @ToString
    private static class SomeClassWithInnerClass {
        private final LocaleAwareContainer<@Valid InnerClass> container;

        @Builder
        @Getter
        @EqualsAndHashCode
        @ToString
        public static class InnerClass {
            @NotBlank
            private final String value1;
        }
    }

    @Builder
    @Getter
    @EqualsAndHashCode
    @ToString
    private static class SomeClassWithString {
        private final LocaleAwareContainer<@NotBlank String> container;
    }
}