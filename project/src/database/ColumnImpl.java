package database;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ColumnImpl implements Column {
    private final String name;
    private final List<String> values;

    public ColumnImpl(String name, List<String> values) {
        this.name = name;
        this.values = values;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<String> getValues() {
        return values;
    }

    @Override
    public void show() {
        System.out.println(name);
        for (String value : values) {
            System.out.println(value);
        }
    }

    @Override
    public void describe() {

    }

    @Override
    public Column head() {
        return null;
    }

    @Override
    public Column head(int lineCount) {
        return null;
    }

    @Override
    public Column tail() {
        return null;
    }

    @Override
    public Column tail(int lineCount) {
        return null;
    }

    @Override
    public Column selectRows(int beginIndex, int endIndex) {
        return null;
    }

    @Override
    public Column selectRowsAt(int... indices) {
        return null;
    }

    @Override
    public Column selectColumns(int beginIndex, int endIndex) {
        return null;
    }

    @Override
    public Column selectColumnsAt(int... indices) {
        return null;
    }

    @Override
    public Column filter(Predicate<String> predicate) {
        return null;
    }

    @Override
    public Column sort(boolean isAscending, boolean isNullFirst) {
        return null;
    }

    public Column getHeader() {
        return null;
    }

}