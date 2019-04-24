package com.geekbrains.main;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.geekbrains.entity.Employee;

import java.io.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class MainClass {
    private static BusinessDB db;
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        try (BusinessDB db = new BusinessDB()) {
            Scanner scanner = new Scanner(System.in);
            String[] input = scanner.nextLine().toLowerCase().split(" ");
            while (input[0].equals("/exit")) {

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
