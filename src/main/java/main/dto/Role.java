package main.dto;

public enum Role {

    USER("app-user"), ADMIN("app-admin");

    private final String value;

    Role(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
