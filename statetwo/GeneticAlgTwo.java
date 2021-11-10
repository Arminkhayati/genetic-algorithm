package statetwo;
import util.Chromosome;
import util.DataLoader;
import util.Info;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
public class GeneticAlgTwo {

    public void genetic(StateTwo stateTwo){
        DataLoader dataLoader = new DataLoader(Info.dataFile);
        double[] probabilities;
        for (int i = 0; i < 10; i++) {
            probabilities = calculateFitnessProbability(stateTwo);
            for (double probability : probabilities) {
                System.out.println(probability);
            }
            selection(stateTwo, probabilities);
            System.out.println("\n After Selection \n");
            for (Chromosome chromosome : stateTwo.population) {
                System.out.println(chromosome.toString());
            }
            System.out.println("\n After Cross over \n");
            crossover(stateTwo);
            System.out.print("\n");
            for (Chromosome chromosome : stateTwo.population) {
                System.out.println(chromosome.toString());
            }
            System.out.println("\n Mutating \n");
            mutation(stateTwo, dataLoader);
            System.out.println("\n**********Next Round***********\n");
        }
    }

    private double[] calculateFitnessProbability(StateTwo stateTwo){
        int[] probabilities = new int[stateTwo.population.length];
        for(int i = 0 ; i < stateTwo.population.length ; i++) {
            Chromosome ch = stateTwo.population[i];
            probabilities[i] = stateTwo.fitness(ch);
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
    private void selection(StateTwo stateTwo, double[] probabilities){
        for(int i = 1 ; i < probabilities.length ; i++)
            probabilities[i] = probabilities[i] + probabilities[i-1];

        Chromosome[] newPopulation = new Chromosome[stateTwo.population.length];
        System.out.println("\n Selection with these statistics : \n");
        for(int i = 0 ; i < probabilities.length ; i++){
            double randomNumber = Math.random();
            System.out.println(randomNumber + " " + probabilities[i]);
            int index = IntStream.range(0, stateTwo.population.length)
                    .filter(x -> randomNumber < probabilities[x])
                    .findFirst()
                    .getAsInt();
            newPopulation[i] = stateTwo.population[index];
        }
        stateTwo.population = newPopulation;
    }

    public void crossover(StateTwo stateTwo){
        Random random = new Random();
        int popSize = stateTwo.population.length;
        for(int i = 0 ; i < popSize ; i+=2){
            Chromosome ch1 = stateTwo.population[i];
            Chromosome ch2 = stateTwo.population[i+1];
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

    public void mutation(StateTwo stateTwo, DataLoader dataLoader){
        Random random = new Random();
        int howManyToMutate = Math.abs(random.nextInt() % stateTwo.population.length);
        for (int i = 0; i < howManyToMutate; i++) {
            int whichOneToMutate = Math.abs(random.nextInt() % stateTwo.population.length);
            Chromosome ch = stateTwo.population[whichOneToMutate];
            int whichGeneToChange = Math.abs(random.nextInt() % ch.size);
            List<Integer> people = dataLoader.data.get(whichGeneToChange + 1);
            System.out.println("\n Chromosome " + whichOneToMutate + " Selected");
            System.out.println(" Gene " + whichGeneToChange + " Selected = " + ch.genes[whichGeneToChange]);
            for (int j = 0 ; j < people.size() ; j++) {
                if(people.get(j) != ch.genes[whichGeneToChange])
                    if(stateTwo.contains(ch.genes, people.get(j)))
                        continue;
                    else
                        ch.genes[whichGeneToChange] = people.get(j);
            }
            System.out.println(" Gene " + whichGeneToChange + " After Mutation = " + ch.genes[whichGeneToChange]);
        }
    }
}
