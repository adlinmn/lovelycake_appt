package com.heroku.java.order.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.heroku.java.bean.Customer;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class CustomerDAO {
    private final DataSource dataSource;

    @Autowired
    public CustomerDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void addCustomer(Customer customer) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "INSERT INTO customer (custname, custaddress, custemail, custpassword) VALUES (?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, customer.getName());
            statement.setString(2, customer.getAddress());
            statement.setString(3, customer.getEmail());
            statement.setString(4, customer.getPassword());

            statement.executeUpdate();
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Customer getCustomerByEmail(String email) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT custname, custaddress, custemail, custpassword FROM customer WHERE custemail=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String custname = resultSet.getString("custname");
                String custaddress = resultSet.getString("custaddress");
                String custpassword = resultSet.getString("custpassword");

                return new Customer(custname, custaddress, email, custpassword);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void updateCustomer(Customer customer) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "UPDATE customer SET custname=?, custaddress=?, custpassword=? WHERE custemail=?";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, customer.getName());
            statement.setString(2, customer.getAddress());
            statement.setString(3, customer.getPassword());
            statement.setString(4, customer.getEmail());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteCustomer(String email) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "DELETE FROM customer WHERE custemail=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, email);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
