import stateone.GeneticAlgOne;
import stateone.StateOne;
import statetwo.GeneticAlgTwo;
import statetwo.StateTwo;
import util.Info;

public class Runner {
    public void runMethod1(){
        StateOne stateOne = new StateOne(Info.dataFile);
        GeneticAlgOne geneticAlgOne = new GeneticAlgOne();
        geneticAlgOne.genetic(stateOne);
    }
    public void runMethod2(){
        StateTwo stateTwo = new StateTwo(Info.dataFile);
        GeneticAlgTwo geneticAlgTwo = new GeneticAlgTwo();
        geneticAlgTwo.genetic(stateTwo);
    }
}
