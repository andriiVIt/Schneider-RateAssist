package DAL.Interface;

import BE.Country;
import BE.Employee;
import BE.Team;

import java.sql.SQLException;
import java.util.List;

public interface IEmployeeDAO {
    List<Employee> getAllEmployees() throws SQLException;
    void deleteEmployee(Employee employee) throws SQLException;
    Employee createEmployee(Employee employee) throws SQLException;
    List<Employee> getAllEmployeesByFilters(List<Integer> listCountryIds, List<Integer> listTeamIds) throws SQLException;
    void assignCountryEmployee(Employee employee, Country country) throws SQLException;
    void assignTeamEmployee(Employee employee, Team team) throws SQLException;
    void updateEmployee(Employee employee) throws SQLException;
    Employee getEmployeeByUsername(String username) throws SQLException;
    void updateEmployeeCredentials(int employeeId, String newUsername, String newPassword) throws SQLException;
}

