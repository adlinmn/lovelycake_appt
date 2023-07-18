package com.heroku.java.order.dao;

import com.heroku.java.bean.Appointment;
import java.sql.Date;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AppointmentDAO {
    private Connection connection;

    public AppointmentDAO(Connection connection) {
        this.connection = connection;
    }

    public void saveAppointment(Appointment appointment) throws SQLException {
        String query = "INSERT INTO appointments (appt_name, custemail, appt_phone, appt_address, date, day, message) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, appointment.getAppt_name());
            preparedStatement.setString(2, appointment.getCustemail());
            preparedStatement.setString(3, appointment.getAppt_phone());
            preparedStatement.setString(4, appointment.getAppt_address());
            preparedStatement.setDate(5, appointment.getDate());
            preparedStatement.setString(6, appointment.getDay());
            preparedStatement.setString(7, appointment.getMessage());

            preparedStatement.executeUpdate();
        }
    }
    public Appointment getLatestAppointment() throws SQLException {
    String query = "SELECT * FROM appointments ORDER BY date DESC LIMIT 1";

    try (PreparedStatement preparedStatement = connection.prepareStatement(query);
         ResultSet resultSet = preparedStatement.executeQuery()) {
        if (resultSet.next()) {
            Appointment appointment = new Appointment();
            appointment.setAppt_name(resultSet.getString("appt_name"));
            appointment.setCustemail(resultSet.getString("custemail"));
            appointment.setAppt_phone(resultSet.getString("appt_phone"));
            appointment.setAppt_address(resultSet.getString("appt_address"));
            appointment.setDate(resultSet.getDate("date"));
            appointment.setDay(resultSet.getString("day"));
            appointment.setTime(resultSet.getTime("time"));
            appointment.setMessage(resultSet.getString("message"));

            return appointment;
        }
    }

    return null;
}

}

