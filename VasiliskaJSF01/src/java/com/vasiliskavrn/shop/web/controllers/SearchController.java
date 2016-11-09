package com.vasiliskavrn.shop.web.controllers;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import com.vasiliskavrn.shop.web.enums.SearchType;

@ManagedBean
@SessionScoped
public class SearchController implements Serializable {

    private SearchType searchType;
    private static Map<String, SearchType> searchList = new HashMap<String, SearchType>();

    public SearchController() {
        ResourceBundle bundle = ResourceBundle.getBundle("com.vasiliskavrn.shop.web.nls.messages", FacesContext.getCurrentInstance().getViewRoot().getLocale());
        searchList.put(bundle.getString("for_all"), SearchType.FOR_ALL);
        searchList.put(bundle.getString("for_boy"), SearchType.FOR_BOY);
        searchList.put(bundle.getString("for_girl"), SearchType.FOR_GIRL);
        searchList.put(bundle.getString("for_man"), SearchType.FOR_MAN);
        searchList.put(bundle.getString("for_women"), SearchType.FOR_WOMEN);
        
    }

    public SearchType getSearchType() {
        return searchType;
    }

    public Map<String, SearchType> getSearchList() {
        return searchList;
    }
}
