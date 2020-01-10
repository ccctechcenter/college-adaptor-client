package org.ccctc.collegeadaptor.exceptions

/**
 * Errors that may be returned by the College Adaptor and stored in the 'code' field of a CollegeAdaptorException.
 */
class CAErrors {

    // General errors
    static String noResultsFound = "noResultsFound"
    static String multipleResultsFound = "multipleResultsFound"
    static String invalidSearchCriteria = "invalidSearchCriteria"

    // Errors for posting enrollments
    static String studentNotEnrollable = "studentNotEnrollable"
    static String studentExists = "studentExists"
    static String generalEnrollmentError = "generalEnrollmentError"
    static String studentNotFound = "studentNotFound"
    static String studentOrTermNotFound = "studentOrTermNotFound"
    static String sisQueryError = "sisQueryError"
    static String sectionNotFound = "sectionNotFound"
    static String sectionExists = "sectionExists"
    static String courseNotFound = "courseNotFound"
    static String courseExists = "courseExists"
    static String alreadyEnrolled = "alreadyEnrolled"
    static String enrollmentNotFound = "enrollmentNotFound"
    static String termExists = "termExists"
    static String termNotFound = "termNotFound"

    // Errors for canvas integration
    static String personNotFound = "personNotFound"

    // Errors in POST for entity creation
    static String entityAlreadyExists = "entityAlreadyExists"

    // Errors for posting assessments
    static String invalidTestName = "invalidTestName"
    static String invalidTestMapping = "invalidTestMapping"
    static String invalidPlacementCourse = "invalidPlacementCourse"

    // Errors for Student's Cohort
    static String cohortConflict = "cohortConflict"
    static String cohortNotFound = "cohortNotFound"
    static String cohortInvalid = "cohortInvalid"
}
