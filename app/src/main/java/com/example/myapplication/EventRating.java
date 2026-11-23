package com.example.myapplication;

public class EventRating implements FirebaseEntity {
    private String ratingId;
    private String guestPhone;
    private float foodStars;
    private float locationStars;
    private float serviceStars;

    public EventRating() {} // חובה עבור Firebase

    public EventRating(String ratingId, String guestPhone, float foodStars, float locationStars, float serviceStars) {
        this.ratingId = ratingId;
        this.guestPhone = guestPhone;
        this.foodStars = foodStars;
        this.locationStars = locationStars;
        this.serviceStars = serviceStars;
    }

    public String getRatingId() { return ratingId; }
    public void setRatingId(String ratingId) { this.ratingId = ratingId; }

    public String getGuestPhone() { return guestPhone; }
    public void setGuestPhone(String guestPhone) { this.guestPhone = guestPhone; }

    public float getFoodStars() { return foodStars; }
    public void setFoodStars(float foodStars) { this.foodStars = foodStars; }

    public float getLocationStars() { return locationStars; }
    public void setLocationStars(float locationStars) { this.locationStars = locationStars; }

    public float getServiceStars() { return serviceStars; }
    public void setServiceStars(float serviceStars) { this.serviceStars = serviceStars; }

    public float getAverageRating() {
        return (foodStars + locationStars + serviceStars) / 3.0f;
    }

    @Override
    public String getEntityId() {
        return ratingId != null ? ratingId : guestPhone;
    }

    @Override
    public String getSummaryInfo() {
        return String.format("דירוג ממוצע: %.1f כוכבים (אורח: %s)", getAverageRating(), guestPhone);
    }
}
