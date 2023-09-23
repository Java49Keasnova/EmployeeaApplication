package telran.employees.service;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

import telran.employees.dto.DepartmentSalary;
import telran.employees.dto.Employee;
import telran.employees.dto.SalaryDistribution;

import java.time.LocalDate;
import java.util.*;

public class CompanyImpl implements Company {
    LinkedHashMap<Long, Employee> employees = new LinkedHashMap<>();
    TreeMap<Integer, Collection<Employee>> employeesSalary = new TreeMap<>();
    TreeMap<LocalDate, Collection<Employee>> employeesAge = new TreeMap<>();
    HashMap<String, Collection<Employee>> employeesDepartment = new HashMap<>();
    static ReentrantReadWriteLock employeesReantranRWLock = new ReentrantReadWriteLock();
    static ReentrantReadWriteLock employeesSalaryReantranRWLock = new ReentrantReadWriteLock();

    static ReentrantReadWriteLock employeesAgeReantranRWLock = new ReentrantReadWriteLock();
    static ReentrantReadWriteLock employeesDepartmentReantranRWLock = new ReentrantReadWriteLock();

    static Lock employeesReadLock = employeesReantranRWLock.readLock();
    static Lock employeesWrightLock = employeesReantranRWLock.writeLock();

    static Lock employeesSalaryReadLock = employeesSalaryReantranRWLock.readLock();
    static Lock employeesSalaryWrightLock = employeesSalaryReantranRWLock.writeLock();

    static Lock employeesAgeReadLock = employeesAgeReantranRWLock.readLock();
    static Lock employeesAgeWrightLock = employeesAgeReantranRWLock.writeLock();

    static Lock employeesDepartmentReadLock = employeesDepartmentReantranRWLock.readLock();
    static Lock employeesDepartmentWrightLock = employeesDepartmentReantranRWLock.writeLock();





    private void allWrigthLock(){
        employeesWrightLock.lock();
        employeesSalaryWrightLock.lock();
        employeesAgeWrightLock.lock();
        employeesDepartmentWrightLock.lock();
    }
    private void allWrightUnlock(){
        employeesWrightLock.unlock();
        employeesSalaryWrightLock.unlock();
        employeesAgeWrightLock.unlock();
        employeesDepartmentWrightLock.unlock();
    }


    @Override
    public boolean addEmployee(Employee empl) {
       try {
           allWrigthLock();
           boolean res = false;
           Employee emplRes = employees.putIfAbsent(empl.id(), empl);
           if (emplRes == null) {
               res = true;
               addEmployeeSalary(empl);
               addEmployeeAge(empl);
               addEmployeeDepartment(empl);
           }
           return res;
       }
       finally {
           allWrightUnlock();
       }
    }

    private <T> void addToIndex(Employee empl, T key, Map<T, Collection<Employee>> map) {
        map.computeIfAbsent(key, k -> new HashSet<>()).add(empl);
    }

    private void addEmployeeSalary(Employee empl) {
        addToIndex(empl, empl.salary(), employeesSalary);

    }

    private void addEmployeeAge(Employee empl) {
        addToIndex(empl, empl.birthDate(), employeesAge);

    }

    private void addEmployeeDepartment(Employee empl) {
        addToIndex(empl, empl.department(), employeesDepartment);

    }

    @Override
    public Employee removeEmployee(long id) {
        try {
            allWrigthLock();
            Employee res = employees.remove(id);
            if (res != null) {
                removeEmployeeSalary(res);
                removeEmployeeAge(res);
                removeEmployeeDepartment(res);
            }
            return res;
        } finally {
            allWrightUnlock();
        }
    }

    private <T> void removeFromIndex(Employee empl, T key, Map<T, Collection<Employee>> map) {

        Collection<Employee> employeesCol = map.get(key);
        employeesCol.remove(empl);
        if (employeesCol.isEmpty()) {
            map.remove(key);
        }
    }

    private void removeEmployeeSalary(Employee empl) {
        int salary = empl.salary();
        removeFromIndex(empl, salary, employeesSalary);

    }

