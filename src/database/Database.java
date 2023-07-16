package database;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Database {
    // 테이블명이 같으면 같은 테이블로 간주된다.
    private static final Set<Table> tables = new HashSet<>();

    // 테이블 이름 목록을 출력한다.
    public static void showTables() {
        for (Table table : tables) {
            System.out.println(table.getName());
        }
    }

    /**
     * 파일로부터 테이블을 생성하고 table에 추가한다.
     *
     * @param csv 확장자는 csv로 가정한다.
     *            파일명이 테이블명이 된다.
     *            csv 파일의 1행은 컬럼명으로 사용한다.
     *            csv 파일의 컬럼명은 중복되지 않는다고 가정한다.
     *            컬럼의 데이터 타입은 int 아니면 String으로 판정한다.
     *            String 타입의 데이터는 ("), ('), (,)는 포함하지 않는 것으로 가정한다.
     */
    public static void createTable(File csv){
        Table table = null;
        try {
            table = new TableImpl(csv);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        tables.add(table); // 테이블 객체를 리스트에 추가합니다.
            }


    // tableName과 테이블명이 같은 테이블을 리턴한다. 없으면 null 리턴.
    public static Table getTable(String tableName) {
        for (Table table : tables) {
            if (table.getName().equals(tableName)) {
                return table;
            }
        }
        return null;
    }

    /**
     * @return 정렬된 새로운 Table 객체를 반환한다. 즉, 첫 번째 매개변수 Table은 변경되지 않는다.
     * @param byIndexOfColumn 정렬 기준 컬럼, 존재하지 않는 컬럼 인덱스 전달시 예외 발생시켜도 됨.
     */
    public static Table sort(Table table, int byIndexOfColumn, boolean isAscending, boolean isNullFirst) {
        // Create a new table with the same columns as the original table
        TableImpl sortedTable = new TableImpl(table);
    
        // Get the column to sort by
        // Column sortColumn = table.getColumn(byIndexOfColumn);
    
        // Get the rows of the table
        List<Object[]> rows = sortedTable.getRows();
    
        // Sort the rows based on the column values
        rows.sort((row1, row2) -> {
            Object value1 = row1[byIndexOfColumn];
            Object value2 = row2[byIndexOfColumn];
    
            // Handle null values
            if (value1 == null && value2 == null) {
                return 0;
            } else if (value1 == null && value2 != null) {
                return isNullFirst ? -1 : 1;
            } else if (value1 != null && value2 == null) {
                return isNullFirst ? 1 : -1;
            }
    
            // Compare the values based on the specified sorting order
            if (value1 instanceof Integer && value2 instanceof Integer) {
                int intValue1 = (int) value1;
                int intValue2 = (int) value2;
                return isAscending ? intValue1 - intValue2 : intValue2 - intValue1;
            } else {
                throw new IllegalArgumentException("Values are not Comparable (String)");
            }
        });
    
        // Update the rows in the sorted table
        sortedTable.setRows(rows);
        sortedTable.makeColumnsByRows(sortedTable.getRows());
        return sortedTable;
    }
    
}
