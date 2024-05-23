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

    public ObservableList<Employee> getEmployees() throws SQLException {
        employees.clear(); // Очистити список перед завантаженням нових даних
        employees.addAll(EmployeeLogic.getAllEmployees());
        return employees;
    }

    public List<Employee> getAllEmployeesSimple() {
        List<Employee> employees = null;
        try {
            employees = EmployeeLogic.getAllEmployees();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }


    public void assignCountryEmployee(Country country, Employee employee) throws SQLException {
        employeeLogic.assignCountryEmployee(country, employee);
        countries.add(country);
    }

    public void assignTeamEmployee(Team team, Employee employee) throws SQLException {
        employeeLogic.assignTeamEmployee(team, employee);
        teams.add(team);
    }

    public Employee createEmployee(Employee employee) throws SQLException {
        Employee e = EmployeeLogic.createEmployee(employee);
        employees.add(e);
        return e;
    }

    public void deleteEmployee(Employee employee) throws SQLException {
        employeeLogic.deleteEmployee(employee);
        employees.remove(employee);
    }

    public List<Employee> getEmployeesByListIds(List<Integer> listCountryIds, List<Integer> teamIds) throws SQLException {
        employees.clear();
        employees.addAll(EmployeeLogic.getAllEmployeesByFilters(listCountryIds, teamIds));
        return employees;
    }

    public void updateEmployee(Employee employee) throws SQLException {
        employeeLogic.updateEmployee(employee);
        int index = employees.indexOf(employee);
        if (index >= 0) {
            employees.set(index, employee);
        }
    }

    public Employee getEmployeeByUsername(String username) throws SQLException {
        return employeeLogic.getEmployeeByUsername(username);
    }

    public boolean isValidEmployee(String inputUsername, String inputPassword) throws SQLException {
        Employee employee = getEmployeeByUsername(inputUsername);
        return employee != null && employee.getPassword().equals(inputPassword);
    }
}
