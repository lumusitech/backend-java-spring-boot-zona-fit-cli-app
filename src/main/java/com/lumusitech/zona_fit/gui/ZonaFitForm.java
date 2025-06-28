package com.lumusitech.zona_fit.gui;

import com.lumusitech.zona_fit.model.Customer;
import com.lumusitech.zona_fit.service.CustomerService;
import com.lumusitech.zona_fit.service.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@Component
public class ZonaFitForm extends JFrame {
    ICustomerService customerService;
    private JPanel mainPanel;
    private JTable customersTable;
    private DefaultTableModel customersTableModel;
    private JTextField nameTextField;
    private JTextField lastnameTextField;
    private JTextField membershipTextField;
    private JButton saveButton;
    private JButton deleteButton;
    private JButton clearButton;
    private Integer customerId;

    @Autowired
    public ZonaFitForm(CustomerService customerService) {
        this.customerService = customerService;
        initForm();
        saveButton.addActionListener(e -> this.addCustomer());
        clearButton.addActionListener(e -> this.clearAll());
        deleteButton.addActionListener(e -> this.deleteCustomer());
        customersTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                loadCustomerFromTable();
            }
        });
    }

    private void addCustomer() {
        if (this.nameTextField.getText().isBlank()) {
            showMsg("Debe proporcionar el nombre del cliente");
            this.nameTextField.requestFocusInWindow();
            return;
        }

        if (this.lastnameTextField.getText().isBlank()) {
            showMsg("Debe proporcionar el apellido del cliente");
            this.lastnameTextField.requestFocusInWindow();
            return;
        }

        Integer membership = null;
        try {
            if (this.membershipTextField.getText().isBlank()) {
                showMsg("Debe proporcionar el número de membresía del cliente");
                this.membershipTextField.requestFocusInWindow();
                return;
            }

            membership = Integer.parseInt(this.membershipTextField.getText());
        } catch (Exception e) {
            showMsg("La membresía debe ser un valor numérico entero");
            this.membershipTextField.requestFocusInWindow();
            return;
        }

        String name = this.nameTextField.getText();
        String lastname = this.lastnameTextField.getText();
        Customer customer = new Customer(this.customerId, name, lastname, membership);
        this.customerService.save(customer);
        listCustomers();

        if (this.customerId == null) {
            clearAll();
            this.showMsg("Cliente guardado exitosamente");
            this.nameTextField.requestFocusInWindow();
        } else {
            clearAll();
            this.showMsg("Cliente actualizado exitosamente");
        }
    }

    private void clearAll() {
        this.customerId = null;
        this.nameTextField.setText("");
        this.lastnameTextField.setText("");
        this.membershipTextField.setText("");
        this.customersTable.getSelectionModel().clearSelection();
    }

    private void loadCustomerFromTable() {
        var selectedRow = customersTable.getSelectedRow();
        if (selectedRow != -1) {
            var id = this.customersTable.getModel().getValueAt(selectedRow, 0).toString();
            this.customerId = Integer.parseInt(id);
            var name = this.customersTable.getModel().getValueAt(selectedRow, 1).toString();
            this.nameTextField.setText(name);
            var lastname = this.customersTable.getModel().getValueAt(selectedRow, 2).toString();
            this.lastnameTextField.setText(lastname);
            var membership = this.customersTable.getModel().getValueAt(selectedRow, 3).toString();
            this.membershipTextField.setText(membership);

        }
    }

    private void deleteCustomer() {
        if (this.customerId == null) {
            this.showMsg("Debe seleccionar un cliente de la tabla a eliminar");
            return;
        }
        Customer customer = new Customer();
        customer.setId(this.customerId);

        this.customerService.delete(customer);
        this.listCustomers();
        this.clearAll();
        this.showMsg("Cliente eliminado");
        this.nameTextField.requestFocusInWindow();
    }

    private void showMsg(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    private void initForm() {
        setContentPane(this.mainPanel);
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        // this.customersTableModel = new DefaultTableModel(0, 4);
        // Avoid edit cells
        this.customersTableModel = new DefaultTableModel(0, 4) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        String[] headersTable = {"ID", "Nombre", "Apellido", "Membresía"};
        this.customersTableModel.setColumnIdentifiers(headersTable);
        this.customersTable = new JTable(this.customersTableModel);
        // Only allow select one row at once
        this.customersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listCustomers();
    }

    private void listCustomers() {
        this.customersTableModel.setRowCount(0);
        var customers = this.customerService.findAll();
        customers.forEach(customer -> {
            Object[] customerRow = {
                    customer.getId(),
                    customer.getName(),
                    customer.getLastname(),
                    customer.getMembership()
            };
            this.customersTableModel.addRow(customerRow);
        });
    }
}
