package GUI.Controller;

import BE.Employee;
import GUI.Model.EmployeeModel;
import javafx.scene.control.ScrollPane;

import java.util.function.Consumer;

public class EmployeeInfoController {


    private Consumer<Employee> onDeleteEmployeeCallback;

    public void setModel(EmployeeModel employeeModel, ScrollPane scrollPane) {
    }

    public void setEmployee(Employee employee) {
    }

    public void setOnDeleteEmployeeCallback(Consumer<Employee> onDeleteEmployeeCallback) {
        this.onDeleteEmployeeCallback = onDeleteEmployeeCallback;
    }
}
