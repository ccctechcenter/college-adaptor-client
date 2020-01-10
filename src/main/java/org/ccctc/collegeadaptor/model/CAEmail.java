package org.ccctc.collegeadaptor.model;

/**
 * Created by anambiar on 5/27/16.
 */
public class CAEmail {

    private CAEmailType type;
    private String emailAddress;

    public CAEmailType getType() {
        return type;
    }

    public void setType(CAEmailType type) {
        this.type = type;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Override
    public String toString() {
        return "CAEmail{" +
                "type=" + type +
                ", emailAddress='" + emailAddress + '\'' +
                '}';
    }
}
