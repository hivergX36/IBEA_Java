package com.nsga2.nsga2;

import java.util.Comparator;

public class Solution {

    double[] solution;
    double[] SumConstraint;
    float Obj1;
    float Obj2;
    boolean valid;
    boolean fitnessCalculated;
    boolean checkrank;
    int rank;
    float crowding_distance;

    Solution(int NbVariable, int NbConstraint) {
        solution = new double[NbVariable];
        SumConstraint = new double[NbConstraint];
        Obj1 = 0;
        Obj2 = 0;
        valid = true;
        FitnessValue = 0;

    }

    public float getFitness1() {
        return this.FitnessValue1;
    }

    public float getFitness2() {
        return this.FitnessValue2;
    }

    public int getrank() {
        return this.rank;
    }

    public double get_crowding_distance() {
        return this.crowding_distance;
    }

    public static Comparator<Solution> OperatorFitness1 = new Comparator<Solution>() {

        @Override
        public int compare(Solution s1, Solution s2) {
            int value = 0;
            if (s1.getFitness1() > s2.getFitness1()) {
                value = -1;
            } else if(s1.getFitness1() < s2.getFitness1()){
                value = 1;
            } 
            else {
                value = 0;

            }

            return value;

        };

    };

  

  

}
