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


@Controller
public class AdminController {
    private final DataSource dataSource;

    @Autowired
    public AdminController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostMapping("/createAccAdmin")
    public String addAccAdmin(HttpSession session, @ModelAttribute("createAccAdmin") Admin admin) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "INSERT INTO admin (id, email, password) VALUES (?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, admin.getId());
            statement.setString(2, admin.getEmail());
            statement.setString(3, admin.getPassword());

            statement.executeUpdate();

            connection.close();
            return "redirect:/adminlogin";
        } catch (SQLException sqe) {
            sqe.printStackTrace();
            return "redirect:/";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }
    
    @GetMapping("/createAccAdmin")
    public String addAdmin(HttpSession session, Admin admin, Model model) {
        return "createAccAdmin";
    }



    @PostMapping("/adminlogin")
public String adminlogin(HttpSession session, @ModelAttribute("adminlogin") Admin admin, Model model) {
    try (Connection connection = dataSource.getConnection()) {
        String sql = "SELECT id, email, password FROM admin WHERE id=? AND email=? AND password=?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, admin.getId());
        statement.setString(2, admin.getEmail());
        statement.setString(3, admin.getPassword());
        ResultSet resultSet = statement.executeQuery();

        String returnPage = "/adminlogin";

        if (resultSet.next()) {
            session.setAttribute("id", admin.getId());
            session.setAttribute("email", admin.getEmail());
            returnPage = "redirect:/adminHomePage";
        }

        connection.close();
        return returnPage;
    } catch (Throwable t) {
        t.printStackTrace();
        return "/adminlogin";
    }
}

@GetMapping("/adminHomePage")
public String adminHomePage(HttpSession session, Model model) {
    String id = (String) session.getAttribute("id");

    if (id != null) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT id, email, password FROM admin WHERE id=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String viewId = resultSet.getString("id");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");

                Admin viewAccAdmin = new Admin(viewId, email, password);
                model.addAttribute("viewAccAdmin", viewAccAdmin);
                return "viewAccAdmin";
            } else {
                return "error";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    return "error";
}

@GetMapping("viewAccAdmin")
public String viewAccAdmin(Model model, HttpSession session) {
    String email = (String) session.getAttribute("email");

    if (email != null) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT id, email, password FROM admin WHERE email=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String viewId = resultSet.getString("id");
                String viewEmail = resultSet.getString("email");
                String password = resultSet.getString("password");

                Admin viewAccAdmin = new Admin(viewId, viewEmail, password);
                model.addAttribute("viewAccAdmin", viewAccAdmin);
                return "viewAccAdmin";
            } else {
                return "error";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    return "error";
}




    @PostMapping("/viewAccAdmin")
    public String viewAccAdmin(HttpSession session, Model model) {
        String id = (String) session.getAttribute("id");

        if (id != null) {
            try (Connection connection = dataSource.getConnection()) {
                String sql = "SELECT id, email, password FROM admin WHERE id=?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, id);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    String viewId = resultSet.getString("id");
                    String email = resultSet.getString("email");
                    String password = resultSet.getString("password");

                    System.out.println("id from db: " + viewId);
                    Admin viewAccAdmin = new Admin(viewId, email, password);
                    model.addAttribute("viewAccAdmin", viewAccAdmin);
                    System.out.println("Session viewAccAdmin: " + model.getAttribute("viewAccAdmin"));
                    return "viewAccAdmin";
                } else {
                    return "error";
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return "error";
    }

    @PostMapping("/updateAccAdmin")
    public String updateAccAdmin(HttpSession session, @ModelAttribute("updateAccAdmin") Admin admin, Model model) {
        String id = admin.getId();
        String email = admin.getEmail();
        String password = admin.getPassword();

        try (Connection connection = dataSource.getConnection()) {
            String sql = "UPDATE admin SET id=?, email=?, password=? WHERE id=?";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, id);
            statement.setString(2, email);
            statement.setString(3, password);
            statement.setString(4, id);

            statement.executeUpdate();

            return "viewAccAdmin";
        } catch (Throwable t) {
            t.printStackTrace();
            return "/adminlogin";
        }
    }

    @PostMapping("/deleteAccAdmin")
    public String deleteAccAdmin(HttpSession session, Model model) {
        String id = (String) session.getAttribute("id");

        if (id != null) {
            try (Connection connection = dataSource.getConnection()) {
                String sql = "DELETE FROM admin WHERE id=?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, id);

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
