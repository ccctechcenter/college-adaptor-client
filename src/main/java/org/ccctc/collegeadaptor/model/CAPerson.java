package org.ccctc.collegeadaptor.model;

import java.util.List;

/**
 * Created by anambiar on 6/3/16.
 */
public class CAPerson {
    private String misCode;
    private String sisPersonId;
    private String firstName;
    private String lastName;
    private List<CAEmail> emailAddresses;
    private String loginId;
    private String loginSuffix;
    private String cccid;

    public String getMisCode() {
        return misCode;
    }

    public void setMisCode(String misCode) {
        this.misCode = misCode;
    }

    public String getSisPersonId() {
        return sisPersonId;
    }

    public void setSisPersonId(String sisPersonId) {
        this.sisPersonId = sisPersonId;
    }

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

    public List<CAEmail> getEmailAddresses() {
        return emailAddresses;
    }

    public void setEmailAddresses(List<CAEmail> emailAddresses) {
        this.emailAddresses = emailAddresses;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getLoginSuffix() {
        return loginSuffix;
    }

    public void setLoginSuffix(String loginSuffix) {
        this.loginSuffix = loginSuffix;
    }

    public String getCccid() {
        return cccid;
    }

    public void setCccid(String cccid) {
        this.cccid = cccid;
    }

    @Override
    public String toString() {
        return "CAPerson{" +
                "misCode='" + misCode + '\'' +
                ", sisPersonId='" + sisPersonId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", emailAddresses=" + emailAddresses +
                ", loginId='" + loginId + '\'' +
                ", loginSuffix='" + loginSuffix + '\'' +
                ", cccid='" + cccid + '\'' +
                '}';
    }
}
