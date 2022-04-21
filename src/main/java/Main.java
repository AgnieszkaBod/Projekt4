import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static java.awt.Color.*;
import static java.awt.FlowLayout.LEFT;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;

public class Main extends JFrame {
    public static String[] headers = new String[15];
    public static Object[][] data;
    public static Object[][] dataPom;
    public static final String filePath = "src/katalog.txt";
    public static int baseRows = 0, pomRows = 0, columns = 15, numeberDupilacedRows = 0, numberNewRows = 0, i = 0, j = 0;
    public static boolean isDuplicated;
    public static List<Integer> editedRows = new ArrayList();
    public static List<Integer> duplicatedRows = new ArrayList();
    public static Main window = new Main();
    public static DefaultTableModel tableModel;

    public static final JButton bImportTxt = new JButton("Importuj dane z txt");
    public static final JButton bExportTxt = new JButton("Eksportuj dane do txt");

    public static final JButton bImportXml = new JButton("Importuj dane z xml");
    public static final JButton bExportXml = new JButton("Eksportuj dane do xml");

    public static final JButton bImportDB = new JButton("Importuj dane z bazy");
    public static final JButton bExportDB = new JButton("Eksportuj dane do bazy");

    public static final JTextArea info = new JTextArea();

    public Main() {
        setSize(1500, 700);
        setTitle("Systemy integracyjne Lab4");
        setLayout(new FlowLayout(LEFT));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    static void getHeaders() throws IOException {
        BufferedReader fileReader = null;
        String line;

        try {
            fileReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(filePath), StandardCharsets.UTF_8));

        } catch (
                FileNotFoundException e) {
            System.out.println("Blad przy otwieraniu pliku!");
        }

