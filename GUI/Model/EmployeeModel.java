package GUI.Model;

import BE.Employee;
import BLL.EmployeeLogic;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.List;

public class EmployeeModel {
    static EmployeeLogic employeeLogic = new EmployeeLogic();

    private static final ObservableList<Employee> employees = FXCollections.observableArrayList();

    public static ObservableList<Employee> getEmployees() throws SQLException {
        employees.addAll(EmployeeLogic.getAllEmployees());
        return employees;
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

}
