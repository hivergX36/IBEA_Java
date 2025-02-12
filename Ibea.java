package com.nsga2.nsga2;

import java.util.*;
import java.io.*;

public class Ibea
 {

    int NbVariable;
    int NbConstraints;
    int IndCandidat;
    int NbPop;
    int Nbind;
    int NbObj;
    int countGeneration; 



    int[][] Price;
    /*Our  */
    double[][] Indicator_Matrix;
    /* Utility function */

    /* Matrix of constraint */
    double[][] CostMatrix;
    /* Vector of solution */

    double[] constraint;
    Solution[] Population;
    Solution[] Echantillon;
    Solution[] List;

    Ibea(String name, Integer nbpop, Integer nbind) {

        this.Nbind = nbind;
        this.NbPop = nbpop;
        this.Population = new Solution[this.NbPop];

        File input = new File(name);

        try {
            Scanner reader = new Scanner(input);
            String[] nums = reader.nextLine().split(",");
            this.NbVariable = Integer.parseInt(nums[0]);
            this.NbConstraints = Integer.parseInt(nums[1]);
            this.NbObj = Integer.parseInt(nums[2]);
            this.Price = new int[this.NbObj][this.NbVariable];
            this.CostMatrix = new double[this.NbConstraints][this.NbVariable];
            this.constraint = new double[this.NbConstraints];

            for (int i = 0; i < this.NbObj; i++) {
                nums = reader.nextLine().split(",");
                System.out.println(i);

                for (int j = 0; j < this.NbVariable; j++) {
                    System.out.println(j);

                    System.out.println(nums[j]);

                    this.Price[i][j] = Integer.parseInt(nums[j]);
                }
            }

            for (int i = 0; i < this.NbConstraints; i++) {
                nums = reader.nextLine().split(",");

                for (int j = 0; j < this.NbVariable; j++) {
                    this.CostMatrix[i][j] = Float.parseFloat(nums[j]);
                }
            }

            nums = reader.nextLine().split(",");

            for (int i = 0; i < NbConstraints; i++) {
                this.constraint[i] = Float.parseFloat(nums[i]);
            }
            reader.close();

        } catch (IOException e) {
            System.out.println("no file");
        }

    };

    public void displayCostMatrix() {
        for (int i = 0; i < NbConstraints; i++) {
            for (int j = 0; j < NbVariable; j++) {
                System.out.println(this.CostMatrix[i][j] + "\t");

            }

            System.out.println("\nd");
        }
    }

    void sumconstraint(Solution individual) {

        double sum;

        for (int i = 0; i < NbConstraints; i++) {
            individual.SumConstraint[i] = 0;
        }

        for (int j = 0; j < NbConstraints; j++) {
            sum = 0;
            for (int i = 0; i < NbVariable; i++) {
                sum += CostMatrix[j][i] * individual.solution[i];
            }
            individual.SumConstraint[j] = sum;
        }

    }


    void BuildMatrixIndicator(){
        double ChoiceIndicator[] = {0,0,0,0};  
        int populationlength = this.Population.length;
        Indicator_Matrix = new double[populationlength][populationlength];
        for(int j = 0; j < populationlength; j ++){
            for(int i = 0; i < populationlength; i ++){
                ChoiceIndicator[0] = Population[j].Obj1 - Population[i].Obj1;
                ChoiceIndicator[1] = Population[j].Obj2 - Population[i].Obj2; 
                ChoiceIndicator[2] = Population[i].Obj1 - Population[j].Obj1;
                ChoiceIndicator[3] = Population[i].Obj2 - Population[j].Obj2;
            }
            if(ChoiceIndicator[0] > ChoiceIndicator[1]){
                this.Indicator_Matrix[j][i] = ChoiceIndicator[0];
            }
            else{
                this.Indicator_Matrix[j][i] = ChoiceIndicator[1];

            };
            if(ChoiceIndicator[2] > ChoiceIndicator[3]){
                this.Indicator_Matrix[j][i] = ChoiceIndicator[0];
            }
            else{
                this.Indicator_Matrix[j][i] = ChoiceIndicator[1];


            };

        }
    }


    void UpdateFitnessValue(){
        int Nbindividuals = this.Population.length;
        float sum; 
        for(int i = 0; i < Nbindividuals; i++){
            for(int j = 0; i < Nbindividuals; j++){
                sum += -Math.Exp(this.Indicator_Matrix[j][i]);
            }

            this.Population[i].FitnessValue = sum + 1; 

        }

    }


    void EnvironementalSelection(){
        Arrays.sort(this.Population,Solution.OperatorFitnessValue);
         Solution[] list_Population = new Solution[this.NbPop];
        for(int i = 0; i < this.Nbpop; i++ ){
            list_Population[i] = this.Population[i];
        }

        This.Population = list_Population;

    }



    void UpdatePopulation(){
        int nbtotal = nbpop + nbind;
        Solution[] UpdateList = new Solution[nbpop + Nbind];
        for(int i = 0; i < nbpop; i++){
            UpdateList[i] = Population[i];
        }
        for(int j = nbpop; j < nbtotal; j++){
            UpdateList[j] = Echantillon[j - nbpop];

        }

        this.Population = UpdateList;
        this.countGeneration += 1;
    }
    
    
    
    
    
    void RepaireSample(Solution individual) {
        int compteur = 0;
        int indice;
        int choix;
        Random random = new Random();
        List<Integer> listChoix = new ArrayList<Integer>();

        for (int i = 0; i < NbVariable; i++) {
            if (individual.solution[i] > 0) {
                listChoix.add(i);
                compteur++;
            }
        }

        indice = random.nextInt(compteur - 0) + 0;
        choix = listChoix.get(indice);
        individual.solution[choix] = 0;

    }

    int checkPopindividual(Solution individual, int compteur) {
        boolean check = true;

        for (int i = 0; i < this.NbConstraints; i++) {
            System.out.println("La contrainte est: " + this.constraint[i] + "\t");
            if (individual.SumConstraint[i] > this.constraint[i] || individual.SumConstraint[i] == 0) {

                check = false;
                break;
            }
        }

        System.out.println("le booléen est: " + check + "\t");

        if (check == false) {
            System.out.println("La solution n'est pas admissible" + "\t");

        } else {

            System.out.println("La solution est  admissible" + "\t");
            Population[compteur] = individual;
            compteur++;

        }

        return compteur;
    }

    void initPopulation() {
        Random random = new Random();

        int compteur = 0;
        int nbrandomseeds;
        int indice;
        int value;
        while (compteur < NbPop) {
            Solution individual = new Solution(this.NbVariable, this.NbConstraints);
            for (int k = 0; k < this.NbVariable; k++) {
                individual.solution[k] = 0;
            }
            nbrandomseeds = random.nextInt(this.NbVariable - 0) + 0;
            System.out.println(nbrandomseeds + "\t");
            for (int j = 0; j < nbrandomseeds; j++) {
                indice = random.nextInt(this.NbVariable - 0) + 0;
                value = random.nextInt(2 - 0) + 0;
                if (value > 0) {

                    individual.solution[indice] = 1;

                }

            }
            sumconstraint(individual);
            displayIndividual(individual);
            compteur = checkPopindividual(individual, compteur);
        }

    }

    void displayIndividual(Solution individual) {

        System.out.println("La solution créée est: ");
        for (int i = 0; i < NbVariable; i++) {

            System.out.println(individual.solution[i]);

        }

        System.out.println("\t");

        System.out.println("Les contraintes sont: ");
        for (int j = 0; j < NbConstraints; j++) {

            System.out.println(individual.SumConstraint[j]);

        }
        System.out.println("\t");
    }

    void displayPopulation() {
        System.out.println("La population est: ");
        for (int i = 0; i < NbPop; i++) {
            System.out.println("individu_" + i + " : ");
            for (int j = 0; j < NbVariable; j++) {

                System.out.println(this.Population[i].solution[j] + " ");
            }

            System.out.println("\t");

        }

    }

    void displayEchantillon() {
        System.out.println("L'Echantillon est: " + "\t");
        for (int i = 0; i < Nbind; i++) {
            System.out.println("individu_" + i + 1 + " : ");
            for (int j = 0; j < NbVariable; j++) {

                System.out.println(Echantillon[i].solution[j] + " ");
            }

            System.out.println("\t");

        }
    }

    void fitnessValuePop(/* std::vector<Solution> & Echantillon, int dim */) {
        float Obj1;
        float Obj2;
        int compteur = 0;
        while (compteur < NbPop) {
            Obj1 = 0;
            Obj2 = 0;

            if (Population[compteur].fitnessCalculated == false) {
                for (int i = 0; i < NbVariable; i++) {
                    Obj1 += Population[compteur].solution[i] * Price[0][i];
                    Obj2 += Population[compteur].solution[i] * Price[1][i];

                }
                Population[compteur].Obj1 = Obj1;
                Population[compteur].Obj2 = Obj2;

                Population[compteur].fitnessCalculated = true;
            }

            compteur++;

        }
    }

    void fitnessValueSample() {
        float Obj1;
        float Obj2;

        int compteur = 0;
        while (compteur < Nbind) {
            Obj1 = 0;
            Obj2 = 0;

            if (Echantillon[compteur].fitnessCalculated == false) {
                for (int i = 0; i < NbVariable; i++) {
                    Obj1 += Echantillon[compteur].solution[i] * Price[0][i];
                    Obj2 += Echantillon[compteur].solution[i] * Price[0][i];

                }
                Echantillon[compteur].Obj1 = Obj1;
                Echantillon[compteur].Obj2 = Obj2;

                Echantillon[compteur].fitnessCalculated = true;
            }

            compteur++;

        }

    }

    void BinarysTournament() {

        Echantillon = new Solution[this.Nbind];

        Solution individual1;
        Solution individual2;
        int compteur = 0;
        int randomIndividual1;
        int randomIndividual2;
        Random random = new Random();
        while (compteur < Nbind) {
            randomIndividual1 = random.nextInt(this.NbPop);
            randomIndividual2 = random.nextInt(this.NbPop);

            individual1 = Population[randomIndividual1];
            individual2 = Population[randomIndividual2];

            if (individual1.rank < individual2.rank) {
                Echantillon[compteur] = individual1;
            } else {
                if (individual1.rank > individual2.rank) {
                    Echantillon[compteur] = individual2;
                } else {
                    if (individual1.crowding_distance > individual2.crowding_distance) {

                        Echantillon[compteur] = individual1;
                    } else {
                        Echantillon[compteur] = individual2;
                    }
                }
            }

            compteur++;
        }
    }

    void CrossoverMutation() {
        Random random = new Random();
        int ind_Parent1 = random.nextInt(Nbind);
        int ind_Parent2 = random.nextInt(Nbind);
        int ind_crossover = random.nextInt(NbVariable);
        int Getmuted;
        Solution Enfant1 = new Solution(this.NbVariable, this.NbConstraints);
        Solution Enfant2 = new Solution(this.NbVariable, this.NbConstraints);
        ;
        /*
         * for(int k = 0; k < NbVariable; k++){
         * 
         * }
         */
        for (int i = 0; i < ind_crossover; i++) {
            Enfant1.solution[i] = Echantillon[ind_Parent1].solution[i];
            Enfant2.solution[i] = Echantillon[ind_Parent2].solution[i];

        }

        for (int j = ind_crossover; j < NbVariable; j++) {

            Enfant2.solution[j] = Echantillon[ind_Parent1].solution[j];
            Enfant1.solution[j] = Echantillon[ind_Parent2].solution[j];

        }

        Getmuted = random.nextInt(3);

        System.out.println("choixmutation: " + Getmuted + "\t");
        if (Getmuted > 0) {

            Addmutation(Enfant1);
            Addmutation(Enfant2);

        }

        sumconstraint(Enfant1);
        sumconstraint(Enfant2);

        CheckSampleindividual(Enfant1, 0, ind_Parent1);
        CheckSampleindividual(Enfant2, 0, ind_Parent2);

    }

    void CheckSampleindividual(Solution individual, int k, int ind) {
        int compteur = k;
        boolean check = true;
        for (int i = 0; i < NbConstraints; i++) {

            System.out.println("La contrainte est: " + constraint[i] + "\t");
            if (individual.SumConstraint[i] > constraint[i] || individual.SumConstraint[i] == 0) {

                check = false;
                break;
            }
        }

        System.out.println("le booléen est: " + check + "\t");
        if (check == false) {

            System.out.println("La solution n'est pas admissible" + "\t");

            if (compteur < 1) {
                RepaireSample(individual);
                compteur++;
                CheckSampleindividual(individual, compteur, ind);

            }
        } else {

            System.out.println("La solution est admissible" + "\t");
            this.Echantillon[ind] = individual;
            compteur++;
        }
    }

    void Addmutation(Solution Enfant) {
        int compteur = 0;
        int indice;
        int choix;
        Random random = new Random();
        List<Integer> listChoix = new ArrayList<Integer>();

        for (int i = 0; i < NbVariable; i++) {
            if (Enfant.solution[i] < 1) {
            }
        }
        for (int i = 0; i < NbVariable; i++) {
            if (Enfant.solution[i] < 1) {
                listChoix.add(i);
                compteur++;
            }
        }

        indice = random.nextInt(compteur);
        choix = listChoix.get(indice);
        Enfant.solution[choix] = 1;

    }




    

    void resolve(int Nbgen) {
        int k = 0; 
        this.countGeneration = 0;
        Random rand = new Random();
        this.initPopulation();
        this.displayPopulation();
        while(this.countGeneration < Nbgen) {
            this.fitnessValuePop();
            System.out.println("fitness pop");

            this.BuildMatrixIndicator();
            System.out.println("Matrix indicator built");

            this.UpdateFitnessValue();
            System.out.println("Fitness value built");

            this.EnvironementalSelection();
            if(this.countGeneration > Nbgen - 1){
                break; 
            }
            this.BinarysTournament();
            System.out.println("fitness tournoi");
            nbCrossover = rand.nextInt(this.Nbind - 0) + 0;
              for(int i = 0; i < nbCrossover; i++){
              this.CrossoverMutation();
              }
             
            this.UpdatePopulation();
            System.out.println("La génération est créée");
        }
        displayPopulation();
        System.out.println("terminé");

    }
};
