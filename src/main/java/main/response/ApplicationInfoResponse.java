package main.response;

public class ApplicationInfoResponse {
    private final String profile;
    private final String version;
    private final String  dbKind;
    private final String  dbUrl;

    public ApplicationInfoResponse(String version, String dbKind, String dbUrl, String profile) {
        this.version = version;
        this.dbKind = dbKind;
        this.dbUrl = dbUrl;
        this.profile = profile;
    }

    public String getProfile() {
        return profile;
    }

    public String getVersion() {
        return version;
    }

    public String getDbKind() {
        return dbKind;
    }

    public String getDbUrl() {
        return dbUrl;
    }
}
