package BE;

public class Team {

    private int id;
    private String teamName;

    // Constructor with ID (typically used when retrieving from database)
    public Team() {}

    public Team(int id, String name) {
        this.id = id;
        this.teamName = name;
    }

    // Constructor without ID (typically used when creating a new team)
    public Team(String name) {
        this.teamName = name;
    }

    // Getter and setter for the team ID
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter and setter for the team name
    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    @Override
    public String toString() {
        return teamName;
    }

    public void setText(String teamName) {

    }
}
