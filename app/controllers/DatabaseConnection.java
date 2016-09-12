package controllers;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.SqlRow;
import com.fasterxml.jackson.databind.JsonNode;
import models.Needs;
import models.Provider;
import models.TargetPopulation;
import play.db.DB;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import scala.Array;
import views.html.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * Created by Nobosny on 6/5/2015.
 */
@Security.Authenticated(Secured.class)
public class DatabaseConnection extends Controller {

    public Result data() {

        Connection conn = DB.getConnection();
        Statement stmt = null;
        List<String> dataInfo = new ArrayList<>();
        String debug = "NO DATA";

        try {

            stmt = conn.createStatement();

            String sql = "SELECT dbo.provider.provider_id, dbo.provider.provider_name FROM dbo.provider";

            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()) {
                dataInfo.add(rs.getInt("provider_id") + ": " + rs.getString("provider_name"));
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            debug = "ERROR: " + e.getMessage();
                    e.printStackTrace();
        } finally {
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }

        return ok(Json.toJson(dataInfo));
        //return ok(data.render(dataInfo));
    }

    public Result targetPopulationData() {

        String sql = "SELECT target_population_code, target_population_name " +
                "FROM provider_need_targetpopulation " +
                "GROUP BY target_population_code, target_population_name " +
                "ORDER BY target_population_name";

        List<SqlRow> query = Ebean.createSqlQuery(sql).findList();
        LinkedList<TargetPopulation> result = new LinkedList<>();

        if (query != null) {
            for(SqlRow r: query){
                result.add(new TargetPopulation(r.getString("target_population_code"), r.getString("target_population_name")));
            }
        }

        return ok(Json.toJson(result));

        /*Connection conn = DB.getConnection();
        Statement stmt = null;
        List<TargetPopulation> output = new ArrayList<TargetPopulation>();

        try {
            stmt = conn.createStatement();
            String sql = "SELECT target_population_code, target_population_name " +
                    "FROM provider_target_population " +
                    "GROUP BY target_population_code, target_population_name " +
                    "ORDER BY target_population_name";

            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                output.add(new TargetPopulation(rs.getString("target_population_code"), rs.getString("target_population_name")));
            }
            rs.close();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }

        return ok(Json.toJson(output));*/
    }

    public Result needsQuery(String searchStr) {

        if (searchStr.equals("")) {
            return needsList();
        }

        String sql = "SELECT taxonomy_code, taxonomy_name " +
                "FROM provider_taxonomy " +
                "WHERE (LOWER(taxonomy_name) LIKE '%" + searchStr.toLowerCase() + "%') " +
                "OR (LOWER(taxonomy_code) LIKE '%" + searchStr.toLowerCase() + "%') " +
                "GROUP BY taxonomy_code, taxonomy_name " +
                "ORDER BY taxonomy_name";

        List<SqlRow> query = Ebean.createSqlQuery(sql).findList();
        LinkedList<Needs> result = new LinkedList<>();

        if (query != null) {
            for(SqlRow r: query){
                result.add(new Needs(r.getString("taxonomy_code"), r.getString("taxonomy_name")));
            }
        }

        return ok(Json.toJson(result));

        /*Connection conn = DB.getConnection();
        Statement stmt = null;
        List<Needs> output = new ArrayList<Needs>();

        try {
            stmt = conn.createStatement();
            String sql = "SELECT taxonomy_code, taxonomy_name " +
                    "FROM provider_taxonomy " +
                    "WHERE (LOWER(taxonomy_name) LIKE '%" + searchStr.toLowerCase() + "%') " +
                    "OR (LOWER(taxonomy_code) LIKE '%" + searchStr.toLowerCase() + "%') " +
                    "GROUP BY taxonomy_code, taxonomy_name " +
                    "ORDER BY taxonomy_name";

            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                output.add(new Needs(rs.getString("taxonomy_code"), rs.getString("taxonomy_name")));
            }
            rs.close();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }

        return ok(Json.toJson(output));*/
    }

