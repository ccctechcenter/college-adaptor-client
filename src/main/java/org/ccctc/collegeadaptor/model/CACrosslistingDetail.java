package org.ccctc.collegeadaptor.model;


import java.io.Serializable;
import java.util.List;

public class CACrosslistingDetail implements Serializable {

    private String name;
    private String sisTermId;
    private String primarySisSectionId;
    private List<String> sisSectionIds;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSisTermId() {
        return sisTermId;
    }

    public void setSisTermId(String sisTermId) {
        this.sisTermId = sisTermId;
    }

    public String getPrimarySisSectionId() {
        return primarySisSectionId;
    }

    public void setPrimarySisSectionId(String primarySisSectionId) {
        this.primarySisSectionId = primarySisSectionId;
    }

    public List<String> getSisSectionIds() {
        return sisSectionIds;
    }

    public void setSisSectionIds(List<String> sisSectionIds) {
        this.sisSectionIds = sisSectionIds;
    }

    @Override
    public String toString() {
        return "CACrosslistingDetail{" +
                "name='" + name + '\'' +
                ", sisTermId='" + sisTermId + '\'' +
                ", primarySisSectionId='" + primarySisSectionId + '\'' +
                ", sisSectionIds=" + sisSectionIds +
                '}';
    }
}
