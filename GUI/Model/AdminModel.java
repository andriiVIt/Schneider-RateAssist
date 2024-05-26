package GUI.Model;

import BE.Admin;
import BLL.AdminLogic;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class AdminModel {
    AdminLogic adminLogic = new AdminLogic();
    private ObservableList<Admin> admins = FXCollections.observableArrayList();

// Constructor that initializes the admin list from the database

    public AdminModel(){
        try{
            admins.addAll(adminLogic.getAllAdmins());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    // Checks if the provided username and password match any admin in the list

    public boolean isValidAdmin(String inputUsername, String inputPassword){
        return admins.stream().anyMatch(admin -> admin.getUsername().equals(inputUsername) && admin.getPassword().equals(inputPassword)) ;
    }
}
