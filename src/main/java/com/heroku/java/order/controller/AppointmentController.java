package com.heroku.java.order.controller;

import com.heroku.java.bean.Appointment;
import com.heroku.java.order.dao.AppointmentDAO;

import javax.sql.DataSource;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalTime;
import java.sql.Time;

@WebServlet("/appointment")
public class AppointmentController extends HttpServlet {
    private AppointmentDAO appointmentDao;
    private DataSource dataSource;

    @Autowired
    private void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            Connection connection = dataSource.getConnection();
            appointmentDao = new AppointmentDAO(connection);
        } catch (SQLException e) {
            throw new ServletException("Error initializing the AppointmentController", e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String appt_name = request.getParameter("name");
        String custemail = request.getParameter("email");
        String appt_phone = request.getParameter("phone");
        String appt_address = request.getParameter("address");
        String date = request.getParameter("date");
        String message = request.getParameter("message");

        Appointment appointment = new Appointment();
        appointment.setAppt_name(appt_name);
        appointment.setCustemail(custemail);
        appointment.setAppt_phone(appt_phone);
        appointment.setAppt_address(appt_address);
        appointment.setDate(java.sql.Date.valueOf(date));
        appointment.setMessage(message);

        try {
            appointmentDao.saveAppointment(appointment);
            response.sendRedirect("createAppointment.html?success=true");
        } catch (SQLException e) {
            throw new ServletException("Error saving the appointment", e);
        }
    }
}
