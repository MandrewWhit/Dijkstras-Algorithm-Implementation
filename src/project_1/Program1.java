/*
 * Name: Michael Whitaker
 * EID: maw5299
 */
package project_1;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

/**
 * Your solution goes in this class.
 * 
 * Please do not modify the other files we have provided for you, as we will use
 * our own versions of those files when grading your project. You are
 * responsible for ensuring that your solution works with the original version
 * of all the other files we have provided for you.
 * 
 * That said, please feel free to add additional files and classes to your
 * solution, as you see fit. We will use ALL of your additional files when
 * grading your solution.
 */
public class Program1 extends AbstractProgram1 {
	

    /**
     * Determines whether a candidate Matching represents a solution to the Stable Marriage problem.
     * Study the description of a Matching in the project documentation to help you with this.
     */
    @Override
    public boolean isStableMatching(Matching marriage) {
        /* TODO implement this function */
    	boolean stable = true;
    	
	    	for(int i=0;i<marriage.getStudentMatching().size();i++) {
	    		if(marriage.getStudentMatching().get(i)!=-1) {
	    			//loop through each students match in the list
		    		int stuPrefIndex = 0;//current index of pref in student's pref list
		    		while(stuPrefIndex<marriage.getStudentPreference().get(i).indexOf(marriage.getStudentMatching().get(i)) && marriage.getStudentPreference().get(i).get(stuPrefIndex)!=marriage.getStudentMatching().get(i)) {
		    			//get index of student preferred residency is matched to
		    			int currentRes = marriage.getStudentPreference().get(i).get(stuPrefIndex);//current residency to check
		    			for(int j=0;j<marriage.getStudentMatching().size();j++) {
		    				if(i!=j) {
			    				if(marriage.getStudentMatching().get(j)==currentRes) {
			    					//if currentRes prefers i to j, there is an instability
			    					boolean leave = false;
			    					for(int k=0;k<marriage.getResidencyPreference().get(currentRes).size();k++) {
			    						if(stable && !leave) {
			    							if(marriage.getResidencyPreference().get(currentRes).get(k)==i) {
			    								stable = false;
			    								System.out.println("residency " + currentRes + " prefers student " + i + " to student " + j);
			    								System.out.println(marriage.getResidencyPreference().get(currentRes).indexOf(i));
			    								System.out.println(marriage.getResidencyPreference().get(currentRes).indexOf(j));
			    								System.out.println(marriage.getStudentPreference().get(i).indexOf(currentRes));
			    								System.out.println(marriage.getStudentPreference().get(i).indexOf(marriage.getStudentMatching().get(i)));
			    							}
			    							if(marriage.getResidencyPreference().get(currentRes).get(k)==j) {
			    								leave = true;
			    							}
			    						}
			    					}
			    				}
		    				}
		    			}
		    			stuPrefIndex++;
		    		}
		    		if(!stable) {
		    			return false;
		    		}
	    		}else {
	    			for(int j=0;j<marriage.getStudentPreference().get(i).size();j++) {
	    				int currentRes = marriage.getStudentPreference().get(i).get(j);
	    				boolean notFound = true;
	    				for(int k=0;k<marriage.getResidencyPreference().get(currentRes).size();k++) {
	    					if(notFound) {
	    						if(marriage.getResidencyPreference().get(currentRes).get(k)==i) {
	    							return false;
	    						}
	    						if(marriage.getResidencyPreference().get(currentRes).get(k)==marriage.getStudentMatching().indexOf(currentRes)) {
	    							notFound = false;
	    						}
	    					}
	    				}
	    			}
	    		}
	    	}
	    	return true;
    	

    }

