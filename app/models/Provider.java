package models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nobosny on 6/7/2015.
 */
public class Provider{
    private int provider_id;
private String provider_name;
private String telephone_number;
private String provider_aka;
private String provider_description;
private String eligibility;
private String handicap_access;
private String hours;
private String intake_procedure;
private String languages;
private String website_address;
private String line1;
private String line2;
private String city;
private String county;
private String postal_code;
private String province;
private String country;
private double Latitude;
private double Longitude;
private String contact_email;
private String contact_name;
private String contact_phone;
private String contact_telephone_areacode;
private String contact_telephone_extension;
private String contact_title;
    private Double distance;

    private List<Needs> needses;
    private List<TargetPopulation> targetPopulation;

    public Provider() {
        needses = new ArrayList<Needs>();
        targetPopulation = new ArrayList<TargetPopulation>();
    }

    public List<TargetPopulation> getTargetPopulation() {
        return targetPopulation;
    }

    public void setTargetPopulation(List<TargetPopulation> targetPopulation) {
        this.targetPopulation = targetPopulation;
    }

    public void addTargetPopulation(TargetPopulation target) {
        this.targetPopulation.add(target);
    }

    public List<Needs> getNeedses() {
        return needses;
    }

    public void setNeedses(List<Needs> needses) {
        this.needses = needses;
    }

    public void addNeed(Needs need) {
        this.needses.add(need);
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public int getProvider_id() {
        return provider_id;
    }

    public void setProvider_id(int provider_id) {
        this.provider_id = provider_id;
    }

    public String getProvider_name(){
        return provider_name;
        }

public void setProvider_name(String provider_name){
        this.provider_name=provider_name;
        }

public String getTelephone_number(){
        return telephone_number;
        }

public void setTelephone_number(String telephone_number){
        this.telephone_number=telephone_number;
        }

public String getProvider_aka(){
        return provider_aka;
        }

public void setProvider_aka(String provider_aka){
        this.provider_aka=provider_aka;
        }

public String getProvider_description(){
        return provider_description;
        }

public void setProvider_description(String provider_description){
        this.provider_description=provider_description;
        }

public String getEligibility(){
        return eligibility;
        }

public void setEligibility(String eligibility){
        this.eligibility=eligibility;
        }

public String getHandicap_access(){
        return handicap_access;
        }

public void setHandicap_access(String handicap_access){
        this.handicap_access=handicap_access;
        }

public String getHours(){
        return hours;
        }

public void setHours(String hours){
        this.hours=hours;
        }

public String getIntake_procedure(){
        return intake_procedure;
        }

public void setIntake_procedure(String intake_procedure){
        this.intake_procedure=intake_procedure;
        }

public String getLanguages(){
        return languages;
        }

public void setLanguages(String languages){
        this.languages=languages;
        }

public String getWebsite_address(){
        return website_address;
        }

public void setWebsite_address(String website_address){
        this.website_address=website_address;
        }

public String getLine1(){
        return line1;
        }

public void setLine1(String line1){
        this.line1=line1;
        }

public String getLine2(){
        return line2;
        }

public void setLine2(String line2){
        this.line2=line2;
        }

public String getCity(){
        return city;
        }

public void setCity(String city){
        this.city=city;
        }

public String getCounty(){
        return county;
        }

public void setCounty(String county){
        this.county=county;
        }

public String getPostal_code(){
        return postal_code;
        }

public void setPostal_code(String postal_code){
        this.postal_code=postal_code;
        }

public String getProvince(){
        return province;
        }

public void setProvince(String province){
        this.province=province;
        }

public String getCountry(){
        return country;
        }

public void setCountry(String country){
        this.country=country;
        }

public double getLatitude(){
        return Latitude;
        }

public void setLatitude(double Latitude){
        this.Latitude=Latitude;
        }

public double getLongitude(){
        return Longitude;
        }

public void setLongitude(double Longitude){
        this.Longitude=Longitude;
        }

public String getContact_email(){
        return contact_email;
        }

public void setContact_email(String contact_email){
        this.contact_email=contact_email;
        }

public String getContact_name(){
        return contact_name;
        }

public void setContact_name(String contact_name){
        this.contact_name=contact_name;
        }

public String getContact_phone(){
        return contact_phone;
        }

public void setContact_phone(String contact_phone){
        this.contact_phone=contact_phone;
        }

public String getContact_telephone_areacode(){
        return contact_telephone_areacode;
        }

public void setContact_telephone_areacode(String contact_telephone_areacode){
        this.contact_telephone_areacode=contact_telephone_areacode;
        }

public String getContact_telephone_extension(){
        return contact_telephone_extension;
        }

public void setContact_telephone_extension(String contact_telephone_extension){
        this.contact_telephone_extension=contact_telephone_extension;
        }

public String getContact_title(){
        return contact_title;
        }

public void setContact_title(String contact_title){
        this.contact_title=contact_title;
        }
        }
