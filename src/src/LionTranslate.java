/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Janaka Pathma Kumara
 */
public class LionTranslate {

    String enAr[] = {"i", "we", "you", "he", "she", "it", "they"};
    String sinAr[] = {"මම", "අපි", "ඔබ", "ඔහු", "ඇය", "එක", "ඔවුන්"};
    String enAmIsAre[] = {"am", "is", "are"};
    String numAr[] = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
    SinhalaTranslator sinhalaTranslator = new SinhalaTranslator();

    public String sinhalaTranslate(String trText) throws SQLException, ClassNotFoundException {
        String returnTrText = "";
        String trTextAr[] = trText.split(" ");
        for (int i = 0; i < trTextAr.length; i++) {
            String trVerb = trTextAr[i].toLowerCase();
//            if (!trVerb.toLowerCase().equals("am") && !trVerb.toLowerCase().equals("is") && !trVerb.toLowerCase().equals("are")) {
            if (!checkAmIsAre(trVerb)) {
                if (checkNumAr(trVerb) == true) {
                    if (i == 0) {
                        returnTrText += trVerb;
                    } else {
                        returnTrText += " " + trVerb;
                    }
                } else if (checkenAr(trVerb) == true) {
                    if (i == 0) {
                        returnTrText += getSinAr(trVerb);
                    } else {
                        returnTrText += " " + getSinAr(trVerb);
                    }
                } else if (checkDB(trVerb) == true) {;
                    if (i == 0) {
                        returnTrText += sqliteDBSearch(trVerb);
                    } else {
                        returnTrText += " " + sqliteDBSearch(trVerb);
                    }
                } else {
                    if (i == 0) {
                        returnTrText += sinhalaTranslator.translate(trVerb, 0, trVerb.length(), false);

                    } else {
                        returnTrText += " " + sinhalaTranslator.translate(trVerb, 0, trVerb.length(), false);
                    }
                }
            }
        }
        return returnTrText;
    }

    private boolean checkAmIsAre(String ar) {
        for (int i = 0; i < enAmIsAre.length; i++) {
            String string = enAmIsAre[i];
            if (string.equals(ar)) {
                return true;
            }

        }
        return false;
    }

    private boolean checkNumAr(String ar) {
        for (int i = 0; i < numAr.length; i++) {
            String st = numAr[i];
            if (st.equals(ar)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkenAr(String ar) {
        for (int i = 0; i < enAr.length; i++) {
            String string = enAr[i];
            if (string.equals(ar)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkDB(String enWrd) throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:./System/User/Data/en-si.db");
        Statement stat = conn.createStatement();
        ResultSet rs = stat.executeQuery("select value from dict where key = '" + enWrd + "';");
        if (rs.next()) {
            return true;
        }
        return false;
    }

    private String getSinAr(String ar) {
        String s = "";
        for (int i = 0; i < enAr.length; i++) {
            String string = enAr[i];
            if (string.equals(ar)) {
                s = sinAr[i];
            }
        }
        return s;
    }

    private String sqliteDBSearch(String enWrd) throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:./System/User/Data/en-si.db");
        Statement stat = conn.createStatement();

        ResultSet rs = stat.executeQuery("select value from dict where key = '" + enWrd + "';");
        String trans[];
        String transA = "";
        while (rs.next()) {
//            for (int i = 0; i < rs.getString("value").split("\\|").length; i++) {
//                trans += rs.getString("value").split("\\|")[i] + "\n";
//            }
            int n;
            for (n = 0; n < rs.getString("value").split("\\|").length; n++) {
            }
            trans = rs.getString("value").split("\\|");
            transA = trans[n - 1];
        }
        rs.close();
        conn.close();
        return transA;
    }
}
