package by.nosevich.carrental.model.enums;

public enum Transmission {
    MECHANICAL("transmission.mechanical"),
    HYDROMECHANICAL("transmission.hydromechanical"),
    HYDRAULIC("transmission.hydraulic"),
    ELECTROMECHANICAL("transmission.electromechanical"),
    AUTOMATIC("transmission.automatic");

    private final String localizationKey;

    Transmission(String localizationKey) {
        this.localizationKey = localizationKey;
    }

    public String getLocalizationKey() {
        return localizationKey;
    }
}