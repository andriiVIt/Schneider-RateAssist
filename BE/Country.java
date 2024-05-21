package BE;

public class Country {

    public Country(){

    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    private int id;
    private String countryName;

    public Country(int id, String name) {
        this.id = id;
        this.countryName = name;
    }
    public Country( String name) {
        this.countryName = name;
    }

    @Override
    public String toString() {
        System.out.println(countryName);
        return countryName;
    }

    public void setText(String countryName) {

    }
}
