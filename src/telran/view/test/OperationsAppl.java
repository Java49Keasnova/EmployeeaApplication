package telran.view.test;


import telran.view.ConsoleInputOutput;
import telran.view.InputOutput;
import telran.view.Item;
import telran.view.Menu;

public class OperationsAppl {

	public static void main(String[] args) {
	InputOutput io = new ConsoleInputOutput();
	CalculatorSubmenu subCalculator = new CalculatorSubmenu("Simple calculator");
	DateSubmenu subDates = new DateSubmenu("Date Operations");
		Menu mainMenu= new Menu("Operations",getMainItems(subCalculator, subDates));
		mainMenu.perform(io);

	}

	private static Item[] getMainItems(CalculatorSubmenu subCalculator, DateSubmenu subDates) {
		
		Menu calculator = new Menu(subCalculator.displayName(), CalculatorSubmenu.getCalculatorItems());
		Menu dateOperatons = new Menu(subDates.displayName(), DateSubmenu.getDateItems());

		return new Item[]{ Item.of(calculator.displayName(), calculator::perform),
							Item.of(dateOperatons.displayName(), dateOperatons::perform),
						    Item.ofExit()
		};
	}

	
}