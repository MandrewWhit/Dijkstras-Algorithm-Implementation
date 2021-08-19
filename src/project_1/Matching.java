package project_1;

import java.util.ArrayList;

/**
 * A Matching represents a candidate solution to the Stable Marriage problem. A Matching may or may
 * not be stable.
 */
public class Matching {
    /**
     * Number of residencys.
     */
    private Integer m;

    /**
     * Number of students.
     */
    private Integer n;

    /**
     * A list containing each residency's preference list.
     */
    private ArrayList<ArrayList<Integer>> residency_preference;

    /**
     * A list containing each student's preference list.
     */
    private ArrayList<ArrayList<Integer>> student_preference;

    /**
     * Number of slots available in each residency.
     */
    private ArrayList<Integer> residency_slots;

    /**
     * Matching information representing the index of residency a student is matched to, -1 if not
     * matched.
     *
     * <p>An empty matching is represented by a null value for this field.
     */
    private ArrayList<Integer> student_matching;

    public Matching(
            Integer m,
            Integer n,
            ArrayList<ArrayList<Integer>> residency_preference,
            ArrayList<ArrayList<Integer>> student_preference,
            ArrayList<Integer> residency_slots) {
        this.m = m;
        this.n = n;
        this.residency_preference = residency_preference;
        this.student_preference = student_preference;
        this.residency_slots = residency_slots;
        this.student_matching = null;
    }

    public Matching(
            Integer m,
            Integer n,
            ArrayList<ArrayList<Integer>> residency_preference,
            ArrayList<ArrayList<Integer>> student_preference,
            ArrayList<Integer> residency_slots,
            ArrayList<Integer> student_matching) {
        this.m = m;
        this.n = n;
        this.residency_preference = residency_preference;
        this.student_preference = student_preference;
        this.residency_slots = residency_slots;
        this.student_matching = student_matching;
    }

    /**
     * Constructs a solution to the stable marriage problem, given the problem as a Matching. Take a
     * Matching which represents the problem data with no solution, and a student_matching which
     * solves the problem given in data.
     *
     * @param data              The given problem to solve.
     * @param student_matching The solution to the problem.
     */
    public Matching(Matching data, ArrayList<Integer> student_matching) {
        this(
                data.m,
                data.n,
                data.residency_preference,
                data.student_preference,
                data.residency_slots,
                student_matching);
    }

    /**
     * Creates a Matching from data which includes an empty solution.
     *
     * @param data The Matching containing the problem to solve.
     */
    public Matching(Matching data) {
        this(
                data.m,
                data.n,
                data.residency_preference,
                data.student_preference,
                data.residency_slots,
                new ArrayList<Integer>(0));
    }

    public void setStudentMatching(ArrayList<Integer> student_matching) {
        this.student_matching = student_matching;
    }

    public Integer getResidencyCount() {
        return m;
    }

    public Integer getStudentCount() {
        return n;
    }

    public ArrayList<ArrayList<Integer>> getResidencyPreference() {
        return residency_preference;
    }

    public ArrayList<ArrayList<Integer>> getStudentPreference() {
        return student_preference;
    }

    public ArrayList<Integer> getResidencySlots() {
        return residency_slots;
    }

    public ArrayList<Integer> getStudentMatching() {
        return student_matching;
    }

    public int totalResidencySlots() {
        int slots = 0;
        for (int i = 0; i < m; i++) {
            slots += residency_slots.get(i);
        }
        return slots;
    }

    public String getInputSizeString() {
        return String.format("m=%d n=%d\n", m, n);
    }

    public String getSolutionString() {
        if (student_matching == null) {
            return "";
        }

        StringBuilder s = new StringBuilder();
        for (int i = 0; i < student_matching.size(); i++) {
            String str = String.format("Student %d Residency %d", i, student_matching.get(i));
            s.append(str);
            if (i != student_matching.size() - 1) {
                s.append("\n");
            }
        }
        return s.toString();
    }

    public String toString() {
        return getInputSizeString() + getSolutionString();
    }
}