        if (fileReader != null) {
            line = fileReader.readLine();
            String[] words = line.split(";");
            int i = 0;
            for (String word : words) {
                headers[i] = word;
                i++;
            }
        }
    }

    static void setButtons() {
        bImportTxt.setSize(100, 100);
        bImportTxt.setBackground(BLUE);
        bExportTxt.setBackground(BLUE);

        bImportXml.setBackground(MAGENTA);
        bExportXml.setBackground(MAGENTA);

        bImportDB.setBackground(GREEN);
        bExportDB.setBackground(GREEN);
    }

    static void findDuplicates(int rowsB, int rowsP) {
        numeberDupilacedRows = 0;
        numberNewRows = 0;
        for (i = 0; i < rowsB; i++) {
            isDuplicated = false;
            for (j = 0; j < rowsP; j++) {
                if (String.valueOf(data[i][0]).equals(String.valueOf(dataPom[j][0])) &&
                        String.valueOf(data[i][1]).equals(String.valueOf(dataPom[j][1])) &&
                        String.valueOf(data[i][2]).equals(String.valueOf(dataPom[j][2])) &&
                        String.valueOf(data[i][3]).equals(String.valueOf(dataPom[j][3])) &&
                        String.valueOf(data[i][4]).equals(String.valueOf(dataPom[j][4])) &&
                        String.valueOf(data[i][5]).equals(String.valueOf(dataPom[j][5])) &&
                        String.valueOf(data[i][6]).equals(String.valueOf(dataPom[j][6])) &&
                        String.valueOf(data[i][7]).equals(String.valueOf(dataPom[j][7])) &&
                        String.valueOf(data[i][8]).equals(String.valueOf(dataPom[j][8])) &&
                        String.valueOf(data[i][9]).equals(String.valueOf(dataPom[j][9])) &&
                        String.valueOf(data[i][10]).equals(String.valueOf(dataPom[j][10])) &&
                        String.valueOf(data[i][11]).equals(String.valueOf(dataPom[j][11])) &&
                        String.valueOf(data[i][12]).equals(String.valueOf(dataPom[j][12])) &&
                        String.valueOf(data[i][13]).equals(String.valueOf(dataPom[j][13])) &&
                        String.valueOf(data[i][14]).equals(String.valueOf(dataPom[j][14]))) {
                    isDuplicated = true;
                }
            }

            if (isDuplicated) {
                duplicatedRows.add(i);
                numeberDupilacedRows++;
            } else {
                numberNewRows++;
            }
        }
        System.out.println(rowsP);
        for (i = rowsP - 1; i >= 0; i--) {
            tableModel.removeRow(i);
        }

        for (i = 0; i < rowsB; i++) {
            tableModel.addRow(data[i]);
        }

    }

    static void importFromTxt() {
        FileReader fileReader = null;
        FileReader fileReaderPom = null;
        String line = "";

        duplicatedRows.clear();

        try {
            fileReader = new FileReader(filePath);
            fileReaderPom = new FileReader(filePath);
        } catch (FileNotFoundException e1) {
            System.out.println("Blad przy otwieraniu pliku!");
        }

        assert fileReader != null;
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        assert fileReaderPom != null;
        BufferedReader bufferedReaderPom = new BufferedReader(fileReaderPom);
        String[] words;

        try {
            bufferedReaderPom.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        pomRows = baseRows;
        baseRows = 0;

        while (true) {
            try {
                if (null == bufferedReaderPom.readLine()) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            baseRows++;
        }
        dataPom = data;
        data = new Object[baseRows][columns];

        try {
            line = bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < baseRows; i++) {
            try {
                line = bufferedReader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            words = line.split(";", -1);
            for (int j = 0; j <= 14; j++) {
                data[i][j] = words[j];
                if (data[i][j] == null || data[i][j].equals("")) {
                    data[i][j] = "Brak informacji";
                }
            }
        }
        findDuplicates(baseRows, pomRows);
        try {
            fileReader.close();
        } catch (IOException e) {
            System.out.println("Blad przy zamykaniu pliku!");
        }
    }

    static void exportToTxt() {
        PrintWriter zapis = null;
        try {
            zapis = new PrintWriter("src/wyniki.txt", StandardCharsets.UTF_8);
        } catch (IOException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }

        for (int i = 0; i < baseRows; i++) {
            for (int j = 0; j < 15; j++) {
                assert zapis != null;
                zapis.print(data[i][j] + ";");
            }
            if (i < baseRows - 1) {
                zapis.print("\n");
            }
        }
        zapis.close();
    }

    static void importFromXml() throws SAXException, ParserConfigurationException {
        File file = new File("laptops.xml");
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = null;
        try {
            document = documentBuilder.parse(file);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(window
                    , "Nie znaleziono pliku xml");
        }
        document.getDocumentElement().normalize();
        System.out.println("Root element: " + document.getDocumentElement().getNodeName());
        NodeList nodeList = document.getElementsByTagName("laptop");

        duplicatedRows.clear();

        dataPom = data;
        pomRows = baseRows;

        baseRows = nodeList.getLength();
        data = new Object[baseRows][columns];
        int j;
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            j = 0;
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                data[i][j] = element.getElementsByTagName("manufacturer").item(0).getTextContent();
                j++;
                data[i][j] = element.getElementsByTagName("size").item(0).getTextContent();
                j++;
                data[i][j] = element.getElementsByTagName("resolution").item(0).getTextContent();
                j++;
                data[i][j] = element.getElementsByTagName("type").item(0).getTextContent();
                j++;
                data[i][j] = element.getElementsByTagName("screen").item(0).getAttributes().getNamedItem("touch").getNodeValue();
                j++;
                data[i][j] = element.getElementsByTagName("name").item(0).getTextContent();
                j++;
                data[i][j] = element.getElementsByTagName("physical_cores").item(0).getTextContent();
                j++;
                data[i][j] = element.getElementsByTagName("clock_speed").item(0).getTextContent();
                j++;
                data[i][j] = element.getElementsByTagName("ram").item(0).getTextContent();
                j++;
                data[i][j] = element.getElementsByTagName("storage").item(0).getTextContent();
                j++;
                data[i][j] = element.getElementsByTagName("disc").item(0).getAttributes().getNamedItem("type").getNodeValue();
                j++;
                data[i][j] = element.getElementsByTagName("name").item(1).getTextContent();
                j++;
                data[i][j] = element.getElementsByTagName("memory").item(0).getTextContent();
                j++;
                data[i][j] = element.getElementsByTagName("os").item(0).getTextContent();
                j++;
                data[i][j] = element.getElementsByTagName("disc_reader").item(0).getTextContent();
            }
        }
        findDuplicates(baseRows, pomRows);
    }

    static void exportToXml() throws ParserConfigurationException, TransformerException, FileNotFoundException {
        DocumentBuilder builder;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();

        Element laptops = document.createElement("laptops");
        laptops.setAttribute("moddate", String.valueOf(new Date()));


        for (i = 0; i < baseRows; i++) {
            j = 0;
            Element laptop = document.createElement("laptop");
            laptop.setAttribute("id", String.valueOf(i + 1));

            Element manufacturer = document.createElement("manufacturer");
            manufacturer.setTextContent((String) data[i][j]);
            j++;

            Element screen = document.createElement("screen");

            Element size = document.createElement("size");
            size.setTextContent((String) data[i][j]);
            j++;

            Element resolution = document.createElement("resolution");
            resolution.setTextContent((String) data[i][j]);
            j++;

            Element type = document.createElement("type");
            type.setTextContent((String) data[i][j]);
            j++;

            screen.setAttribute("touch", (String) data[i][j]);
            j++;

            Element processor = document.createElement("processor");

            Element name = document.createElement("name");
            name.setTextContent((String) data[i][j]);
            j++;

            Element physical_cores = document.createElement("physical_cores");
            physical_cores.setTextContent((String) data[i][j]);
            j++;

            Element clock_speed = document.createElement("clock_speed");
            clock_speed.setTextContent((String) data[i][j]);
            j++;

            Element ram = document.createElement("ram");
            ram.setTextContent((String) data[i][j]);
            j++;

            Element disc = document.createElement("disc");

            Element storage = document.createElement("storage");
            storage.setTextContent((String) data[i][j]);
            j++;

            disc.setAttribute("type", (String) data[i][j]);
            j++;

            Element graphic_card = document.createElement("graphic_card");

            Element nameGPU = document.createElement("name");
            nameGPU.setTextContent((String) data[i][j]);
            j++;

            Element memory = document.createElement("memory");
            memory.setTextContent((String) data[i][j]);
            j++;


            Element os = document.createElement("os");
            os.setTextContent((String) data[i][j]);
            j++;


            Element disc_reader = document.createElement("disc_reader");
            disc_reader.setTextContent((String) data[i][j]);

            laptop.appendChild(manufacturer);

            screen.appendChild(size);
            screen.appendChild(resolution);
            screen.appendChild(type);
            laptop.appendChild(screen);

            processor.appendChild(name);
            processor.appendChild(physical_cores);
            processor.appendChild(clock_speed);
            laptop.appendChild(processor);

            laptop.appendChild(ram);

            disc.appendChild(storage);
            laptop.appendChild(disc);

            graphic_card.appendChild(nameGPU);
            graphic_card.appendChild(memory);
            laptop.appendChild(graphic_card);

            laptop.appendChild(os);

            laptop.appendChild(disc_reader);

            laptops.appendChild(laptop);
        }

        document.appendChild(laptops);

        Transformer t = TransformerFactory.newInstance().newTransformer();
        t.setOutputProperty(OutputKeys.INDENT, "yes");
        t.setOutputProperty(OutputKeys.METHOD, "xml");
        t.transform(new DOMSource(document), new StreamResult(new FileOutputStream("laptops.xml")));

    }

    static void importFromDataBase() throws SQLException {
        var dataBase = new DataBase();
        Connection connection = dataBase.connect();
        ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM laptops");
        ResultSet resultSetPom = connection.createStatement().executeQuery("SELECT * FROM laptops");

        duplicatedRows.clear();
        dataPom = new Object[baseRows][columns];
        dataPom = data;
        pomRows = baseRows;

        baseRows = 0;
        while (resultSetPom.next()) {
            baseRows++;
        }

        data = new Object[baseRows][columns];
        i = 0;
        while (resultSet.next()) {
            j = 0;
            data[i][j] = resultSet.getString("producent");
            j++;
            data[i][j] = resultSet.getString("przekatna");
            j++;
            data[i][j] = resultSet.getString("rozdzielczosc");
            j++;
            data[i][j] = resultSet.getString("matryca");
            j++;
            data[i][j] = resultSet.getString("dotykowy");
            j++;
            data[i][j] = resultSet.getString("procesor");
            j++;
            data[i][j] = resultSet.getString("rdzenie");
            j++;
            data[i][j] = resultSet.getString("taktowanie");
            j++;
            data[i][j] = resultSet.getString("ram");
            j++;
            data[i][j] = resultSet.getString("dysk");
            j++;
            data[i][j] = resultSet.getString("rdysku");
            j++;
            data[i][j] = resultSet.getString("nazwagraf");
            j++;
            data[i][j] = resultSet.getString("uklgraf");
            j++;
            data[i][j] = resultSet.getString("system");
            j++;
            data[i][j] = resultSet.getString("naped");
            i++;
        }
        findDuplicates(baseRows, pomRows);
    }

    static void exportToDataBase() throws SQLException {
        var dataBase = new DataBase();
        Connection connection = dataBase.connect();
        ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM laptops");
        ResultSet resultSetPom = connection.createStatement().executeQuery("SELECT * FROM laptops");

        numeberDupilacedRows = 0;
        numberNewRows = 0;
        isDuplicated = false;

        while (resultSetPom.next()) {
            pomRows++;
        }

        dataPom = new Object[pomRows][columns];
        i = 0;
        while (resultSet.next()) {
            j = 0;
            dataPom[i][j] = resultSet.getString("producent");
            j++;
            dataPom[i][j] = resultSet.getString("przekatna");
            j++;
            dataPom[i][j] = resultSet.getString("rozdzielczosc");
            j++;
            dataPom[i][j] = resultSet.getString("matryca");
            j++;
            dataPom[i][j] = resultSet.getString("dotykowy");
            j++;
            dataPom[i][j] = resultSet.getString("procesor");
            j++;
            dataPom[i][j] = resultSet.getString("rdzenie");
            j++;
            dataPom[i][j] = resultSet.getString("taktowanie");
            j++;
            dataPom[i][j] = resultSet.getString("ram");
            j++;
            dataPom[i][j] = resultSet.getString("dysk");
            j++;
            dataPom[i][j] = resultSet.getString("rdysku");
            j++;
            dataPom[i][j] = resultSet.getString("nazwagraf");
            j++;
            dataPom[i][j] = resultSet.getString("uklgraf");
            j++;
            dataPom[i][j] = resultSet.getString("system");
            j++;
            dataPom[i][j] = resultSet.getString("naped");
            i++;
        }

        for (i = 0; i < baseRows; i++) {
            for (j = 0; j < pomRows; j++) {
                if (String.valueOf(data[i][0]).equals(String.valueOf(dataPom[j][0])) &&
                        String.valueOf(data[i][1]).equals(String.valueOf(dataPom[j][1])) &&
                        String.valueOf(data[i][2]).equals(String.valueOf(dataPom[j][2])) &&
                        String.valueOf(data[i][3]).equals(String.valueOf(dataPom[j][3])) &&
                        String.valueOf(data[i][4]).equals(String.valueOf(dataPom[j][4])) &&
                        String.valueOf(data[i][5]).equals(String.valueOf(dataPom[j][5])) &&
                        String.valueOf(data[i][6]).equals(String.valueOf(dataPom[j][6])) &&
                        String.valueOf(data[i][7]).equals(String.valueOf(dataPom[j][7])) &&
                        String.valueOf(data[i][8]).equals(String.valueOf(dataPom[j][8])) &&
                        String.valueOf(data[i][9]).equals(String.valueOf(dataPom[j][9])) &&
                        String.valueOf(data[i][10]).equals(String.valueOf(dataPom[j][10])) &&
                        String.valueOf(data[i][11]).equals(String.valueOf(dataPom[j][11])) &&
                        String.valueOf(data[i][12]).equals(String.valueOf(dataPom[j][12])) &&
                        String.valueOf(data[i][13]).equals(String.valueOf(dataPom[j][13])) &&
                        String.valueOf(data[i][14]).equals(String.valueOf(dataPom[j][14]))) {
                    isDuplicated = true;
                }
            }
            if (!isDuplicated) {
                connection.createStatement().execute("INSERT INTO laptops  (producent, przekatna, rozdzielczosc, matryca, dotykowy, " +
                        "procesor, rdzenie, taktowanie, ram," +
                        "dysk, rdysku, nazwagraf, uklgraf, system, naped) VALUES" +
                        "('" + data[i][0] + "','" + data[i][1] + "','" + data[i][2] + "','" +
                        data[i][3] + "','" + data[i][4] + "','" + data[i][5] + "','" + data[i][6] + "','"
                        + data[i][7] + "','" + data[i][8] + "','" + data[i][9] + "','" + data[i][10] + "','"
                        + data[i][11] + "','" + data[i][12] + "','" + data[i][13] + "','" + data[i][14] + "')");
                numberNewRows++;
                System.out.println(numberNewRows);
            } else {
                numeberDupilacedRows++;
                isDuplicated = false;
            }
        }
    }

    public static void main(String[] args) {
        try {
            getHeaders();
        } catch (IOException e) {
            e.printStackTrace();
        }

        setButtons();
        info.setText("Integracja systemów");
        info.setFocusable(false);

        tableModel = new DefaultTableModel(0, 0);     //TableModel
        tableModel.setColumnIdentifiers(headers);

        JTable table = new JTable(tableModel) {
            @Override
            public void setValueAt(Object aValue, int row, int column) {
                if (aValue.toString().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(window, "Pole nie może być puste!");
                } else if ((column == 4 || column == 10) && aValue.toString().trim().length() != 3) {
                    JOptionPane.showMessageDialog(window, "Tekst musi miec 3 znaki!");
                } else if (column == 1 && !aValue.toString().endsWith("\"")) {
                    JOptionPane.showMessageDialog(window, "Pole musi się kończyć na \"");
                } else if (column == 2 & !aValue.toString().matches("[0-9]+x[0-9]+")) {
                    JOptionPane.showMessageDialog(window, "Wprowadź wartość według wzoru, np. 1920x1080");
                } else {
                    super.setValueAt(aValue, row, column);

                    if (!String.valueOf(data[row][column]).equals(String.valueOf(aValue))) {
                        data[row][column] = aValue;
                        editedRows.add(row);
                    }
                }
            }

            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component component = super.prepareRenderer(renderer, row, column);
                Color color;

                if (editedRows.contains(row))
                    color = Color.WHITE;
                else if (duplicatedRows.contains(row))
                    color = Color.RED;
                else
                    color = Color.GRAY;

                component.setBackground(color);
                window.repaint();
                return component;
            }
        };

        JScrollPane scrollPane = new JScrollPane(table, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(1480, 600));
        window.add(bImportTxt);
        window.add(bExportTxt);
        window.add(bImportXml);
        window.add(bExportXml);
        window.add(bImportDB);
        window.add(bExportDB);
        window.add(info);
        window.add(scrollPane);
        window.setVisible(true);

        bImportTxt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                importFromTxt();
                info.setText("Wczytano dane z pliku txt:  " + numberNewRows + " nowych rekordow, " + numeberDupilacedRows + " duplikatow");
                editedRows.clear();
                window.repaint();
            }
        });

        bExportTxt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportToTxt();
                info.setText("Zapisano dane do pliku txt");
            }
        });

        bImportXml.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    importFromXml();
                    info.setText("Wczytano dane z pliku xml:  " + numberNewRows + " nowych rekordow, " + numeberDupilacedRows + " duplikatow");
                    editedRows.clear();
                    window.repaint();
                } catch (SAXException | ParserConfigurationException ex) {
                    ex.printStackTrace();
                }
            }
        });

        bExportXml.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    exportToXml();
                } catch (ParserConfigurationException | TransformerException ex) {
                    ex.printStackTrace();
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });

        bImportDB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    importFromDataBase();
                    info.setText("Wczytano dane z bazy danych:  " + numberNewRows + " nowych rekordow, " + numeberDupilacedRows + " duplikatow");
                    editedRows.clear();
                    window.repaint();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        bExportDB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    exportToDataBase();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}

