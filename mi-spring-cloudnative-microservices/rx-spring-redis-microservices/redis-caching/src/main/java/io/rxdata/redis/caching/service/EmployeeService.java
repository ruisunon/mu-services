package io.rxdata.redis.caching.service;

import io.rxdata.redis.caching.Employee;

import java.util.List;
import java.util.Map;

public interface EmployeeService {

    void save(Employee employee);
    void update(Employee employee);
    void delete(Long id);
    Employee findById(Long id);
    List<Employee> getAllEmployees(String key);
    List<Employee> findEmployeeById(List<Long> id);
    Map<Long, Employee> getAll();
}