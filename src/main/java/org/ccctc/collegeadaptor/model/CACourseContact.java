package org.ccctc.collegeadaptor.model;

/**
 * Created by jrscanlon on 12/9/15.
 */
public class CACourseContact {

    private CAInstructionalMethod instructionalMethod;
    private Float hours;

    public CAInstructionalMethod getInstructionalMethod() {
        return instructionalMethod;
    }

    public void setInstructionalMethod(CAInstructionalMethod instructionalMethod) {
        this.instructionalMethod = instructionalMethod;
    }

    public Float getHours() {
        return hours;
    }

    public void setHours(Float hours) {
        this.hours = hours;
    }

    @Override
    public String toString() {
        return "CACourseContact{" +
                "instructionalMethod=" + instructionalMethod +
                ", hours=" + hours +
                '}';
    }
}
