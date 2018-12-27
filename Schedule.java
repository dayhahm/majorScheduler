/**
 * This class creates a schedule for a student given a major requirements text file and a tgf
 * file of the major class dependencies.
 * Kate and Debbie shared the implemenation of this class.
 * @author Kate Lin and Debbie Hahm
 * @version 12.11.18
 */

import java.util.*;
import java.io.*;
import javafoundations.*;

public class Schedule
{
    private AdjListsGraph<Course> majorGraph;
    private ArrayQueue<Course> schedule;
    private HashMap<String, String> reqs;
    private String major, hashFile;
    private int units, numElectives;
    private Vector<Course> skipped;

    /**
     * Constructor for objects of class Schedule.
     * 
     * @param   graphFile    name of the tgf file for the graph
     * @param   hfile        name of the txt file for the requirements hashmap
     */
    public Schedule(String graphFile, String hFile) {
        major = "";
        majorGraph = AdjListsGraphFile(graphFile);
        units = 0;

        hashFile = hFile;
        reqs = txtFileToHashMap();

        skipped = new Vector<Course>();
        numElectives = units;

        Iterator<String> values = reqs.values().iterator();
        Vector<String> groupCheck = new Vector<String>();
        int count = 0;
        while (values.hasNext()) {
            String value = values.next();
            if (isNumeric(value)) {
                numElectives -= Integer.parseInt(value);
            }
            //case of group reqs
            else {
                if (!groupCheck.contains(value)) {
                    numElectives -= value.length();
                    groupCheck.add(value);
                }
            }

        }
        schedule = new ArrayQueue<Course>();
    }

    //-------------------FILE READING METHODS-----------------------------

    /** 
     * Creates and returns a new graph using the given file. If the file
     * cannot be found, an error message is printed
     * 
     * @param   tgf_file_name   name of the tgf file
     * @return                  a graph according to the given file
     */
    public static AdjListsGraph<Course> AdjListsGraphFile(String tgf_file_name) {
        AdjListsGraph<Course> g = new AdjListsGraph<Course>();
        try{ // to read from the tgf file
            Scanner scanner = new Scanner(new File(tgf_file_name));
            //read vertices
            while (!scanner.next().equals("#")){
                String courseAndDescription = scanner.nextLine();
                String[] splitLine = courseAndDescription.split(":");
                String skip =  scanner.nextLine().trim();
                if (skip.equals("Can be skipped")) {
                    g.addVertex(new Course(splitLine[0].trim(), splitLine[1].trim(), true));
                }
                else {
                    g.addVertex(new Course(splitLine[0].trim(), splitLine[1].trim())); 
                }
            }
            //read arcs
            while (scanner.hasNext()){
                int from = scanner.nextInt();
                int to = scanner.nextInt();
                g.addArc(from-1, to-1);
            }
            scanner.close();
        } catch (IOException ex) {
            System.out.println("The file was not found: " + ex);
        }
        return g;
    }

    /** 
     * Creates a HashMap out of a txt file. The key is the name of the class and the 
     * value the number of times that class has to be taken.
     * 
     * @return   a hashmap with the key as the name of the class and value as the amount of the class
     */
    public HashMap<String, String> txtFileToHashMap() {
        HashMap<String, String> reqs = new HashMap<String, String>();

        try {
            Scanner scan = new Scanner(new File(hashFile));
            major = scan.nextLine();    //first line of file is department name
            units = scan.nextInt();     //second line of file is number of total units needed for major
            scan.nextLine();
            while (scan.hasNext()) {
                String line = scan.nextLine();
                String[] splitLine = line.split(":");

                reqs.put(splitLine[0], splitLine[1]);
            }
        } catch (FileNotFoundException x) {
            System.out.println(x);
        }

        return reqs;
    }

    //-------------------GRAPH TRAVERSAL METHODS-----------------------------

