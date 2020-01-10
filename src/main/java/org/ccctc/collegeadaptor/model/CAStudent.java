package org.ccctc.collegeadaptor.model;

import java.util.Date;
import java.util.List;

/**
 * Created by pway on 3/16/16.
 */
public class CAStudent  {

    private String cccid;
    private String sisPersonId;
    private String sisTermId;
    private List<CACohort> cohorts;

    // Eligibility to use Exchange
    private String visaType;
    private Boolean hasCaliforniaAddress;
    private Boolean isIncarcerated;
    private Boolean isConcurrentlyEnrolled;

    // Matriculation eligibility
    private Boolean hasAttendedOrientation;
    private Boolean hasEducationPlan;
    private Boolean hasMathAssessment;
    private Boolean hasEnglishAssessment;

    // Term Based information
    private ApplicationStatus applicationStatus;
    private CAResidentStatus residentStatus;
    private Boolean hasHold;
    private Date registrationDate;
    private Boolean hasFinancialAidAward;
    private Boolean hasBogfw;
    private Boolean dspsEligible;

    // Other student information
    private Float accountBalance;

    public String getCccid() {
        return cccid;
    }

    public void setCccid(String cccid) {
        this.cccid = cccid;
    }

    public String getSisPersonId() {
        return sisPersonId;
    }

    public void setSisPersonId(String sisPersonId) {
        this.sisPersonId = sisPersonId;
    }

    public String getSisTermId() {
        return sisTermId;
    }

    public void setSisTermId(String sisTermId) {
        this.sisTermId = sisTermId;
    }

    public List<CACohort> getCohorts() {
        return cohorts;
    }

    public void setCohorts(List<CACohort> cohorts) {
        this.cohorts = cohorts;
    }

    public String getVisaType() {
        return visaType;
    }

    /**
     * Sets the VISA type of the student
     * @param visaType - VISA type should be upper case letters and numbers ONLY, no spaces, dashes, etc
     */
    public void setVisaType(String visaType) {
        this.visaType = visaType;
    }

    public Boolean getHasCaliforniaAddress() {
        return hasCaliforniaAddress;
    }

    public void setHasCaliforniaAddress(Boolean hasCaliforniaAddress) {
        this.hasCaliforniaAddress = hasCaliforniaAddress;
    }

    public Boolean getIncarcerated() {
        return isIncarcerated;
    }

    public void setIncarcerated(Boolean incarcerated) {
        isIncarcerated = incarcerated;
    }

    public Boolean getConcurrentlyEnrolled() {
        return isConcurrentlyEnrolled;
    }

    public void setConcurrentlyEnrolled(Boolean concurrentlyEnrolled) {
        isConcurrentlyEnrolled = concurrentlyEnrolled;
    }

    public Boolean getHasAttendedOrientation() {
        return hasAttendedOrientation;
    }

    public void setHasAttendedOrientation(Boolean hasAttendedOrientation) {
        this.hasAttendedOrientation = hasAttendedOrientation;
    }

    public Boolean getHasEducationPlan() {
        return hasEducationPlan;
    }

    public void setHasEducationPlan(Boolean hasEducationPlan) {
        this.hasEducationPlan = hasEducationPlan;
    }

    public Boolean getHasMathAssessment() {
        return hasMathAssessment;
    }

    public void setHasMathAssessment(Boolean hasMathAssessment) {
        this.hasMathAssessment = hasMathAssessment;
    }

    public Boolean getHasEnglishAssessment() {
        return hasEnglishAssessment;
    }

    public void setHasEnglishAssessment(Boolean hasEnglishAssessment) {
        this.hasEnglishAssessment = hasEnglishAssessment;
    }

    public ApplicationStatus getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(ApplicationStatus applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    public CAResidentStatus getResidentStatus() {
        return residentStatus;
    }

    public void setResidentStatus(CAResidentStatus residentStatus) {
        this.residentStatus = residentStatus;
    }

    public Boolean getHasHold() {
        return hasHold;
    }

    /**
     * Sets the value to determine whether the student has a hold that would prevent registration for the term
     * @param hasHold - true or false
     */
    public void setHasHold(Boolean hasHold) {
        this.hasHold = hasHold;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Float getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(Float accountBalance) {
        this.accountBalance = accountBalance;
    }

    public Boolean getHasFinancialAidAward() {
        return hasFinancialAidAward;
    }

    /**
     * Sets the value to determine whether the student has any active financial aid award for the term
     * @param hasFinancialAidAward - true or false
     */
    public void setHasFinancialAidAward(Boolean hasFinancialAidAward) {
        this.hasFinancialAidAward = hasFinancialAidAward;
    }

    public Boolean getHasBogfw() {
        return hasBogfw;
    }

    /**
     * Sets the value to determine whether the student has any active BOG Fee Waiver for the term
     * @param hasBogfw - true or false
     */
    public void setHasBogfw(Boolean hasBogfw) {
        this.hasBogfw = hasBogfw;
    }

    public Boolean getDspsEligible() {
        return dspsEligible;
    }

    public void setDspsEligible(Boolean dspsEligible) {
        this.dspsEligible = dspsEligible;
    }

    @Override
    public String toString() {
        return "CAStudent{" +
                "cccid='" + cccid + '\'' +
                ", sisPersonId='" + sisPersonId + '\'' +
                ", sisTermId='" + sisTermId + '\'' +
                ", cohorts=" + cohorts +
                ", visaType='" + visaType + '\'' +
                ", hasCaliforniaAddress=" + hasCaliforniaAddress +
                ", isIncarcerated=" + isIncarcerated +
                ", isConcurrentlyEnrolled=" + isConcurrentlyEnrolled +
                ", hasAttendedOrientation=" + hasAttendedOrientation +
                ", hasEducationPlan=" + hasEducationPlan +
                ", hasMathAssessment=" + hasMathAssessment +
                ", hasEnglishAssessment=" + hasEnglishAssessment +
                ", applicationStatus=" + applicationStatus +
                ", residentStatus=" + residentStatus +
                ", hasHold=" + hasHold +
                ", registrationDate=" + registrationDate +
                ", hasFinancialAidAward=" + hasFinancialAidAward +
                ", hasBogfw=" + hasBogfw +
                ", dspsEligible=" + dspsEligible +
                ", accountBalance=" + accountBalance +
                '}';
    }
}
