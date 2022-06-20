package shared.mapper;

import java.sql.ResultSet;

import models.Employee;

public class EmployeeMapper {

    public Employee mapToEmployee(Employee employee, ResultSet rs) {
        try {
            employee.setEmp_id(rs.getInt("emp_id"));
            employee.setName(rs.getString("name"));
            employee.setAddress(rs.getString("address"));
            employee.setSalary(rs.getLong("salary"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return employee;
    }
}
