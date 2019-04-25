package com.geekbrains.main;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.geekbrains.entity.AddInfo;
import com.geekbrains.entity.Employee;

import java.io.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class MainClass {
    private static BusinessDB db;
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        final String help = "/exit - выйти из приложения\n/addEmployee имя должность зарплата возраст ид_поля_доп.информация - добавить работника\n" +
                "/addInfo номер_телефона адрес - добавить информацию в бд\n/showAllEmploeyy - вывести всех работников\n/showAvgSalary - средняя з/п по всем работникам\n" +
                "/showAvgSalaryByPos должность - вывести среднюю з/п по должности\n/findByPhoneNumber номер_телефона - найти работника по номеру телефона";
        try (BusinessDB db = new BusinessDB()) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Введите /exit для выхода");
            String[] input = scanner.nextLine().split(" ");
            while (!input[0].equals("/exit")) {
                if (input[0].equalsIgnoreCase("/help")) {
                    System.out.println(help);
                } else if (input[0].equalsIgnoreCase("/addinfo")) {
                    AddInfo addInfo = new AddInfo();
                    try {
                        addInfo.setPhoneNumber(input[1]);
                        addInfo.setAddress(input[2]);
                        db.insertAddInfo(addInfo);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.out.println("Недостаточно аргументов");
                    }
                } else if (input[0].equalsIgnoreCase("/addEmployee")) {
                    Employee employee = new Employee();
                    try {
                        employee.setName(input[1]);
                        employee.setPosition(input[2]);
                        employee.setSalary(Integer.valueOf(input[3]));
                        employee.setAge(Integer.valueOf(input[4]));
                        employee.setAddInfo(new AddInfo(Integer.valueOf(input[5]),"", ""));
                        db.insertEmploeyy(employee);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.out.println("Недостаточно аргументов");
                    }
                } else if (input[0].equalsIgnoreCase("/showAllEmploeyy")) {
                    List<Employee> employeeList = db.getEmployee();
                    for (Employee employee : employeeList) {
                        System.out.print(employee.getId() + " " + employee.getName() + " " + employee.getAge() + " " + employee.getPosition() + " " + employee.getSalary());
                        if (employee.getAddInfo().getId() != 0) {
                            System.out.print(" " + employee.getAddInfo().getAddress() + " " + employee.getAddInfo().getPhoneNumber());
                        }
                        System.out.println();
                    }
                }
                input = scanner.nextLine().split(" ");
            }
        }
    }
    public static void exportInDB() throws IOException, SQLException {
        File file = new File("exportFromFile.xml");
        XmlMapper xmlMapper = new XmlMapper();
        List<Employee> employeeList = xmlMapper.readValue(new FileInputStream(file), new TypeReference<List<Employee>>(){});
        db.insertEmploeyy(employeeList);
    }
    public static void exportFromDB() throws IOException {
        List<Employee> employeeList = db.getEmployee();
        File file = new File("exportFromDB.txt");
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            for (Employee employee : employeeList) {
                out.writeObject(employee);
            }
        }

    }
}
