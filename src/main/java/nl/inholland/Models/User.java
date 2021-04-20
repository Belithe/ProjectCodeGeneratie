package nl.inholland.Models;

import java.util.Date;

public class User {

    private Date birthdate;
    private String email;
    private String firstname;
    private int id;
    private String lastname;
    private String password;
    private int phone;
    private Date registrationdate;
    private boolean isEmployee;
    private boolean isCustomer;

    public User(Date birthdate, String email, String firstname, int id, String lastname, String password, int phone, Date registrationdate, boolean isEmployee, boolean isCustomer) {
        this.birthdate = birthdate;
        this.email = email;
        this.firstname = firstname;
        this.id = id;
        this.lastname = lastname;
        this.password = password;
        this.phone = phone;
        this.registrationdate = registrationdate;
        this.isEmployee = isEmployee;
        this.isCustomer = isCustomer;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public Date getRegistrationdate() {
        return registrationdate;
    }

    public void setRegistrationdate(Date registrationdate) {
        this.registrationdate = registrationdate;
    }

    public boolean isEmployee() {
        return isEmployee;
    }

    public void setEmployee(boolean employee) {
        isEmployee = employee;
    }

    public boolean isCustomer() {
        return isCustomer;
    }

    public void setCustomer(boolean customer) {
        isCustomer = customer;
    }

}
