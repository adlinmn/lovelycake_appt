package com.heroku.java.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.heroku.java.bean.Admin;
import jakarta.servlet.http.HttpSession;
import com.heroku.java.order.dao.AdminDAO;

@Controller
public class AdminController {
    private final AdminDAO adminDAO;

    @Autowired
    public AdminController(AdminDAO adminDAO) {
        this.adminDAO = adminDAO;
    }

    @PostMapping("/createAccAdmin")
    public String addAccAdmin(HttpSession session, @ModelAttribute("createAccAdmin") Admin admin) {
        adminDAO.addAdmin(admin);
        return "redirect:/adminlogin";
    }

    @PostMapping("/adminlogin")
    public String adminlogin(HttpSession session, @ModelAttribute("adminlogin") Admin admin, Model model) {
        boolean authenticated = adminDAO.authenticateAdmin(admin);

        if (authenticated) {
            session.setAttribute("admin_id", admin.getId());
            session.setAttribute("admin_email", admin.getEmail());
            return "redirect:/loginHomeAdmin";
        }

        return "redirect:/adminlogin";
    }

    @GetMapping("/adminHomePage")
    public String adminHomePage(HttpSession session, Model model) {
        String admin_id = (String) session.getAttribute("admin_id");

        if (admin_id != null) {
            Admin admin = adminDAO.getAdminById(admin_id);

            if (admin != null) {
                model.addAttribute("viewAccAdmin", admin);
                return "viewAccAdmin";
            } else {
                return "error";
            }
        }

        return "error";
    }

    @GetMapping("viewAccAdmin")
    public String viewAccAdmin(Model model, HttpSession session) {
        String admin_id = (String) session.getAttribute("admin_id");

        if (admin_id != null) {
            Admin admin = adminDAO.getAdminById(admin_id);

            if (admin != null) {
                model.addAttribute("viewAccAdmin", admin);
                return "viewAccAdmin";
            } else {
                return "error";
            }
        }

        return "error";
    }

    @PostMapping("/updateAccAdmin")
    public String updateAccAdmin(HttpSession session, @ModelAttribute("updateAccAdmin") Admin admin, Model model) {
        admin.setId((String) session.getAttribute("admin_id"));
        adminDAO.updateAdmin(admin);
        return "redirect:/viewAccAdmin";
    }

    @PostMapping("/deleteAccAdmin")
    public String deleteAccAdmin(HttpSession session, Model model) {
        String admin_id = (String) session.getAttribute("admin_id");

        if (admin_id != null) {
            adminDAO.deleteAdmin(admin_id);
            return "redirect:/adminlogin";
        }

        return "deleteError";
    }

    @GetMapping("/loginHomeAdmin")
    public String loginHomeAdmin() {
        return "loginHomeAdmin";
    }

    @PostMapping("/viewAccAdmin")
    public String viewAccAdmin(HttpSession session, Model model) {
        String admin_id = (String) session.getAttribute("admin_id");

        if (admin_id != null) {
            Admin admin = adminDAO.getAdminById(admin_id);

            if (admin != null) {
                model.addAttribute("viewAccAdmin", admin);
                return "viewAccAdmin";
            } else {
                return "error";
            }
        }

        return "error";
    }
}

