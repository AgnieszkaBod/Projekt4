import java.sql.*;

public class Main {
    private Connection con;
    private Statement st;
    private ResultSet rs;


    public void connect() {
        try {
            Class.forName("org.postgresql.Driver");

            con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "password");
            st = con.createStatement();


        } catch (Exception ex) {
            System.out.println("Blad polaczenia: " + ex);
        }
    }

    public void insertData() {

        String query = "INSERT INTO laptops (producent, przekatna) VALUES ('intel', '14\"')";
        try {
            st.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Main db = new Main();
        db.connect();
        db.insertData();
    }
}
