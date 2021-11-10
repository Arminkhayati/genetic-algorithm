package util;

import java.util.List;

public class Chromosome {

    public int[] genes;
    public int size;
    public Chromosome(int numOfGenes){
        size = numOfGenes;
        genes = new int[numOfGenes];
    }

    @Override
    public String toString() {
        String s = "[ ";
        for (int gene : genes)
            s += gene + ", ";
        s += "]";
        return s;
    }
}
