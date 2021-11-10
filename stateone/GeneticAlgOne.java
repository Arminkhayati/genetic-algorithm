package stateone;

import util.Chromosome;
import util.DataLoader;
import util.Info;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GeneticAlgOne {

    public void genetic(StateOne stateOne){
        DataLoader dataLoader = new DataLoader(Info.dataFile);
        double[] probabilities;
        for (int i = 0; i < 10; i++) {
            probabilities = calculateFitnessProbability(stateOne);
            for (double probability : probabilities) {
                System.out.println(probability);
            }
            selection(stateOne, probabilities);
            System.out.println("\n After Selection \n");
            for (Chromosome chromosome : stateOne.population) {
                System.out.println(chromosome.toString());
            }
            System.out.println("\n After Cross over \n");
            crossover(stateOne);
            System.out.print("\n");
            for (Chromosome chromosome : stateOne.population) {
                System.out.println(chromosome.toString());
            }
            System.out.println("\n Mutating \n");
            mutation(stateOne, dataLoader);
            System.out.println("\n**********Next Round***********\n");
        }
    }

    private double[] calculateFitnessProbability(StateOne stateOne){
        int[] probabilities = new int[stateOne.population.length];
        for(int i = 0 ; i < stateOne.population.length ; i++) {
           Chromosome ch = stateOne.population[i];
           probabilities[i] = stateOne.fitness(ch);
            System.out.println("Fitness of " + ch.toString() + " = " + probabilities[i]);
        }
        int sumOfFitnesses = Arrays.stream(probabilities).sum();
        double[] doubles = Arrays.stream(probabilities).asDoubleStream().toArray();
//        System.out.println("Sum of fitness " + sumOfFitnesses );
        System.out.println("\n Probabilities : \n");
        doubles = Arrays.stream(doubles).
                map(x ->  (Math.abs(x - sumOfFitnesses) / sumOfFitnesses) / (probabilities.length - 1)).
                toArray();
        return doubles;
    }
    private void selection(StateOne stateOne, double[] probabilities){
        for(int i = 1 ; i < probabilities.length ; i++)
            probabilities[i] = probabilities[i] + probabilities[i-1];

        Chromosome[] newPopulation = new Chromosome[stateOne.population.length];
        System.out.println("\n Selection with these statistics : \n");
        for(int i = 0 ; i < probabilities.length ; i++){
           double randomNumber = Math.random();
            System.out.println(randomNumber + " " + probabilities[i]);
           int index = IntStream.range(0, stateOne.population.length)
                   .filter(x -> randomNumber < probabilities[x])
                   .findFirst()
                   .getAsInt();
           newPopulation[i] = stateOne.population[index];
        }
        stateOne.population = newPopulation;
    }

    public void crossover(StateOne stateOne){
        Random random = new Random();
        int popSize = stateOne.population.length;
        for(int i = 0 ; i < popSize ; i+=2){
            Chromosome ch1 = stateOne.population[i];
            Chromosome ch2 = stateOne.population[i+1];
            int crossPoint = Math.abs(random.nextInt() % ch1.size);
            int[] temp = new int[ch1.size - crossPoint];
            System.out.println(" Crossover number " + i/2 + " from : " + crossPoint);
            //copy ch1 to temp
            System.arraycopy(ch1.genes, crossPoint, temp,0, temp.length);
            //copy ch2 to ch1
            System.arraycopy(ch2.genes, crossPoint, ch1.genes, crossPoint, temp.length);
            //copy temp to ch1
            System.arraycopy(temp, 0, ch2.genes, crossPoint, temp.length);
        }
    }

    public void mutation(StateOne stateOne, DataLoader dataLoader){
        Random random = new Random();
        int howManyToMutate = Math.abs(random.nextInt() % stateOne.population.length);
        for (int i = 0; i < howManyToMutate; i++) {
            int whichOneToMutate = Math.abs(random.nextInt() % stateOne.population.length);
            Chromosome ch = stateOne.population[whichOneToMutate];
            int whichGeneToChange = Math.abs(random.nextInt() % ch.size);
            List<Integer> people = dataLoader.data.get(whichGeneToChange + 1);
            System.out.println("\n Chromosome " + whichOneToMutate + " Selected");
            System.out.println(" Gene " + whichGeneToChange + " Selected = " + ch.genes[whichGeneToChange]);
            for (int j = 0 ; j < people.size() ; j++) {
                if(people.get(j) != ch.genes[whichGeneToChange])
                    if(stateOne.contains(ch.genes, people.get(j)))
                        continue;
                    else
                        ch.genes[whichGeneToChange] = people.get(j);
            }
            System.out.println(" Gene " + whichGeneToChange + " After Mutation = " + ch.genes[whichGeneToChange]);
        }
    }
}