    /**
     * Determines a solution to the Stable Marriage problem from the given input set. Study the
     * project description to understand the variables which represent the input to your solution.
     *
     * @return A stable Matching.
     */
    @Override
    public Matching stableMarriageGaleShapley_studentoptimal(Matching marriage) {
        /* TODO implement this function */
    	boolean notStable = true;
    	int[] residencySlotsFilled = new int[marriage.getResidencyCount()];
    	int size = marriage.getStudentCount();
    	//initialize student matching and queue of unmatched students
    	ArrayList<Integer> studentMatching = new ArrayList<Integer>();
    	ArrayList<Integer> studentQueue = new ArrayList<Integer>();
    	ArrayList<ArrayList<Integer>> residencyMatches = new ArrayList<ArrayList<Integer>>();
    	for(int i=0;i<size;i++) {
    		studentMatching.add(i, -1);
    		studentQueue.add(i);
    		ArrayList<Integer> r = new ArrayList<Integer>();
    		residencyMatches.add(r);
    	}
    	//create data structure where each residency is mapped to a pref array
    	ArrayList<Integer[]> prefs = new ArrayList<Integer[]>();
    	for(int i=0;i<marriage.getResidencyCount();i++) {
    		Integer a[] = new Integer[marriage.getResidencyPreference().get(i).size()];
    		for(int j=0;j<marriage.getResidencyPreference().get(i).size();j++) {
    			a[marriage.getResidencyPreference().get(i).get(j)] = j;
    		}
    		prefs.add(a);
    	}
    	
    	marriage.setStudentMatching(studentMatching);
    	
    	while(!studentQueue.isEmpty()) {
    		//find a student who doesn't have a match
    		Integer findUnmatchedStudent = 0; // index of match for each student
    		Integer index = 0;// index of current student in list
    		boolean notFound = true;
    		//get next student to match
    		findUnmatchedStudent = studentQueue.get(0);
    		
    		
    		
    			//match student to residency
    			boolean notMatched = true;
    			boolean endOfPreferenceList = false;
    			int preferenceIndex = 0; //index of current residency targeted by student
    			while(notMatched && (!endOfPreferenceList)) {
    				//propose to i residency in preference list
    				int currentResIndex = marriage.getStudentPreference().get(findUnmatchedStudent).get(preferenceIndex);
    				ArrayList<Integer> l = marriage.getResidencySlots();// l is the list of residency slots
    				int slots = l.get(currentResIndex);
    				if(slots>residencySlotsFilled[currentResIndex]) {
    					residencySlotsFilled[currentResIndex] = residencySlotsFilled[currentResIndex] + 1;
    					ArrayList<Integer> sm = marriage.getStudentMatching();
    					sm.set(findUnmatchedStudent, currentResIndex);
    					marriage.setStudentMatching(sm);
    					studentQueue.remove(findUnmatchedStudent);
    					residencyMatches.get(currentResIndex).add(findUnmatchedStudent);
    					notMatched = false;
    				}else {
    					//compare residency rankings of current student and students already matched
    					boolean replaced = false;
    					//while(!replaced) {
	    					for(int i=0;i<residencyMatches.get(currentResIndex).size();i++) {
	    						//loop through student matching to find students already matched to residency
	    						//if(marriage.getStudentMatching().get(i)==currentResIndex) {
	    							//now loop through residency preference list and see who is higher
	    							int pIndex = 0; //index of current position in residency preference list
	    							boolean encountered = false; //if find one of the students leave loop
	    							if(!replaced) {
		    							if(prefs.get(currentResIndex)[findUnmatchedStudent]<prefs.get(currentResIndex)[residencyMatches.get(currentResIndex).get(i)]) {
		    								ArrayList<Integer> sm = marriage.getStudentMatching();
	    			    					sm.set(findUnmatchedStudent, currentResIndex);
	    			    					sm.set(residencyMatches.get(currentResIndex).get(i), -1);
	    			    					marriage.setStudentMatching(sm);
	    			    					studentQueue.remove(0);
	    			    					studentQueue.add(residencyMatches.get(currentResIndex).get(i));
	    			    					notMatched = false;
	    			    					residencyMatches.get(currentResIndex).add(findUnmatchedStudent);
	    			    					residencyMatches.get(currentResIndex).remove(residencyMatches.get(currentResIndex).get(i));
	    			    					replaced = true;
		    							}
	    							}
	    							
	    						}
	    					}
    					//}
    					
    				//}
    				preferenceIndex++;
    				if(preferenceIndex>=marriage.getStudentPreference().get(findUnmatchedStudent).size()) {
    					endOfPreferenceList = true;
    					ArrayList<Integer> sm = marriage.getStudentMatching();
    					if(sm.get(findUnmatchedStudent)==-1) {
    						sm.set(findUnmatchedStudent, -2);
        					marriage.setStudentMatching(sm);
        					studentQueue.remove(0);
    					}
    				}
    			}
    		}
    	//}
    	ArrayList<Integer> sm = marriage.getStudentMatching();
    	for(int i=0;i<sm.size();i++) {
    		if(sm.get(i)==-2) {
    			sm.set(i, -1);
    		}
    	}
		marriage.setStudentMatching(sm);
        return marriage;
    }

