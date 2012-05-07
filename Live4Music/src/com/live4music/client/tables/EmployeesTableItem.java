package com.live4music.client.tables;

/**
 * 
 * Employee item for the employees table data structure
 * 
 * holds single employee data:
 * - employee id
 * - employee first name
 * - employee last name
 * - date of employment
 * - date of birth
 * - address
 * - phone
 * - cell phone
 * - employing store id
 * - position
 */
public class EmployeesTableItem {
	
	private int employeeID;
	private String firstName;
	private String lastName;
	private String employmentDate;
	private String birthDate;
	private String address;
	private String phone;
	private String cellPhone;
	private int storeID;
	private EmployeePositionsEnum position;
	
	/**
	 * constructor for an employee table item
	 * @param employeeID
	 * @param storeID
	 */
	public EmployeesTableItem(int employeeID, int storeID){
		this.employeeID = employeeID;
		this.storeID = storeID;
	}
	
	/**
	 * constructor for an employee table item
	 * accepts all employee details
	 * @param employeeID
	 * @param firstName
	 * @param lastName
	 * @param employmentDate
	 * @param birthDate
	 * @param address
	 * @param phone
	 * @param cellPhone
	 * @param storeID
	 * @param position
	 */
	public EmployeesTableItem(int employeeID, String firstName, String lastName, String employmentDate,
			String birthDate, String address, String phone, String cellPhone, int storeID, EmployeePositionsEnum position){
		this.employeeID = employeeID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.employmentDate = employmentDate;
		this.birthDate = birthDate;
		this.address = address;
		this.phone = phone;
		this.cellPhone = cellPhone;
		this.storeID = storeID;
		this.position = position;
	}

	/////////////////////////
	// getters and setters //
	/////////////////////////
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmploymentDate() {
		return employmentDate;
	}

	public void setEmploymentDate(String employmentDate) {
		this.employmentDate = employmentDate;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCellPhone() {
		return cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}

	public EmployeePositionsEnum getPosition() {
		return position;
	}

	public void setPosition(EmployeePositionsEnum position) {
		this.position = position;
	}

	public int getEmployeeID() {
		return employeeID;
	}

	public int getStoreID() {
		return storeID;
	}
}
