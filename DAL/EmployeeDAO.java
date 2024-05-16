package DAL;

import BE.Country;
import BE.Employee;
import BE.Team;
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
        String sqlDeleteFromEmployeeCountry = "DELETE FROM EmployeeCountry WHERE employeeid = ?";
        String sqlDeleteFromEmployeeTeam = "DELETE FROM EmployeeTeam WHERE employeeid = ?";
        String sqlDeleteEmployee = "DELETE FROM Employee WHERE ID = ?";

        try (Connection con = connectionManager.getConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement pstDeleteFromEmployeeCountry = con.prepareStatement(sqlDeleteFromEmployeeCountry);
                 PreparedStatement pstDeleteFromEmployeeTeam = con.prepareStatement(sqlDeleteFromEmployeeTeam);
                 PreparedStatement pstDeleteEmployee = con.prepareStatement(sqlDeleteEmployee)) {

                pstDeleteFromEmployeeCountry.setInt(1, employee.getId());
                pstDeleteFromEmployeeCountry.executeUpdate();

                pstDeleteFromEmployeeTeam.setInt(1, employee.getId());
                pstDeleteFromEmployeeTeam.executeUpdate();

                pstDeleteEmployee.setInt(1, employee.getId());
                pstDeleteEmployee.executeUpdate();

                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        }
    }

    public Employee createEmployee(Employee employee) throws SQLException {
        String sql = "INSERT INTO Employee (Name, Salary, OverheadPercentage, WorkHours, Utilization, ResourceType, FixedAmount, ImageData) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = connectionManager.getConnection();
             PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, employee.getName());
            pst.setDouble(2, employee.getSalary());
            pst.setDouble(3, employee.getOverheadPercentage());
            pst.setDouble(4, employee.getWorkHours());
            pst.setDouble(5, employee.getUtilization());
            pst.setString(6, employee.getResourceType());
            pst.setDouble(7, employee.getFixedAmount());
            pst.setBytes(8, employee.getImageData());

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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return allEmployees;
    }
    public void assignCountryEmployee(Employee employee, Country country) throws SQLException {
        try (Connection con = connectionManager.getConnection()) {
            con.setAutoCommit(false);
            con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            PreparedStatement pst = con.prepareStatement("INSERT INTO EmployeeCountry(employeeid, countryid) VALUES(?, ?)", Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, employee.getId());
            pst.setInt(2, country.getId());
            pst.executeUpdate();
            con.commit();
        } catch (SQLException e) {
            try (Connection con = connectionManager.getConnection()) {
                con.rollback();
            }
            throw e;
        }
    }
    public void assignTeamEmployee(Employee employee, Team team) throws SQLException {
        try (Connection con = connectionManager.getConnection()) {
            con.setAutoCommit(false);
            con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            PreparedStatement pst = con.prepareStatement("INSERT INTO EmployeeTeam(employeeid, teamid) VALUES(?, ?)", Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, employee.getId());
            pst.setInt(2, team.getId());
            pst.executeUpdate();
            con.commit();
        } catch (SQLException e) {
            try (Connection con = connectionManager.getConnection()) {
                con.rollback();
            }
            throw e;
        }
    }
    public void updateEmployee(Employee employee) throws SQLException {
        String sql = "UPDATE Employee SET Name = ?, Salary = ?, OverheadPercentage = ?, WorkHours = ?, Utilization = ?, ResourceType = ?, FixedAmount = ? WHERE ID = ?";

        try (Connection con = connectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, employee.getName());
            pstmt.setDouble(2, employee.getSalary());
            pstmt.setDouble(3, employee.getOverheadPercentage());
            pstmt.setDouble(4, employee.getWorkHours());
            pstmt.setDouble(5, employee.getUtilization());
            pstmt.setString(6, employee.getResourceType());
            pstmt.setDouble(7, employee.getFixedAmount());

            pstmt.setInt(8, employee.getId());

            pstmt.executeUpdate();
        }
    }
}
