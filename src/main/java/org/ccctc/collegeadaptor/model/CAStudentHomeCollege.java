package org.ccctc.collegeadaptor.model;

/**
 * Created by Rasul on 10/13/16.
 */
public class CAStudentHomeCollege {

    private String cccid;
    private String misCode;

    public String getMisCode() {
        return misCode;
    }

    public void setMisCode(String misCode) {
        this.misCode = misCode;
    }

    public String getCccid() {
        return cccid;
    }

    public void setCccid(String cccid) {
        this.cccid = cccid;
    }

    @Override
    public String toString() {
        return "CAStudentHomeCollege{" +
                " cccid: " + cccid +
                ", misCode: " + misCode + "}";
    }
}
