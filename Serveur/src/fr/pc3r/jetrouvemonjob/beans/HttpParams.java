package fr.pc3r.jetrouvemonjob.beans;

public class HttpParams {
    private String name;
    private String value;

    public HttpParams(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
