package telran.employees.dto;

import java.io.Serializable;

public record SalaryDistribution(int minSalary, int maxSalary, int amountEmployee) implements Serializable {
}
