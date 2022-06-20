package forms;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;

import config.DBConfig;
import models.Employee;
import services.EmployeeService;

public class EmployeeForm {

	public JFrame frmEmployeeEntry;
	private JTextField txtName;
	private JTextField txtAddress;
	private JTextField txtSearch;
	private EmployeeService employeeService;
	private Employee employee;
	private JTextField txtSalary;
	private JTable tblEmployee;
	private DefaultTableModel dtm = new DefaultTableModel();

	private final DBConfig dbConfig = new DBConfig();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EmployeeForm window = new EmployeeForm();
					window.frmEmployeeEntry.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public EmployeeForm() {
		initialize();
		this.initializeDependency();
		this.setTableDesign();
		this.loadAllEmployees();
	}

	private void initializeDependency() {
		this.employeeService = new EmployeeService();
	}

	private void setTableDesign() {
		dtm.addColumn("ID");
		dtm.addColumn("Name");
		dtm.addColumn("Phone");
		dtm.addColumn("Address");
		this.tblEmployee.setModel(dtm);
	}

	private void loadAllEmployees() {
		this.dtm = (DefaultTableModel) this.tblEmployee.getModel();
		this.dtm.getDataVector().removeAllElements();
		this.dtm.fireTableDataChanged();

		try (Statement st = this.dbConfig.getConnection().createStatement()) {

			String query = "SELECT * FROM emp";

			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				Object[] dataRow = { rs.getInt("emp_id"), rs.getString("name"), rs.getString("address"),
						rs.getString("salary") };

				dtm.addRow(dataRow);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		this.tblEmployee.setModel(dtm);
	}

	private void resetFormData() {
		txtName.setText("");
		txtAddress.setText("");
		txtSalary.setText("");
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmEmployeeEntry = new JFrame();
		frmEmployeeEntry.setTitle("Employee Entry");
		frmEmployeeEntry.setBounds(100, 100, 1000, 500);
		frmEmployeeEntry.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmEmployeeEntry.getContentPane().setLayout(null);

		JLabel lblName = new JLabel("Name");
		lblName.setHorizontalAlignment(SwingConstants.LEFT);
		lblName.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblName.setBounds(47, 39, 85, 29);
		frmEmployeeEntry.getContentPane().add(lblName);

		txtName = new JTextField();
		txtName.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txtName.setColumns(10);
		txtName.setBounds(47, 78, 193, 29);
		frmEmployeeEntry.getContentPane().add(txtName);

		JLabel lblAddress = new JLabel("Address");
		lblAddress.setHorizontalAlignment(SwingConstants.LEFT);
		lblAddress.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblAddress.setBounds(47, 126, 85, 29);
		frmEmployeeEntry.getContentPane().add(lblAddress);

		txtAddress = new JTextField();
		txtAddress.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txtAddress.setColumns(10);
		txtAddress.setBounds(47, 165, 193, 29);
		frmEmployeeEntry.getContentPane().add(txtAddress);

		JButton btnSave = new JButton("Save");
		btnSave.setMnemonic('S');
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Employee employee = new Employee();
				employee.setName(txtName.getText());
				employee.setAddress(txtAddress.getText());
				employee.setSalary(Long.parseLong(txtSalary.getText()));

				if (!employee.getName().isBlank() && !employee.getAddress().isBlank() && employee.getSalary() != 0) {

					employeeService.createEmployee(employee);
					resetFormData();
					loadAllEmployees();

				} else {
					JOptionPane.showMessageDialog(null, "Enter Required Field");
				}
			}
		});
		btnSave.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnSave.setBounds(47, 309, 193, 29);
		frmEmployeeEntry.getContentPane().add(btnSave);

		txtSearch = new JTextField();
		txtSearch.setToolTipText("");
		txtSearch.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txtSearch.setColumns(10);
		txtSearch.setBounds(669, 78, 165, 29);
		frmEmployeeEntry.getContentPane().add(txtSearch);

		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});
		btnSearch.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnSearch.setBounds(854, 78, 85, 29);
		frmEmployeeEntry.getContentPane().add(btnSearch);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(276, 132, 662, 292);
		frmEmployeeEntry.getContentPane().add(scrollPane);

		tblEmployee = new JTable();
		tblEmployee.setFont(new Font("Tahoma", Font.PLAIN, 15));
		scrollPane.setViewportView(tblEmployee);
		this.tblEmployee.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
			if (!tblEmployee.getSelectionModel().isSelectionEmpty()) {

				String id = tblEmployee.getValueAt(tblEmployee.getSelectedRow(), 0).toString();

				employee = employeeService.findEmployeeById(id);

				txtName.setText(employee.getName());
				txtAddress.setText(employee.getAddress());
				txtSalary.setText(employee.getSalary() + "");

			}
		});

		JLabel lblSalary = new JLabel("Salary");
		lblSalary.setHorizontalAlignment(SwingConstants.LEFT);
		lblSalary.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblSalary.setBounds(47, 212, 85, 29);
		frmEmployeeEntry.getContentPane().add(lblSalary);

		txtSalary = new JTextField();
		txtSalary.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txtSalary.setColumns(10);
		txtSalary.setBounds(47, 251, 193, 29);
		frmEmployeeEntry.getContentPane().add(txtSalary);

		JButton btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				employee.setName(txtName.getText());
				employee.setAddress(txtAddress.getText());
				employee.setSalary(Long.parseLong(txtSalary.getText()));
				employee.setAddress(txtAddress.getText());
				if (!employee.getName().isBlank() && !employee.getAddress().isBlank() && employee.getSalary() != 0) {

					employeeService.updateEmployee(String.valueOf(employee.getEmp_id()), employee);
					resetFormData();
					loadAllEmployees();
					employee = null;

				}
			}
		});
		btnUpdate.setMnemonic('U');
		btnUpdate.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnUpdate.setBounds(47, 358, 193, 29);
		frmEmployeeEntry.getContentPane().add(btnUpdate);

		JButton btnDelete = new JButton("Delete");
		btnDelete.setMnemonic('D');
		btnDelete.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnDelete.setBounds(47, 406, 193, 29);
		frmEmployeeEntry.getContentPane().add(btnDelete);

	}
}