    public Result needsList() {
        String sql = "SELECT taxonomy_code, taxonomy_name " +
                "FROM provider_taxonomy " +
                "GROUP BY taxonomy_code, taxonomy_name " +
                "ORDER BY taxonomy_name";

        List<SqlRow> query = Ebean.createSqlQuery(sql).findList();
        LinkedList<Needs> result = new LinkedList<>();

        if (query != null) {
            for(SqlRow r: query){
                result.add(new Needs(r.getString("taxonomy_code"), r.getString("taxonomy_name")));
            }
        }

        return ok(Json.toJson(result));


        /*Connection conn = DB.getConnection();
        Statement stmt = null;
        List<Needs> output = new ArrayList<Needs>();

        try {
            stmt = conn.createStatement();
            String sql = "SELECT taxonomy_code, taxonomy_name " +
                    "FROM provider_taxonomy " +
                    "GROUP BY taxonomy_code, taxonomy_name " +
                    "ORDER BY taxonomy_name";

            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                output.add(new Needs(rs.getString("taxonomy_code"), rs.getString("taxonomy_name")));
            }
            rs.close();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }

        return ok(Json.toJson(output));*/
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result search() {
        JsonNode json = request().body().asJson();

        double lat = json.get("lat").asDouble();
        double lon = json.get("lon").asDouble();
        String needs = json.get("needs").asText();
        int distance = json.get("distance").asInt();
        int needsNumber = json.get("needsNumber").asInt();
        int providers = json.get("providers").asInt();
        String target = json.get("target").asText();
        String langSearch = json.get("langSearch").asText();

        String sql = "SELECT * " +
                "FROM (" +
                "SELECT " +
                "provider.* ," +
                "provider_taxonomy.taxonomy_code," +
                "provider_taxonomy.taxonomy_name," +
                "(3959 * ACOS(" +
                "COS(RADIANS(" + lat + "))" +
                "* COS(RADIANS(provider.latitude))" +
                "* COS(RADIANS(provider.longitude) - RADIANS(" + lon + "))" +
                "+ SIN(RADIANS(" + lat + "))" +
                "* SIN(RADIANS(provider.latitude)))) AS distance " +
                "FROM provider " +
                "INNER JOIN provider_taxonomy " +
                "ON provider.provider_id = provider_taxonomy.provider_id " +
                "WHERE provider_taxonomy.taxonomy_code IN (" + needs + ")" +
                "" +
                ") as T1 " +
                "WHERE T1.distance < " + distance + " " +
                "ORDER BY T1.distance";

        if (langSearch.length() > 0) {
            sql = "SELECT * " +
                    "FROM (" +
                    "SELECT " +
                    "provider.* ," +
                    "provider_taxonomy.taxonomy_code," +
                    "provider_taxonomy.taxonomy_name," +
                    "(3959 * ACOS(" +
                    "COS(RADIANS(" + lat + "))" +
                    "* COS(RADIANS(provider.latitude))" +
                    "* COS(RADIANS(provider.longitude) - RADIANS(" + lon + "))" +
                    "+ SIN(RADIANS(" + lat + "))" +
                    "* SIN(RADIANS(provider.latitude)))) AS distance " +
                    "FROM provider " +
                    "INNER JOIN provider_taxonomy " +
                    "ON provider.provider_id = provider_taxonomy.provider_id " +
                    "WHERE provider_taxonomy.taxonomy_code IN (" + needs + ")" +
                    " AND provider.languages LIKE '%" + langSearch + "%'" +
                    "" +
                    ") as T1 " +
                    "WHERE T1.distance < " + distance + " " +
                    "ORDER BY T1.distance";
        }

        if (!target.equalsIgnoreCase("none")) {
            sql = "SELECT * " +
                    "FROM (" +
                    "SELECT " +
                    "provider.* ," +
                    "provider_taxonomy.taxonomy_code," +
                    "provider_taxonomy.taxonomy_name," +
                    "provider_target_population.*," +
                    "(3959 * ACOS(" +
                    "COS(RADIANS(" + lat + "))" +
                    "* COS(RADIANS(provider.latitude))" +
                    "* COS(RADIANS(provider.longitude) - RADIANS(" + lon + "))" +
                    "+ SIN(RADIANS(" + lat + "))" +
                    "* SIN(RADIANS(provider.latitude)))) AS distance " +
                    "FROM provider " +
                    "INNER JOIN provider_taxonomy " +
                    "ON provider.provider_id = provider_taxonomy.provider_id " +
                    "INNER JOIN provider_target_population " +
                    "ON provider_taxonomy.provider_service_code_id = provider_target_population.provider_service_code_id " +
                    "WHERE provider_taxonomy.taxonomy_code IN (" + needs + ") " +
                    "AND provider_target_population.target_population_code = '" + target + "'" +
                    ") as T1 " +
                    "WHERE T1.distance < " + distance + " " +
                    "ORDER BY T1.distance";
        }

        Connection conn = DB.getConnection();
        Statement stmt = null;
        LinkedList<Provider> output = new LinkedList<>();

        LinkedHashMap<String, Provider> providersList = new LinkedHashMap<String, Provider>();

        try {
            stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                String providerName = rs.getString("provider_name");
                if (!providersList.containsKey(providerName)) {
                    Provider provider = new Provider();
                    provider.setProvider_id(rs.getInt("provider_id"));
                    provider.setProvider_name(providerName);
                    provider.setTelephone_number(rs.getString("telephone_number"));
                    provider.setProvider_aka(rs.getString("provider_aka"));
                    provider.setProvider_description(rs.getString("provider_description"));
                    provider.setEligibility(rs.getString("eligibility"));
                    provider.setHandicap_access(rs.getString("handicap_access"));
                    provider.setHours(rs.getString("hours"));
                    provider.setIntake_procedure(rs.getString("intake_procedure"));
                    provider.setLanguages(rs.getString("languages"));
                    provider.setWebsite_address(rs.getString("website_address"));
                    provider.setLine1(rs.getString("line1"));
                    provider.setLine2(rs.getString("line2"));
                    provider.setCity(rs.getString("city"));
                    provider.setCounty(rs.getString("county"));
                    provider.setPostal_code(rs.getString("postal_code"));
                    provider.setProvince(rs.getString("province"));
                    //provider.setCountry(rs.getString("country"));
                    provider.setDistance(rs.getDouble("distance"));
                    provider.setLatitude(rs.getDouble("Latitude"));
                    provider.setLongitude(rs.getDouble("Longitude"));

                    provider.addNeed(new Needs(
                            rs.getString("taxonomy_code"),
                            rs.getString("taxonomy_name")
                        ));

                    providersList.put(providerName, provider);
                } else {
                    providersList.get(providerName).addNeed(new Needs(
                            rs.getString("taxonomy_code"),
                            rs.getString("taxonomy_name")
                        ));
                }
            }
            rs.close();
            stmt.close();
            conn.close();



            if (needsNumber > 0) {
                for (String key : providersList.keySet()) {
                    if (providersList.get(key).getNeedses().size() >= needsNumber) {
                        output.add(providersList.get(key));
                    }
                }
            } else {
                for (String key : providersList.keySet()) {
                    output.add(providersList.get(key));
                }
            }

            if (providers > 0) {
                if (output.size() > providers) {
                    while (output.size() > providers) {
                        output.removeLast();
                    }
                }
            }

            providersList.clear(); //saves memory

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }

        return ok(Json.toJson(output));
    }