    private void removeEmployeeAge(Employee empl) {
        removeFromIndex(empl, empl.birthDate(), employeesAge);

    }

    private void removeEmployeeDepartment(Employee empl) {
        removeFromIndex(empl, empl.department(), employeesDepartment);

    }

    @Override
    public Employee getEmployee(long id) {
    try{ employeesReadLock.lock();
        return employees.get(id);
    } finally {
        employeesReadLock.unlock();
    }
    }

    @Override
    public List<Employee> getEmployees() {
        try {employeesReadLock.lock();
            return new ArrayList<>(employees.values());
        } finally {
            employeesReadLock.unlock();
        }
    }

    @Override
    public List<DepartmentSalary> getDepartmentSalaryDistribution() {
    try {
        employeesReadLock.lock();
        return employees.values().stream()
                .collect(Collectors.groupingBy(Employee::department, Collectors.averagingInt(Employee::salary)))
                .entrySet().stream().map(e -> new DepartmentSalary(e.getKey(), e.getValue())).toList();
    } finally {
        employeesReadLock.unlock();
    }
    }

    @Override
    public List<SalaryDistribution> getSalaryDistribution(int interval) {
try {employeesReadLock.lock();
    return employees.values().stream()
            .collect(Collectors.groupingBy(e -> e.salary() / interval, Collectors.counting())).entrySet().stream()
            .map(e -> new SalaryDistribution(e.getKey() * interval, e.getKey() * interval + interval - 1,
                    e.getValue().intValue()))
            .sorted((sd1, sd2) -> Integer.compare(sd1.minSalary(), sd2.minSalary())).toList();
} finally {
    employeesReadLock.unlock();
}
    }

    @Override
    public List<Employee> getEmployeesByDepartment(String department) {
        try {employeesDepartmentReadLock.lock();
            Collection<Employee> employeesCol = employeesDepartment.get(department);
            ArrayList<Employee> res = new ArrayList<>();
            if (employeesCol != null) {
                res.addAll(employeesCol);
            }
            return res;
        } finally {
            employeesDepartmentReadLock.unlock();
        }
    }

    @Override
    public List<Employee> getEmployeesBySalary(int salaryFrom, int salaryTo) {
try {
    employeesSalaryReadLock.lock();
        return employeesSalary.subMap(salaryFrom, true, salaryTo, true).values().stream()
            .flatMap(col -> col.stream().sorted((empl1, empl2) -> Long.compare(empl1.id(), empl2.id())))
            .toList();
} finally {
    employeesSalaryReadLock.lock();

}
    }

    @Override
    public List<Employee> getEmployeesByAge(int ageFrom, int ageTo) {
        try { employeesAgeReadLock.lock();
        LocalDate dateTo = LocalDate.now().minusYears(ageFrom);
        LocalDate dateFrom = LocalDate.now().minusYears(ageTo);
        return employeesAge.subMap(dateFrom, true, dateTo, true).values().stream()
                .flatMap(col -> col.stream()
                        .sorted((empl1, empl2) -> Long.compare(empl1.id(), empl2.id())))
                .toList();
        } finally {
            employeesAgeReadLock.unlock();
        }
    }

    @Override
    public Employee updateSalary(long id, int newSalary) {
        try {
    allWrigthLock();
            Employee empl = removeEmployee(id);
            if (empl != null) {
                Employee newEmployee = new Employee(id, empl.name(),
                        empl.department(), newSalary, empl.birthDate());
                addEmployee(newEmployee);
            }
            return empl;
        }
        finally {
            allWrightUnlock();
        }
    }

    @Override
    public Employee updateDepartment(long id, String newDepartment) {
        try { allWrigthLock();
            Employee empl = removeEmployee(id);
            if (empl != null) {
                Employee newEmployee = new Employee(id, empl.name(),
                        newDepartment, empl.salary(), empl.birthDate());
                addEmployee(newEmployee);
            }
            return empl;
        } finally {
            allWrightUnlock();
        }
        }


}