package de.n04r.demo.hibernatevalidatorgenericcontainer;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Locale;
import java.util.Map;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class LocaleAwareContainer<T> {
    @Valid
    private final T defaultValue;
    @NotNull
    private final Map<Locale, @Valid T> valuesPerLocale;
}
