package org.ccctc.collegeadaptor.model;

/**
 * Course Exchange enrollment information
 */
public class CACourseExchangeEnrollment {
    private String misCode;
    private String collegeName;
    private Float units;
    private String c_id;
    private CASection section;

    public String getMisCode() {
        return misCode;
    }

    public void setMisCode(String misCode) {
        this.misCode = misCode;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public Float getUnits() {
        return units;
    }

    public void setUnits(Float units) {
        this.units = units;
    }

    public String getC_id() {
        return c_id;
    }

    public void setC_id(String c_id) {
        this.c_id = c_id;
    }

    public CASection getSection() {
        return section;
    }

    public void setSection(CASection section) {
        this.section = section;
    }

    @Override
    public String toString() {
        return "CourseExchangeEnrollment{" +
                "misCode='" + misCode + '\'' +
                ", collegeName='" + collegeName + '\'' +
                ", units=" + units +
                ", c_id='" + c_id + '\'' +
                ", section=" + section +
                '}';
    }
}
