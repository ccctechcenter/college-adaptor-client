package org.ccctc.collegeadaptor.model;

import java.util.Date;

/**
 * Created by jrscanlon on 12/9/15.
 */
public class CAEnrollment {

    private String cccid;
    private String sisPersonId;
    private String sisTermId;
    private String sisSectionId;
    private CAEnrollmentStatus enrollmentStatus;
    private Date enrollmentStatusDate;
    private Float units;
    private Boolean passNoPass;
    private Boolean audit;
    private String grade;
    private Date gradeDate;
    private String sisCourseId;
    private String c_id;
    private String title;
    private Date lastDateAttended;

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

    public String getSisSectionId() {
        return sisSectionId;
    }

    public void setSisSectionId(String sisSectionId) {
        this.sisSectionId = sisSectionId;
    }

    public CAEnrollmentStatus getEnrollmentStatus() {
        return enrollmentStatus;
    }

    public void setEnrollmentStatus(CAEnrollmentStatus enrollmentStatus) {
        this.enrollmentStatus = enrollmentStatus;
    }

    public Date getEnrollmentStatusDate() {
        return enrollmentStatusDate;
    }

    public void setEnrollmentStatusDate(Date enrollmentStatusDate) {
        this.enrollmentStatusDate = enrollmentStatusDate;
    }

    public Float getUnits() {
        return units;
    }

    public void setUnits(Float units) {
        this.units = units;
    }

    public Boolean getPassNoPass() {
        return passNoPass;
    }

    public void setPassNoPass(Boolean passNoPass) {
        this.passNoPass = passNoPass;
    }

    public Boolean getAudit() {
        return audit;
    }

    public void setAudit(Boolean audit) {
        this.audit = audit;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Date getGradeDate() {
        return gradeDate;
    }

    public void setGradeDate(Date gradeDate) {
        this.gradeDate = gradeDate;
    }

    public String getSisCourseId() {
        return sisCourseId;
    }

    public void setSisCourseId(String sisCourseId) {
        this.sisCourseId = sisCourseId;
    }

    public String getC_id() {
        return c_id;
    }

    public void setC_id(String c_id) {
        this.c_id = c_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getLastDateAttended() {
        return lastDateAttended;
    }

    public void setLastDateAttended(Date lastDateAttended) {
        this.lastDateAttended = lastDateAttended;
    }

    @Override
    public String toString() {
        return "CAEnrollment{" +
                "cccid='" + cccid + '\'' +
                ", sisPersonId='" + sisPersonId + '\'' +
                ", sisTermId='" + sisTermId + '\'' +
                ", sisSectionId='" + sisSectionId + '\'' +
                ", enrollmentStatus=" + enrollmentStatus +
                ", enrollmentStatusDate=" + enrollmentStatusDate +
                ", units=" + units +
                ", passNoPass=" + passNoPass +
                ", audit=" + audit +
                ", grade='" + grade + '\'' +
                ", gradeDate=" + gradeDate +
                ", sisCourseId='" + sisCourseId + '\'' +
                ", c_id='" + c_id + '\'' +
                ", title='" + title + '\'' +
                ", lastDateAttended=" + lastDateAttended +
                '}';
    }
}
