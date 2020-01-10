package org.ccctc.collegeadaptor.model;

/**
 * Cohort
 */
public class CACohort {
    private CACohortTypeEnum name;
    private String description;
    public CACohortTypeEnum getName() {
        return name;
    }

    public String getDescription() {
        return this.name.getDescription();
    }

    public void setName(CACohortTypeEnum name) {
        this.name = name;
    }
}