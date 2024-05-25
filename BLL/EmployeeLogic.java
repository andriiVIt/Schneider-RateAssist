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
    static IEmployeeDAO employeeDAO = new EmployeeDAO();

    public static Employee createEmployee(Employee employee) throws SQLException {
        return employeeDAO.createEmployee(employee);
    }

    public static List<Employee> getAllEmployees() throws SQLException {
        return employeeDAO.getAllEmployees();
    }

    public static List<Employee> getAllEmployeesByFilters(List<Integer> listCountryIds, List<Integer> listTeamIds) throws SQLException {
        return employeeDAO.getAllEmployeesByFilters(listCountryIds, listTeamIds);
    }

    public void deleteEmployee(Employee employee) throws SQLException {
        employeeDAO.deleteEmployee(employee);
    }

    public void assignCountryEmployee(Country country, Employee employee) throws SQLException {
        employeeDAO.assignCountryEmployee(employee, country);
    }

    public void assignTeamEmployee(Team team, Employee employee) throws SQLException {
        employeeDAO.assignTeamEmployee(employee, team);
    }

    public void updateEmployee(Employee employee) throws SQLException {
        employeeDAO.updateEmployee(employee);
    }
    public Employee getEmployeeByUsername(String username) throws SQLException {
        return employeeDAO.getEmployeeByUsername(username);
    }
    public void updateEmployeeCredentials(int employeeId, String newUsername, String newPassword) throws SQLException {
        employeeDAO.updateEmployeeCredentials(employeeId, newUsername, newPassword);
    }


}