    public Result userGuide () {
        return ok(views.html.userguide.render());
    }

    public Result providerDetails (int providerId) {
        Connection conn = DB.getConnection();
        Statement stmt = null;
        Provider provider = new Provider();
        String sql = "";

        try {
            stmt = conn.createStatement();
            sql = "SELECT provider.*, " +
                    "provider_taxonomy.taxonomy_code, " +
                    "provider_taxonomy.taxonomy_name, " +
                    "provider_target_population.target_population_code, " +
                    "provider_target_population.target_population_name " +
                    "FROM provider " +
                    "INNER JOIN provider_taxonomy ON provider.provider_id = provider_taxonomy.provider_id " +
                    "LEFT JOIN provider_target_population ON provider_taxonomy.provider_service_code_id = provider_target_population.provider_service_code_id " +
                    "WHERE provider.provider_id = " + providerId;

            ArrayList<String> needs = new ArrayList<String>();
            ArrayList<String> targets = new ArrayList<String>();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                provider.setProvider_id(rs.getInt("provider_id"));
                provider.setProvider_name(rs.getString("provider_name"));
                provider.setTelephone_number(rs.getString("telephone_number"));
                provider.setProvider_aka(rs.getString("provider_aka"));
                provider.setProvider_description(rs.getString("provider_description"));
                provider.setEligibility(rs.getString("eligibility"));
                provider.setHandicap_access(rs.getString("handicap_access"));
                provider.setHours(rs.getString("hours"));
                provider.setIntake_procedure(rs.getString("intake_procedure"));
                provider.setLanguages(rs.getString("languages"));
                provider.setWebsite_address(rs.getString("website_address"));
                provider.setLine1(rs.getString("line1"));
                provider.setLine2(rs.getString("line2"));
                provider.setCity(rs.getString("city"));
                provider.setCounty(rs.getString("county"));
                provider.setPostal_code(rs.getString("postal_code"));
                provider.setProvince(rs.getString("province"));
                //provider.setCountry(rs.getString("country"));
                provider.setLatitude(rs.getDouble("Latitude"));
                provider.setLongitude(rs.getDouble("Longitude"));


                provider.addNeed(new Needs(
                        rs.getString("taxonomy_code"),
                        rs.getString("taxonomy_name")
                ));
                needs.add(rs.getString("taxonomy_code"));

                if (rs.getString("target_population_code") != null) {
                    provider.addTargetPopulation(new TargetPopulation(
                            rs.getString("target_population_code"),
                            rs.getString("target_population_name")
                    ));
                    targets.add(rs.getString("target_population_code"));
                }
            }



            while (rs.next()) {
                Needs n = new Needs(
                        rs.getString("taxonomy_code"),
                        rs.getString("taxonomy_name")
                );
                if (!needs.contains(n.getCode())) {
                    provider.addNeed(n);
                    needs.add(n.getCode());
                }



                if (rs.getString("target_population_code") != null) {
                    TargetPopulation t = new TargetPopulation(
                            rs.getString("target_population_code"),
                            rs.getString("target_population_name")
                    );
                    if (!targets.contains(t.getCode())) {
                        provider.addTargetPopulation(t);
                        targets.add(t.getCode());
                    }
                }
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }

        //return ok(Json.toJson(provider));
        return ok(views.html.provider.render(provider));
    }

}
