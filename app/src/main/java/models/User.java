package models;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import static config.FirebaseConfig.getFirebaseDatabase;

public class User {

    private String id;
    private String name;
    private String email;
    private String password;
    private Double totalRevenue = 0.00;
    private Double totalExpense = 0.00;

    public User() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(Double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public Double getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(Double totalExpense) {
        this.totalExpense = totalExpense;
    }

    public void save() {
        DatabaseReference databaseReference = getFirebaseDatabase();
        databaseReference.child("users")
                .child(this.id)
                .setValue(this);
    }
}
