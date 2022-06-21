package services;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import config.DBConfig;
import models.Employee;
import shared.mapper.EmployeeMapper;

public class EmployeeService {
    private final EmployeeMapper employeeMapper;
	private final DBConfig dbConfig;

	public EmployeeService() {
	    this.employeeMapper = new EmployeeMapper();
		this.dbConfig = new DBConfig();
	}

	public void createEmployee(Employee employee) {
		try {

			PreparedStatement ps = this.dbConfig.getConnection()
					.prepareStatement("INSERT INTO emp (name, address, salary) VALUES (?, ?, ?)");

			ps.setString(1, employee.getName());
			ps.setString(2, employee.getAddress());
			ps.setInt(3, (int) employee.getSalary());
			ps.executeUpdate();
			ps.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Employee findEmployeeById(String id) {
		Employee employee = new Employee();

		try (Statement st = this.dbConfig.getConnection().createStatement()) {

			String query = "SELECT * FROM emp WHERE emp_id = " + id + ";";

			ResultSet rs = st.executeQuery(query);

			if (rs.next()) {
				this.employeeMapper.mapToEmployee(employee, rs);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return employee;
	}
	
    public void updateEmployee(String id, Employee employee) {
        try {
            PreparedStatement ps = this.dbConfig.getConnection()
                    .prepareStatement("UPDATE emp SET name=?, address=?, salary=? WHERE emp_id=?");

            ps.setString(1, employee.getName());
            ps.setString(2, employee.getAddress());
            ps.setLong(3, employee.getSalary());
            ps.setString(4, id);

            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {

        	e.printStackTrace();
        }
    }

    public List<Employee> findAllEmployees() {

        List<Employee> employeeList = new ArrayList<>();
        
        try (Statement st = this.dbConfig.getConnection().createStatement()) {

            String query = "SELECT * FROM emp";

            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                Employee employee = new Employee();
                employeeList.add(this.employeeMapper.mapToEmployee(employee, rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return employeeList;

    }

}
