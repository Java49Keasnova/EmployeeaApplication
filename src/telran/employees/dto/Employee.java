package telran.employees.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public record Employee(long id, String name,
                       String department, int salary, LocalDate birthDate) implements Serializable {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee employee)) return false;
        return id == employee.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}