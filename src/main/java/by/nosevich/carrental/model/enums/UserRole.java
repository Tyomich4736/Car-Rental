package by.nosevich.carrental.model.enums;

public enum UserRole {
    ADMIN("userRole.admin"), CLIENT("userRole.client"), EMPLOYEE("userRole.employee");

    private final String localizationKey;

    UserRole(String localizationKey) {
        this.localizationKey = localizationKey;
    }

    public String getLocalizationKey() {
        return localizationKey;
    }
}
