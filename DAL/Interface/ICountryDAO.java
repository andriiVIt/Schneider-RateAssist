package DAL.Interface;

import BE.Country;
import java.sql.SQLException;
import java.util.List;

public interface ICountryDAO {
    List<Country> getAllCountries() throws SQLException;
    Country createCountry(Country country) throws SQLException;
    void deleteRatesByCountryId(int countryId) throws SQLException;
    void deleteCountry(Country country) throws SQLException;
    void updateCountryEmployee(int employeeId, int countryId) throws SQLException;
    List<Country> getCountriesForTeamId(int teamId) throws SQLException;
}