    /** 
     * Performs a randomized breadth-first search traversal of a graph starting 
     * from the given start point, startV. Enqueues points of traversal into 
     * schedule queue. Returns a boolean array of which nodes have been visited.
     * 
     * @param   startV   the start point for traversal
     * @param   visited  a boolean array keeping track of which nodes have been visited
     * @return           boolean array of traversed courses
     */
    public boolean[] queueBFS(Course startV, boolean[] visited) {
        Random r = new Random();
        LinkedQueue<Course> q = new LinkedQueue<Course>();

        q.enqueue(startV);
        schedule.enqueue(startV);
        visited[majorGraph.getIndexOfVertex(startV)] = true;
        if (startV.equals(findSmallestReq())) {
            fulfillsNormalReq(startV);
        }

        while (!q.isEmpty()) { 
            Course point = q.dequeue();
            int indexPoint = majorGraph.getIndexOfVertex(point);
            int numNeighbors = majorGraph.getArc(indexPoint).size();

            Vector<Integer> indices = new Vector<Integer>();
            for (int i = 0; i < numNeighbors; i++) {
                if (!visited[majorGraph.getIndexOfVertex(majorGraph.getArc(indexPoint).get(i))]) {
                    indices.add(majorGraph.getIndexOfVertex(majorGraph.getArc(indexPoint).get(i)));
                }
            }
            int size = indices.size();
            for (int i = 0; i < size; i++) {
                int indexAdj = indices.remove(r.nextInt(indices.size()));
                Course adjacentPoint = majorGraph.getVertex(indexAdj);

                if (!visited[indexAdj]) {      
                    visited[indexAdj] = true;
                    if (fulfillsNormalReq(adjacentPoint)) {
                        q.enqueue(adjacentPoint);
                        schedule.enqueue(adjacentPoint);
                    }

                    else if (fulfillsOtherReq(adjacentPoint)) {
                        double probs = r.nextDouble();
                        if (probs > (1 / majorGraph.getNumVertices())) {
                            q.enqueue(adjacentPoint);
                            schedule.enqueue(adjacentPoint);
                        }
                    }

                    else if (numElectives != 0) {
                        double probs = r.nextDouble();
                        if (probs > (1 / majorGraph.getNumVertices())) {
                            numElectives--;
                            q.enqueue(adjacentPoint);
                            schedule.enqueue(adjacentPoint);
                        }
                    }
                }
            }
        }

        return visited;
    }

    /** 
     * Getter for name of the major
     * 
     * @return   name of the major
     */
    public String getMajor() {
        return major;
    }

    /** 
     * Setter for name of the major
     * 
     * @param   newMajor   new major
     */
    public void setMajor(String newMajor) {
        major = newMajor;
    }

    /** 
     * Checks if a string contains only numbers using try-catch exceptions.
     * 
     * @param   str   the string to check
     * @return      boolean representation of whether or not the string is numeric
     */
    public static boolean isNumeric(String str)  {  
        try  
        {  
            int num = Integer.parseInt(str);
            return true;
        }  
        catch(NumberFormatException nfe)  
        {  
            return false;  
        }  
    }

    /** 
     * Finds the lowest level course out of all the requirements and returns that course.
     * 
     * @return      lowest level requirement
     */
    public Course findSmallestReq() {
        Random r = new Random();
        HashMap<String, String> map = txtFileToHashMap();
        Iterator<String> itr = map.keySet().iterator();
        Course c;

        String smallest = itr.next();
        while (itr.hasNext()) {
            if (isNumeric(smallest.substring(smallest.length() - 3))) {
                int smallestCourseNum = Integer.parseInt(smallest.substring(smallest.length() - 3));
                String temp = itr.next();
                if (isNumeric(temp.substring(temp.length() - 3))) {
                    int tempNum = Integer.parseInt(temp.substring(temp.length() - 3));
                    if (tempNum < smallestCourseNum) {
                        smallest = temp;
                    }
                }
            }
            else {
                //System.out.println(majorGraph.getNumVertices());
                int randomIndex = r.nextInt(majorGraph.getNumVertices());
                //System.out.println(randomIndex);
                return majorGraph.getVertex(randomIndex);
            }
        }

        for  (int i = 0; i < majorGraph.getNumVertices(); i++) {
            if (majorGraph.getVertex(i).getCourseName().equals(smallest)) {
                return majorGraph.getVertex(i);
            }
        }

        return null;
    }

