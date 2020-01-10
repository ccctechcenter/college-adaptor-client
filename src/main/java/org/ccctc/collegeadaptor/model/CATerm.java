package org.ccctc.collegeadaptor.model;

import java.util.Date;

/**
 * Created by anambiar on 5/27/16.
 */
public class CATerm {

    private String misCode;
    private String sisTermId;
    private Integer year;
    private CATermSession session;
    private CATermType type;
    private Date start;
    private Date end;
    private Date preRegistrationStart;
    private Date preRegistrationEnd;
    private Date registrationStart;
    private Date registrationEnd;
    private Date addDeadline;
    private Date dropDeadline;
    private Date withdrawalDeadline;
    private Date feeDeadline;
    private Date censusDate;
    private String description;


    public String getMisCode() {
        return misCode;
    }

    public void setMisCode(String misCode) {
        this.misCode = misCode;
    }

    public String getSisTermId() {
        return sisTermId;
    }

    /**
     * Sets the identifier/code representing a term in the SIS.
     * This should be something a school or district administrator will know.
     * Example: 0290
     * @param sisTermId - term id in the SIS
     */
    public void setSisTermId(String sisTermId) {
        this.sisTermId = sisTermId;
    }

    public Integer getYear() {
        return year;
    }

    /**
     * 4 digits Academic Year. Example: 1997
     * @param year - 4 digit year
     */
    public void setYear(Integer year) {
        this.year = year;
    }

    public CATermSession getSession() {
        return session;
    }

    /**
     * Sets the term session. Spring/Fall/Winter/Summer for Term based Schools.
     * Example: Fall
     * @param session - the type of session
     * @see CATermSession
     */
    public void setSession(CATermSession session) {
        this.session = session;
    }

    public CATermType getType() {
        return type;
    }

    public void setType(CATermType type) {
        this.type = type;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Date getPreRegistrationStart() {
        return preRegistrationStart;
    }

    public void setPreRegistrationStart(Date preRegistrationStart) {
        this.preRegistrationStart = preRegistrationStart;
    }

    public Date getPreRegistrationEnd() {
        return preRegistrationEnd;
    }

    public void setPreRegistrationEnd(Date preRegistrationEnd) {
        this.preRegistrationEnd = preRegistrationEnd;
    }

    public Date getRegistrationStart() {
        return registrationStart;
    }

    public void setRegistrationStart(Date registrationStart) {
        this.registrationStart = registrationStart;
    }

    public Date getRegistrationEnd() {
        return registrationEnd;
    }

    public void setRegistrationEnd(Date registrationEnd) {
        this.registrationEnd = registrationEnd;
    }

    public Date getAddDeadline() {
        return addDeadline;
    }

    public void setAddDeadline(Date addDeadline) {
        this.addDeadline = addDeadline;
    }

    public Date getDropDeadline() {
        return dropDeadline;
    }

    /**
     * Sets the last Date to Drop from class with no charges
     * @param dropDeadline - date example: 1997-08-10
     */
    public void setDropDeadline(Date dropDeadline) {
        this.dropDeadline = dropDeadline;
    }

    public Date getWithdrawalDeadline() {
        return withdrawalDeadline;
    }

    /**
     * Sets the last Date to with draw from the class with class fee refund
     * @param withdrawalDeadline - date example: 1997-08-10
     */
    public void setWithdrawalDeadline(Date withdrawalDeadline) {
        this.withdrawalDeadline = withdrawalDeadline;
    }

    public Date getFeeDeadline() {
        return feeDeadline;
    }

    public void setFeeDeadline(Date feeDeadline) {
        this.feeDeadline = feeDeadline;
    }

    public Date getCensusDate() {
        return censusDate;
    }

    public void setCensusDate(Date censusDate) {
        this.censusDate = censusDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "CATerm{" +
                "misCode='" + misCode + '\'' +
                ", sisTermId='" + sisTermId + '\'' +
                ", year=" + year +
                ", session=" + session +
                ", type=" + type +
                ", start=" + start +
                ", end=" + end +
                ", preRegistrationStart=" + preRegistrationStart +
                ", preRegistrationEnd=" + preRegistrationEnd +
                ", registrationStart=" + registrationStart +
                ", registrationEnd=" + registrationEnd +
                ", addDeadline=" + addDeadline +
                ", dropDeadline=" + dropDeadline +
                ", withdrawalDeadline=" + withdrawalDeadline +
                ", feeDeadline=" + feeDeadline +
                ", censusDate=" + censusDate +
                ", description='" + description + '\'' +
                '}';
    }
}
