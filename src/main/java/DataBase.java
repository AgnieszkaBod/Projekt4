import java.sql.*;

public class DataBase {
    private Connection con;
    private Statement st;
    private ResultSet rs;


    public Connection connect() {
        try {
            Class.forName("org.postgresql.Driver");

            con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "password");
            st = con.createStatement();


        } catch (Exception ex) {
            System.out.println("Blad polaczenia: " + ex);
        }
        return con;
    }

    public void insertData() {

        String query = "INSERT INTO laptops "
                + " (id, producent, przekatna, rozdzielczosc, matryca, dotykowy, procesor, rdzenie, taktowanie, ram," +
                " dysk, rdysku, nazwagraf, uklgraf, system, naped) values" +
                "(1,'Dell', '12\"', 'Brak informacji', 'matowa', 'nie', 'intel i7', '4', '2800', '8GB', '240GB', 'SSD', 'intel HD Graphics 4000', '1GB', 'Windows 7 Home', 'Brak informacji')," +
                "(2, 'Asus', '14\"', '1600x900', 'matowa', 'nie', 'intel i5', '4', 'Brak informacji', '16GB', '120GB', 'SSD', 'intel HD Graphics 5000', '1GB', 'Brak informacji', 'brak')," +
                "(3, 'Fujitsu', '14\"', '1920x1080', 'blyszczaca', 'tak', 'intel i7', '8', '1900', '24GB', '500GB', 'HDD', 'intel HD Graphics 520',  '1GB', 'brak systemu', 'Blu-Ray')," +
                "(4, 'Huawei', '13\"', 'Brak informacji', 'matowa', 'nie', 'intel i7', '4', '2400', '12GB', '24GB', 'HDD', 'NVIDIA GeForce GTX 1050', 'Brak informacji', 'Brak informacji', 'brak')," +
                "(5, 'MSI', '17\"', '1600x900', 'blyszczaca', 'tak', 'intel i7', '4', '3300', '8GB', '60GB', 'SSD', 'AMD Radeon Pro 455', '1GB', 'Windows 8.1 Profesional', 'DVD')," +
                "(6, 'Dell', 'Brak informacji', '1280x800', 'matowa','nie', 'intel i7', '4', '2800', '8GB', '240GB', 'SSD', 'Brak informacji', 'Brak informacji', 'Windows 7 Home','brak')," +
                "(7, 'Asus', '14\"', '1600x900', 'matowa', 'nie', 'intel i5', '4', '2800', 'Brak informacji', '120GB', 'SSD', 'intel HD Graphics 5000', '1GB', 'Windows 10 Home', 'Brak informacji')," +
                "(8, 'Fujitsu', '15\"', '1920x1080', 'blyszczaca', 'tak', 'intel i7', '8', '2800', '24GB', '500GB', 'HDD', 'intel HD Graphics 520', 'Brak informacji', 'brak systemu', 'Blu-Ray')," +
                "(9, 'Samsung', '13\"', '1366x768',  'matowa', 'nie', 'intel i7', '4', '2800', '12GB', '24GB' , 'HDD', 'NVIDIA GeForce GTX 1050', '1GB', 'Windows 10 Home', 'brak')," +
                "(10, 'Sony', '16\"', 'Brak informacji', 'blyszczaca', 'tak', 'intel i7', '4', '2800', '8GB', 'Brak informacji', 'Brak informacji', 'AMD Radeon Pro 455', '1GB',  'Windows 7 Profesional', 'DVD')," +
                "(11, 'Samsung', '12\"', '1280x800', 'matowa', 'nie', 'intel i7', 'Brak informacji', '2120', 'Brak informacji', 'Brak informacji', 'Brak informacji', 'intel HD Graphics 4000', '1GB', 'Brak informacji', 'brak')," +
                "(12, 'Samsung', '14\"', '1600x900', 'matowa',  'nie', 'intel i5', 'Brak informacji', 'Brak informacji', 'Brak informacji', 'Brak informacji', 'SSD', 'intel HD Graphics 5000', '1GB', 'Windows 10 Home', ' brak')," +
                "(13, 'Fujitsu', '15\"', '1920x1080', 'blyszczaca', 'tak', 'intel i7', '8', '2800', '24GB', '500GB', 'HDD', 'intel HD Graphics 520', 'Brak informacji', 'brak systemu', 'Blu-Ray')," +
                "(14, 'Huawei', '13\"', '1366x768', 'matowa', 'nie', 'intel i7', '4', '3000', 'Brak informacji', '24GB', 'HDD', 'NVIDIA GeForce GTX 1050', 'Brak informacji', 'Windows 10 Home', 'brak')," +
                "(15, 'MSI', '17\"', '1600x900',  'blyszczaca', 'tak', 'intel i7', '4', '9999', '8GB', '60GB', 'SSD', 'AMD Radeon Pro 455', '1GB', 'Windows 7 Profesional', 'Brak informacji')," +
                "(16, 'Huawei', '14\"', 'Brak informacji', 'matowa', 'nie', 'intel i7', '4', '2200', '8GB', '16GB', 'HDD', 'NVIDIA GeForce GTX 1080', 'Brak informacji', 'Brak informacji', 'brak')," +
                "(17, 'MSI', '17\"', '1600x900', 'blyszczaca',  'tak', 'intel i7', '4', '3300', '8GB', '60GB', 'SSD', 'AMD Radeon Pro 455', '1GB', 'Brak informacji', 'Brak informacji')," +
                "(18, 'Asus', 'Brak informacji', '1600x900', 'blyszczaca', 'tak', 'intel i5', '2', '3200', '16GB', '320GB', 'HDD', 'Brak informacji', 'Brak informacji', 'Windows 7 Home', 'brak')," +
                "(19, 'Asus', '14\"', '1600x900', 'matowa', 'nie', 'intel i5', '4', '2800', 'Brak informacji', '120GB', 'SSD', 'intel HD Graphics 5000', '1GB', 'Windows 10 Profesional', 'Brak informacji')," +
                "(20, 'Fujitsu', '14\"', '1280x800', 'blyszczaca', 'tak', 'intel i7', '8', '2800', '24GB', '500GB', 'HDD', 'intel HD Graphics 520', 'Brak informacji', 'brak systemu', 'Blu-Ray')," +
                "(21, 'Samsung', '12\"', '1600x900', 'Brak informacji', 'nie', 'intel i5', '4', '2800', '12GB', '24GB', 'HDD', 'NVIDIA GeForce GTX 1050', '1GB', 'Windows 8.1 Home', 'brak')," +
                "(22, 'Sony', '11\"', 'Brak informacji', 'blyszczaca', 'tak', 'intel i7', '4', '2800', '8GB', 'Brak informacji', 'Brak informacji', 'AMD Radeon Pro 455', '1GB', 'Windows 7 Profesional', 'brak')," +
                "(23, 'Samsung', '13\"', '1366x768', 'Brak informacji', 'nie', 'intel i5', 'Brak informacji', '2120', 'Brak informacji', 'Brak informacji', 'Brak informacji', 'intel HD Graphics 4000', '2GB', 'Brak informacji', 'DVD')," +
                "(24, 'Samsung', '15\"', '1920x1080', 'matowa', 'nie', 'intel i9', 'Brak informacji',' Brak informacji', 'Brak informacji', 'Brak informacji', 'SSD', 'intel HD Graphics 4000', '2GB', 'Windows 10 Profesional', 'Blu-Ray');";

        try {
            st.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
