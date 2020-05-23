package sample;

import javafx.collections.ObservableList;

import java.sql.*;
import java.util.Properties;

public class sqlCommands {
    static Connection  conn1 = null;
    public static Connection getConnected(String userLogin,String userPass, String DBnm) throws Exception {
//1. Подключение к БД
        //протокол:подпротокол(СУБД)://хост/БД
        String url = "jdbc:postgresql://127.0.0.1:5432/" + DBnm,
                userName = userLogin, userPassd = userPass;

        Class.forName("org.postgresql.Driver");
        Properties props = new Properties();
// Ensure EscapeSyntaxCallmode property set to support procedures if no return value
        props.setProperty("escapeSyntaxCallMode", "callIfNoReturn");
        props.setProperty("user", userName);
        props.setProperty("password", userPassd);
        conn1 = DriverManager.getConnection(url, props);

        System.out.println("Соединение установлено");
        return conn1;
    }


    public static void ins(int id, String name, int amount, int cost) throws SQLException {
        //3. Обращение к хранимой функции-процедуре(CallableStatement)
        conn1.setAutoCommit(true);
        CallableStatement cstat1 = null;
        cstat1 = conn1.prepareCall("{ call insertintable(?,?,?,?) }");
        cstat1.setInt(1, id);
        cstat1.setString(2, name);
        cstat1.setInt(3, amount);
        cstat1.setInt(4, cost);
        cstat1.execute();
        System.out.println("Вставка прошла успешно");
        cstat1.close();
    }

    public static void deleteT(String name) throws SQLException {
        CallableStatement cstat2 = null;
        cstat2 = conn1.prepareCall("{ call deletefromtable(?) }");
        cstat2.setString(1, name);
        cstat2.execute();
        System.out.println("Удаление прошло успешно");
        cstat2.close();
    }
    public static void clearT() throws SQLException {
        CallableStatement cstat3 = null;
        cstat3 = conn1.prepareCall("{ call cleartable() }");
        cstat3.execute();
        System.out.println("Очистка прошла успешно");
        cstat3.close();
    }

    public static void updateT(int id, String name, int amount, int cost) throws SQLException {
        CallableStatement cstat3 = null;
        cstat3 = conn1.prepareCall("{ call updatetable(?,?,?,?) }");
        cstat3.setInt(1, id);
        cstat3.setString(2, name);
        cstat3.setInt(3, amount);
        cstat3.setInt(4, cost);
        cstat3.execute();
        System.out.println("Обновление прошло успешно");
        cstat3.close();
    }

    public static ObservableList lookinT(ObservableList<ControllerMenu.Ingredient> list1) throws SQLException {
        list1.clear();
        PreparedStatement prepstat2 = null;
        prepstat2 = conn1.prepareStatement("select * from lookattable()");
        ResultSet rs1 = prepstat2.executeQuery();
        while(rs1.next()) {
            ControllerMenu.Ingredient in1 = new ControllerMenu.Ingredient();
            in1.setId(rs1.getInt(1));
            in1.setName(rs1.getString(2));
            in1.setAmount(rs1.getInt(3));
            in1.setCost(rs1.getInt(4));
            list1.add(in1);
        }
        prepstat2.close();
        return list1;
    }

    public static ObservableList searchinT(String name,ObservableList<ControllerMenu.Ingredient> list2) throws SQLException {
        list2.clear();
        PreparedStatement prepstat3 = null;
        prepstat3 = conn1.prepareStatement("select * from searchintable(?)");
        prepstat3.setString(1, name);
        ResultSet rs2 = prepstat3.executeQuery();
        while(rs2.next()) {
            ControllerMenu.Ingredient in1 = new ControllerMenu.Ingredient();
            in1.setId(rs2.getInt(1));
            in1.setName(rs2.getString(2));
            in1.setAmount(rs2.getInt(3));
            in1.setCost(rs2.getInt(4));
            list2.add(in1);
        }
        prepstat3.close();
        System.out.println("Search прошло успешно");
        return list2;
    }


    public static void createDB(String Dbname, String User, String Pass) throws Exception {
        PreparedStatement prepstat3 = null;
        prepstat3 = conn1.prepareStatement("select create_db(?, ?, ?)");
        prepstat3.setString(1, Dbname);
        prepstat3.setString(2, User);
        prepstat3.setString(3, Pass);
        prepstat3.execute();
        prepstat3.close();
        //TBD
    }

    public static void createTb(Connection conn) throws SQLException {
        Statement stat4 = conn.createStatement();
        stat4.execute("CREATE TABLE ingredients (\n" +
                "     id    integer PRIMARY KEY not null,\n" +
                "     name   text NOT NULL,\n" +
                "\t amount   integer NOT NULL,\n" +
                "\t cost   integer NOT NULL);" +
                "create or replace procedure InsertInTable(id int, name text, amount int, cost int)\n" +
                "language 'plpgsql' as\n" +
                "$$\n" +
                "begin\n" +
                "insert into ingredients values(id, name, amount, cost);\n" +
                "end;\n" +
                "$$;" +
                "create or replace function searchintable(names text)\n" +
                "returns table(id int, nameIng text, amount int, cost int) as\n" +
                "$$\n" +
                "begin\n" +
                "return query select * from ingredients where name = names;\n" +
                "end\n" +
                "$$ language plpgsql;" +
                "create or replace function lookattable()\n" +
                "returns table(id int, nameIng text, amount int, cost int) as\n" +
                "$$\n" +
                "begin\n" +
                "return query select * from ingredients;\n" +
                "end\n" +
                "$$ language plpgsql;" +
                "create or replace procedure deletefromtable(names text)\n" +
                "language 'plpgsql' as\n" +
                "$$\n" +
                "begin\n" +
                "delete from ingredients where name = names;\n" +
                "end;\n" +
                "$$;" +
                "create or replace procedure clearTable()\n" +
                "language 'plpgsql' as\n" +
                "$$\n" +
                "begin\n" +
                "delete from ingredients;\n" +
                "end;\n" +
                "$$;" +
                "create or replace procedure updateTable(idx integer, namen text, amountn integer, costn integer)\n" +
                "language 'plpgsql' as\n" +
                "$$\n" +
                "begin\n" +
                "update ingredients set name = namen, amount = amountn, cost = costn where id = idx;\n" +
                "end;\n" +
                "$$");
        stat4.close();
    }


    public static void dropDB(String Dbname, String User, String Pass) throws SQLException {
        PreparedStatement prepstat3 = null;
        prepstat3 = conn1.prepareStatement("select drop_db(?,?,?)");
        prepstat3.setString(1, Dbname);
        prepstat3.setString(2, User);
        prepstat3.setString(3, Pass);
        prepstat3.execute();
        prepstat3.close();
    }
}
//CLOSE EVERYTHING!!!