    /** 
     * Checks if a course fulfills a required course for a major for courses that are
     * explicitly mentioned in the major requirement. This includes courses that are
     * part of a group of courses required for the major (ie take 2 of 200, 220, 225, or 255).
     * If it fulfills a requirement, it updates the hashmap containing the requirements for the major.
     *
     * @param   c   the course to check
     * @return      boolean representation of whether or not the course fills a major requirement.
     */
    public boolean fulfillsNormalReq(Course c) {
        String courseName = c.getCourseName();
        if (reqs.containsKey(courseName)) {
            String coursesLeft = reqs.get(courseName);
            if (isNumeric(coursesLeft)) { //if it's a single required course for the major
                reqs.remove(courseName);
            } else { //if it's one of a group of classes that's required for the major
                int numLeft = coursesLeft.length();
                if (numLeft > 1) { // if more than one course left needed in the group, decrement by going from sth like AAA --> AA 
                    reqs.replace(courseName, coursesLeft.substring(0, numLeft-1));
                } else if (numLeft == 1) {
                    reqs.remove(courseName);
                }
            }
            return true;
        }
        return false;
    }

    /** 
     * Checks if a course fulfills a required group of courses for the major. This includes
     * taking a 200 level course (2XX), a 300 level course (3XX), or a 200 OR 300 level course (2ZZ).
     * If it fulfills a requirement, it updates the hashmap containing the requirements for the major.
     *
     * @param   c   the course to check
     * @return      boolean representation of whether or not the course fills a major requirement.
     */
    public boolean fulfillsOtherReq(Course c) {
        String courseName = c.getCourseName();
        courseName = courseName.substring(0, courseName.length() - 2);
        String xx = courseName + "XX";
        String zz = courseName.substring(0, courseName.length()-1) + "2ZZ"; //checks if department name + 2ZZ is in the hashmap
        if (reqs.containsKey(xx)) { //checking for if it's fulfilling a 2XX or 3XX requirement
            int numCoursesLeft = Integer.parseInt(reqs.get(xx));
            if (numCoursesLeft > 1) {
                reqs.replace(xx, Integer.toString(numCoursesLeft-1));
            } else if (numCoursesLeft == 1) {
                reqs.remove(xx);
            }
            return true;
        }
        else if (reqs.containsKey(zz)) {
            int numCoursesLeft = Integer.parseInt(reqs.get(zz));
            if (numCoursesLeft > 1) {
                reqs.replace(zz, Integer.toString(numCoursesLeft-1));
            } else if (numCoursesLeft == 1) {
                reqs.remove(zz);
            }
            return true;
        }
        return false;
    }

    /** 
     * Traverses the graph using fillReqs to make sure that each course is visited and 
     * has its requirement filled in.
     * 
     */
    public void fillAllReqs() {
        boolean[] visited = new boolean[majorGraph.getNumVertices()];

        visited = fillReqs(findSmallestReq(), visited);   //fills in reqs for what is probably the largest, connected component of the graph
        for (int i = 0; i < majorGraph.getNumVertices(); i++) {
            if (!visited[i]) {
                visited = fillReqs(majorGraph.getVertex(i), visited);
            }
        }
    }

