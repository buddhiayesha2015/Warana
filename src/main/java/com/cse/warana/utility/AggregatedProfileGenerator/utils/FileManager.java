package com.cse.warana.utility.AggregatedProfileGenerator.utils;

import com.cse.warana.utility.infoHolders.Candidate;
import com.cse.warana.utility.infoHolders.Profile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by Thilina on 11/15/2014.
 */
public class FileManager {

    public void Normalize(File file,String destination) {
        try {
            Scanner sc=new Scanner(file);
            String line;
            Double max= Double.valueOf(Integer.MIN_VALUE),min= Double.valueOf(Integer.MAX_VALUE),ratio;
            Map<String,Double> normalizedMap=new HashMap<String, Double>();
            Double val;
            normalizedMap=FileToMap(file);
            while (sc.hasNextLine()){
                line=sc.nextLine();
                String s=line.split(",")[0].split("\\|")[0].trim();
                val=Double.parseDouble(line.split(",")[1]);
                normalizedMap.put(s,val);
                if (val<min)
                    min=val;
                if(val>max)
                    max=val;
            }
            normalizedMap=NormalizeMap((HashMap<String, Double>) normalizedMap);
            normalizedMap= SortByComparator(normalizedMap);
            ratio=100/(max-min);
            new File(destination+"/"+file.getParentFile().getName()).mkdirs();
            File output=new File(destination+"/"+file.getParentFile().getName()+"/"+file.getName());
            FileWriter writer = new FileWriter(output);

            for (Map.Entry<String, Double> entry : normalizedMap.entrySet()) {
//                val=(entry.getValue()-min)*ratio;
                writer.write(entry.getKey()+","+entry.getValue()+"\n");
            }
            writer.flush();
            writer.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Map<String, Double> NormalizeMap(HashMap<String,Double> map){
        Double max= Double.valueOf(Integer.MIN_VALUE),min= Double.valueOf(Integer.MAX_VALUE),ratio;
        for (Double val : map.values()) {
            if (val>max)
                max=val;
            if (val<min)
                min=val;
        }
        if (max-min==0){
            ratio=0.0;
        }
        else {
            ratio=100/(max-min);
        }

        for (Map.Entry<String, Double> entry : map.entrySet()) {
            map.put(entry.getKey(),(entry.getValue()-min)*ratio);
        }
        return map;
    }
    public void writeFile(String name,HashMap<String,Double> map, String destinationPath){
//        boolean b = new File(filePath+"/"+name).mkdirs();

        boolean f = new File(destinationPath).mkdirs();
        File file=new File(destinationPath+name+".csv");
        // creates the file
        try {
            if(!file.exists())
                file.createNewFile();
            FileWriter writer = new FileWriter(file);
            for (Map.Entry<String, Double> entry : map.entrySet()) {
                writer.write(entry.getKey()+","+entry.getValue().toString()+"\n");
            }
            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public Map<String, Double> SortByComparator(Map<String, Double> unsortMap) {

        // Convert Map to List
        List<Map.Entry<String, Double>> list =  new LinkedList<Map.Entry<String, Double>>(unsortMap.entrySet());

        // Sort list with comparator, to compare the Map values
        Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
            public int compare(Map.Entry<String, Double> o1,
                               Map.Entry<String, Double> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        // Convert sorted map back to a Map
        Map<String, Double> sortedMap = new LinkedHashMap<String, Double>();
        for (Iterator<Map.Entry<String, Double>> it = list.iterator(); it.hasNext();) {
            Map.Entry<String, Double> entry = it.next();
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }
    public HashMap<String,Double> FileToMap(File file){
        HashMap<String,Double> map=new HashMap<String, Double>();
        try {
            Scanner sc=new Scanner(file);
            while (sc.hasNextLine()){
                String line=sc.nextLine();
                String[] ary= line.split(",");
//                System.out.println(ary[1]);
//                System.out.println(ary[0].split("\\|")[0]);
                if(ary.length>1) {
                    map.put(ary[0].split("\\|")[0].trim(), Double.parseDouble(ary[1]));
                }
                else {
                    map.put(ary[0].split("\\|")[0].trim(),0.0);
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println(file.getName()+" not found");
            return map;
        }

        return map;
    }

    public void WriteFile(String fileName, Map<String, Double> dataMap, String destinationPath) {
        boolean f = new File(destinationPath).mkdirs();
        File file=new File(destinationPath+fileName);
        // creates the file
        try {
            if(!file.exists())
                file.createNewFile();
            FileWriter writer = new FileWriter(file);
            for (Map.Entry<String, Double> entry : dataMap.entrySet()) {
                writer.write(entry.getKey().replace(".csv","")+","+entry.getValue().toString()+"\n");
            }
            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void WriteFile(String destinationPath, Map.Entry<String, String> entry) {
        boolean f = new File(destinationPath).mkdirs();
//        System.out.println(entry.getKey().replaceAll("^[.\\\\/:*?\"<>|]?[\\\\/:*?\"<>|]*", "")+".txt");
        File file=new File(destinationPath+"/"+entry.getKey().replaceAll("[^a-zA-Z0-9\\.\\-]", "_")+".txt");
        // creates the file
        try {
            if(!file.exists())
                file.createNewFile();
            FileWriter writer = new FileWriter(file);
            writer.write(entry.getKey());
            writer.write(entry.getValue());
            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void WriteFile(String fileName, HashMap<String, Integer> dataMap, String destinationPath) {
        boolean f = new File(destinationPath).mkdirs();
        File file=new File(destinationPath+fileName);
        // creates the file
        try {
            if(!file.exists())
                file.createNewFile();
            FileWriter writer = new FileWriter(file);
            for (Map.Entry<String, Integer> entry : dataMap.entrySet()) {
                writer.write(entry.getKey().replace(".csv","")+","+entry.getValue().toString()+"\n");
            }
            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public HashMap<String, Double> RemoveDuplications(HashMap<String, Double> map) {
        String shortKey;
        HashMap<String, Double> clone = (HashMap<String, Double>) map.clone();
        double score=0;
        for (String key : map.keySet()) {
            score=map.get(key);
            for (String otherKey : map.keySet()) {
                if (key.toLowerCase().contains(otherKey.toLowerCase()) && !key.equals(otherKey)){

                    score+=map.get(otherKey);
                    clone.remove(otherKey);
                }
            }
            if(clone.containsKey(key)) {
                clone.put(key, score);
            }
        }
        clone= (HashMap<String, Double>) SortByComparator(clone);

//        for (String s : clone.keySet()) {
//            System.out.println(s);
//        }

        return clone;
    }

    private Double getMax(Double a, Double b) {
        if (a>b){
            return a;
        }
        else {
            return b;
        }
    }


    public HashMap<String, Double> GetWeightMap(String weightMapPath) {
        File file=new File(weightMapPath);
        HashMap<String, Double> weightsMap=new HashMap<String,Double>();
        if (file.exists()){
            weightsMap = FileToMap(file);
        }
        return weightsMap;
    }

    public void WriteFile(String destinationPath, HashMap<String, Double> weightMap) {
        boolean f = new File(destinationPath).mkdirs();
        File file=new File(destinationPath);
        // creates the file
        try {
            if(!file.exists())
                file.createNewFile();
            FileWriter writer = new FileWriter(file);
            for (Map.Entry<String, Double> entry : weightMap.entrySet()) {
                writer.write(entry.getKey()+","+entry.getValue().toString()+"\n");
            }
            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void WriteAbbrFile(String destinationPath,String fileName, HashMap<String, String> pairs) {
        boolean f = new File(destinationPath).mkdirs();
        File file=new File(destinationPath+"/"+fileName);
        // creates the file
        try {
            if(!file.exists())
                file.createNewFile();
            FileWriter writer = new FileWriter(file);
            for (Map.Entry<String, String> entry : pairs.entrySet()) {
                writer.write(entry.getKey().toLowerCase().trim()+","+entry.getValue().toLowerCase().trim()+"\n");
            }
            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, String> FileToStrStrMap(File file) {
        HashMap<String,String> map=new HashMap<String, String>();
        try {
            Scanner sc=new Scanner(file);
            while (sc.hasNextLine()){
                String line=sc.nextLine();
                String[] ary= line.split(",");
//                System.out.println(ary[1]);
//                System.out.println(ary[0].split("\\|")[0]);
                if(ary.length>1) {
                    map.put(ary[0].split("\\|")[0].trim(), ary[1].trim());
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println(file.getName()+" not found");
            return map;
        }

        return map;
    }

    public void WriteCandidate(Candidate candidate) {
        HashMap<String,String> candMap=new HashMap<String,String>();
        candMap.put("profile",candidate.toString());
        candMap.put("profile2",candidate.toString());
        for (Map.Entry<String, String> entry : candMap.entrySet()) {
            WriteFile(Config.profilesPath + "/" + candidate.getProfile().getId(),entry );
        }


    }
}
