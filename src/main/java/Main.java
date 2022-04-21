import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

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

        baseRows = 0;

        try {
            bufferedReaderPom.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                if (null == bufferedReaderPom.readLine()) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            baseRows++;
        }

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
            for (int j = 0; j <=14; j++) {
                data[i][j] = words[j];
                if (data[i][j] == null || data[i][j].equals("")) {
                    data[i][j] = "Brak informacji";
                }
            }
        }

        try {
            fileReader.close();
        } catch (IOException e) {
            System.out.println("Blad przy zamykaniu pliku!");
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
    }
}

