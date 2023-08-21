package telran.employees;

import telran.employees.controller.CompanyController;
import telran.employees.service.Company;
import telran.employees.service.CompanyImpl;
import telran.view.ConsoleInputOutput;
import telran.view.Item;
import telran.view.Menu;

import java.util.ArrayList;

public class CompanyAppl {
    private static final String DEFAULT_FILE_NAME ="employees.data";
    private static String fileName;
    public static void main(String[] args) {

        fileName = args.length > 0? args[0]: DEFAULT_FILE_NAME;
        Company company = new CompanyImpl();
        company.restore(fileName);
        ArrayList<Item> companyItems = CompanyController.getCompanyItems(company);
        companyItems.add(Item.of("Exit & save", io -> company.save(fileName), true));
        Menu menu = new Menu("Company Application", companyItems);
                menu.perform(new ConsoleInputOutput());
    }
}
