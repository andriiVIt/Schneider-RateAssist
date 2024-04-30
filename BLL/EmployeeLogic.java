package BLL;

import BE.Employee;
import DAL.EmployeeDAO;

import java.sql.SQLException;

public class EmployeeLogic {
    static EmployeeDAO employeeDAO = new EmployeeDAO();


    public static Employee createEmployee(Employee employee)throws SQLException {
        return employeeDAO.createEmployee(employee);
    }

    public Employee getAllEmployees()throws SQLException {
        return employeeDAO.getAllEmployees();
    }

    public void deleteEmployee(Employee employee) throws SQLException{
        employeeDAO.deleteEmployee(employee);
    }
}
