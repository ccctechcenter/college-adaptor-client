package org.ccctc.collegeadaptor.model;

/**
 * Created by anambiar on 5/27/16.
 */
public class CAInstructor {

    private String firstName;
    private String lastName;
    private String emailAddress;
    private String sisPersonId;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getSisPersonId() { return sisPersonId; }

    public void setSisPersonId( String sisPersonId) {
        this.sisPersonId = sisPersonId;
    }

    @Override
    public String toString() {
        return "CAInstructor{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", sisPersonId='" + sisPersonId + '\'' +
                '}';
    }
}
