package BLL;

import BE.Employee;
import DAL.EmployeeDAO;

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

    public void deleteEmployee(Employee employee) throws SQLException {
        employeeDAO.deleteEmployee(employee);
    }
}
