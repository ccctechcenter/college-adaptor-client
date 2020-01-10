package org.ccctc.collegeadaptor.model;

/**
 * Created by jrscanlon on 3/2/16.
 */
public class CAPrerequisiteStatus {

    private CAPrerequisiteStatusEnum status;
    private String message;

    /**
     * Status of the prerequisite
     */
    public CAPrerequisiteStatusEnum getStatus() {
        return status;
    }

    public void setStatus(CAPrerequisiteStatusEnum status) {
        this.status = status;
    }

    /**
     * Message indicating why a student may or may not meet the prerequisite requirements
     */
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
