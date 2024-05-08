package BE;

public class Employee {
    private int id;
    private String name;
    private String location;
    private double salary;
    private double overheadPercentage;
    private String team;
    private double workHours;
    private double utilization;
    private String resourceType;
    private double fixedAmount;
    private byte[] imageData;

    // Constructor with ID (typically used when retrieving from database)
    public Employee(int id, String name, String location, double salary, double overheadPercentage, String team,
                    double workHours, double utilization, String resourceType, double fixedAmount, byte[] imageData) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.salary = salary;
        this.overheadPercentage = overheadPercentage;
        this.team = team;
        this.workHours = workHours;
        this.utilization = utilization;
        this.resourceType = resourceType;
        this.fixedAmount = fixedAmount;
        this.imageData = imageData;
    }

    // Constructor without ID (typically used when creating a new employee)
    public Employee(String name, String location, double salary, double overhead, String team, double workHours, double utilization, String resourceType, String note, byte[] imageData) {
        this.name = name;
        this.location = location;
        this.salary = salary;
        this.overheadPercentage = overhead;
        this.team = team;
        this.workHours = workHours;
        this.utilization = utilization;
        this.resourceType = resourceType;
        this.fixedAmount = fixedAmount; // Assume that fixed amount can be optional or derived
        this.imageData = imageData;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public double getSalary() {
        return  salary;
    }

    public double getOverheadPercentage() {
        return overheadPercentage;
    }

    public String getTeam() {
        return team;
    }

    public double getWorkHours() {
        return workHours;
    }

    public double getUtilization() {
        return utilization;
    }

    public String getResourceType() {
        return resourceType;
    }

    public double getFixedAmount() {
        return fixedAmount;
    }

    public byte[] getImageData() {
        return imageData;
    }

    // Setter
    public void setId(int id) {
        this.id = id;
    }
    @Override
    public String toString() {
        return name;
    }

    public int getCountryID() {
        return 0;
    }

    public int getTeamID() {
        return 0;
    }
}