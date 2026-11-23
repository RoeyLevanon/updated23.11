package com.example.myapplication;

public abstract class Person {
    protected String fullName;
    protected String phone;

    public Person() {} // חובה עבור Firebase

    public Person(String fullName, String phone) {
        this.fullName = fullName;
        this.phone = phone;
    }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    // פעולה אבסטרקטית שכל מחלקה יורשת חייבת לממש
    public abstract String getPersonRoleDescription();
}