    /** 
     * Traverses a graph from the given start point and fills in the requirement for
     * each class that it visits.
     * 
     * @param   startV   the start point for traversal
     * @param   visited  a boolean array keeping track of which nodes have been visited
     * @return           boolean array of traversed courses
     */
    public boolean[] fillReqs(Course startV, boolean[] visited) {
        ArrayStack<Course> stk = new ArrayStack<Course>();

        stk.push(startV);
        visited[majorGraph.getIndexOfVertex(startV)] = true;

        while (!stk.isEmpty()) {
            Course point = stk.peek();
            LinkedList<Course> neighbors = majorGraph.getArc(majorGraph.getIndexOfVertex(point));
            Vector<Integer> indices = new Vector<Integer>();

            for (int i = 0; i < neighbors.size(); i++) {
                if (!visited[majorGraph.getIndexOfVertex(neighbors.get(i))]) {
                    indices.add(majorGraph.getIndexOfVertex(neighbors.get(i)));
                }
            }

            if (indices.size() == 0) {
                stk.pop();
            }

            else {
                Course neighbor = majorGraph.getVertex(indices.remove(0));

                stk.push(neighbor);

                neighbor.setReq(point.getCourseName());
                visited[majorGraph.getIndexOfVertex(neighbor)] = true;
            }
        }

        return visited;
    }

    /** 
     * Returns an ArrayList containing all the classes a student can skip/test out of.
     * 
     * @return           ArrayList representation all skippable classes
     */
    public ArrayList findSkippableClasses() {
        Random r = new Random();
        ArrayList skippable = new ArrayList();
        LinkedQueue<Course> q = new LinkedQueue<Course>();
        boolean[] visited = new boolean[majorGraph.getNumVertices()];  //initialized to false

        Course startV = findSmallestReq();
        q.enqueue(startV);
        if (startV.getCanBeSkipped()) {
            skippable.add(startV);
        }

        visited[majorGraph.getIndexOfVertex(startV)] = true;

        //System.out.println(startV);

        while (!q.isEmpty()) { 
            Course point = q.dequeue();
            int indexPoint = majorGraph.getIndexOfVertex(point);
            int numNeighbors = majorGraph.getArc(indexPoint).size();

            for (int i = 0; i < numNeighbors; i++) {
                Course adjacentPoint = majorGraph.getArc(indexPoint).get(i);
                int indexAdj = majorGraph.getIndexOfVertex(adjacentPoint);

                if (!visited[indexAdj]) {
                    q.enqueue(adjacentPoint);
                    if (adjacentPoint.getCanBeSkipped()) {
                        skippable.add(adjacentPoint);
                    }
                    visited[indexAdj] = true;
                }
            }
        }
        return skippable;
    }

    public void skipClasses(Course startCourse) {
        if (!startCourse.getReq().equals("")) {
            Course req = new Course (startCourse.getReq(), "");
            System.out.println(req);
            skipped.add(req);
            skipClasses(req);
        }
    }

    /** 
     * Creates a schedule using queueBFS, starting from the given start point. Also fills in any 
     * remaining units that are not covered by the requirements.
     * 
     * @param   start   starting point for the traversal
     */
    public void createASchedule(Course skipToClass) {
        Course start = findSmallestReq();
        Random r = new Random();
        boolean[] visited = new boolean[majorGraph.getNumVertices()];
        ArrayList<Course> skippables = findSkippableClasses();
        for (int i = 0; i < skippables.size(); i++) {
            if (skippables.get(i).equals(skipToClass)) {
                skipToClass = skippables.get(i);
            }
        }

        skipClasses(skipToClass);
        numElectives += skipped.size();

        visited = queueBFS(start, visited);
        for (int i = 0; i < majorGraph.getNumVertices(); i++) {
            if ((!visited[i]) && (majorGraph.getVertex(i).getReq() == null)) {
                visited = queueBFS(majorGraph.getVertex(i), visited);
            }
        }

        if (schedule.size() != units) {
            while (reqs.size() != 0) {
                Course randomCourse = majorGraph.getVertex(r.nextInt(majorGraph.getNumVertices()));
                while (visited[majorGraph.getIndexOfVertex(randomCourse)]) {
                    randomCourse = majorGraph.getVertex(r.nextInt(majorGraph.getNumVertices()));
                }
                if (fulfillsNormalReq(randomCourse)) {
                    schedule.enqueue(randomCourse);
                }

                else if(fulfillsOtherReq(randomCourse)) {
                    schedule.enqueue(randomCourse);
                }
            }
            int size = schedule.size();
            int i = 0;
            while(numElectives != 0) {
                Course randomCourse = majorGraph.getVertex(r.nextInt(majorGraph.getNumVertices()));
                while (visited[majorGraph.getIndexOfVertex(randomCourse)]) {
                    randomCourse = majorGraph.getVertex(r.nextInt(majorGraph.getNumVertices()));
                }

                if (randomCourse.getReq().equals("")){
                    numElectives--;
                    schedule.enqueue(randomCourse);
                }

                else {
                    Course randomCourseReq = new Course(randomCourse.getReq(), "");
                    if (visited[majorGraph.getIndexOfVertex(randomCourseReq)]) {
                        numElectives--;
                        schedule.enqueue(randomCourse);
                    }
                }
            }
        }

        int skipSize = skipped.size();
        for (int k = 0; k < skipSize; k++) {
            System.out.println(skipped.get(k));
        }

        int size = schedule.size();
        ArrayQueue<Course> temp = new ArrayQueue<Course>();
        for (int j = 0; j < size; j++) {
            Course current = schedule.dequeue();
            if (!skipped.contains(current)) {
                temp.enqueue(current);
            }
        }

        schedule = temp;
    }

