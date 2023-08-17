package telran.view.test;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.function.BiFunction;

import telran.view.InputOutput;
import telran.view.Item;
import telran.view.Menu;

public class DateSubmenu extends Menu {

	public DateSubmenu(String name) {
		super(name);

	}

	public  Item[] getDateItems() {
		Item[] items = { 
				Item.of("Date after a given number of days", io -> calculate(io, LocalDate::plusDays)),
				Item.of("Date before a given number of days", io -> calculate(io, LocalDate::minusDays)),
				Item.of("Days between two dates", io -> calculateBetween(io)),
				Item.ofExit() };
		return items;
	}

	private  void calculate(InputOutput io, BiFunction<LocalDate, Long, LocalDate> action) {
		LocalDate first = io.readDate("Enter date in format yyyy-mm-dd", "Must be date in ISO format");
		long second = io.readLong("Count of days", "Must be number");
		io.writeLine(action.apply(first, second));
	}

	private  void calculateBetween(InputOutput io) {
		LocalDate first = io.readDate("Enter date in format yyyy-mm-dd", "Must be date in ISO format");
		LocalDate second = io.readDate("Enter date in format yyyy-mm-dd", "Must be date in ISO format");
		io.writeLine(ChronoUnit.DAYS.between(first, second));
	}

}
