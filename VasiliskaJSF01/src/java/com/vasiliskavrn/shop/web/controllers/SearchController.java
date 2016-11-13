package com.vasiliskavrn.shop.web.controllers;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import com.vasiliskavrn.shop.web.beans.Goods;
import com.vasiliskavrn.shop.web.db.Database;
import com.vasiliskavrn.shop.web.enums.SearchType;

@ManagedBean
@SessionScoped
public class SearchController implements Serializable {

    private boolean requestFromPager;
    private int selectedClothId; // выбранный раздел одежды
    private char selectedLetter; // выбранная буква алфавита
    private int goodsOnPage = 2;    
    private SearchType searchType;// хранит выбранный тип поиска
    private String searchString; // хранит поисковую строку
    private long selectedPageNumber = 1; // выбранный номер страницы в постраничной навигации
    private static Map<String, SearchType> searchList = new HashMap<String, SearchType>();
    private ArrayList<Goods> currentGoodsList; // текущий список книг для отображения
    private ArrayList<Integer> pageNumbers = new ArrayList<Integer>(); // общее кол-во книг (не на текущей странице, а всего), нажно для постраничности
    private long totalGoodsCount; // общее кол-во книг (не на текущей странице, а всего), нажно для постраничности
    private String currentSql;// последний выполнный sql без добавления limit
    
    public final String DEFAULT_SQL = "SELECT g.goods_id,g.goods_art,\n" +
"	   ct.cloth_name_one,\n" +
"       s.sex_name,\n" +
"       pr.prod_country,\n" +
"       cm.comp_name,\n" +
"       p.price_val,\n" +
"       f.firme_name,\n" +
"       c.col_name,\n" +
"       sz.size_name,\n" +
"       b.brand_country,\n" +
"       i.image_cotnent\n" +
"  FROM vasiliska2016.goods_tab g,\n" +
"       vasiliska2016.cloth_tab ct,\n" +
"	vasiliska2016.sex_tab   s,\n" +
"       vasiliska2016.producer_tab pr,\n" +
"       vasiliska2016.composition_tab cm,\n" +
"       vasiliska2016.price_tab p,\n" +
"       vasiliska2016.firme_tab f,\n" +
"       vasiliska2016.color_tab c,\n" +
"       vasiliska2016.size_tab  sz,\n" +
"       vasiliska2016.brand_tab  b,\n" +
"       vasiliska2016.image_tab  i\n" +
" WHERE 1=1\n" +
"   and g.goods_cloth = ct.id_cloth_tab\n" +
"   and g.goods_sex = s.id_sex_tab\n" +
"   and g.goods_produser = pr.id_producer_tab\n" +
"   and g.goods_comp = cm.id_comp_tab\n" +
"   and g.goods_price = p.id_price_tab\n" +
"   and g.goods_firm = f.id_firme_table\n" +
"   and g.goods_color = c.id_color_tab\n" +
"   and g.goods_size = sz.id_size_table\n" +
"   and g.goods_coun_br = b.id_brand_tab\n" +
"   and g.goods_image = i.id_image_tab ";

    
    

