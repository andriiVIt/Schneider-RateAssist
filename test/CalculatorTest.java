package test;

import BE.Employee;
import GUI.Controller.CalculatorController;
import GUI.Model.CalculationModel;
import GUI.Controller.EmployeeInfoController;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CalculatorTest extends ApplicationTest {

    private CalculatorController calculatorController;
    private Employee mockEmployee;
    private CalculationModel mockCalculationModel;
    private EmployeeInfoController mockEmployeeInfoController;

    @Override
    public void start(Stage stage) {
        // This method is required by TestFX, but can remain empty for this test
    }

    @BeforeEach
    void setUp() throws Exception {
        calculatorController = new CalculatorController();
        calculatorController.markupField = new TextField();
        calculatorController.gmField = new TextField();
        calculatorController.hourlyRateLabel = new Label();

        mockEmployee = mock(Employee.class);
        mockCalculationModel = mock(CalculationModel.class);
        mockEmployeeInfoController = mock(EmployeeInfoController.class);

        calculatorController.setEmployee(mockEmployee);
        calculatorController.setCalculationModel(mockCalculationModel);
        calculatorController.setEmployeeInfoController(mockEmployeeInfoController);
    }

    @Test
    void calculateHourlyRate() throws Exception {
        // Create a latch to wait for the JavaFX Application Thread to complete
        CountDownLatch latch = new CountDownLatch(1);

        // Setting up mock employee data
        when(mockEmployee.getSalary()).thenReturn(500000.0); // Example: 500000 DKK/year
        when(mockEmployee.getOverheadPercentage()).thenReturn(0.2); // Example: 20%
        when(mockEmployee.getFixedAmount()).thenReturn(40000.0); // Example: 40000 DKK
        when(mockEmployee.getWorkHours()).thenReturn(2000.0); // Example: 2000 hours/year
        when(mockEmployee.getUtilization()).thenReturn(0.8); // Example: 80%

        // Setting up input fields
        Platform.runLater(() -> {
            try {
                calculatorController.markupField.setText("10"); // Example: 10%
                calculatorController.gmField.setText("15"); // Example: 15%

                // Calling the method to test
                calculatorController.calculateHourlyRate();
            } finally {
                latch.countDown();
            }
        });

        // Wait for the JavaFX Application Thread to finish
        latch.await();

        // Expected calculation
        double salary = 500000.0;
        double overhead = 0.2;
        double fixedAmount = 40000.0;
        double workHours = 2000.0;
        double utilization = 0.8;
        double markup = 0.1;
        double gm = 0.15;

        double cost = salary * (1 + overhead) + fixedAmount;
        double effectiveHours = workHours * utilization;
        double baseRate = cost / effectiveHours;
        double calculatedRate = baseRate * (1 + markup + gm);

        // Assertion to check if the calculated rate is correct
        assertEquals(String.format("%.2f", calculatedRate), calculatorController.hourlyRateLabel.getText(), "Calculated hourly rate is incorrect");

        // Print results to console
        System.out.println("Test calculateHourlyRate:");
        System.out.println("Expected rate: " + String.format("%.2f DKK", calculatedRate) );
        System.out.println("Calculated rate: " + String.format("%.2f DKK", Double.parseDouble(calculatorController.hourlyRateLabel.getText())));

    }

    @Test
    void saveRate() throws Exception {
        // Create a latch to wait for the JavaFX Application Thread to complete
        CountDownLatch latch = new CountDownLatch(1);

        // Setting up mock employee data
        when(mockEmployee.getId()).thenReturn(1);

        // Directly setting the calculated rate for the test
        Platform.runLater(() -> {
            try {
                calculatorController.calculatedRate = 500.0;

                // Calling the method to test
                calculatorController.saveRate();
            } finally {
                latch.countDown();
            }
        });

        // Wait for the JavaFX Application Thread to finish
        latch.await();

        // Verify if createCalculation was called with correct parameters
        verify(mockCalculationModel).createCalculation(any());

        // Verify if updateCalculatedRate was called with correct parameters
        verify(mockEmployeeInfoController).updateCalculatedRate(500.0);

        // Print results to console
        System.out.println("Test saveRate:");
        System.out.println("Calculated rate saved: " + calculatorController.calculatedRate);
    }
}
