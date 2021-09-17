package de.n04r.demo.hibernatevalidatorgenericcontainer;

import jakarta.validation.valueextraction.ExtractedValue;
import jakarta.validation.valueextraction.ValueExtractor;

public class LocaleAwareContainerValueExtractor implements ValueExtractor<LocaleAwareContainer<@ExtractedValue ?>> {
    @Override
    public void extractValues(LocaleAwareContainer<?> originalValue, ValueExtractor.ValueReceiver receiver) {
        // Why do we need the special case for String (and for other "simple" types) here?
        if (originalValue.getDefaultValue() instanceof String) {
            receiver.value("defaultValue", originalValue.getDefaultValue());
            if (originalValue.getValuesPerLocale() != null) {
                originalValue.getValuesPerLocale().forEach((key, value) -> receiver.keyedValue("valuesPerLocale", key, value));
            } else {
                receiver.iterableValue("valuesPerLocale", null);
            }
        } else {
            // Generic type is NOT String, so assume some inner DTO
            receiver.value(null, originalValue);
        }
    }
}
