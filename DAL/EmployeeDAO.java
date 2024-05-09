package DAL;

import BE.Country;
import BE.Employee;
import DAL.db.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    private ConnectionManager connectionManager;

    public EmployeeDAO() {
        connectionManager = new ConnectionManager();
    }

    public List<Employee> getAllEmployees() throws SQLException {
        List<Employee> allEmployees = new ArrayList<>();
        try (Connection con = connectionManager.getConnection()) {
            String sql = "SELECT * FROM Employee";
            Statement statement = con.createStatement();

            if (statement.execute(sql)) {
                ResultSet resultSet = statement.getResultSet();
                while (resultSet.next()) {
                    int id = resultSet.getInt("ID");
                    String name = resultSet.getString("Name");

                    double salary = resultSet.getDouble("Salary");
                    double overheadPercentage = resultSet.getDouble("OverheadPercentage");

                    double workHours = resultSet.getDouble("WorkHours");
                    double utilization = resultSet.getDouble("Utilization");
                    String resourceType = resultSet.getString("ResourceType");
                    double fixedAmount = resultSet.getDouble("FixedAmount");
                    byte[] imageData = resultSet.getBytes("ImageData");

                    Employee employee = new Employee(id, name, salary, overheadPercentage, workHours, utilization, resourceType, fixedAmount, imageData);
                    allEmployees.add(employee);
                }
            }
        }
        return allEmployees;
    }

    public void deleteEmployee(Employee employee) throws SQLException {
        String sql = "DELETE FROM Employee WHERE ID = ?";
        try (Connection con = connectionManager.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, employee.getId());
            pst.executeUpdate();
        }
    }

    public Employee createEmployee(Employee employee) throws SQLException {
        String sql = "INSERT INTO Employee (Name, Salary, OverheadPercentage, WorkHours, Utilization, ResourceType, FixedAmount, ImageData) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = connectionManager.getConnection();
             PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, employee.getName());

            pst.setDouble(3, employee.getSalary());
            pst.setDouble(4, employee.getOverheadPercentage());

            pst.setDouble(6, employee.getWorkHours());
            pst.setDouble(7, employee.getUtilization());
            pst.setString(8, employee.getResourceType());
            pst.setDouble(9, employee.getFixedAmount());
            pst.setBytes(10, employee.getImageData());
            pst.executeUpdate();

            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                employee.setId(rs.getInt(1));
            } else {
                throw new SQLException("Creating employee failed, no ID obtained.");
            }
            return employee;
        }
    }

    public List<Employee> getAllEmployeesByFilters(List<Integer> listCountryIds) {
        List<Employee> allEmployees = new ArrayList<>();
        String inClause = listCountryIds.toString().replace("[", "(").replace("]", ")");

        String sql = "SELECT E.*, C.CountryName FROM Employee E JOIN Country C ON C.ID = E.CountryID WHERE C.ID IN " + inClause;

        try (Connection con = connectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String name = resultSet.getString("Name");
                String location = resultSet.getString("CountryName");
                double salary = resultSet.getDouble("Salary");
                double overheadPercentage = resultSet.getDouble("OverheadPercentage");
                String team = resultSet.getString("Team");
                double workHours = resultSet.getDouble("WorkHours");
                double utilization = resultSet.getDouble("Utilization");
                String resourceType = resultSet.getString("ResourceType");
                double fixedAmount = resultSet.getDouble("FixedAmount");
                byte[] imageData = resultSet.getBytes("ImageData");

                Employee employee = new Employee(id, name, salary, overheadPercentage, workHours, utilization, resourceType, fixedAmount, imageData);
                allEmployees.add(employee);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return allEmployees;
    }
}
