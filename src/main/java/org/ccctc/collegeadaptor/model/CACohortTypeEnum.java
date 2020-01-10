package org.ccctc.collegeadaptor.model;

/**
 * Cohort types
 */
public enum CACohortTypeEnum {

    COURSE_EXCHANGE("Course Exchange");

    private final String description;

    private CACohortTypeEnum(final String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
