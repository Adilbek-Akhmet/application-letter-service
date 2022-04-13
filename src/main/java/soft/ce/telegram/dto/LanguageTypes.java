package soft.ce.telegram.dto;

public enum LanguageTypes {
    KAZAKH("Қазақша"),
    RUSSIA("Русский");

    private final String name;

    LanguageTypes(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
