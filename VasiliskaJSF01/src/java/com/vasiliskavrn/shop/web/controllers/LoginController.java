package com.vasiliskavrn.shop.web.controllers;


import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@RequestScoped
public class LoginController {

    public LoginController() {
    }

    public String login() {
        return "goods";
    }

    public String exit(){  
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "exit";
    }

}