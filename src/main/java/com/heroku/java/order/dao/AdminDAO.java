package com.heroku.java.order.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.heroku.java.bean.Admin;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class AdminDAO {
    private final DataSource dataSource;

    @Autowired
    public AdminDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void addAdmin(Admin admin) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "INSERT INTO admin (admin_id, admin_email, admin_password) VALUES (?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, admin.getId());
            statement.setString(2, admin.getEmail());
            statement.setString(3, admin.getPassword());

            statement.executeUpdate();
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean authenticateAdmin(Admin admin) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT admin_id FROM admin WHERE admin_id=? AND admin_email=? AND admin_password=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, admin.getId());
            statement.setString(2, admin.getEmail());
            statement.setString(3, admin.getPassword());
            ResultSet resultSet = statement.executeQuery();

            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public Admin getAdminById(String adminId) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT admin_id, admin_email, admin_password FROM admin WHERE admin_id=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, adminId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String adminEmail = resultSet.getString("admin_email");
                String adminPassword = resultSet.getString("admin_password");

                return new Admin(adminId, adminEmail, adminPassword);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void updateAdmin(Admin admin) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "UPDATE admin SET admin_email=?, admin_password=? WHERE admin_id=?";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, admin.getEmail());
            statement.setString(2, admin.getPassword());
            statement.setString(3, admin.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAdmin(String adminId) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "DELETE FROM admin WHERE admin_id=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, adminId);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

