package DAL.Interface;

import BE.Admin;
import java.sql.SQLException;
import java.util.List;

public interface IAdminDAO {
    List<Admin> getAllAdmins() throws SQLException;
}