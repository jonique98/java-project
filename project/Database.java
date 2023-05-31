import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Database {
    public static void main(String[] args) {
        createTable(new File("rsc/authors.csv"));
    }

    public static void createTable(File file) {
        String delimiter = ","; // CSV 파일에서 각 값을 구분하는 구분자를 지정합니다.

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            List<Table> tables = new ArrayList<>();

            while ((line = br.readLine()) != null) {
                String[] values = line.split(delimiter); // 구분자를 기준으로 값을 분리합니다.

                // 분리된 값들을 이용하여 테이블 객체 생성
                Table table = new Table(values); // 예시로 3개의 열(column)이 있다고 가정합니다.
                tables.add(table); // 테이블 객체를 리스트에 추가합니다.
            }

            // 각 열의 최대 너비를 계산합니다.
            int[] maxColumnWidths = calculateMaxColumnWidths(tables);

            // 생성된 테이블 객체들을 출력 또는 필요한 작업을 수행합니다.
            for (Table table : tables) {
                System.out.println(table.toFormattedString("|", maxColumnWidths)); // 테이블 객체를 |로 구분하여 출력합니다.
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int[] calculateMaxColumnWidths(List<Table> tables) {
        int columnCount = tables.get(0).getColumnCount();
        int[] maxColumnWidths = new int[columnCount];

        for (Table table : tables) {
            for (int i = 0; i < columnCount; i++) {
                int currentWidth = table.getColumnWidth(i);
                if (currentWidth > maxColumnWidths[i]) {
                    maxColumnWidths[i] = currentWidth;
                }
            }
        }

        return maxColumnWidths;
    }
}

class Table {
    ArrayList<String> column = new ArrayList<>();

    public Table(String[] column) {
        for (int i = 0; i < column.length; i++) {
            this.column.add(column[i]);
        }
    }

    public int getColumnCount() {
        return column.size();
    }

    public int getColumnWidth(int columnIndex) {
        return column.get(columnIndex).length();
    }

    public String toFormattedString(String delimiter, int[] maxColumnWidths) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < column.size(); i++) {
            String value = column.get(i);
            int padding = maxColumnWidths[i] - value.length();
            sb.append(" ".repeat(padding)).append(value).append(delimiter);
        }
        // 마지막 구분자 제거
        sb.setLength(sb.length() - delimiter.length());
        return sb.toString();
    }

    @Override
    public String toString() {
        return column.toString();
    }
}
