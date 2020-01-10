package org.ccctc.collegeadaptor.model;

/**
 * Financial Aid Units
 */
public class CAFinancialAidUnits {
    private String cccid;
    private String sisTermId;
    private CACourseExchangeEnrollment ceEnrollment;

    public String getCccid() {
        return cccid;
    }

    public void setCccid(String cccid) {
        this.cccid = cccid;
    }

    public String getSisTermId() {
        return sisTermId;
    }

    public void setSisTermId(String sisTermId) {
        this.sisTermId = sisTermId;
    }

    public CACourseExchangeEnrollment getCeEnrollment() {
        return ceEnrollment;
    }

    public void setCeEnrollment(CACourseExchangeEnrollment ceEnrollment) {
        this.ceEnrollment = ceEnrollment;
    }

    @Override
    public String toString() {
        return "FinancialAidUnits{" +
                "cccid='" + cccid + '\'' +
                ", sisTermId='" + sisTermId + '\'' +
                ", ceEnrollment=" + ceEnrollment +
                '}';
    }
}
