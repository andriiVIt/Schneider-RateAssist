package GUI.Model;

import BE.Country;
import BE.Employee;
import BE.Team;
import BLL.EmployeeLogic;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.List;

public class EmployeeModel {
    static EmployeeLogic employeeLogic = new EmployeeLogic();

    private ObservableList<Employee> employees = FXCollections.observableArrayList();
    private ObservableList<Country> countries = FXCollections.observableArrayList();
    private ObservableList<Team> teams = FXCollections.observableArrayList();
    // Retrieves all employees and updates the ObservableList
    public ObservableList<Employee> getEmployees() throws SQLException {
        employees.clear(); // Clear the list before loading new data
        employees.addAll(EmployeeLogic.getAllEmployees());
        return employees;
    }
    // Retrieves all employees as a simple list
    public List<Employee> getAllEmployeesSimple() {
        List<Employee> employees = null;
        try {
            employees = EmployeeLogic.getAllEmployees();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    // Assigns a country to an employee and updates the ObservableList
    public void assignCountryEmployee(Country country, Employee employee) throws SQLException {
        employeeLogic.assignCountryEmployee(country, employee);
        countries.add(country);
    }
    // Assigns a team to an employee and updates the ObservableList
    public void assignTeamEmployee(Team team, Employee employee) throws SQLException {
        employeeLogic.assignTeamEmployee(team, employee);
        teams.add(team);
    }
    // Creates a new employee and adds them to the ObservableList
    public Employee createEmployee(Employee employee) throws SQLException {
        Employee e = EmployeeLogic.createEmployee(employee);
        employees.add(e);
        return e;
    }
    // Deletes an employee and removes them from the ObservableList
    public void deleteEmployee(Employee employee) throws SQLException {
        employeeLogic.deleteEmployee(employee);
        employees.remove(employee);
    }
    // Retrieves employees by country and team IDs and updates the ObservableList
    public List<Employee> getEmployeesByListIds(List<Integer> listCountryIds, List<Integer> teamIds) throws SQLException {
        employees.clear();
        employees.addAll(EmployeeLogic.getAllEmployeesByFilters(listCountryIds, teamIds));
        return employees;
    }
    // Updates the information of an existing employee in the ObservableList
    public void updateEmployee(Employee employee) throws SQLException {
        employeeLogic.updateEmployee(employee);
        int index = employees.indexOf(employee);
        if (index >= 0) {
            employees.set(index, employee);
        }
    }
    // Retrieves an employee by their username
    public Employee getEmployeeByUsername(String username) throws SQLException {
        return employeeLogic.getEmployeeByUsername(username);
    }

    // Validates if the provided username and password match an existing employee
    public boolean isValidEmployee(String inputUsername, String inputPassword) throws SQLException {
        Employee employee = getEmployeeByUsername(inputUsername);
        return employee != null && employee.getPassword().equals(inputPassword);
    }
    // Updates the credentials of an employee
    public void updateEmployeeCredentials(int employeeId, String newUsername, String newPassword) throws SQLException {
        employeeLogic.updateEmployeeCredentials(employeeId, newUsername, newPassword);
        Employee employee = getEmployeeById(employeeId);
        if (employee != null) {
            employee.setLoginName(newUsername);
            employee.setPassword(newPassword);
        }
    }
    // Retrieves an employee by their ID
    private Employee getEmployeeById(int employeeId) {
        for (Employee employee : employees) {
            if (employee.getId() == employeeId) {
                return employee;
            }
        }
        return null;
    }
}
