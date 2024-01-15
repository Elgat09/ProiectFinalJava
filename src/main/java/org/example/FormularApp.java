package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

class FormularJava extends JFrame {

    private JTextField textField;
    private JCheckBox checkBox;
    private JRadioButton radioButton1;
    private JRadioButton radioButton2;
    private ButtonGroup radioGroup;
    private JComboBox<String> comboBox;
    private JSpinner spinner;
    private DefaultTableModel tableModel;

    public FormularJava() {
        setTitle("Car Information Form");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initializeUI();


        String[] columnNames = {"Brand", "Model", "Year", "Fuel Type"};
        tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);


        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
        loadDataFromJson();
    }

    private void initializeUI() {

        setLayout(new GridLayout(7, 2, 10, 10));


        add(new JLabel("Brand:"));
        textField = new JTextField();
        add(textField);


        add(new JLabel("Year:"));
        spinner = new JSpinner();
        add(spinner);


        add(new JLabel("Fuel Type:"));
        String[] values = {"Gasoline", "Diesel", "Electric"};
        comboBox = new JComboBox<>(values);
        add(comboBox);


        add(new JLabel("Condition:"));
        radioButton1 = new JRadioButton("New");
        radioButton2 = new JRadioButton("Used");
        radioGroup = new ButtonGroup();
        radioGroup.add(radioButton1);
        radioGroup.add(radioButton2);
        JPanel radioPanel = new JPanel();
        radioPanel.add(radioButton1);
        radioPanel.add(radioButton2);
        add(radioPanel);


        JPanel checkBoxPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        add(new JLabel("Features:"));
        checkBox = new JCheckBox("Air Conditioning");
        checkBoxPanel.add(checkBox);


        JCheckBox checkBoxOption1 = new JCheckBox("Sunroof");
        checkBoxPanel.add(checkBoxOption1);
        add(checkBoxPanel);


        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveDataToJson();
                loadDataFromJson();
            }
        });
        add(saveButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        add(cancelButton, BorderLayout.SOUTH);
    }

    private void saveDataToJson() {

        JSONObject jsonData = new JSONObject();
        jsonData.put("Brand", textField.getText());
        jsonData.put("Condition", checkBox.isSelected() ? "New" : "Used");
        jsonData.put("Year", spinner.getValue());
        jsonData.put("Fuel Type", comboBox.getSelectedItem());
        jsonData.put("Option", radioButton1.isSelected() ? "Air Conditioning" : "Sunroof");


        try (FileWriter file = new FileWriter("date.json", true)) {
            file.write(jsonData.toJSONString() + "\n");
            file.flush();
            JOptionPane.showMessageDialog(this, "Data saved successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving data!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadDataFromJson() {

        tableModel.setRowCount(0);

        try (BufferedReader reader = new BufferedReader(new FileReader("date.json"))) {
            JSONParser jsonParser = new JSONParser();
            String line;
            while ((line = reader.readLine()) != null) {
                JSONObject jsonData = (JSONObject) jsonParser.parse(line);
                Object[] rowData = {
                        jsonData.get("Brand"),
                        jsonData.get("Model"),
                        jsonData.get("Year"),
                        jsonData.get("Fuel Type"),

                };
                tableModel.addRow(rowData);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data from JSON!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FormularJava();
            }
        });
    }
}
