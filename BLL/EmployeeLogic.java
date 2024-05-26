package BLL;

import BE.Country;
import BE.Employee;
import BE.Team;
import DAL.CountryDAO;
import DAL.EmployeeDAO;
import DAL.Interface.IEmployeeDAO;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.List;

public class EmployeeLogic {
    // Create a static instance of the IEmployeeDAO interface using EmployeeDAO implementation
    static IEmployeeDAO employeeDAO = new EmployeeDAO();

    // Method to create a new employee
    public static Employee createEmployee(Employee employee) throws SQLException {
        return employeeDAO.createEmployee(employee);
    }
    // Method to retrieve all employees from the database
    public static List<Employee> getAllEmployees() throws SQLException {
        return employeeDAO.getAllEmployees();
    }
    // Method to retrieve employees by country and team filters
    public static List<Employee> getAllEmployeesByFilters(List<Integer> listCountryIds, List<Integer> listTeamIds) throws SQLException {
        return employeeDAO.getAllEmployeesByFilters(listCountryIds, listTeamIds);
    }
    // Method to delete an employee
    public void deleteEmployee(Employee employee) throws SQLException {
        employeeDAO.deleteEmployee(employee);
    }

    // Method to assign a country to an employee
    public void assignCountryEmployee(Country country, Employee employee) throws SQLException {
        employeeDAO.assignCountryEmployee(employee, country);
    }

    // Method to assign a team to an employee
    public void assignTeamEmployee(Team team, Employee employee) throws SQLException {
        employeeDAO.assignTeamEmployee(employee, team);
    }
    // Method to update employee details
    public void updateEmployee(Employee employee) throws SQLException {
        employeeDAO.updateEmployee(employee);
    }
    // Method to retrieve an employee by username
    public Employee getEmployeeByUsername(String username) throws SQLException {
        return employeeDAO.getEmployeeByUsername(username);
    }
    // Method to update employee credentials

    public void updateEmployeeCredentials(int employeeId, String newUsername, String newPassword) throws SQLException {
        employeeDAO.updateEmployeeCredentials(employeeId, newUsername, newPassword);
    }


}
