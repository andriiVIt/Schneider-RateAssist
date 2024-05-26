package BLL;

import BE.Admin;
import DAL.AdminDAO;
import DAL.Interface.IAdminDAO;

import java.sql.SQLException;
import java.util.List;

public class AdminLogic {
    // Create an instance of the IAdminDAO interface using AdminDAO implementation
    IAdminDAO adminDAO = new AdminDAO();
    // Method to retrieve all Admin records from the database
    public List<Admin> getAllAdmins() throws SQLException {
      // Call the getAllAdmins method from the DAO to get the list of all admins
        return adminDAO.getAllAdmins();
}
}
