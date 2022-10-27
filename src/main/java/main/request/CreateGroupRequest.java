package main.request;

public class CreateGroupRequest {

    private String groupName;

    public CreateGroupRequest() {
    }

    public CreateGroupRequest(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
