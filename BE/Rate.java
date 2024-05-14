package BE;

public class Rate {
    // Variables
    private Country country;
    private Team team;
    private String rate;
    private int id; // Assuming 'id' should be an integer

    // Constructor to initialize the Rate object
    public Rate(int id, Country country, Team team, String rate) {
        this.id = id;
        this.country = country;
        this.team = team;
        this.rate = rate;
    }

    public Rate() {

    }
    public Rate( Country country, Team team, String rate) {
        this.id = id;
        this.country = country;
        this.team = team;
        this.rate = rate;
    }

    // Getters
    public int getId() {
        return id;
    }

    public Country getCountry() {
        return country;
    }

    public Team getTeam() {
        return team;
    }

    public String getRate() {
        return rate;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    // Optional: Override toString() for easy printing of Rate objects
    @Override
    public String toString() {
        return "Rate{" +
                "id=" + id +
                ", country='" + country + '\'' +
                ", team='" + team + '\'' +
                ", rate='" + rate + '\'' +
                '}';
    }
}

