package com.heroku.java.order.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.heroku.java.bean.Menu;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Component
public class MenuDAO {
    private final DataSource dataSource;

    @Autowired
    public MenuDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void addMenu(Menu menu) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "INSERT INTO menu (menu_id, menu_name, menu_desc, price) VALUES (?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, menu.getMenu_id());
            statement.setString(2, menu.getMenu_name());
            statement.setString(3, menu.getMenu_desc());
            statement.setFloat(4, menu.getPrice());

            statement.executeUpdate();
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Menu> getAllMenus() {
        ArrayList<Menu> menus = new ArrayList<>();
        try (Connection con = dataSource.getConnection()) {
            String sql = "SELECT * FROM menu";
            PreparedStatement statement = con.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String menu_id = rs.getString("menu_id");
                String menu_name = rs.getString("menu_name");
                String menu_desc = rs.getString("menu_desc");
                float price = rs.getFloat("price");

                Menu menu = new Menu(menu_id, menu_name, menu_desc, price);
                menus.add(menu);
            }
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return menus;
    }

    public Menu getMenuById(String menuId) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT * FROM menu WHERE menu_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, menuId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                String menu_id = rs.getString("menu_id");
                String menu_name = rs.getString("menu_name");
                String menu_desc = rs.getString("menu_desc");
                float price = rs.getFloat("price");

                return new Menu(menu_id, menu_name, menu_desc, price);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Return null if the menu item is not found or an error occurs
    }

    public void updateMenu(Menu menu) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "UPDATE menu SET menu_name = ?, menu_desc = ?, price = ? WHERE menu_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, menu.getMenu_name());
            statement.setString(2, menu.getMenu_desc());
            statement.setFloat(3, menu.getPrice());
            statement.setString(4, menu.getMenu_id());

            statement.executeUpdate();
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteMenu(String menuId) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "DELETE FROM menu WHERE menu_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, menuId);
            statement.executeUpdate();
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
