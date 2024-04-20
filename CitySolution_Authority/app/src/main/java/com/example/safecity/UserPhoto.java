package com.example.safecity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UserPhoto {
    String photoid;
    String timestamp;
    String date;
    String time;
    String userid;
    String imagepath;
    String description;
    String confidence;
    String status;
    String category,locality,city,pincode,country,district,division,city_corporation;
     String acknowlegedDate,solvedDate;

    public String getAcknowlegedDate() {
        return acknowlegedDate;
    }

    public void setAcknowlegedDate(String acknowlegedDate) {
        this.acknowlegedDate = acknowlegedDate;
    }

    public String getSolvedDate() {
        return solvedDate;
    }

    public void setSolvedDate(String solvedDate) {
        this.solvedDate = solvedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



    public String getConfidence() {
        return confidence;
    }

    public void setConfidence(String confidence) {
        this.confidence = confidence;
    }



    public String getPhotoid() {
        return photoid;
    }

    public void setPhotoid(String photoid) {
        this.photoid = photoid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getCity_corporation() {
        return city_corporation;
    }

    public void setCity_corporation(String city_corporation) {
        this.city_corporation = city_corporation;
    }
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("photoid", photoid);
        result.put("category",category );
        result.put("city", city);
        result.put("city_corporation", city_corporation);
        result.put("confidence", confidence);
        result.put("country", country);
        result.put("date", date);
        result.put("description", description);
        result.put("district", district);
        result.put("division", division);
        result.put("imagepath", imagepath);
        result.put("locality", locality);
        result.put("pincode", pincode);
        result.put("status", status);
        result.put("time", time);
        result.put("timestamp", timestamp);
        result.put("userid", userid);


        // Add other fields as needed
        return result;
    }
}
