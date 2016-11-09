package com.vasiliskavrn.shop.web.beans;

import com.vasiliskavrn.shop.web.db.Database;
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

@ManagedBean
@ApplicationScoped
public class Cloths {
    
    private ArrayList<Cloth> clothList = new ArrayList<Cloth>();

    private ArrayList<Cloth> getCloth() {
        Statement stmt = null;
        ResultSet rs = null;
        Connection conn = null;
        try {
            conn = Database.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select * from vasiliska2016.cloth_tab order by cloth_name");
            while (rs.next()) {
                Cloth cloth = new Cloth();
                cloth.setName(rs.getString("cloth_name"));
                cloth.setId(rs.getLong("id_cloth_tab"));
                clothList.add(cloth);
            }

        } catch (SQLException ex) {
            Logger.getLogger(Cloths.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (stmt!=null) stmt.close();
                if (rs!=null)rs.close();
                if (conn!=null)conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(Cloths.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return clothList;
    }

    public ArrayList<Cloth> getClothList() {
        if (!clothList.isEmpty()) {
            return clothList;
        } else {
            return getCloth();
        }
    }

  

}
