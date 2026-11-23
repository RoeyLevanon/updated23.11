package com.example.myapplication;

public class ChefDish extends Menu {
    private boolean sharedOnTable;

    public ChefDish() {
        super();
    }

    public ChefDish(String id, String name, String description, boolean sharedOnTable) {
        super(id, name, description);
        this.sharedOnTable = sharedOnTable;
    }

    public boolean isSharedOnTable() { return sharedOnTable; }
    public void setSharedOnTable(boolean sharedOnTable) { this.sharedOnTable = sharedOnTable; }

    @Override
    public String getCategoryDetails() {
        return sharedOnTable ? "מנת שף למרכז השולחן" : "מנת שף אישית לפתיחה";
    }
}