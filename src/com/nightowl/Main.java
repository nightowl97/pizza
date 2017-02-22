package com.nightowl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.lang.Math;


public class Main {

    static int rows, cols, min, max;
    static List<String> list;
    static List<List<String>> data = new ArrayList<>();
    static List<Slice> slicespace = possibleSlices(data);

    public static void main(String[] args) {
        // Read the file
        try{
            File file = new File(System.getProperty("user.dir") + "/example.in");
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
        Slice.entirepizza = data;
        long starttime = System.currentTimeMillis();
        System.out.println("Possible slices: " + possibleSlices(data).size());
        for (Slice s: possibleSlices(data)){
            System.out.println("next slice: ");
            for(List<String> line : s.getActualslice()){
                System.out.println(line.toString());
            }
        }
        System.out.println("Timing: ");
        System.out.println(((System.currentTimeMillis() - starttime) / 1000.0) + " seconds");
        // getConfs(new int[] {3, 4, 2, 5});
    }

    /**
     *
     * @param s
     * @return true if slice is valid, false otherwise
     */
    public static boolean checkValid(Slice s){
        // Validate
        int Tnum = 0;
        int Mnum = 0;
        for(List<String> row : s.getActualslice()){
            for(String cell : row){
                if (cell.equals("M")){ Mnum++; }
                else if (cell.equals("T")){ Tnum++; }
                else{
                    throw new IllegalArgumentException();
                }
            }
        }
        return !(Tnum < min || Mnum < min || s.getSize() > max);
    }

    /**
     *
     * @param s1
     * @param s2
     * @return true if slice1 and slice2 have at least one common cell, false otherwise
     */
    public static boolean checkOverlap(Slice s1, Slice s2){
        if (s1.getRow1() > s2.getRow2() || s2.getCol1() > s1.getRow2()){
            return false;
        }else if (s2.getRow1() > s1.getRow2() || s1.getCol1() > s2.getCol2()){
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
    public static List<Slice> possibleSlices(List<List<String>> pizza){
        List<Slice> result = new ArrayList<>();
        for (int i = 0; i < rows; i++){
            //System.out.println(i);
            System.gc();
            for (int j = i; j < rows; j++){
                if (j - i > max){ break; }
                for (int u = 0; u < cols; u++){
                    for (int v = u; v < cols; v++){
                        // Slice s = new Slice(i, j, u, v);
                        if ((j - i + 1) * (v - u + 1) > max){
                            break;
                        } else if (checkValid(new Slice(i, j, u, v))){
                            Slice current = new Slice(i, j, u, v);
                            result.add(current);
                        }
                    }
                }
            }

        }
        return result;
    }

    /** Checks if two slices are adjacent, in taxicab distance
     *
     * @param slice1 first slice
     * @param slice2 second slice
     * @return true if adjacent, false otherwise.
     */
    public static boolean isAdj(Slice slice1, Slice slice2){
        // Get rid of obvious case
        if (checkOverlap(slice1, slice2)){ return false; }

        boolean condition1 = slice1.getRow2() == slice2.getRow1() - 1 && slice1.getCol2() > slice2.getCol1() && slice1.getCol1() < slice2.getCol2();
        boolean condition2 = slice2.getRow2() == slice1.getRow1() - 1 && slice2.getCol2() > slice1.getCol1() && slice2.getCol1() < slice1.getCol2();
        boolean condition3 = slice1.getCol2() == slice2.getCol1() - 1 && slice1.getRow1() < slice2.getRow2() && slice1.getRow2() > slice2.getRow1();
        boolean condition4 = slice2.getCol2() == slice1.getCol1() - 1 && slice2.getRow1() < slice1.getRow2() && slice2.getRow2() > slice1.getRow1();

        return condition1 || condition2 || condition3 || condition4;
    }

    public static Slice sum(Slice s1, Slice s2){
        Slice res = new Slice(0, 0, 0, 0);
        if (checkOverlap(s1, s2) || !isAdj(s1, s2)){ throw new IllegalArgumentException();}
        res.setRow1(Math.min(s1.getRow1(), s2.getRow1()));
        res.setRow2(Math.max(s1.getRow2(), s2.getRow2()));
        res.setCol1(Math.min(s1.getCol1(), s2.getCol1()));
        res.setCol2(Math.max(s1.getCol2(), s2.getCol2()));

        return res;
    }

    /**
     *
     * @param s
     * @return two dimensional list of possible pizza configurations involving slice param
     */
    public static List<Slice> getConfs(Slice s){
        // Get Non overlapping slices
        List<Slice> nonoverlap = new ArrayList<>();
        List<Slice> neighbors = new ArrayList<>();
        for (Slice sl : slicespace) {
            if (!checkOverlap(s, sl)) {
                nonoverlap.add(sl);
                if (isAdj(s, sl)) {
                    neighbors.add(sl);
                }
            }
        }
        // Validation
        for (Slice sl: neighbors){
            rec(sl, nonoverlap);
        }

        return nonoverlap;
    }

    private static boolean rec(Slice s, List<Slice> nonov) {
        //TODO: Base case
        if (nonov.isEmpty()){
            return true;
        }else{
            List<Slice> nonoverlap = new ArrayList<>();
            List<Slice> neighs = new ArrayList<>();
            for (Slice sl : nonov){
                if (!checkOverlap(s, sl)){
                    nonoverlap.add(sl);
                    if(isAdj(s, sl)){
                        neighs.add(sl);
                    }
                }
            }
        }
    }
}