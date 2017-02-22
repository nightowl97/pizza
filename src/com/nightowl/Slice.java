package com.nightowl;

import java.util.ArrayList;
import java.util.List;

public class Slice {

    static List<List<String>> entirepizza;
    // coordinates
    private int row1, row2, col1, col2, size;
    // actual slice ith "T"s and "M"s.
    private List<List<String>> actualslice;

    public Slice(int row1, int row2, int col1, int col2){
        this.row1 = row1;
        this.row2 = row2;
        this.col1 = col1;
        this.col2 = col2;
        this.size = (row2 - row1 + 1) * (col2 - col1 + 1);
    }

    public List<List<String>> setActualslice() {
        List<List<String>> temp = new ArrayList<>();
        for (int i = this.row1; i <= this.row2; i++){
            temp.add(entirepizza.get(i).subList(col1, col2 + 1));
        }
        return temp;
    }

    // Getters
    public List<List<String>> getActualslice() {
        this.actualslice = setActualslice();
        return actualslice;
    }

    public int getRow1() {
        return row1;
    }

    public int getRow2() {
        return row2;
    }

    public int getCol1() {
        return col1;
    }

    public int getCol2() {
        return col2;
    }

    public int getSize() {
        return size;
    }

    // Setters
    public void setRow1(int row1) {
        this.row1 = row1;
    }

    public void setRow2(int row2) {
        this.row2 = row2;
    }

    public void setCol1(int col1) {
        this.col1 = col1;
    }

    public void setCol2(int col2) {
        this.col2 = col2;
    }
}
