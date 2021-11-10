package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class DataLoader {

    public Map<Integer,List<Integer>> data;
    public Map<Integer, Integer> salary;
    public int numOfFields;
    public int numOfPeople;
    public DataLoader(File file){
        data = new HashMap<Integer,List<Integer>>();
        salary = new HashMap<Integer,Integer>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line1 = br.readLine();
            String line2 = br.readLine();
            if(line1 != null && line2 != null){
                numOfFields = Integer.parseInt(line1);
                numOfPeople = Integer.parseInt(line2);
                helpFunction(br);
            }
            else throw new InputMismatchException("Wrong or Missing Input...");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void helpFunction(BufferedReader br) throws IOException {
        // Build Map with Fields as key and list of people as value
        for(int i = 1 ; i <= numOfFields ; i++)
            data.put(i , new ArrayList<Integer>());
        String line;
        // Filling the map with our dataset
        for(int i = 1 ; i <= numOfPeople && (line = br.readLine()) != null; i++){
            // get list of fields that a person is specialized in it.
            List<Integer> fields = Arrays.asList(line.split(","))
                    .stream()
                    .map(s -> Integer.valueOf(s))
                    .collect(Collectors.toList());
            // Build Map with People as key and Salary as value
            salary.put(i, fields.size() * 1000);
            // Set Person Number at 'data' Map Value list.(keys are fields)
            for (Integer field : fields)
                data.get(field).add(i);
        }
    }
}
