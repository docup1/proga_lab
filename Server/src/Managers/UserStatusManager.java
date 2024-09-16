package Managers;
public class UserStatusManager {
    private boolean status;
    private String userName;
    public UserStatusManager(boolean status, String userName){
        this.status = status;
        this.userName = userName;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public boolean getStatus() {
        return status;
    }
}
