package database;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.function.Predicate;

public class TableImpl implements Table {
    private final String name;
    private final List<String> header;
    private final List<List<String>> rows;

    public TableImpl(File csv) throws FileNotFoundException {
        this.name = csv.getName().substring(0, csv.getName().indexOf("."));
        this.header = null;
        this.rows = null;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void show() {
    }

    @Override
    public void describe() {

    }

    @Override
    public Table head() {
        for (int i = 0; i < 5; i++) {
            System.out.println(rows.get(i));
        }
        return null;
    }

    @Override
    public Table head(int lineCount) {
        for (int i = 0; i < lineCount; i++) {
            System.out.println(rows.get(i));
        }
        return null;
    }

    @Override
    public Table tail() {
        for (int i = rows.size() - 5; i < rows.size(); i++) {
            System.out.println(rows.get(i));
        }
        return null;
    }

    @Override
    public Table tail(int lineCount) {
        for (int i = rows.size() - lineCount; i < rows.size(); i++) {
            System.out.println(rows.get(i));
        }
        return null;
    }

    @Override
    public Table selectRows(int beginIndex, int endIndex) {
        return null;
    }

    @Override
    public Table selectRowsAt(int... indices) {

        return null;
    }

    @Override
    public Table selectColumns(int beginIndex, int endIndex) {

        return null;
    }

    @Override
    public Table selectColumnsAt(int... indices) {
        
        return null;
    }

    // @Override
    public Table innerJoin(Table another, JoinColumn... joinColumns) {
        
        return null;
    }

    // @Override
    public Table outerJoin(Table another, JoinColumn... joinColumns) {
        return null;
    }

    @Override
    public Table crossJoin(Table another) {
        return null;
    }


    @Override
    public Table sort(int byIndexOfColumn, boolean isAscending, boolean isNullFirst) {
        return null;
    }


    @Override
    public int getRowCount() {
        return 0;
    }

    @Override
    public int getColumnCount() {
        return 0;
    }


    @Override
    public Column getColumn(int index) {
        return null;
    }

    @Override
    public Column getColumn(String name) {
        return null;
    }

    @Override
    public Table fullOuterJoin(Table rightTable, List<JoinColumn> joinColumns) {
        return null;
    }

    @Override
    public Table innerJoin(Table rightTable, List<JoinColumn> joinColumns) {
        return null;
    }

    @Override
    public Table outerJoin(Table rightTable, List<JoinColumn> joinColumns) {
        return null;
    }


    @Override
    public <T> Table selectRowsBy(String columnName, Predicate<T> predicate) {
        return null;
    }

}