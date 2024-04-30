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

    // Constructor
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
        return salary;
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
}
