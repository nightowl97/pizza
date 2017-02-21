package com.nightowl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class Main {

    static int rows, cols, min, max;
    static List<String> list;
    static List<List<String>> data = new ArrayList<>();

    public static void main(String[] args) {
        // Read the file
        try{
            File file = new File(System.getProperty("user.dir") + "/small.in");
            Scanner s = new Scanner(file);
            list = new ArrayList<>();
            while (s.hasNext()){
                list.add(s.next());
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        // Set up
        rows = Integer.parseInt(list.get(0));
        cols = Integer.parseInt(list.get(1));
        min = Integer.parseInt(list.get(2));
        max = Integer.parseInt(list.get(3));
        for(int i = 4; i < list.size(); i++){
            ArrayList<String> curr = new ArrayList<>(Arrays.asList(list.get(i).split("")));
            data.add(curr);
        }
        long starttime = System.currentTimeMillis();
        System.out.println("Possible slices: " + possibleSlices(data).size());
        System.out.println("Timing: ");
        System.out.println(System.currentTimeMillis() - starttime);
        getConfs(new int[] {3, 4, 2, 5});
    }

    /**
     *
     * @param args slice coordinates as {upperrow, lowerrow,leftcolumn, rightcolumn}
     * @return true if slice is valid, false otherwise
     */
    public static boolean checkValid(int[] args){
        // Get slice
        List<List<String>> slice = getSlice(args);
        // Validate
        int Tnum = 0;
        int Mnum = 0;
        for(List<String> row : slice){
            for(String cell : row){
                if (cell.equals("M")){ Mnum++; }
                else if (cell.equals("T")){ Tnum++; }
                else{
                    throw new IllegalArgumentException();
                }
            }
        }
        return !(Tnum < min || Mnum < min || slice.size() * slice.get(0).size() > max);
    }

    /**
     *
     * @param slice1
     * @param slice2
     * @return true if slice1 and slice2 have at least one common cell, false otherwise
     */
    public static boolean checkOverlap(int[] slice1, int[] slice2){
        if (slice1[0] > slice2[1] || slice2[2] > slice1[3]){
            return false;
        }else if (slice2[0] > slice1[1] || slice1[2] > slice2[3]){
            return false;
        }else {
            return true;
        }
    }

    /**
     *
     * @param pizza takes the entire pizza
     * @return all posible slices' coordinates
     */
    public static List<List<Integer>> possibleSlices(List<List<String>> pizza){
        List<List<Integer>> result = new ArrayList<>();
        for (int i = 0; i < rows; i++){
            for (int j = i; j < rows; j++){
                if (j - i > max){ break; }
                for (int u = 0; u < cols; u++){
                    for (int v = u; v < cols; v++){
                        if (v - u > max || (v - u + 1) * (j - i + 1) > max){
                            break;
                        } else if (checkValid(new int[] {i, j, u, v})){
                            List<Integer> current = Arrays.asList(i, j, u, v);
                            result.add(current);
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * Gets the actual slice from the pizza using slice coordinates
     * @param args array of coordinates as {upperrow, lowerrow,leftcolumn, rightcolumn}
     * @return 2 dimensional List from data
     */
    public static List<List<String>> getSlice(int[] args){
        List<List<String>> result = new ArrayList<>();
        for (int i = args[0]; i <= args[1]; i++){
            result.add(data.get(i).subList(args[2], args[3] + 1));
        }
        return result;
    }

    /**
     *
     * @param slice
     * @return two dimensional list of possible pizza configurations involving slice param
     */
    public static List<List<Integer>> getConfs(int[] slice){
        // Get Non overlapping slices
        List<List<Integer>> slicespace = possibleSlices(data);
        List<List<Integer>> nonoverlap = new ArrayList<>();
        for (List<Integer> vect : slicespace){
            // Convert Integer[] to primitive int[]
            int[] array =  vect.stream().mapToInt(i->i).toArray();// goddammit java..
            if (!checkOverlap(slice, array)){
                nonoverlap.add(vect);
            }
        }
        System.out.println("non-overlapping slices: \n" + nonoverlap.size());
        //TODO: Use nonoverlap to build possible configurations
        return nonoverlap;
    }

    /** Checks if two slices are adjacent, in taxicab distance
     *
     * @param slice1 first slice
     * @param slice2 second slice
     * @return true if adjacent, false otherwise.
     */
    public static boolean isAdj(int[] slice1, int[] slice2){
        // Get rid of obvious case
        if (checkOverlap(slice1, slice2)){ return false; }
        // It should work but good f**king luck debugging this man..
        // Here comes the moneeyyyy here comes the money! moneymoneymoneymoney...
        boolean condition1 = slice1[1] == slice2[0] - 1 && slice1[3] > slice2[2] && slice1[2] < slice2[3];
        boolean condition2 = slice2[1] == slice1[0] - 1 && slice2[3] > slice1[2] && slice2[2] < slice1[3];
        boolean condition3 = slice1[3] == slice2[2] - 1 && slice1[0] < slice2[1] && slice1[1] > slice2[0];
        boolean condition4 = slice2[3] == slice1[2] - 1 && slice2[0] < slice1[1] && slice2[1] > slice1[0];

        return condition1 || condition2 || condition3 || condition4;
    }

}
