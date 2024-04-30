package DAL;

import BE.Employee;
import DAL.db.ConnectionManager;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {


    private ConnectionManager connectionManager;

    public EmployeeDAO(){

        connectionManager = new ConnectionManager();

    }

    public List<Employee> getAllEmployees()throws SQLException{
        List<Employee> allEmployees = new ArrayList<>();
        try (Connection con = connectionManager.getConnection()) {
            String sql = "SELECT * FROM Employee";
            Statement statement = con.createStatement();

            if (statement.execute(sql)) {
                ResultSet resultSet = statement.getResultSet();
                while (resultSet.next()) {
                    int id = resultSet.getInt("ID");
                    String name = resultSet.getString("Name");
                    String location = resultSet.getString("Location");
                    double salary = resultSet.getDouble("Salary");
                    double overheadPercentage = resultSet.getDouble("OverheadPercentage");
                    String team = resultSet.getString("Team");
                    double workHours = resultSet.getDouble("WorkHours");
                    double utilization = resultSet.getDouble("Utilization");
                    String resourceType = resultSet.getString("ResourceType");
                    double fixedAmount = resultSet.getDouble("FixedAmount");
                    byte[] imageData = resultSet.getBytes("imageData");

                    Employee employee = new Employee(id, name, location, salary, overheadPercentage, team,workHours,utilization,resourceType,fixedAmount, imageData);
                    allEmployees.add(employee);
                }
            }
        } return allEmployees;



    }


    public void deleteEmployee(Employee employee)throws SQLException {
        Connection con = null;

        try {
            // Obtain a connection from the connection manager
            con = connectionManager.getConnection();
            // Manage transactions manually
            con.setAutoCommit(false);
            // Set the transaction isolation level
            con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            // If there are related tables where the employee ID is a foreign key, delete these entries first
            // Assuming there might be tables like EmployeeProjects, EmployeeBenefits etc. Adjust these to your actual use case

            // Example: Delete from EmployeeProjects where the employee ID matches
            try (PreparedStatement pstEmployeeProjects = con.prepareStatement("DELETE FROM EmployeeProjects WHERE employeeid = ?")) {
                pstEmployeeProjects.setInt(1, employee.getId());
                pstEmployeeProjects.executeUpdate();
            }

            // Example: Delete from EmployeeBenefits where the employee ID matches
            try (PreparedStatement pstEmployeeBenefits = con.prepareStatement("DELETE FROM EmployeeBenefits WHERE employeeid = ?")) {
                pstEmployeeBenefits.setInt(1, employee.getId());
                pstEmployeeBenefits.executeUpdate();
            }

            // Finally, delete the employee from the Employee table
            try (PreparedStatement pstEmployee = con.prepareStatement("DELETE FROM Employee WHERE EmployeeID = ?")) {
                pstEmployee.setInt(1, employee.getId());
                pstEmployee.executeUpdate();
            }

            // Commit the transaction if all operations were successful
            con.commit();
        } catch (SQLException e) {
            // Rollback any changes made during the transaction on the same connection
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    // Log the exception or handle the rollback failure
                    ex.printStackTrace();
                }
            }
            // Rethrow the original SQLException
            throw e;
        } finally {
            // Attempt to reset the connection's auto-commit mode to true if it's not null
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                } catch (SQLException ex) {
                    // Handle or log this exception
                    ex.printStackTrace();
                }
            }
            // Since we didn't use try-with-resources for the connection (to access it in the catch block),
            // we need to ensure it's closed here
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    // Handle or log this exception
                    ex.printStackTrace();
                }
            }
        }
    }



    public Employee createEmployee(Employee employee)throws SQLException {

        try (Connection con = connectionManager.getConnection()){
            con.setAutoCommit(false);
            con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            PreparedStatement pst = con.prepareStatement("INSERT INTO Employee (Name, Location, Salary, OverheadPercentage, Team, WorkHours, Utilization, ResourceType, FixedAmount, ImageData) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

            pst.setString(1, employee.getName());
            pst.setString(2, employee.getLocation());
            pst.setDouble(3, employee.getSalary());  // Using as 'budget' or 'cost' perhaps
            pst.setDouble(4, employee.getOverheadPercentage());
            pst.setString(5, employee.getTeam());  // Could denote a team involved in the event
            pst.setDouble(6, employee.getWorkHours());  // Hours dedicated to the event
            pst.setDouble(7, employee.getUtilization());  // Utilization of resources
            pst.setString(8, employee.getResourceType());  // Type of resource, could be venue type etc.
            pst.setDouble(9, employee.getFixedAmount());  // Fixed cost related to the event
            pst.setBytes(10, employee.getImageData());

            pst.execute();
            if (pst.getGeneratedKeys().next()) {
                int id = pst.getGeneratedKeys().getInt(1);
                employee.setId(id);
                con.commit();
                return employee;
            } else {
                con.rollback();
                throw new RuntimeException("Id not set");
            }
        } catch (SQLException e) {
            try (Connection con = connectionManager.getConnection()) {
                con.rollback();
            }
            throw e;
        }
        }
    }

