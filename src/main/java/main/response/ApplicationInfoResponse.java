package main.response;

public class ApplicationInfoResponse {
    private String version;
    String dbKind;
    String dbUrl;

    public ApplicationInfoResponse(String version, String dbKind, String dbUrl) {
        this.version = version;
        this.dbKind = dbKind;
        this.dbUrl = dbUrl;
    }


    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDbKind() {
        return dbKind;
    }

    public void setDbKind(String dbKind) {
        this.dbKind = dbKind;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

}
