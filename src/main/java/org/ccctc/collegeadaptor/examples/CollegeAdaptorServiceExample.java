package org.ccctc.collegeadaptor.examples;

import org.ccctc.collegeadaptor.CollegeAdaptorService;
import org.ccctc.collegeadaptor.model.CAEnrollment;
import org.ccctc.collegeadaptor.model.CASection;
import org.ccctc.collegeadaptor.model.CATerm;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Example of the College Adaptor Service
 *
 * @author zrogers
 */
public class CollegeAdaptorServiceExample {

    @Autowired
    CollegeAdaptorService collegeAdaptorService;

    /**
     * Grab a list of terms from a college adaptor and find a term with sections and enrollments to collect some
     * information about.
     *
     * @param misCode MIS Code
     * @return true if example succeeded
     */
    public boolean example(String misCode) {
        int count = 0;

        // Get all terms
        List<CATerm> terms = collegeAdaptorService.getTerms(misCode);
        System.out.println(terms.size() + " terms found");

        // Find a term within the last two years or up to two years into the future that has sectoins
        for (CATerm term : terms) {
            LocalDate startDate = term.getStart().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (startDate.isAfter(LocalDate.now().minus(2, ChronoUnit.YEARS))
                    && startDate.isBefore(LocalDate.now().plus(2, ChronoUnit.YEARS))) {
                List<CASection> sections = collegeAdaptorService.getSectionsByTerm(misCode, term.getSisTermId());
                System.out.println(term.getSisTermId() + " contains " + sections.size() + " sections");

                for(CASection section : sections) {
                    List<CAEnrollment> enrollments = collegeAdaptorService.getEnrollmentsBySection("001",
                            section.getSisTermId(), section.getSisSectionId());

                    System.out.println(section.getSisCourseId() + "-" + section.getSisSectionId() + " has "
                            + enrollments.size() + " enrollments");

                    // Limit to the first 100 sections
                    if (++count >= 100) break;
                }

                if (count > 0) break;
            }
        }

        return (count > 0);
    }
}