    /**
     * Determines a solution to the Stable Marriage problem from the given input set. Study the
     * project description to understand the variables which represent the input to your solution.
     *
     * @return A stable Matching.
     */
    @Override
    public Matching stableMarriageGaleShapley_residencyoptimal(Matching marriage) {
        /* TODO implement this function */
    	boolean notStable = true;
    	int[] residencySlotsFilled = new int[marriage.getResidencyCount()];
    	int size = marriage.getStudentCount();
    	ArrayList<Integer> studentMatching = new ArrayList<Integer>();
    	for(int i=0;i<size;i++) {
    		studentMatching.add(i, -1);
    	}
    	
    	//create lookup pref data structure
    	ArrayList<Integer[]> prefs = new ArrayList<Integer[]>();
    	for(int i=0;i<marriage.getStudentCount();i++) {
    		Integer a[] = new Integer[marriage.getStudentPreference().get(i).size()];
    		for(int j=0;j<marriage.getStudentPreference().get(i).size();j++) {
    			a[marriage.getStudentPreference().get(i).get(j)] = j;
    		}
    		prefs.add(a);
    	}
    	
    	marriage.setStudentMatching(studentMatching);
    	
    	while(notStable) {
    		notStable = false;
    		int numResidencies = marriage.getResidencyCount();
    		for(int i=0;i<numResidencies;i++) {
    			//loop through all residencies and have them propose to students if empty slots remain
    			if(residencySlotsFilled[i]<marriage.getResidencySlots().get(i)) {
    				//propose to top rated student in residency i's list
    				for(int j=0;j<marriage.getResidencyPreference().get(i).size();j++) {
    					if(residencySlotsFilled[i]<marriage.getResidencySlots().get(i)) {
    						//check if current best candidate is already assigned to a residency
    						int currentStudentIndex = marriage.getResidencyPreference().get(i).get(j);
    						if(marriage.getStudentMatching().get(currentStudentIndex)==-1) {
    							residencySlotsFilled[i] = residencySlotsFilled[i] + 1;
    							notStable = true;
    							//match student with current residency
    							marriage.getStudentMatching().set(currentStudentIndex, i);
    						}else {
    							//look and see if current student prefers current res or the one they are already assigned to
    							int currentAssignedRes = marriage.getStudentMatching().get(currentStudentIndex);
    							//loop through current student's prefs
    							boolean foundRes = false;
    							if(prefs.get(currentStudentIndex)[i]<prefs.get(currentStudentIndex)[marriage.getStudentMatching().get(currentStudentIndex)]) {
    								notStable = true;
									residencySlotsFilled[i] = residencySlotsFilled[i] + 1;
									residencySlotsFilled[currentAssignedRes] = residencySlotsFilled[currentAssignedRes] - 1;
									marriage.getStudentMatching().set(currentStudentIndex, i);
    							}
    							
    						}
    					}
    				}
    			}
    		}
    	}
    	
    	
      
        return marriage;
    }
}