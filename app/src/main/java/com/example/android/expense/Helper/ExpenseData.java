package com.example.android.expense.Helper;

import java.time.LocalDateTime;

/**
 * Created by Saksham Negi on 5/7/19
 */
public class ExpenseData {
    public String description;
    public int amount;
    public String time;

    public String getTime() {
        return time;
    }

    //default constructor mandatory for firebase database
    public ExpenseData(){

    }

    public ExpenseData(String description, int amount, String time) {
        this.description = description;
        this.amount = amount;
        this.time = time;

    }

    //getters
    public String getDescription() {
        return description;
    }

    public int getAmount() {
        return amount;
    }

}
