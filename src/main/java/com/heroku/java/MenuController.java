package com.heroku.java;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

@Controller
public class MenuController {
    private final DataSource dataSource;

    @Autowired
    public MenuController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostMapping("/createMenu")
    public String addMenu(HttpSession session, @ModelAttribute("menu") Menu menu) {
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
    public String addMenu(HttpSession session, Menu menu, Model model) {
        return "createMenu";
    }

    @GetMapping("/viewMenuAdmin")
public String viewMenuAdmin(HttpSession session, Model model) {
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
        model.addAttribute("menus", menus);
        return "viewMenuAdmin";
    } catch (SQLException sqe) {
        sqe.printStackTrace();
        return "error";
    } catch (Exception e) {
        e.printStackTrace();
        return "error";
    }
}
    
  @GetMapping("/updateMenu")
  public String showUpdateForm(@RequestParam("menuId") String menuId, Model model) {
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

        Menu menu = new Menu(menu_id, menu_name, menu_desc, price);
        model.addAttribute("menu", menu);
      }
      connection.close();
      return "updateMenu";
    } catch (SQLException sqe) {
      sqe.printStackTrace();
      return "error";
    } catch (Exception e) {
      e.printStackTrace();
      return "error";
    }
  }

  @PostMapping("/updateMenu")
  public String updateMenu(@ModelAttribute("menu") Menu menu) {
    try (Connection connection = dataSource.getConnection()) {
      String sql = "UPDATE menu SET menu_name = ?, menu_desc = ?, price = ? WHERE menu_id = ?";
      PreparedStatement statement = connection.prepareStatement(sql);

      statement.setString(1, menu.getMenu_name());
      statement.setString(2, menu.getMenu_desc());
      statement.setFloat(3, menu.getPrice());
      statement.setString(4, menu.getMenu_id());

      statement.executeUpdate();

      connection.close();
      return "redirect:/viewMenuAdmin";
    } catch (SQLException sqe) {
      sqe.printStackTrace();
      return "error";
    } catch (Exception e) {
      e.printStackTrace();
      return "error";
    }
  }
}
