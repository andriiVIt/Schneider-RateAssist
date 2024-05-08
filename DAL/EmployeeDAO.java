package DAL;
import BE.Country;
import BE.Employee;
import BE.Team;
import DAL.db.ConnectionManager;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.*;
import java.time.LocalDate;
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
            String sql = "SELECT * FROM Employee JOIN dbo.Country C ON C.id = Employee.CountryID ";
            Statement statement = con.createStatement();

            if (statement.execute(sql)) {
                ResultSet resultSet = statement.getResultSet();
                while (resultSet.next()) {
                    int id = resultSet.getInt("ID");
                    String name = resultSet.getString("Name");
                    String location = resultSet.getString("countryName");
                    double salary = resultSet.getDouble("Salary");
                    double overheadPercentage = resultSet.getDouble("OverheadPercentage");
                    String team = resultSet.getString("Team");
                    double workHours = resultSet.getDouble("WorkHours");
                    double utilization = resultSet.getDouble("Utilization");
                    String resourceType = resultSet.getString("ResourceType");
                    double fixedAmount = resultSet.getDouble("FixedAmount");
                    byte[] imageData = resultSet.getBytes("imageData");

                    Employee employee = new Employee(id, name, location, salary, overheadPercentage, team, workHours, utilization, resourceType, fixedAmount, imageData);
                    allEmployees.add(employee);
                }
            }
        } catch (SQLServerException e) {
            throw new RuntimeException(e);
        }
        return allEmployees;
    }

    public void deleteEmployee(Employee employee) throws SQLException {
        try (Connection con = connectionManager.getConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement pstEmployee = con.prepareStatement("DELETE FROM Employee WHERE  id = ?")) {
                pstEmployee.setInt(1, employee.getId());
                pstEmployee.executeUpdate();
            }
            con.commit();
        } catch (SQLException e) {
            throw e;
        }
    }

    public Employee createEmployee(Employee employee, Team team,Country country) throws SQLException {
        String insertEmployeeSql = "INSERT INTO Employee (Name, Salary, OverheadPercentage, WorkHours, Utilization, ResourceType, FixedAmount, ImageData, CountryID, TeamID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String insertCountrySql = "INSERT INTO Country (countryName) VALUES (?)";
        String insertTeamSql = "INSERT INTO Team (teamName) VALUES (?)";

        try (Connection con = connectionManager.getConnection();
             PreparedStatement insertEmployeePst = con.prepareStatement(insertEmployeeSql, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement insertCountryPst = con.prepareStatement(insertCountrySql, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement insertTeamPst = con.prepareStatement(insertTeamSql, Statement.RETURN_GENERATED_KEYS)) {

            // Inserting country
            insertCountryPst.setString(1, country.getCountryName());
            int countryAffectedRows = insertCountryPst.executeUpdate();
            if (countryAffectedRows == 0) {
                throw new SQLException("Creating country failed, no rows affected.");
            }
            ResultSet countryKeys = insertCountryPst.getGeneratedKeys();
            int countryId;
            if (countryKeys.next()) {
                countryId = countryKeys.getInt(1);
            } else {
                throw new SQLException("Creating country failed, no ID obtained.");
            }

            // Inserting team
            insertTeamPst.setString(1, team.getTeamName());
            int teamAffectedRows = insertTeamPst.executeUpdate();
            if (teamAffectedRows == 0) {
                throw new SQLException("Creating team failed, no rows affected.");
            }
            ResultSet teamKeys = insertTeamPst.getGeneratedKeys();
            int teamId;
            if (teamKeys.next()) {
                teamId = teamKeys.getInt(1);
            } else {
                throw new SQLException("Creating team failed, no ID obtained.");
            }

            // Inserting employee
            insertEmployeePst.setString(1, employee.getName());
            insertEmployeePst.setDouble(2, employee.getSalary());
            insertEmployeePst.setDouble(3, employee.getOverheadPercentage());
            insertEmployeePst.setDouble(4, employee.getWorkHours());
            insertEmployeePst.setDouble(5, employee.getUtilization());
            insertEmployeePst.setString(6, employee.getResourceType());
            insertEmployeePst.setDouble(7, employee.getFixedAmount());
            insertEmployeePst.setBytes(8, employee.getImageData());
            insertEmployeePst.setInt(9, countryId);
            insertEmployeePst.setInt(10, teamId);

            insertEmployeePst.executeUpdate();

            // Getting the generated ID
            ResultSet rs = insertEmployeePst.getGeneratedKeys();
            if (rs.next()) {
                employee.setId(rs.getInt(1));
            } else {
                con.rollback();
                throw new RuntimeException("Creating employee failed, no ID obtained.");
            }
            con.commit();
        } catch (SQLException e) {
            throw e;
        }
        return employee;
    }

    public List<Employee> getAllEmployeesByFilters(List<Integer> listCountryIds) {
        List<Employee> allEmployees = new ArrayList<>();
        String inClause = listCountryIds.toString().replace("[", "(").replace("]", ")");
        String sql = "SELECT E.*, C.countryName FROM dbo.Employee E "
                + "JOIN dbo.Country C ON C.id = E.CountryID "
                + "WHERE C.id IN " + inClause;

        try (Connection con = connectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String name = resultSet.getString("Name");
                String location = resultSet.getString("countryName");
                double salary = resultSet.getDouble("Salary");
                double overheadPercentage = resultSet.getDouble("OverheadPercentage");
                String teamId = resultSet.getString("Team");
                double workHours = resultSet.getDouble("WorkHours");
                double utilization = resultSet.getDouble("Utilization");
                String resourceType = resultSet.getString("ResourceType");
                double fixedAmount = resultSet.getDouble("FixedAmount");
                byte[] imageData = resultSet.getBytes("ImageData");

                Employee employee = new Employee(id, name, location, salary, overheadPercentage, teamId, workHours, utilization, resourceType, fixedAmount, imageData);
                allEmployees.add(employee);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return allEmployees;
    }

    public Country createCountry(Country country) throws SQLException {
        try (Connection con = connectionManager.getConnection();
             PreparedStatement pst = con.prepareStatement("INSERT INTO Country (countryName) VALUES (?)", Statement.RETURN_GENERATED_KEYS)) {
            con.setAutoCommit(false);
            pst.setString(1, country.getCountryName());
            pst.executeUpdate();

            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                country.setId(rs.getInt(1));
            } else {
                con.rollback();
                throw new RuntimeException("Creating country failed, no ID obtained.");
            }
            con.commit();
        } catch (SQLException e) {
            throw e;
        }
        return country;
    }
}
