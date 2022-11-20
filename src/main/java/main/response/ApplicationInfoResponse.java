package main.response;

public class ApplicationInfoResponse {
    private final String profile;
    private final String version;
    private final String dbUrl;

    private final String serviceDateTime;

    public ApplicationInfoResponse(String version, String dbUrl, String profile, String serviceDateTime) {
        this.version = version;
        this.dbUrl = dbUrl;
        this.profile = profile;
        this.serviceDateTime = serviceDateTime;
    }

    public String getServiceDateTime() {
        return serviceDateTime;
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
