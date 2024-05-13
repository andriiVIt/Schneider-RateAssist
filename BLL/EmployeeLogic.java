package BLL;

import BE.Country;
import BE.Employee;
import BE.Team;
import DAL.CountryDAO;
import DAL.EmployeeDAO;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.List;

public class EmployeeLogic {
    static EmployeeDAO employeeDAO = new EmployeeDAO();

    public static Employee createEmployee(Employee employee) throws SQLException {
        return employeeDAO.createEmployee(employee);
    }

    // Повертає список працівників, а не одного працівника
    public static List<Employee> getAllEmployees() throws SQLException {
        return employeeDAO.getAllEmployees();

    }

    public static List<Employee> getAllEmployeesByFilters(List<Integer> listCountryIds) throws SQLException {
        return employeeDAO.getAllEmployeesByFilters(listCountryIds);

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
}
