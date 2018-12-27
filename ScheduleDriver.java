
/**
 * Write a description of class ScheduleDriver here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
import java.util.*;
import javafoundations.*;
public class ScheduleDriver
{
    public static void main(String[] args) {
        Schedule cs = new Schedule("graphs/csgraph.tgf", "reqs/csreqs.txt");
        cs.fillAllReqs();
        Course cs230 = new Course("CS230", "An introduction to techniques and building blocks for organizing large programs. Topics include modules, abstract data types, recursion, algorithmic efficiency, and the use and implementation of standard data structures and algorithms, such as lists, trees, graphs, stacks, queues, priority queues, tables, sorting, and searching. Students become familiar with these concepts through weekly programming assignments using the Java programming language. Students are required to attend an additional two-hour laboratory section each week.", true);
        cs.createASchedule(cs230);
        System.out.println(cs);
        
        // Schedule econ = new Schedule("graphs/econgraph.tgf", "reqs/econreqs.txt");
        // econ.fillAllReqs();
        // econ.createASchedule();
        // System.out.println(econ);
    }
}
