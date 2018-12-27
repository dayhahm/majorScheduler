/**
 * Representation of a course at Wellesley, describing the course's content, its prerequisites, and 
 * whether or not the course can be skipped.
 *
 * @author Debbie
 * @version 1.0
 */
public class Course
{
    private String courseName;
    private String description;
    private String req;
    private boolean canBeSkipped;

    /**
     * Constructor for objects of class Course. Takes two parameters. Assumes that this course
     * cannot be skipped
     * 
     * @param   name    name of the course
     * @param   desc    description of the course
     */
    public Course(String name, String desc)
    {
        courseName = name;
        String[] descSplit = desc.split(":");
        description = String.join(" ", descSplit);
        req = "";
        canBeSkipped = false;
    }
    
    /**
     * Constructor for objects of class Course. Takes three parameters.
     * 
     * @param   name    name of the course
     * @param   desc    description of the course
     * @param   skip    if the course can be skipped or not
     */
    public Course(String name, String desc, boolean skip)
    {
        courseName = name;
        description = desc;
        req = "";
        canBeSkipped = skip;
    }
    
    /**
     * Getter for courseName
     * 
     * @return    course name
     */
    public String getCourseName()
    {
        return courseName;
    }
    
    /**
     * Setter for courseName
     * 
     * @param   newName   new course number
     */
    public void setCourseName(String newName)
    {
        courseName = newName;
    }
    
    /**
     * Getter for description
     * 
     * @return    description
     */
    public String getDescription()
    {
        return description;
    }
    
    /**
     * Setter for description
     * 
     * @param   newDescription   new course description
     */
    public void setDescription(String newDescription)
    {
        description = newDescription;
    }
    
    /**
     * Getter for req
     * 
     * @return    requirement, or if no requirement an empty string
     */
    public String getReq()
    {
        return req;
    }
    
    /**
     * Setter for req
     * 
     * @param   newReq   requirement for course
     */
    public void setReq(String newReq)
    {
        req = newReq;
    }
    
    /**
     * Getter for canBeSkipped
     * 
     * @return    if you can skip this course
     */
    public Boolean getCanBeSkipped()
    {
        return canBeSkipped;
    }
    
    /**
     * Setter for canBeSkipped
     * 
     * @param   skip   if you can skip a course or not
     */
    public void setReq(boolean skip)
    {
        canBeSkipped = skip;
    }
    
    /**
     * Determins if the course is equal to another course, c1. If the names of the courses
     * are the same, the courses are considered equal.
     * 
     * @param   c1   course you are comparing the current course to
     * @return       true, if the name of the two are the same, false otherwise
     */
    public boolean equals(Object c1) {
        if (c1 instanceof Course) {
            Course c = (Course) (c1);
            if (this.getCourseName().equals(c.getCourseName())) {
                return true;
            }
        }
            
        return false;
    }
    
    /**
     * Returns a string representation of the Course class
     * 
     * @return      formated string representation
     */
    public String toString() {
        String s = courseName + ": " + description;
        String canSkip = "";
        if (canBeSkipped) {
            return s + "\n\tCan be skipped";
        }
        
        return s + "\n\tCannot be skipped";
    }
}