    public SearchController() {
        
         fillGoodsAll();
        
//        ResourceBundle bundle = ResourceBundle.getBundle("com.vasiliskavrn.shop.web.nls.messages", FacesContext.getCurrentInstance().getViewRoot().getLocale());
//        searchList.put(bundle.getString("for_all"), SearchType.FOR_ALL);
//        searchList.put(bundle.getString("for_boy"), SearchType.FOR_BOY);
//        searchList.put(bundle.getString("for_girl"), SearchType.FOR_GIRL);
//        searchList.put(bundle.getString("for_man"), SearchType.FOR_MAN);
//        searchList.put(bundle.getString("for_women"), SearchType.FOR_WOMEN);
        
    }

//      
    
    
    private void fillGoodsBySQL(String sql) {

        
        StringBuilder sqlBuilder = new StringBuilder(sql);
        currentSql = sql;
        
        Statement stmt = null;
        ResultSet rs = null;
        Connection conn = null;
                     

        try {
            
            conn = Database.getConnection();
            stmt = conn.createStatement();

            System.out.println(requestFromPager);
            if (!requestFromPager) {

                rs = stmt.executeQuery(sqlBuilder.toString());
                rs.last();

                totalGoodsCount = rs.getRow();
                fillPageNumbers(totalGoodsCount, goodsOnPage);
            
            }
            
            
            if (totalGoodsCount > goodsOnPage) {
                sqlBuilder.append(" limit ").append(selectedPageNumber * goodsOnPage - goodsOnPage).append(",").append(goodsOnPage);
            }

            System.out.println(sqlBuilder.toString());
            rs = stmt.executeQuery(sqlBuilder.toString());

            currentGoodsList = new ArrayList<Goods>();

            while (rs.next()) {
                Goods goods = new Goods();
                goods.setId(rs.getLong("goods_id"));
                goods.setArticle(rs.getString("goods_art"));
                goods.setName(rs.getString("cloth_name_one"));
                goods.setSex(rs.getString("sex_name"));
                goods.setCountryMade(rs.getString("prod_country"));
                goods.setPrice(rs.getString("price_val"));
                goods.setFirme(rs.getString("firme_name"));
                goods.setColor(rs.getString("col_name"));
                goods.setSize(rs.getString("size_name"));
                goods.setComposition(rs.getString("comp_name"));
                goods.setCountryBrand(rs.getString("brand_country"));
//                goods.setImage(rs.getBytes("image_cotnent"));
                currentGoodsList.add(goods);
            }

        } catch (SQLException ex) {
            Logger.getLogger(SearchController.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(SearchController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void fillGoodsAll() {
        
        StringBuilder sql = new StringBuilder(DEFAULT_SQL);
             sql.append(" order by ct.cloth_name_one ");
             fillGoodsBySQL(sql.toString()); 
    }
    
    
    private void submitValues(Character selectedLetter, long selectedPageNumber, int selectedGenreId, boolean requestFromPager) {
        this.selectedLetter = selectedLetter;
        this.selectedPageNumber = selectedPageNumber;
        this.selectedClothId = selectedGenreId;
        this.requestFromPager = requestFromPager;
      }

    public String fillGoodsByCloth() {
        imitateLoading();

        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        
        selectedClothId = Integer.valueOf(params.get("cloth_id"));
        
        submitValues(' ', 1,selectedClothId , false);
        
//        Integer cloth_id = Integer.valueOf(params.get("cloth_id"));
        
        StringBuilder sql = new StringBuilder(DEFAULT_SQL);
        sql.append("   and ct.id_cloth_tab = "+ selectedClothId + " order by ct.cloth_name_one ");
        System.out.println(sql.toString());
        fillGoodsBySQL(sql.toString());
        
        return "goods";
    }

    public String fillGoodsByLetter() {
        imitateLoading();

        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
//        String searchLetter = params.get("letter");
        selectedLetter = params.get("letter").charAt(0);

        submitValues(selectedLetter, 1, -1, false);
        
        StringBuilder sql = new StringBuilder(DEFAULT_SQL);
        sql.append(" and substr(ct.cloth_name_one,1,1)='"+ selectedLetter + "' order by ct.cloth_name_one ");
        System.out.println(sql.toString());
        fillGoodsBySQL(sql.toString());
        
        return "goods";
    }

    public String  fillGoodsBySearch() {
        imitateLoading();
        
        submitValues(' ', 1, -1, false);

        if (searchString.trim().length() == 0) {
            fillGoodsAll();
            return "goods";
        }

        StringBuilder sql = new StringBuilder(DEFAULT_SQL);                   

        if (searchType == SearchType.FOR_BOY) {
            sql.append(" and s.sex_name='Мальчики' and lower(ct.cloth_name_one) like '%" + searchString.toLowerCase().substring(0, 4) + "%' order by ct.cloth_name_one  " );

        } else if (searchType == SearchType.FOR_GIRL) {
            sql.append(" and s.sex_name='Девочки' and lower(ct.cloth_name_one) like '%" + searchString.toLowerCase().substring(0, 4) + "%' order by ct.cloth_name_one  " );
        }
        else if (searchType == SearchType.FOR_MAN) {
            sql.append(" and s.sex_name='Мужчины' and lower(ct.cloth_name_one) like '%" + searchString.toLowerCase().substring(0, 4) + "%' order by ct.cloth_name_one  " );
        }
        else if (searchType == SearchType.FOR_WOMEN) {
            sql.append(" and s.sex_name='Женщины' and lower(ct.cloth_name_one) like '%" + searchString.toLowerCase().substring(0, 4) + "%' order by ct.cloth_name_one  " );
        } 
        else if (searchType == SearchType.FOR_ALL) {
            sql.append(" and lower(ct.cloth_name_one) like '%" + searchString.toLowerCase().substring(0, 4) + "%' order by ct.cloth_name_one  " );
        }


        System.out.println(sql.toString());         
        fillGoodsBySQL(sql.toString());
        
        return "goods";        
    }
     
    
    public void selectPage() {
        imitateLoading();
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        selectedPageNumber = Integer.valueOf(params.get("page_number"));
        requestFromPager = true;
        fillGoodsBySQL(currentSql);
    }

    public byte[] getImage(int id) {
        Statement stmt = null;
        ResultSet rs = null;
        Connection conn = null;

        byte[] image = null;

        try {
            conn = Database.getConnection();
            stmt = conn.createStatement();

            rs = stmt.executeQuery(" select i.image_cotnent FROM vasiliska2016.goods_tab g, vasiliska2016.image_tab  i "
                                    + "WHERE g.goods_image = i.id_image_tab and g.goods_id=" + id);
            while (rs.next()) {
                image = rs.getBytes("image_cotnent");
            }

        } catch (SQLException ex) {
            Logger.getLogger(Goods.class
                    .getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(Goods.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }

        return image;
    }

    public Character[] getRussianLetters() {
        Character[] letters = new Character[33];
        letters[0] = 'А';
        letters[1] = 'Б';
        letters[2] = 'В';
        letters[3] = 'Г';
        letters[4] = 'Д';
        letters[5] = 'Е';
        letters[6] = 'Ё';
        letters[7] = 'Ж';
        letters[8] = 'З';
        letters[9] = 'И';
        letters[10] = 'Й';
        letters[11] = 'К';
        letters[12] = 'Л';
        letters[13] = 'М';
        letters[14] = 'Н';
        letters[15] = 'О';
        letters[16] = 'П';
        letters[17] = 'Р';
        letters[18] = 'С';
        letters[19] = 'Т';
        letters[20] = 'У';
        letters[21] = 'Ф';
        letters[22] = 'Х';
        letters[23] = 'Ц';
        letters[24] = 'Ч';
        letters[25] = 'Ш';
        letters[26] = 'Щ';
        letters[27] = 'Ъ';
        letters[28] = 'Ы';
        letters[29] = 'Ь';
        letters[30] = 'Э';
        letters[31] = 'Ю';
        letters[32] = 'Я';

        return letters;
    }
    
    
    private void fillPageNumbers(long totalClothCount, int clothCountOnPage) {

        int pageCount = clothCountOnPage > 0 ? (int) ((totalClothCount / clothCountOnPage) + 1) : 0;

        pageNumbers.clear();
        for (int i = 1; i <= pageCount; i++) {
            pageNumbers.add(i);
        }

    }
    
    public ArrayList<Integer> getPageNumbers() {
        return pageNumbers;
    }

    public void setPageNumbers(ArrayList<Integer> pageNumbers) {
        this.pageNumbers = pageNumbers;
    }   

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public SearchType getSearchType() {
        return searchType;
    }

    public void setSearchType(SearchType searchType) {
        this.searchType = searchType;
    }

    public Map<String, SearchType> getSearchList() {
        return searchList;
    }

    public ArrayList<Goods> getCurrentGoodsList() {
        return currentGoodsList;
    } 
    
    public void setTotalGoodsCount(long goodsCount) {
        this.totalGoodsCount = goodsCount;
    }

    public long getTotalGoodsCount() {
        return totalGoodsCount;
    }    

    public int getSelectedClothId() {
        return selectedClothId;
    }

    public void setSelectedClothId(int selectedClothId) {
        this.selectedClothId = selectedClothId;
    }

    public char getSelectedLetter() {
        return selectedLetter;
    }

    public void setSelectedLetter(char selectedLetter) {
        this.selectedLetter = selectedLetter;
    }    
    
    public int getGoodsOnPage() {
        return goodsOnPage;
    }

    public void setGoodsOnPage(int goodsOnPage) {
        this.goodsOnPage = goodsOnPage;
    }

    public void setSelectedPageNumber(long selectedPageNumber) {
        this.selectedPageNumber = selectedPageNumber;
    }

    public long getSelectedPageNumber() {
        return selectedPageNumber;
    }
    
    private void imitateLoading() {
        try {
            Thread.sleep(500);// имитация загрузки процесса
        } catch (InterruptedException ex) {
            Logger.getLogger(SearchController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
}
