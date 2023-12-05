package com.example.demo.util;

public class FullName {

    private String firstName;
    private String lastName;

    public FullName(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        FullName fullName = (FullName) other;
        return firstName.equals(fullName.getFirstName()) &&
               lastName.equals(fullName.getLastName());
    }

    @Override
    public String toString() {
        return firstName + lastName;
    }
}
