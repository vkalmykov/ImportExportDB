package com.geekbrains.main;

import com.geekbrains.entity.AddInfo;
import com.geekbrains.entity.Employee;

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;


public class BusinessDB implements AutoCloseable {
    private Connection connection;
    private PreparedStatement insertEmployee;
    private PreparedStatement insertAddInfo;
    private PreparedStatement getAverageFromPosition;

    public BusinessDB() throws ClassNotFoundException, SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:business.db");
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException("Драйвер не обнаружен. Подключение не удалось");
        } catch (SQLException e) {
            throw new SQLException("Ошибка подключения");
        }
        insertEmployee = connection.prepareStatement("INSERT INTO employee (name, position, age, salary, addinfo.id) VALUES (?, ?, ?, ?, ?");
        insertAddInfo = connection.prepareStatement("INSERT INTO addinfo (phone_number, address) VALUES (?, ?)");
        getAverageFromPosition = connection.prepareStatement("SELECT AVG(salary) FROM (SELECT salary FROM employee WHERE position='?')");
    }

    public void close() {
        try {
            insertAddInfo.close();
            insertEmployee.close();
            connection.close();
        } catch (SQLException ignored) {
        }
    }

    public boolean insertEmploeyy(Employee employee) throws SQLException {
        //1? - name
        //2? - position
        //3? - age
        //4? - salary
        //5? - ref addinfo.id
        try {
            insertEmployee.setString(1, employee.getName());
            insertEmployee.setString(2, employee.getPosition());
            insertEmployee.setInt(3, employee.getAge());
            insertEmployee.setBigDecimal(4, BigDecimal.valueOf(employee.getSalary()));
            if (employee.getAddInfo() == null) {
                insertEmployee.setNull(5, Types.INTEGER);
            } else {
                insertEmployee.setInt(5, employee.getAddInfo().getId());
            }
            return insertEmployee.execute();
        } catch (SQLException e) {
            throw new SQLException("Insert Fail");
        }
    }

    public boolean insertAddInfo(AddInfo addInfo) throws SQLException {
        //1? - phone_number
        //2? - address
        try {
            insertEmployee.setString(1, addInfo.getPhoneNumber());
            insertEmployee.setString(2, addInfo.getAddress());
            return insertEmployee.execute();
        } catch (SQLException e) {
            throw new SQLException("Insert fail");
        }
    }

    public List<Employee> getEmployee() {
        ResultSet resultSet = null;
        List<Employee> employeeList = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            resultSet = statement.executeQuery("SELECT * FROM addinfo");
            Map<Integer, AddInfo> addInfoMap = new HashMap<>();
            while (resultSet.next()) {
                addInfoMap.put(resultSet.getInt(1),
                        new AddInfo(resultSet.getInt(1),
                                resultSet.getString(2),
                                resultSet.getString(3)));
            }
            addInfoMap.put(0, null);
            resultSet = statement.executeQuery("SELECT * FROM employee");
            Employee employee;
            while (resultSet.next()) {
                employee = new Employee(resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getInt(4),
                        resultSet.getBigDecimal(5).floatValue(),
                        addInfoMap.get(resultSet.getInt(6)));
                employeeList.add(employee);
            }
        } catch (SQLException e) {

        } finally {
            try {
                resultSet.close();
            } catch (SQLException e) {

            }
        }
        return employeeList;
    }

    public List<AddInfo> getAddInfo() {
        List<AddInfo> addInfoList = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM addinfo")) {
            while (resultSet.next()) {
                addInfoList.add(new AddInfo(resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return addInfoList;
    }

    public float getAvareageSalary() throws SQLException {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT AVG(salary) FROM employee")) {
            resultSet.next();
            return resultSet.getBigDecimal(1).floatValue();
        }
    }

    public float getAvareageSalary(String position) throws SQLException {
        getAverageFromPosition.setString(1, position);
        try (ResultSet resultSet = getAverageFromPosition.executeQuery()) {
            resultSet.next();
            return resultSet.getBigDecimal(1).floatValue();
        }
    }
}
