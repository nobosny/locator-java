package models;

/**
 * Created by Nobosny on 6/6/2015.
 */
public class Needs {
    private String code;
    private String name;

    public Needs(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
