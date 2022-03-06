package by.nosevich.carrental.model.enums;

public enum OrderStatus {
    WAITING("orderStatus.waiting"),
    ACTIVE("orderStatus.active"),
    ENDED("orderStatus.ended"),
    CANCELED("orderStatus.canceled"),
    UNCONFIRMED("orderStatus.unconfirmed");

    private final String localizationKey;

    OrderStatus(String localizationKey) {
        this.localizationKey = localizationKey;
    }

    public String getLocalizationKey() {
        return localizationKey;
    }
}
