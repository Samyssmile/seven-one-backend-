package main.response;

public class ApplicationInfoResponse {
    private final String profile;
    private final String version;
    private final String dbUrl;

    public ApplicationInfoResponse(String version, String dbUrl, String profile) {
        this.version = version;
        this.dbUrl = dbUrl;
        this.profile = profile;
    }

    public String getProfile() {
        return profile;
    }

    public String getVersion() {
        return version;
    }

    public String getDbUrl() {
        return dbUrl;
    }
}
