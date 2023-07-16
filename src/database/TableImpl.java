package database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

class TableImpl implements Table {
   
    String name;
    private List<Column> columns;
    private List<Object[]> rows;


    //생성자
    TableImpl() {
        this.name = null;;
        this.columns = new ArrayList<>();
        this.rows = new ArrayList<>();
    }

    //table객체를 받아서 새로운 table 생성
    TableImpl(Table table) {
        name = table.getName();
        this.columns = new ArrayList<>(((TableImpl)table).columns);
        this.rows = new ArrayList<>(((TableImpl)table).rows);
    }

    //File 읽은 후 columns와 rows 초기화한 table todtjd
    TableImpl(File csv) throws IOException {
        this.name = csv.getName().substring(0, csv.getName().indexOf("."));
        columns = new ArrayList<>();
        this.rows = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader(csv));
        String line;

        // 첫 번째 줄에서 카테고리 개수를 가져옴
        if ((line = br.readLine()) != null) {
            String[] categories = line.split(",");
            int numColumns = categories.length;

            // 각 열마다 Column 객체 생성 및 데이터 읽기
            for (int i = 0; i < numColumns; i++) {
                ColumnImpl column = new ColumnImpl(csv, i);
                columns.add(column);
            }

            for (int i = 0; i < columns.get(0).count(); i++){
                Object [] o = new Object[columns.size()];
                for(int j = 0; j < columns.size(); j++){
                    if (columns.get(j).isNumericColumn()){
                        o[j] = columns.get(j).getValue(i, Integer.class);
                    }
                    else{
                        o[j] = columns.get(j).getValue(i);
                    }
                }
                rows.add(o);
            }
            // for (int i = 0; i < rows.size(); i++){
            //     for (int j = 0; j < rows.get(i).length; j++){
            //     System.out.print(rows.get(i)[j] + "  " + rows.get(i)[j].getClass());
            //     }
            //     System.out.println(" ");
            // }
        }

