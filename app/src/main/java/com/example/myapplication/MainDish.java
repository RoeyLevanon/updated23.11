package com.example.myapplication;

public class MainDish extends Menu {
    private boolean vegetarian;
    private String sideDish;

    public MainDish() {
        super();
    }

    public MainDish(String id, String name, String description, boolean vegetarian, String sideDish) {
        super(id, name, description);
        this.vegetarian = vegetarian;
        this.sideDish = sideDish;
    }

    public boolean isVegetarian() { return vegetarian; }
    public void setVegetarian(boolean vegetarian) { this.vegetarian = vegetarian; }

    public String getSideDish() { return sideDish; }
    public void setSideDish(String sideDish) { this.sideDish = sideDish; }

    @Override
    public String getCategoryDetails() {
        String type = vegetarian ? "צמחוני / טבעוני" : "בשרי / דגים";
        return type + " | תוספת: " + sideDish;
    }
}