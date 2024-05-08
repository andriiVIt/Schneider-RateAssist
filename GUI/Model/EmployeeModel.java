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

    private  ObservableList<Employee> employees = FXCollections.observableArrayList();


    public ObservableList<Employee> getEmployees() throws SQLException {
        employees.clear(); // Очистити список перед завантаженням нових даних
        employees.addAll(EmployeeLogic.getAllEmployees());
        return employees;
    }


    public Employee createEmployee(Employee employee, Team team, Country country) throws SQLException {
        Employee e = EmployeeLogic.createEmployee(employee , team, country);
        employees.add(e);
        return e;
    }
    public void deleteEmployee(Employee employee) throws SQLException {
        employeeLogic.deleteEmployee(employee);
        employees.remove(employee);
    }


    public List<Employee> getEmployeesByListIds(List<Integer> listCountryIds) throws SQLException {
        employees.clear();
        employees.addAll(EmployeeLogic.getAllEmployeesByFilters(listCountryIds));
        return employees;
    }
}
