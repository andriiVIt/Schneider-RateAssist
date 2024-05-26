package DAL;

import BE.Country;
import BE.Employee;
import BE.Team;
import DAL.Interface.IEmployeeDAO;
import DAL.db.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO implements IEmployeeDAO {
    // Instance of ConnectionManager to manage database connections
    private ConnectionManager connectionManager;

    // Constructor to initialize ConnectionManager
    public EmployeeDAO() {
        connectionManager = new ConnectionManager();
    }

    // Method to retrieve all employees from the database
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
                    String loginName = resultSet.getString("LoginName");
                    String password = resultSet.getString("Password");

                    Employee employee = new Employee(id, name, salary, overheadPercentage, workHours, utilization, resourceType, fixedAmount, imageData, loginName, password);
                    allEmployees.add(employee);
                }
            }
        }
        return allEmployees;
    }
    // Method to delete an employee and related records from the database
    public void deleteEmployee(Employee employee) throws SQLException {
        String sqlDeleteFromEmployeeCountry = "DELETE FROM EmployeeCountry WHERE employeeid = ?";
        String sqlDeleteFromEmployeeTeam = "DELETE FROM EmployeeTeam WHERE employeeid = ?";
        String sqlDeleteFromCalculatedRates = "DELETE FROM CalculatedRates WHERE employeeId = ?";
        String sqlDeleteEmployee = "DELETE FROM Employee WHERE ID = ?";

        try (Connection con = connectionManager.getConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement pstDeleteFromEmployeeCountry = con.prepareStatement(sqlDeleteFromEmployeeCountry);
                 PreparedStatement pstDeleteFromEmployeeTeam = con.prepareStatement(sqlDeleteFromEmployeeTeam);
                 PreparedStatement pstDeleteFromCalculatedRates = con.prepareStatement(sqlDeleteFromCalculatedRates);
                 PreparedStatement pstDeleteEmployee = con.prepareStatement(sqlDeleteEmployee)) {

                pstDeleteFromEmployeeCountry.setInt(1, employee.getId());
                pstDeleteFromEmployeeCountry.executeUpdate();

                pstDeleteFromEmployeeTeam.setInt(1, employee.getId());
                pstDeleteFromEmployeeTeam.executeUpdate();

                pstDeleteFromCalculatedRates.setInt(1, employee.getId());
                pstDeleteFromCalculatedRates.executeUpdate();

                pstDeleteEmployee.setInt(1, employee.getId());
                pstDeleteEmployee.executeUpdate();

                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        }
    }
    // Method to create a new employee
    public Employee createEmployee(Employee employee) throws SQLException {
        String sql = "INSERT INTO Employee (Name, Salary, OverheadPercentage, WorkHours, Utilization, ResourceType, FixedAmount, ImageData, LoginName, Password) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
            pst.setString(9, employee.getLoginName());
            pst.setString(10, employee.getPassword());

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
    // Method to retrieve employees based on country and team filters
    public List<Employee> getAllEmployeesByFilters(List<Integer> listCountryIds, List<Integer> listTeamIds) throws SQLException {
        List<Employee> allEmployees = new ArrayList<>();
        String countryInClause = listCountryIds != null ? listCountryIds.toString().replace("[", "(").replace("]", ")") : null;
        String teamInClause = listTeamIds != null ? listTeamIds.toString().replace("[", "(").replace("]", ")") : null;

        StringBuilder sql = new StringBuilder("SELECT E.*, C.CountryName, T.TeamName FROM Employee E ")
                .append("LEFT JOIN EmployeeCountry EC ON E.ID = EC.EmployeeID ")
                .append("LEFT JOIN Country C ON EC.CountryID = C.ID ")
                .append("LEFT JOIN EmployeeTeam ET ON E.ID = ET.EmployeeID ")
                .append("LEFT JOIN Team T ON ET.TeamID = T.ID ");

        if (countryInClause != null || teamInClause != null) {
            sql.append("WHERE ");
            if (countryInClause != null) {
                sql.append("C.ID IN ").append(countryInClause);
            }
            if (teamInClause != null) {
                if (countryInClause != null) {
                    sql.append(" AND ");
                }
                sql.append("T.ID IN ").append(teamInClause);
            }
        }

        try (Connection con = connectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql.toString())) {
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
                String loginName = resultSet.getString("LoginName");
                String password = resultSet.getString("Password");

                Employee employee = new Employee(id, name, salary, overheadPercentage, workHours, utilization, resourceType, fixedAmount, imageData, loginName, password);
                allEmployees.add(employee);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return allEmployees;
    }
    // Method to assign a country to an employee
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
    // Method to assign a team to an employee
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
    // Method to update an existing employee's details
    public void updateEmployee(Employee employee) throws SQLException {
        String sql = "UPDATE Employee SET Name = ?, Salary = ?, OverheadPercentage = ?, WorkHours = ?, Utilization = ?, ResourceType = ?, FixedAmount = ?, ImageData = ?, LoginName = ?, Password = ? WHERE ID = ?";

        try (Connection con = connectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, employee.getName());
            pstmt.setDouble(2, employee.getSalary());
            pstmt.setDouble(3, employee.getOverheadPercentage());
            pstmt.setDouble(4, employee.getWorkHours());
            pstmt.setDouble(5, employee.getUtilization());
            pstmt.setString(6, employee.getResourceType());
            pstmt.setDouble(7, employee.getFixedAmount());
            pstmt.setBytes(8, employee.getImageData());
            pstmt.setString(9, employee.getLoginName());
            pstmt.setString(10, employee.getPassword());
            pstmt.setInt(11, employee.getId());

            pstmt.executeUpdate();
        }
    }
    // Method to fetch a coordinator by their username
    public Employee getEmployeeByUsername(String username) throws SQLException {
        Employee employee = null;
        try (Connection con = connectionManager.getConnection()) {
            String sql = "SELECT * FROM Employee WHERE LoginName = ?;";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, username);

            ResultSet resultSet = pst.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String name = resultSet.getString("Name");
                double salary = resultSet.getDouble("Salary");
                double overheadPercentage = resultSet.getDouble("OverheadPercentage");
                double workHours = resultSet.getDouble("WorkHours");
                double utilization = resultSet.getDouble("Utilization");
                String resourceType = resultSet.getString("ResourceType");
                double fixedAmount = resultSet.getDouble("FixedAmount");
                byte[] imageData = resultSet.getBytes("ImageData");
                String loginName = resultSet.getString("LoginName");
                String password = resultSet.getString("Password");

                employee = new Employee(id, name, salary, overheadPercentage, workHours, utilization, resourceType, fixedAmount, imageData, loginName, password);
            }
        }
        return employee;
    }
    // Method to update an employee's credentials
    public void updateEmployeeCredentials(int employeeId, String newUsername, String newPassword) throws SQLException {
        String sql = "UPDATE Employee SET LoginName = ?, Password = ? WHERE ID = ?";
        try (Connection con = connectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, newUsername);
            pstmt.setString(2, newPassword);
            pstmt.setInt(3, employeeId);
            pstmt.executeUpdate();
        }
    }

}
