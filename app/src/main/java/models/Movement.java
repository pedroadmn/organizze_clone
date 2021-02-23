package models;

import helper.Base64Custom;
import helper.DateCustom;

import static config.FirebaseConfig.getFirebaseAuth;
import static config.FirebaseConfig.getFirebaseDatabase;

public class Movement {

    private String id;
    private String date;
    private String category;
    private String description;
    private String type;
    private Double value;

    public Movement() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

        getFirebaseDatabase().child("movements")
                .child(userId)
                .child(monthYear)
                .push()
                .setValue(this);
    }
}
