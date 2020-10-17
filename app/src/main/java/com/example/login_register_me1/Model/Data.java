package com.example.login_register_me1.Model;

public class Data {
    String type;
    String note;
    String date;
    String id;
    String amount;

    public Data(String type, String note, String date, String id, String amount) {
        this.type = type;
        this.note = note;
        this.date = date;
        this.id = id;
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Data(){

    }

}
