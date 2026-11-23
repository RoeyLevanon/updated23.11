package com.example.myapplication;

public class Guest extends Person {
    private int tableNumber;
    private boolean checkedIn;

    public Guest() {
        super();
    }

    public Guest(String fullName, String phone, int tableNumber, boolean checkedIn) {
        super(fullName, phone);
        this.tableNumber = tableNumber;
        this.checkedIn = checkedIn;
    }

    public int getTableNumber() { return tableNumber; }
    public void setTableNumber(int tableNumber) { this.tableNumber = tableNumber; }

    public boolean isCheckedIn() { return checkedIn; }
    public void setCheckedIn(boolean checkedIn) { this.checkedIn = checkedIn; }

    @Override
    public String getPersonRoleDescription() {
        return "אורח באירוע | שולחן מספר " + tableNumber;
    }
}