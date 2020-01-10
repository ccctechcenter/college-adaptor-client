package org.ccctc.collegeadaptor.model;

import java.util.Date;
import java.util.List;

/**
 * Created by anambiar on 5/27/16.
 */
public class CASection {

    private String sisSectionId;
    private String sisTermId;
    private String sisCourseId;
    private List<CAInstructor> instructors;
    private Integer maxEnrollments;
    private Integer maxWaitlist;
    private Float minimumUnits;
    private Float maximumUnits;
    private List<CAMeetingTime> meetingTimes;
    private Integer weeksOfInstruction;
    private String campus;
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
    private String title;
    private CASectionStatus status;
    private CACrosslistingDetail crosslistingDetail;

    public CACrosslistingStatus getCrosslistingStatus() {
        if (crosslistingDetail == null)
            return CACrosslistingStatus.NotCrosslisted;

        if (sisSectionId != null && sisSectionId.equals(crosslistingDetail.getPrimarySisSectionId()))
            return CACrosslistingStatus.CrosslistedPrimary;

        return CACrosslistingStatus.CrosslistedSecondary;
    }


    public String getSisSectionId() {
        return sisSectionId;
    }

    /**
     * Sets the sisSectionId to be something a school or district administrator will know.
     * This combined with the course and term will uniquely identify the section.
     * @param sisSectionId - identifier/code representing a section of a course in a term in the SIS
     */
    public void setSisSectionId(String sisSectionId) {
        this.sisSectionId = sisSectionId;
    }

    public String getSisTermId() {
        return sisTermId;
    }

    public void setSisTermId(String sisTermId) {
        this.sisTermId = sisTermId;
    }

    public String getSisCourseId() {
        return sisCourseId;
    }

    public void setSisCourseId(String sisCourseId) {
        this.sisCourseId = sisCourseId;
    }

    public List<CAInstructor> getInstructors() {
        return instructors;
    }

    public void setInstructors(List<CAInstructor> instructors) {
        this.instructors = instructors;
    }

    public Integer getMaxEnrollments() {
        return maxEnrollments;
    }

    public void setMaxEnrollments(Integer maxEnrollments) {
        this.maxEnrollments = maxEnrollments;
    }

    public Integer getMaxWaitlist() {
        return maxWaitlist;
    }

    public void setMaxWaitlist(Integer maxWaitlist) {
        this.maxWaitlist = maxWaitlist;
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

    public List<CAMeetingTime> getMeetingTimes() {
        return meetingTimes;
    }

    public void setMeetingTimes(List<CAMeetingTime> meetingTimes) {
        this.meetingTimes = meetingTimes;
    }

    public Integer getWeeksOfInstruction() {
        return weeksOfInstruction;
    }

    public void setWeeksOfInstruction(Integer weeksOfInstruction) {
        this.weeksOfInstruction = weeksOfInstruction;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
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

    public void setDropDeadline(Date dropDeadline) {
        this.dropDeadline = dropDeadline;
    }

    public Date getWithdrawalDeadline() {
        return withdrawalDeadline;
    }

    /**
     * Sets the last date to drop the section without receiving a withdrawal grade.
     * @param withdrawalDeadline - date example 11/1/2015
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

    public String getTitle() { return title; }

    /**
     * Sets the section title which may be different from course title. Example: Film/Television Production Workshop
     * @param title - section title
     */
    public void setTitle(String title) { this.title = title; }

    public CASectionStatus getStatus() { return status; };

    /**
     * Sets the status of the section
     * @param status - Active, Cancelled, Pending
     * @see CASectionStatus
     */
    public void setStatus(CASectionStatus status) { this.status = status; }

    public CACrosslistingDetail getCrosslistingDetail() {
        return crosslistingDetail;
    }

    public void setCrosslistingDetail(CACrosslistingDetail crosslistingDetail) {
        this.crosslistingDetail = crosslistingDetail;
    }

    @Override
    public String toString() {
        return "CASection{" +
                "sisSectionId='" + sisSectionId + '\'' +
                ", sisTermId='" + sisTermId + '\'' +
                ", sisCourseId='" + sisCourseId + '\'' +
                ", instructors=" + instructors +
                ", maxEnrollments=" + maxEnrollments +
                ", maxWaitlist=" + maxWaitlist +
                ", minimumUnits=" + minimumUnits +
                ", maximumUnits=" + maximumUnits +
                ", meetingTimes=" + meetingTimes +
                ", weeksOfInstruction=" + weeksOfInstruction +
                ", campus='" + campus + '\'' +
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
                ", title='" + title + '\'' +
                ", status=" + status +
                ", crosslistingDetail=" + crosslistingDetail +
                '}';
    }
}
