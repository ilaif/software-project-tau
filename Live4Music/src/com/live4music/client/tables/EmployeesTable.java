package com.live4music.client.tables;

import java.util.*;

/**
 * created by Ariel
 * 
 * employees table data structure
 * ==============================
 * hold employees entries for current store:
 * - store id
 * - store's employees list
 */
public class EmployeesTable extends Table {
	private int storeID;
	private Map<Integer,EmployeesTableItem> employees = new HashMap<Integer,EmployeesTableItem>();
	
	/**
	 * constructor for employees table without employees table as input
	 * @param storeID
	 * @param employees
	 */
	public EmployeesTable(int storeID){
		super("EmployeesTable");
		this.storeID = storeID;
	}
	
	/**
	 * constructor for employees table with employees table
	 * @param storeID
	 * @param employees
	 */
	public EmployeesTable(int storeID, HashMap<Integer,EmployeesTableItem> employees){
		super("EmployeesTable");
		this.storeID = storeID;
		this.employees = employees;
	}

	/////////////////////////////////
	// adders, getters and setters //
	/////////////////////////////////
	
	/**
	 * employee adder
	 */
	public void addEmployee(EmployeesTableItem e){
		employees.put(e.getEmployeeID(), e);
	}
	
	/**
	 * explicit employee adder (with all employee fields) 
	 * @param employeeID
	 * @param firstName
	 * @param lastName
	 * @param employmentDate
	 * @param birthDate
	 * @param address
	 * @param phone
	 * @param cellPhone
	 * @param position
	 */
	public void addEmployee(int employeeID, String firstName, String lastName, String employmentDate,
			String birthDate, String address, String phone, String cellPhone, EmployeePositionsEnum position){
		EmployeesTableItem e = new EmployeesTableItem(employeeID, firstName, lastName, employmentDate,
				birthDate, address, phone, cellPhone, this.storeID, position);
		this.employees.put(employeeID, e);
	}
	
	/**
	 * remove employee
	 * @param employeeID
	 */
	public void removeEmployee(int employeeID){
		this.employees.remove(employeeID);
	}
	
	/**
	 * employee getter
	 * @param employeeID
	 * @return
	 */
	public EmployeesTableItem getEmployee(int employeeID){
		return employees.get(employeeID);
	}
	
	public Map<Integer,EmployeesTableItem> getEmployees() {
		return employees;
	}

	public void setEmployees(Map<Integer, EmployeesTableItem> employees) {
		this.employees = employees;
	}

	public int getStoreID() {
		return storeID;
	}
	
	
}
