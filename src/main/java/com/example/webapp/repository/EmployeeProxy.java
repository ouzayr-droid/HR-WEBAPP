package com.example.webapp.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.example.webapp.CustomProperties;
import com.example.webapp.model.Employee;

import io.micrometer.core.ipc.http.HttpSender.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class EmployeeProxy {
    @Autowired
    private CustomProperties props; 
    /**
    * Get all employees
    * @return An iterable of all employees
    */

    public Iterable<Employee> getEmployees()
    {
        String baseApiUrl = props.getApiUrl();
        String getEmployeesUrl = baseApiUrl+"/employees";

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Iterable<Employee>> response = restTemplate.exchange(
            getEmployeesUrl,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<Iterable<Employee>>(){}
        );
        log.debug("Appel à la création d'un employer "+response.getStatusCode().toString());

        return response.getBody();
    }

    /**
     * Get an employee by id
     * @param id The id of the employee
     * @return The employee which matches the id
    */
    public Employee getEmployees(int id) {
        String baseApiUrl = props.getApiUrl();
        // On construit l'URL spécifique : http://localhost:9000/employee/1
        String getEmployeeUrl = baseApiUrl + "/employee/" + id;

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Employee> response = restTemplate.exchange(
                getEmployeeUrl,
                HttpMethod.GET,
                null,
                Employee.class
        );

        log.debug("Get Employee call " + response.getStatusCode().toString());

        return response.getBody();
    }

    public Employee creatEmployee(Employee e)
    {
        String baseApiUrl = props.getApiUrl();
        String createEmployeeUrl = baseApiUrl+"/employee";

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Employee> request = new HttpEntity<Employee>(e);
        ResponseEntity<Employee> response = restTemplate.exchange(
            createEmployeeUrl,
            HttpMethod.POST,
            request,
            Employee.class
        );

        log.debug("Create Employee call "+response.getStatusCode().toString());

        return response.getBody();
    }

    /**
     * Update an existing employee
     * @param e The employee to update
     * @return The updated employee returned by the API
    */
   public Employee updateEmployee(Employee e)
   {
        String baseApiUrl = props.getApiUrl();
        String updateEmployeeUrl = baseApiUrl+"/employee/"+e.getId();

        RestTemplate restTemplate = new RestTemplate();

        // On prépare le corps de la requête (le JSON de l'employé modifié)
        HttpEntity<Employee> request = new HttpEntity<Employee>(e);
        ResponseEntity<Employee> response = restTemplate.exchange(
            updateEmployeeUrl, 
            HttpMethod.PUT, // On utilise PUT pour une modification complète
            request, 
            Employee.class
        );

        log.debug("Update Employee call " + response.getStatusCode().toString());
        
        return response.getBody();
   }

    /**
     * Delete an employee using exchange method of RestTemplate
     * @param id The id of the employee to delete
    */
    public void deleteEmployee(int id)
    {
        String baseApiUrl = props.getApiUrl();
        String deleteEmployeeUrl = baseApiUrl+"/employee/"+id;

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Void> response = restTemplate.exchange
        (
            deleteEmployeeUrl,
            HttpMethod.DELETE,
            null,
            Void.class
        );

        log.debug("Instruction de suppresion d'un employé envoyé avec succès "+response.getStatusCode().toString());
    }
}
