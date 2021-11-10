package statetwo;
import util.Chromosome;
import util.DataLoader;
import util.Info;

import java.io.File;
import java.util.Random;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
public class StateTwo {
    // Info.Chromosome value range is from 1 to n
    // 0 means there is no value.
    public Chromosome[] population;

    public StateTwo(File dataPath, int populationSize){
        DataLoader dataLoader = new DataLoader(dataPath);
        population = new Chromosome[populationSize];
        for (int i = 0 ; i < population.length; i++)
            population[i] = new Chromosome(dataLoader.numOfFields);
        randomPopulation(dataLoader);
    }
    public StateTwo(File dataPath){
        DataLoader dataLoader = new DataLoader(dataPath);
        population = new Chromosome[4];
        for (int i = 0 ; i < population.length; i++)
            population[i] = new Chromosome(dataLoader.numOfFields);
        randomPopulation(dataLoader);
    }

    private void randomPopulation(DataLoader info){
        Random random = new Random(System.currentTimeMillis());
        for (Chromosome chromosome : population) {
            for(int i = 1; i <= chromosome.size; i++){
                int peopleOfField = info.data.get(i).size();
//                System.out.println(peopleOfField);
                int randomIndex = Math.abs(random.nextInt() % peopleOfField);
                int person = info.data.get(i).get(randomIndex);
                if(contains(chromosome.genes,person)){

                    for (int j = 0; j < peopleOfField; j++) {
                        person = info.data.get(i).get(j);
                        if(contains(chromosome.genes,person))
                            continue;
                        else{
                            chromosome.genes[i-1] = person;
                            break;
                        }
                    }

                }else chromosome.genes[i-1] = person;
            }
//            System.out.println("aaaaaaaaa");
        }
    }

    public boolean contains(int[] array, int value){
        return IntStream.of(array)
                .anyMatch(x -> x == value);
    }

    public int fitness(Chromosome chromosome){
        int pow2OfFieldsSupported = (int) Math.pow(chromosome.size, 2);
        int peopleHiredSalarySum = 0;
        for (int gene : chromosome.genes)
            if(gene > 0)
                peopleHiredSalarySum += new DataLoader(Info.dataFile).salary.get(gene);
        return pow2OfFieldsSupported - (peopleHiredSalarySum / 1000);
    }
}
