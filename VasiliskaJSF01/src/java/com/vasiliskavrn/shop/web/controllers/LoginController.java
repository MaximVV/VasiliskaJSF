package com.vasiliskavrn.shop.web.controllers;


import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean
@RequestScoped
public class LoginController {

    public LoginController() {
    }

    public String login() {
        return "goods";
    }


}