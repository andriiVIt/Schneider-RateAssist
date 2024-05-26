package test;

import BE.Employee;
import GUI.Model.CalculationModel;
import GUI.Controller.CalculatorController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CalculatorControllerTest {

    private CalculatorController calculatorController;
    private Employee mockEmployee;
    private CalculationModel mockCalculationModel;
    private Label mockHourlyRateLabel;
    private TextField mockMarkupField;
    private TextField mockGmField;

    @BeforeEach
    public void setUp() {
        calculatorController = new CalculatorController();

        // Mocking dependencies
        mockEmployee = Mockito.mock(Employee.class);
        mockCalculationModel = Mockito.mock(CalculationModel.class);
        mockHourlyRateLabel = Mockito.mock(Label.class);
        mockMarkupField = Mockito.mock(TextField.class);
        mockGmField = Mockito.mock(TextField.class);

        // Setting up the controller
        calculatorController.setEmployee(mockEmployee);
        calculatorController.setCalculationModel(mockCalculationModel);
        calculatorController.hourlyRateLabel = mockHourlyRateLabel;
        calculatorController.markupField = mockMarkupField;
        calculatorController.gmField = mockGmField;
    }

    @Test
    public void testCalculateHourlyRate() {
        when(mockEmployee.getSalary()).thenReturn(50000.0);
        when(mockEmployee.getOverheadPercentage()).thenReturn(0.2);
        when(mockEmployee.getFixedAmount()).thenReturn(5000.0);
        when(mockEmployee.getWorkHours()).thenReturn(2000.0);
        when(mockEmployee.getUtilization()).thenReturn(0.8);
        when(mockMarkupField.getText()).thenReturn("10");
        when(mockGmField.getText()).thenReturn("5");

        calculatorController.calculateHourlyRate();

        verify(mockHourlyRateLabel).setText(anyString());
    }

    @Test
    public void testSaveRate() throws SQLException {
        when(mockEmployee.getId()).thenReturn(1);
        calculatorController.calculatedRate = 100.0;

        calculatorController.saveRate();

        verify(mockCalculationModel).createCalculation(any());
    }

    @Test
    public void testShowErrorOnInvalidInput() {
        when(mockMarkupField.getText()).thenReturn("invalid");
        when(mockGmField.getText()).thenReturn("invalid");

        calculatorController.calculateHourlyRate();

        verify(mockHourlyRateLabel, never()).setText(anyString());
    }
}