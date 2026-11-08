import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;



interface Taxable {
double TAX_RATE = 0.15;        
double calculateTax();
}

interface BonusCalculable {
double calculateBonus(double performanceScore);
}



abstract class Employee implements Comparable<Employee>, Taxable, BonusCalculable {
private String id;
private String name;
protected double baseSalary;
private LocalDate joinDate;

public Employee(String id, String name, double baseSalary, LocalDate joinDate) {
    this.id = id;
    this.name = name;
    this.baseSalary = baseSalary;
    this.joinDate = joinDate;
}


public abstract double calculateNetSalary();


@Override
public int compareTo(Employee other) {
    return Double.compare(other.calculateNetSalary(), this.calculateNetSalary());
}


@Override
public double calculateTax() {
    return calculateNetSalary() * TAX_RATE;
}

@Override
public abstract double calculateBonus(double performanceScore);

public String getId() { return id; }
public String getName() { return name; }
public double getBaseSalary() { return baseSalary; }
public LocalDate getJoinDate() { return joinDate; }

@Override
public String toString() {
    return String.format("%-10s | %-12s | %-18s | Net: %,10.2f | Tax: %,9.2f",
            id, name, getClass().getSimpleName(), calculateNetSalary(), calculateTax());
}
}



class FullTimeEmployee extends Employee {
private double allowance;

public FullTimeEmployee(String id, String name, double baseSalary,
                        LocalDate joinDate, double allowance) {
    super(id, name, baseSalary, joinDate);
    this.allowance = allowance;
}

@Override
public double calculateNetSalary() {
    return baseSalary + allowance;
}


@Override
public double calculateBonus(double performanceScore) {
    return baseSalary * 0.10 * performanceScore;
}
}


class ContractorEmployee extends Employee {
private int hourlyRate;
private int hoursWorked;

public ContractorEmployee(String id, String name, LocalDate joinDate,
                          int hourlyRate, int hoursWorked) {
    super(id, name, 0, joinDate);   
    this.hourlyRate = hourlyRate;
    this.hoursWorked = hoursWorked;
}

@Override
public double calculateNetSalary() {
    return (double) hourlyRate * hoursWorked;
}

@Override
public double calculateBonus(double performanceScore) {
    return performanceScore > 4.0 ? 500.0 : 0.0;
}
}



public class PayrollSystem {

public static void main(String[] args) {
    
    List<Employee> employees = new ArrayList<>();
    employees.add(new FullTimeEmployee("FT-001", "Ahmed", 5000, LocalDate.of(2018, 3, 15), 800));
    employees.add(new FullTimeEmployee("FT-002", "Salma", 6500, LocalDate.of(2015, 7, 1), 1000));
    employees.add(new FullTimeEmployee("FT-003", "Omar", 4200, LocalDate.of(2021, 1, 10), 500));
    employees.add(new ContractorEmployee("CT-001", "Khaled", LocalDate.of(2020, 5, 20), 40, 160));
    employees.add(new ContractorEmployee("CT-002", "Mariam", LocalDate.of(2022, 9, 5), 55, 120));

    
    Collections.sort(employees);
    System.out.println("=== الترتيب حسب صافي الراتب (تنازلي) ===");
    employees.forEach(System.out::println);

    Comparator<Employee> byJoinDate = Comparator.comparing(Employee::getJoinDate);
    employees.
            sort(byJoinDate);
    System.out.println("\n=== الترتيب حسب تاريخ الانضمام (الأقدم أولاً) ===");
    employees.forEach(e -> System.out.println(e + " | Joined: " + e.getJoinDate()));

    double performanceScore = 4.5;
    double totalTax = 0;
    double totalBonus = 0;

    for (Employee e : employees) {
        totalTax += e.calculateTax();
        totalBonus += e.calculateBonus(performanceScore);
    }

    System.out.println("\n=== الإجماليات (درجة التقييم = " + performanceScore + ") ===");
    System.out.printf("Total Tax   : %,.2f%n", totalTax);
    System.out.printf("Total Bonus : %,.2f%n", totalBonus);

    System.out.println("\n=== تفاصيل خاصة بكل نوع ===");
    for (Employee e : employees) {
        if (e instanceof FullTimeEmployee) {
            FullTimeEmployee ft = (FullTimeEmployee) e;
            System.out.println(ft.getName() + " -> موظف دوام كامل، الأساسي: " + ft.getBaseSalary());
        } else if (e instanceof ContractorEmployee) {
            ContractorEmployee ce = (ContractorEmployee) e;
            System.out.println(ce.getName() + " -> متعاقد، الأساسي: " + ce.getBaseSalary());
        }
    }
}
}