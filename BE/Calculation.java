package BE;

public class Calculation {
    private int id; // Додано поле id
    private int employeeId;
    private double rate;

    public Calculation(int employeeId, double rate) {
        this.employeeId = employeeId;
        this.rate = rate;
    }

    // Доданий новий конструктор
    public Calculation(int id, int employeeId, double rate) {
        this.id = id;
        this.employeeId = employeeId;
        this.rate = rate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
