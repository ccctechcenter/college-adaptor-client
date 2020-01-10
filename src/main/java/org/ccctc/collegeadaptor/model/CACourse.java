package org.ccctc.collegeadaptor.model;

import java.util.Date;
import java.util.List;

/**
 * Created by jrscanlon on 2/22/16.
 */
public class CACourse  {

    private String misCode;
    private String sisCourseId;
    private String sisTermId;
    // Identifier for C-ID.net course
    private String c_id;
    private String controlNumber;
    private String subject;
    private String number;
    private String title;
    private String description;
    private String outline;
    private String prerequisites;
    private String corequisites;
    private List<CACourse> prerequisiteList;
    private List<CACourse> corequisiteList;
    private Float minimumUnits;
    private Float maximumUnits;
    private CATransferStatus transferStatus;
    private CACreditStatus creditStatus;
    private CAGradingMethod gradingMethod;
    private List<CACourseContact> courseContacts;
    private Float fee;
    // The start date of the course availability for sections.
    private Date start;
    // The end date of the course availability for sections.
    private Date end;
    // The status of a course.
    private CACourseStatus status;

    public String getMisCode() {
        return misCode;
    }

    /**
     * 3 digit college MIS Code. Example - 111
     * @param misCode - 3 digit code
     */
    public void setMisCode(String misCode) {
        this.misCode = misCode;
    }

    public String getSisCourseId() {
        return sisCourseId;
    }

    /**
     * Sets the identifier/code representing a course in the SIS.
     * This should be something a school or district administrator will know.
     * @param sisCourseId
     */
    public void setSisCourseId(String sisCourseId) {
        this.sisCourseId = sisCourseId;
    }

    public String getSisTermId() {
        return sisTermId;
    }

    public void setSisTermId(String sisTermId) {
        this.sisTermId = sisTermId;
    }

    public String getC_id() {
        return c_id;
    }

    public void setC_id(String c_id) {
        this.c_id = c_id;
    }

    public String getControlNumber() {
        return controlNumber;
    }

    public void setControlNumber(String controlNumber) {
        this.controlNumber = controlNumber;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOutline() {
        return outline;
    }

    public void setOutline(String outline) {
        this.outline = outline;
    }

    public String getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(String prerequisites) {
        this.prerequisites = prerequisites;
    }

    public String getCorequisites() {
        return corequisites;
    }

    public void setCorequisites(String corequisites) {
        this.corequisites = corequisites;
    }

    public List<CACourse> getPrerequisiteList() {
        return prerequisiteList;
    }

    public void setPrerequisiteList(List<CACourse> prerequisiteList) {
        this.prerequisiteList = prerequisiteList;
    }

    public List<CACourse> getCorequisiteList() {
        return corequisiteList;
    }

    public void setCorequisiteList(List<CACourse> corequisiteList) {
        this.corequisiteList = corequisiteList;
    }

    public Float getMinimumUnits() {
        return minimumUnits;
    }

    public void setMinimumUnits(Float minimumUnits) {
        this.minimumUnits = minimumUnits;
    }

    public Float getMaximumUnits() {
        return maximumUnits;
    }

    public void setMaximumUnits(Float maximumUnits) {
        this.maximumUnits = maximumUnits;
    }

    public CATransferStatus getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(CATransferStatus transferStatus) {
        this.transferStatus = transferStatus;
    }

    public CACreditStatus getCreditStatus() {
        return creditStatus;
    }

    public void setCreditStatus(CACreditStatus creditStatus) {
        this.creditStatus = creditStatus;
    }

    public CAGradingMethod getGradingMethod() {
        return gradingMethod;
    }

    public void setGradingMethod(CAGradingMethod gradingMethod) {
        this.gradingMethod = gradingMethod;
    }

    public List<CACourseContact> getCourseContacts() {
        return courseContacts;
    }

    public void setCourseContacts(List<CACourseContact> courseContacts) {
        this.courseContacts = courseContacts;
    }

    public Float getFee() {
        return fee;
    }

    public void setFee(Float fee) {
        this.fee = fee;
    }

    public Date getStart() {
        return start;
    }

    /**
     * Sets the date course is available for building sections.
     * @param start - date
     */
    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    /**
     * Date course is no longer available for building sections
     * @param end - date
     */
    public void setEnd(Date end) {
        this.end = end;
    }

    public CACourseStatus getStatus() {
        return status;
    }

    /**
     * Sets the status of the course.
     * @param status - Active or Inactive
     * @see CACourseStatus
     */
    public void setStatus(CACourseStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "CACourse{" +
                "misCode='" + misCode + '\'' +
                ", sisCourseId='" + sisCourseId + '\'' +
                ", sisTermId='" + sisTermId + '\'' +
                ", c_id='" + c_id + '\'' +
                ", controlNumber='" + controlNumber + '\'' +
                ", subject='" + subject + '\'' +
                ", number='" + number + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", outline='" + outline + '\'' +
                ", prerequisites='" + prerequisites + '\'' +
                ", corequisites='" + corequisites + '\'' +
                ", prerequisiteList=" + prerequisiteList +
                ", corequisiteList=" + corequisiteList +
                ", minimumUnits=" + minimumUnits +
                ", maximumUnits=" + maximumUnits +
                ", transferStatus=" + transferStatus +
                ", creditStatus=" + creditStatus +
                ", gradingMethod=" + gradingMethod +
                ", courseContacts=" + courseContacts +
                ", fee=" + fee +
                ", start=" + start +
                ", end=" + end +
                ", status=" + status +
                '}';
    }
}