    /** 
     * Returns an ArrayList containing a single semester's worth of classes.
     * 
     * @param   sched   the full schedule of classes form which do dequeue
     * @param   numSem  the number of semesters a student will have
     * @return           ArrayList representation of a semester
     */
    public ArrayList getSem(ArrayQueue<Course> sched, int numSem) {
        int numCourse = sched.size()/numSem; // min num courses needed per semester
        ArrayList semester = new ArrayList(numCourse);
        for (int i = 0; i < sched.size(); i++) {
            if (sched.size() != 0) {
                Course curr = sched.first();
                Course req = new Course(curr.getReq(), "");
                if (!semester.contains(req)) {
                    semester.add(curr);
                } else {
                    sched.dequeue();
                    return semester;
                }
            }
        }
        return semester;
    }

    public ArrayList<Course> getStartingCourses() {
        HashMap<String, String> map = txtFileToHashMap();
        Iterator<String> itr = map.keySet().iterator();
        ArrayList<Course> courses = new ArrayList<>();
        ArrayList<String> courseNames = new ArrayList<>();

        String cur = null;
        while (itr.hasNext()) {
            cur = itr.next();
            if (cur.length() > 3) {
                if (isNumeric(cur.substring(2, 3))) {
                    int num = Integer.parseInt(cur.substring(2, 3));
                    if (num == 1) {
                        courseNames.add(cur);
                    }
                }
            }
        }

        for  (int i = 0; i < majorGraph.getNumVertices(); i++) {
            for (int j=0; j<courseNames.size(); j++) {
                if (majorGraph.getVertex(i).getCourseName().equals(courseNames.get(j))) {
                    courses.add(majorGraph.getVertex(i));
                }
            }
        }
        return courses;
    }

    public ArrayList<Course> getCourses() {
        ArrayList<Course> courses = new ArrayList<Course>();
        ArrayQueue<Course> temp = new ArrayQueue<Course>();
        System.out.println(schedule.size());
        while (!schedule.isEmpty()) {
            Course c = schedule.dequeue();
            courses.add(c);
            temp.enqueue(c);
        }

        schedule = temp;
        return courses;
    }


    /** 
     * Creates and returns a string representation of a schedule.
     * 
     * @return           string representation of a schedule
     */
    public String toString() {
        String s = "";
        ArrayQueue<Course> temp = new ArrayQueue<Course>();
        //System.out.println(schedule.size());
        while (!schedule.isEmpty()) {
            Course c = schedule.dequeue();
            s += c + "\n";
            temp.enqueue(c);
        }

        schedule = temp;
        return s;
    }
}
