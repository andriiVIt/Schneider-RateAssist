package BLL;

import BE.Admin;
import DAL.AdminDAO;

import java.sql.SQLException;
import java.util.List;

public class AdminLogic {
    AdminDAO adminDAO = new AdminDAO();
    // Method to retrieve all Admin records from the database
    public List<Admin> getAllAdmins() throws SQLException {

        return adminDAO.getAllAdmins();
}
}
