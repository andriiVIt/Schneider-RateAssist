package BE;

public class Employee {
    private int id;
    private String name;

    private double salary;
    private double overheadPercentage;

    private double workHours;
    private double utilization;
    private String resourceType;
    private double fixedAmount;
    private byte[] imageData;

    // Constructor with ID (typically used when retrieving from database)
    public Employee(int id, String name, double salary, double overheadPercentage,
                    double workHours, double utilization, String resourceType, double fixedAmount, byte[] imageData) {
        this.id = id;
        this.name = name;

        this.salary = salary;
        this.overheadPercentage = overheadPercentage;

        this.workHours = workHours;
        this.utilization = utilization;
        this.resourceType = resourceType;
        this.fixedAmount = fixedAmount;
        this.imageData = imageData;
    }

    // Constructor without ID (typically used when creating a new employee)
    public Employee(String name, double salary, double overheadPercentage, double workHours, double utilization, String resourceType, double fixedAmount, byte[] imageData) {
        this.name = name;
        this.salary = salary;
        this.overheadPercentage = overheadPercentage;
        this.workHours = workHours;
        this.utilization = utilization;
        this.resourceType = resourceType;
        this.fixedAmount = fixedAmount;
        this.imageData = imageData;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public void setOverheadPercentage(double overheadPercentage) {
        this.overheadPercentage = overheadPercentage;
    }

    public void setWorkHours(double workHours) {
        this.workHours = workHours;
    }

    public void setUtilization(double utilization) {
        this.utilization = utilization;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public void setFixedAmount(double fixedAmount) {
        this.fixedAmount = fixedAmount;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }



    public double getSalary() {
        return  salary;
    }

    public double getOverheadPercentage() {
        return overheadPercentage;
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


}