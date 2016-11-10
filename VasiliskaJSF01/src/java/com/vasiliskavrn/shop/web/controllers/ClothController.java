package com.vasiliskavrn.shop.web.controllers;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import com.vasiliskavrn.shop.web.beans.Cloth;
import com.vasiliskavrn.shop.web.db.Database;

@ManagedBean(eager = true)
@ApplicationScoped
public class ClothController implements Serializable {

    private ArrayList<Cloth> clothList;

    public ClothController() {
        fillClothsAll();
    }

    private void fillClothsAll() {
        Statement stmt = null;
        ResultSet rs = null;
        Connection conn = null;

        clothList = new ArrayList<Cloth>();

        try {
            conn = Database.getConnection();

            stmt = conn.createStatement();
            rs = stmt.executeQuery("select * from vasiliska2016.cloth_tab order by cloth_name");
            while (rs.next()) {
                Cloth cloth  = new Cloth ();
                cloth.setName(rs.getString("cloth_name"));
                cloth.setId(rs.getLong("id_cloth_tab"));
                clothList.add(cloth);
            }

        } catch (SQLException ex) {
            Logger.getLogger(ClothController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ClothController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public ArrayList<Cloth> getClothList() {
        return clothList;
    }
}
