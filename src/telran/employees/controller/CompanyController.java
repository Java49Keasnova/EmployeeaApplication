package telran.employees.controller;

import telran.employees.dto.Employee;
import telran.employees.service.Company;
import telran.view.InputOutput;
import telran.view.Item;

import java.time.LocalDate;
import java.util.*;

public class CompanyController {
    private static final long MIN_ID = 100000 ;
    private static final long MAX_ID = 999999;
    private static final int MIN_SALARY = 6000;
    private static final int MAX_SALARY = 50000;
    private static final int MAX_AGE = 70;
    private static final int MIN_AGE = 20;

    private static Set<String> departments = new HashSet<>(Arrays.asList("QA", "Development", "Audit", " Management", "Accounting"));
    static Company company ;
    public static ArrayList<Item> getCompanyItems(Company company) {
        CompanyController.company = company;
        ArrayList<Item> res = new ArrayList<>(Arrays.asList(
                getItems()
        ));
        return res;
    }

    private static Item[] getItems() {
        return new Item[]{ Item.of("Add new Employee", CompanyController::addEmployeeItem),
                Item.of("Remove Employee", CompanyController::removeEmployeeItem),
                Item.of("All Employees", CompanyController::getEmployeesItem),
                Item.of("Data about Employee", CompanyController::getEmployeeItem),
                Item.of("Employees by salary", CompanyController::getEmployeesBySalaryItem),
                Item.of("Employees by department", CompanyController::getEmployeesByDepartmentItem),
                Item.of("Update salary", CompanyController::updateSalaryItem),
                Item.of("Update department", CompanyController::updateDepartmentItem),
                Item.of("Departments and salary", CompanyController::getDepartmentSalaryDistributionItem),
                Item.of("Distribution by salary", CompanyController::getSalaryDistributionItem),
                Item.of("Employees by age", CompanyController::getEmployeesByAgeItem),


        };
    }
    static void addEmployeeItem(InputOutput io){
        long id  = io.readLong("Enter Employee Identity", "Wrong identity wale", MIN_ID, MAX_ID);
        if(company.getEmployee(id) == null){
        String name = io.readString("Enter name", "Wrong Name", str -> str.matches("[A-Z][a-z]+"));
        String department = io.readString("Enter Department" + departments.toString(), "Wrong department",  departments);
        int salary = io.readInt("Enter Salary", "Wrong salary", MIN_SALARY, MAX_SALARY) ;
        LocalDate birthDate = io.readDate("Enter Birth Day", "Wrong birth Date Entered", getBirthDate(MAX_AGE), getBirthDate(MIN_AGE));
        company.addEmployee(new Employee(id, name, department, salary, birthDate));
       io.writeLine(String.format("Employee with ID %d has been added", id));
        }
         else {
            io.writeLine( String.format("Employee with ID %d already exists", id));
        }
    }

    private static LocalDate getBirthDate(int age) {
        return LocalDate.now().minusYears(age);
    }
    static void removeEmployeeItem(InputOutput io){
        long id  = io.readLong("Enter Employee Identity", "Wrong identity wale", MIN_ID, MAX_ID);
        Employee res = company.removeEmployee(id);
        io.writeLine(res != null ? res + " was removed from your company" :(String.format("Employee with ID %d doesn't work in this company", id) ));


    }
    static void getEmployeeItem (InputOutput io){
        long id  = io.readLong("Enter Employee Identity", "Wrong identity number", MIN_ID, MAX_ID);
        Employee res =company.getEmployee(id);
        io.writeLine( res == null ? String.format("Employee with ID %d doesn't work in this company", id) : res );
    }
  static void getEmployeesItem(InputOutput io){
       company.getEmployees().forEach(io::writeLine);
  }
  static void  getDepartmentSalaryDistributionItem(InputOutput io){
        company.getDepartmentSalaryDistribution().forEach(io::writeLine);

  }
    static void  getSalaryDistributionItem(InputOutput io){
        int interval = io.readInt("Enter  interval salary", "Wrong internal", 0, MAX_SALARY);
        company.getSalaryDistribution(interval).forEach(io::writeLine);

    }

    static void  getEmployeesByDepartmentItem(InputOutput io){
       String department = io.readString("Please choose department from list" + departments.toString() , "This department is not from list", departments);
       company.getEmployeesByDepartment(department).forEach(io::writeLine);

    }
    static void getEmployeesBySalaryItem(InputOutput io) {
        int salaryFrom = io.readInt("Enter salary from ", "Wrong salary", MIN_SALARY, MAX_SALARY);
        int salaryTo= io.readInt("Enter salary to ", "Wrong salary", MIN_SALARY, MAX_SALARY);
        if(salaryTo > salaryFrom){
            company.getEmployeesBySalary(salaryFrom, salaryTo).forEach(io::writeLine);
        } else {
            io.writeLine("The Salary to must be greater than salary from");
        }


    }
    static void getEmployeesByAgeItem(InputOutput io) {
        int ageFrom = io.readInt("Enter age from ", "Wrong age", MIN_AGE, MAX_AGE);
        int ageTo= io.readInt("Enter age to ", "Wrong age", MIN_AGE, MAX_AGE);
        if(ageTo > ageFrom){
            company.getEmployeesByAge(ageFrom, ageTo).forEach(io::writeLine);
        } else {
            io.writeLine("The Age to must be greater than Age from");
        }

    }
    static void updateSalaryItem(InputOutput io){
        long id  = io.readLong("Enter Employee Identity for update salary", "Wrong identity number", MIN_ID, MAX_ID);
        if(company.getEmployee(id) != null) {
            int newSalary = io.readInt("Enter new salary", "Wrong salary", MIN_SALARY, MAX_SALARY);
            Employee res = company.updateSalary(id, newSalary);
            io.writeLine(String.format("Employee with ID %d was updated %s", id, res));
        } else {
            io.writeLine((String.format("Employee with ID %d doesn't work in this company", id)));
        }

    }
    static void updateDepartmentItem(InputOutput io){
        long id  = io.readLong("Enter Employee Identity for update department", "Wrong identity number", MIN_ID, MAX_ID);
        if(company.getEmployee(id) != null) {
            String department = io.readString("Please choose department from list" , "This department not from list", departments);
            Employee res = company.updateDepartment(id, department);
            io.writeLine(String.format("Employee with ID %d was updated %s", id, res));
        } else {
            io.writeLine((String.format("Employee with ID %d doesn't work in this company", id)));
        }

    }
}
