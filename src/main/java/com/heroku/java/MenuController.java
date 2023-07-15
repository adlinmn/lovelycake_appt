package com.heroku.java;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Controller
public class MenuController {
    private final DataSource dataSource;

    @Autowired
    public MenuController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostMapping("/createMenu")
    public String addMenu(HttpSession session, @ModelAttribute("menu") Menus menu) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "INSERT INTO menu (menu_id, menu_name, menu_desc, price) VALUES (?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, menu.getMenu_id());
            statement.setString(2, menu.getMenu_name());
            statement.setString(3, menu.getMenu_desc());
            statement.setFloat(4, menu.getPrice());

            statement.executeUpdate();

            connection.close();
            return "redirect:/viewMenuAdmin";
        } catch (SQLException sqe) {
            sqe.printStackTrace();
            return "redirect:/";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }

    @GetMapping("/createMenu")
    public String addMenu(HttpSession session, Menus menu, Model model) {
        return "createMenu";
    }

    @PostMapping("/viewMenuAdmin")
    public String viewMenuAdmin(HttpSession session, Model model) {
        String menuId = (String) session.getAttribute("menu_id");

        if (menuId != null) {
            try (Connection connection = dataSource.getConnection()) {
                String sql = "SELECT menu_id, menu_name, menu_desc, price FROM menu WHERE menu_id=?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, menuId);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    String menuName = resultSet.getString("menu_name");
                    String menuDesc = resultSet.getString("menu_desc");
                    float price = resultSet.getFloat("price");

                    System.out.println("menu_name from db: " + menuName);
                    Menus viewMenuAdmin = new Menus(menuId, menuName, menuDesc, price);
                    model.addAttribute("viewMenuAdmin", viewMenuAdmin);
                    System.out.println("Session viewMenuAdmin: " + model.getAttribute("viewMenuAdmin"));
                    return "viewMenu";
                } else {
                    return "error";
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return "error";
    }

    @PostMapping("/updateMenu")
    public String updateMenu(HttpSession session, @ModelAttribute("updateMenu") Menus menu, Model model) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "UPDATE menu SET menu_name=?, menu_desc=?, price=? WHERE menu_id=?";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, menu.getMenu_name());
            statement.setString(2, menu.getMenu_desc());
            statement.setFloat(3, menu.getPrice());
            statement.setString(4, menu.getMenu_id());

            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                // Update successful
                return "redirect:/viewMenu";
            } else {
                // Update failed
                model.addAttribute("error", "Failed to update menu");
                return "error";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            model.addAttribute("error", "Failed to update menu");
            return "error";
        }
    }

    @PostMapping("/deleteMenu")
    public String deleteMenu(HttpSession session, Model model) {
        String menuId = (String) session.getAttribute("menu_id");

        if (menuId != null) {
            try (Connection connection = dataSource.getConnection()) {
                String sql = "DELETE FROM menu WHERE menu_id=?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, menuId);

                int rowsAffected = statement.executeUpdate();

                if (rowsAffected > 0) {
                    session.invalidate();
                    return "redirect:/adminlogin";
                } else {
                    System.out.println("Delete failed");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return "deleteError";
            }
        }

        return "deleteError";
    }
}
