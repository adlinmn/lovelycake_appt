package com.heroku.java.order.dao;

import com.heroku.java.bean.Appointment;
import java.sql.Date;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
}

