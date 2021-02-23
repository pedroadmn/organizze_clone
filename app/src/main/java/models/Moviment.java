package models;

import helper.Base64Custom;
import helper.DateCustom;

import static config.FirebaseConfig.getFirebaseAuth;
import static config.FirebaseConfig.getFirebaseDatabase;

public class Moviment {

    private String date;
    private String category;
    private String description;
    private String type;
    private Double value;

    public Moviment() {

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public void save() {
        String userId = Base64Custom.encondeBase64(getFirebaseAuth().getCurrentUser().getEmail());
        String monthYear = DateCustom.monthYearChoseDate(this.date);

        getFirebaseDatabase().child("moviments")
                .child(userId)
                .child(monthYear)
                .push()
                .setValue(this);
    }
}