        br.close();
    }

    /* 
    //Override 메소드 구현
    */
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void show() {
        int columnCount = columns.size();
        int rowCount = 0;
    
        // 테이블의 행 개수는 첫 번째 열(Column)의 행 개수로 설정
        if (columnCount > 0) {
            rowCount = columns.get(0).count();
        }
    
        // 각 열의 최대 너비 계산
        int[] columnWidths = new int[columnCount];
        for (int j = 0; j < columnCount; j++) {
            ColumnImpl column = (ColumnImpl)columns.get(j);
            int maxWidth = column.getHeader().length(); // 헤더의 너비
            for (int i = 0; i < rowCount; i++) {
                Object value = column.getValue(i);
                if (value != null) {
                    int valueWidth = value.toString().length();
                    maxWidth = Math.max(maxWidth, valueWidth);
                }
            }
            columnWidths[j] = maxWidth;
        }
    
        // 칼럼 이름 출력
        for (int j = 0; j < columnCount; j++) {
            String formattedColumn = String.format("%" + (columnWidths[j] + 1) + "s", columns.get(j).getHeader());
            System.out.print(formattedColumn + " |");
        }
        System.out.println();
    
        // 구분선 출력
        for (int j = 0; j < columnCount; j++) {
            int columnWidth = columnWidths[j];
            String separator = "-".repeat(columnWidth + 2);
            System.out.print(separator + "|");
        }
        System.out.println();
    
        // 칼럼 값 출력
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                Object value = columns.get(j).getValue(i);
                String formattedValue = "";
                if (value != null) {
                    formattedValue = String.format("%" + (columnWidths[j] + 1) + "s", value);
                } else {
                    formattedValue = String.format("%" + (columnWidths[j] + 1) + "s", "null");
                }
                System.out.print(formattedValue + " |");
            }
            System.out.println();
        }
    }
    
    
    @Override
    public void describe() {
        int intCount = 0;
        int stringCount = 0;

        System.out.println("<" + this + ">");
        System.out.println("RangeIndex: " + getRowCount() + " entries, 0 to " + (getRowCount() - 1));
        System.out.println("Data columns (total " + getColumnCount() + " columns)");
        System.out.printf("# |        Columns |   Non-null Count | Dtype\n");
        
        for (int i = 0; i < columns.size(); i++) {
            Column column = columns.get(i);
            int nonNullCount = 0;
            
            System.out.printf("%d | %14s | ", i, column.getHeader());
            
            for (int j = 0; j < getRowCount(); j++) {
                Object value = column.getValue(j);
                if (value != null) {
                    nonNullCount++;
                }
            }
            
            System.out.printf("%7d non-null | ", nonNullCount);
            
            if (column.isNumericColumn()) {
                System.out.printf("%3s", "int");
                intCount++;
            } else {
                System.out.printf("%3s", "String");
                stringCount++;
            }
            
            System.out.println("");
        }
        
        System.out.printf("dtypes: int(%d), String(%d)\n\n", intCount, stringCount);
    }
    
    

    @Override
    public Table head() {
        return head(5);
    }
    
    @Override
    public Table head(int lineCount) {
        TableImpl newTable = new TableImpl();
    
        // 헤더 정보 복사
        newTable.name = name;
        newTable.columns.addAll(columns);
    
        // 칼럼 정보 복사
        for (int i = 0; i < columns.size(); i++) {
            Column column = columns.get(i);
            ColumnImpl newColumn = new ColumnImpl();
            newColumn.head = column.getHeader();
    
            List<Object> selectedRows = new ArrayList<>();
            int numRowsToCopy = Math.min(lineCount, column.count());
            for (int j = 0; j < numRowsToCopy; j++) {
                selectedRows.add(column.getValue(j));
            }
    
            newColumn.columns.addAll(selectedRows);
            newTable.columns.set(i, newColumn);
        }
    
        return newTable;
    }
    
    @Override
    public Table tail() {
        return tail(5);
    }
    
    @Override
    public Table tail(int lineCount) {
        TableImpl newTable = new TableImpl();
    
        // 이름 복사
        newTable.name = name;
        newTable.columns.addAll(columns);
    
        // 칼럼 헤더 복사
        for (int i = 0; i < columns.size(); i++) {
            Column column = columns.get(i);
            ColumnImpl newColumn = new ColumnImpl();
            newColumn.head = column.getHeader();
    
            List<Object> selectedRows = new ArrayList<>();
            int numRowsToCopy = Math.min(lineCount, column.count());
            int startIndex = Math.max(0, column.count() - numRowsToCopy);
            for (int j = startIndex; j < column.count(); j++) {
                selectedRows.add(column.getValue(j));
            }
    
            newColumn.columns.addAll(selectedRows);
            newTable.columns.set(i, newColumn);
        }
    
        return newTable;
    }
    

    public Table selectRows(int beginIndex, int endIndex) {
        if (beginIndex < 0 || endIndex > getColumn(0).count() || beginIndex > endIndex) {
            throw new IndexOutOfBoundsException("Invalid");
        }
        
        TableImpl newTable = new TableImpl();
    
        // 이름 복사
        newTable.name = name;

        // 칼럼 헤더 복사 && 데이터 복사
        for (int i = 0; i < getColumnCount(); i++) {
            Column column = getColumn(i);
            ColumnImpl newColumn = new ColumnImpl();
            newColumn.head = column.getHeader();
            List<Object> values = new ArrayList<>(endIndex - beginIndex);
            for (int j = beginIndex; j < endIndex; j++) {
                values.add(column.getValue(j));
            }
            newColumn.columns = values;
            newTable.columns.add(newColumn);
        }
    
        return newTable;
    }
    
    
    @Override
    public Table selectRowsAt(int... indices) {
        TableImpl newTable = new TableImpl();
    
        // 이름 복사
        newTable.name = name;

        // 칼럼 헤더 복사 && 데이터 복사
        for (int i = 0; i < getColumnCount(); i++) {
            Column column = getColumn(i);
            ColumnImpl newColumn = new ColumnImpl();
            newColumn.head = column.getHeader();
            List<Object> selectedRows = new ArrayList<>();
            for (int index : indices) {
                if (index < 0 || index >= column.count()) {
                    throw new IndexOutOfBoundsException("Invalid row index" + index);
                }
                selectedRows.add(column.getValue(index));
            }
            newColumn.columns = selectedRows;
            newTable.columns.add(newColumn);
        }
    
        return newTable;
    }
    
    

    @Override
    public Table selectColumns(int beginIndex, int endIndex) {
        if (beginIndex < 0 || endIndex > getColumnCount() || beginIndex > endIndex) {
            throw new IndexOutOfBoundsException("Invalid index 에러" + endIndex);
        }
        
        TableImpl newTable = new TableImpl();

        // Copy header information
        newTable.name = name;
        newTable.columns.addAll(columns.subList(beginIndex, endIndex));

        return newTable;
    }

    @Override
    public Table selectColumnsAt(int... indices) {
        TableImpl newTable = new TableImpl();

        // Copy header information
        newTable.name = name;
        for (int index : indices) {
            if (index < 0 || index >= getColumnCount()) {
                throw new IndexOutOfBoundsException("Invalid column" + index);
            }
            newTable.columns.add(getColumn(index));
        }

        return newTable;
    }


    @Override
    public Table sort(int byIndexOfColumn, boolean isAscending, boolean isNullFirst) {
            // Create a new table with the same columns as the original table
            TableImpl sortedTable = (TableImpl)Database.getTable(this.name);
        
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
        


    @Override
    public int getRowCount() {
        if (columns.isEmpty()) {
            return 0;
        }
        return columns.get(0).count();
    }
    
    @Override
    public int getColumnCount() {
        return columns.size();
    }
    

    @Override
    public Column getColumn(int index) {
        if (index < 0 || index >= columns.size()) {
            throw new IndexOutOfBoundsException("Invalid column");
        }
        return columns.get(index);
    }
    
    @Override
    public Column getColumn(String name) {
        int columnIndex = -1;
        for (int i = 0; i < columns.size(); i++) {
            if (columns.get(i).getHeader().equals(name)) {
                columnIndex = i;
                break;
            }
        }
        
        if (columnIndex == -1) {
            throw new IllegalArgumentException("Column not found" + name);
        }
        
        return columns.get(columnIndex);
    }

    @Override
public <T> Table selectRowsBy(String columnName, Predicate<T> predicate) {
    TableImpl selectedTable = new TableImpl(this);

    // Get the column index based on the column name
    int columnIndex = -1;
    for(int i = 0; i < this.getColumns().size(); i++){
        if(this.getColumn(i).getHeader().equals(columnName)){
            columnIndex = i;
        }
    }
    if (columnIndex == -1){
        throw new IndexOutOfBoundsException("columnName error");
    }

    // Get the rows from the current table
    List<Object []> rows = this.getRows();
    List<Object []> newRows = new ArrayList<>();

    // Iterate over the rows and check the predicate for the specified column
    for (int i = 0; i < rows.size(); i++) {
        Object value = rows.get(i)[columnIndex];

        // Check if the value satisfies the predicate
        if (predicate.test((T) value)) {
            newRows.add(rows.get(i));
        }
    }

    // Create columns in the selected table based on the rows
    selectedTable.setRows(newRows);
    selectedTable.makeColumnsByRows(selectedTable.getRows());

    return selectedTable;
}


    @Override
    public Table crossJoin(Table rightTable) {

        TableImpl crossjoinedTable = new TableImpl(this);

        List<Object[]> crossJoinedRows = new ArrayList<>();
        
        for(int i = 0; i < crossjoinedTable.getColumns().size(); i++) {
            String namePlusHead = crossjoinedTable.getName() + "." +crossjoinedTable.getColumn(i).getHeader();
            ColumnImpl temp = new ColumnImpl(namePlusHead);
            crossjoinedTable.getColumns().set(i, temp);
        }

        for(int i = 0; i < ((TableImpl)rightTable).getColumns().size(); i++){
            String namePlusHead = rightTable.getName() + "." +rightTable.getColumn(i).getHeader();
            ColumnImpl temp = new ColumnImpl(namePlusHead);
            crossjoinedTable.addColumns(temp);
        }
    
        // Get the rows from the current table
        List<Object[]> currentRows = this.getRows();
        // Get the rows from the another table
        List<Object[]> anotherRows = ((TableImpl) rightTable).getRows();

    
        // Generate the cross-joined rows
        for (Object[] currentRow : currentRows) {
            for (Object[] anotherRow : anotherRows) {
                Object[] crossJoinedRow = new Object[currentRow.length + anotherRow.length];
                System.arraycopy(currentRow, 0, crossJoinedRow, 0, currentRow.length);
                System.arraycopy(anotherRow, 0, crossJoinedRow, currentRow.length, anotherRow.length);
                crossJoinedRows.add(crossJoinedRow);
            }
        }
        
        crossjoinedTable.setRows(crossJoinedRows);
        crossjoinedTable.makeColumnsByRows(crossjoinedTable.getRows());
        return crossjoinedTable;
    }
    
    
    
    @Override
    public Table innerJoin(Table rightTable, List<JoinColumn> joinColumns) {
        Table crossjoinedTable = this.crossJoin(rightTable);
        // System.out.println(((TableImpl)crossjoinedTable).getRows().size());
        List<Object[]> newRows = new ArrayList<>();

        String headName1 = ((TableImpl)this).getName() + "." + joinColumns.get(0).getColumnOfThisTable();
        String headName2 = ((TableImpl)rightTable).getName() + "." + joinColumns.get(0).getColumnOfAnotherTable();
        Column original = ((TableImpl)crossjoinedTable).getColumn(headName1);
        Column another = ((TableImpl)crossjoinedTable).getColumn(headName2);
        
        for(int i = 0; i < original.count(); i++){
            if(original.getValue(i) == null && another.getValue(i) == null ){
                newRows.add(((TableImpl)crossjoinedTable).getRow(i));
            }
            else if (original.getValue(i).equals(another.getValue(i))){
                newRows.add(((TableImpl)crossjoinedTable).getRow(i));
                    }
                }
                ((TableImpl)crossjoinedTable).setRows(newRows);
                ((TableImpl)crossjoinedTable).makeColumnsByRows(newRows);
                return (crossjoinedTable);
            }
    
    @Override
    public Table outerJoin(Table rightTable, List<JoinColumn> joinColumns) {
        Table crossjoinedTable = this.crossJoin(rightTable);
        // System.out.println(((TableImpl)crossjoinedTable).getRows().size());
        List<Object[]> newRows = new ArrayList<>();

        String headName1 = ((TableImpl)this).getName() + "." + joinColumns.get(0).getColumnOfThisTable();
        String headName2 = ((TableImpl)rightTable).getName() + "." + joinColumns.get(0).getColumnOfAnotherTable();
        Column original = ((TableImpl)crossjoinedTable).getColumn(headName1);
        Column another = ((TableImpl)crossjoinedTable).getColumn(headName2);
        
        for(int i = 0; i < original.count(); i++){
            if(original.getValue(i) == null || another.getValue(i) == null){
                    continue ;
                }
            else if (original.getValue(i).equals(another.getValue(i))){
                    newRows.add(((TableImpl)crossjoinedTable).getRow(i));
                    }
                }
            
            for(int i = 0; i < ((TableImpl)this).getRows().size(); i++){
                int flag = 0;
                for(int j = 0; j <newRows.size(); j++){
                    if (((TableImpl)this).getRow(i)[0].equals(newRows.get(j)[0])){
                        flag = 1;
                    }
                }
                if (flag == 0){
                    Object[] newRow = new Object[newRows.get(0).length];
                    int k = 0;
                    while ( k < ((TableImpl)this).getRow(i).length){
                        newRow[k] = ((TableImpl)this).getRow(i)[k];
                        k++;
                    }
                    while (k < newRows.get(0).length){
                        newRow[k] = null;
                        k++;
                    }
                    newRows.add(newRow);
                }
            }
            ((TableImpl)crossjoinedTable).setRows(newRows);
            ((TableImpl)crossjoinedTable).makeColumnsByRows(newRows);
                return (crossjoinedTable);
    }
    

    @Override
    public Table fullOuterJoin(Table rightTable, List<JoinColumn> joinColumns) {
        Table crossjoinedTable = this.crossJoin(rightTable);
        // System.out.println(((TableImpl)crossjoinedTable).getRows().size());
        List<Object[]> newRows = new ArrayList<>();

        String headName1 = ((TableImpl)this).getName() + "." + joinColumns.get(0).getColumnOfThisTable();
        String headName2 = ((TableImpl)rightTable).getName() + "." + joinColumns.get(0).getColumnOfAnotherTable();
        Column original = ((TableImpl)crossjoinedTable).getColumn(headName1);
        Column another = ((TableImpl)crossjoinedTable).getColumn(headName2);
        
        for(int i = 0; i < original.count(); i++){
            if(original.getValue(i) == null || another.getValue(i) == null){
                    continue ;
                }
            else if (original.getValue(i).equals(another.getValue(i))){
                    newRows.add(((TableImpl)crossjoinedTable).getRow(i));
                    }
                }
            
            for(int i = 0; i < ((TableImpl)this).getRows().size(); i++){
                int flag = 0;
                for(int j = 0; j <newRows.size(); j++){
                    if (((TableImpl)this).getRow(i)[0].equals(newRows.get(j)[0])){
                        flag = 1;
                    }
                }
                if (flag == 0){
                    Object[] newRow = new Object[newRows.get(0).length];
                    int k = 0;
                    while ( k < ((TableImpl)this).getRow(i).length){
                        newRow[k] = ((TableImpl)this).getRow(i)[k];
                        k++;
                    }
                    while (k < newRows.get(0).length){
                        newRow[k] = null;
                        k++;
                    }
                    newRows.add(newRow);
                }
            }
            for(int i = 0; i < ((TableImpl)rightTable).getRows().size(); i++){
                int flag = 0;
                for(int j = 0; j <newRows.size(); j++){
                    if (((TableImpl)rightTable).getRow(i)[0].equals(newRows.get(j)[5])){
                        flag = 1;
                    }
                }
                if (flag == 0){
                    Object[] newRow = new Object[newRows.get(0).length];
                    int k = 0;
                    while (k < ((TableImpl)this).getRow(i).length){
                        newRow[k] = null;
                        k++;
                    }
                    int m = 0;
                    while (k < newRows.get(0).length){
                        newRow[k] = ((TableImpl)rightTable).getRow(i)[m];
                        k++;
                        m++;
                    }
                    newRows.add(newRow);
                }
            }
            ((TableImpl)crossjoinedTable).setRows(newRows);
            ((TableImpl)crossjoinedTable).makeColumnsByRows(newRows);
                return (crossjoinedTable);
    }


    /**
     * Overriding이 아닌 필요한 메소드
     * 접근제어자는 default
     */
    List<Column> getColumns(){
        return columns;
    }

    void addColumns(Column column){
        if (column == null){
            throw new NullPointerException("Ivalid column");
        }
        columns.add(column);
    }

    void setColumns(List<ColumnImpl> columns){
        if (columns == null){
            throw new NullPointerException("Ivalid column");
        }
        this.columns = new ArrayList<>(columns);
    }
        
    Object[] getRow(int rowIndex) {
            if (rowIndex < 0 || rowIndex >= rows.size()) {
                throw new IndexOutOfBoundsException("row index 에러");
            }
            return rows.get(rowIndex);
        }
        
    List<Object[]> getRows(){
        return rows;
    }

    void setRows(List<Object []> rows){
        this.rows = new ArrayList<>(rows);
    }

    void addRow(Object[] row) {
        // if (row.length != columns.size()) {
        //     throw new IllegalArgumentException("row size 에러");
        // }
            rows.add(row);
        }
        
    void makeColumnsByRows(List<Object[]> rows) {
            
            if (!rows.isEmpty()) {
                int numColumns = rows.get(0).length;
        
                // Create columns
                for (int i = 0; i < numColumns; i++) {
                    ColumnImpl column = new ColumnImpl();
                    column.head = this.columns.get(i).getHeader();
                    List<Object> columnValues = new ArrayList<>();
                    for (Object[] row : rows) {
                        columnValues.add(row[i]);
                    }
                    column.setValues(columnValues);
                    this.columns.set(i, column);
                }
            }
    }
}

    

