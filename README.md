# Demo for Hibernate Validator Generic Container

This demo shows a weird workaround in a [Hibernate Value Extractor](src/main/java/de/n04r/demo/hibernatevalidatorgenericcontainer/LocaleAwareContainerValueExtractor.java) for a [custom generic container](src/main/java/de/n04r/demo/hibernatevalidatorgenericcontainer/LocaleAwareContainer.java) which is supposed to be validated.

[See the unit test](src/test/java/de/n04r/demo/hibernatevalidatorgenericcontainer/LocaleAwareContainerValueExtractorTest.java) to see that it works in principle, but the special case for `String` is nasty. If it's omitted, a `ClassCastException` is thrown by Hibernate Validator.