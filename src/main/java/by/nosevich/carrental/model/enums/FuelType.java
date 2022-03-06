package by.nosevich.carrental.model.enums;

public enum FuelType {
    PETROL("fuelType.petrol"), DIESEL("fuelType.diesel"), ELECTRIC("fuelType.electric");

    private final String localizationKey;

    FuelType(String localizationKey) {
        this.localizationKey = localizationKey;
    }

    public String getLocalizationKey() {
        return localizationKey;
    }
}