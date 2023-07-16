package database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ColumnImpl implements Column {

    String head;
    List<Object> columns;

    //기본 생성자
    ColumnImpl() {
        head = null;
        columns = new ArrayList<>();
    }

    ColumnImpl(String head) {
        this.head = head;
        this.columns = new ArrayList<>();
    }

    //columns를 받아서 초기화
    ColumnImpl(String head , List<Object> column) {
        this.head = head;
        this.columns = new ArrayList<>(column);
    }

    ColumnImpl(ColumnImpl column) {
        this.head = column.head;
        this.columns = new ArrayList<>(column.columns);
    }
    
    //File을 읽고 head와 columns 초기화
    ColumnImpl(File csv, int index) throws IOException {
        // columnNames = csv.getName().substring(0, csv.getName().indexOf("."));

        head = null;

        BufferedReader br = new BufferedReader(new FileReader(csv));
        String line;

         // 첫 번째 행을 읽어서 배열의 이름으로 저장
        if ((line = br.readLine()) != null) {
            head = line.split(",")[index];
        }
        columns = new ArrayList<>();

        // 나머지 행을 읽어서 열별로 데이터 저장
        while ((line = br.readLine()) != null) {
                Object convertedValue;
                String value;
                try{
                value = line.split(",")[index];
                }catch (ArrayIndexOutOfBoundsException e){
                value = null;
                }
                try {
                    // 숫자로 변환 가능한 경우 int로 저장
                    convertedValue = Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    // 숫자로 변환할 수 없는 경우 String으로 저장
                    convertedValue = value;
                }
                columns.add(convertedValue);
            }
        br.close();
    }

    /*
     * Override 메소드 구현
     */
    @Override
    public String getHeader() {
        return head;
    }

    @Override
    public String getValue(int index) {
        Object value = columns.get(index);
        return (value != null) ? value.toString() : null;
    }

    @Override
    public <T extends Number> T getValue(int index, Class<T> t) {
        Object value = columns.get(index);
        if (value == null) {
            return null;
        }
        if (t.equals(Double.class)) {
            return t.cast(Double.parseDouble(value.toString()));
        } else if (t.equals(Long.class)) {
            return t.cast(Long.parseLong(value.toString()));
        } else if (t.equals(Integer.class)) {
            return t.cast(Integer.parseInt(value.toString()));
        } else {
            throw new IllegalArgumentException("Unsupported type: " + t.getSimpleName());
        }
    }

    @Override
    public void setValue(int index, String value) {
        columns.set(index, value);
    }

    @Override
    public void setValue(int index, int value) {
        columns.set(index, value);
    }

    @Override
    public int count() {
        return columns.size();
    }

    @Override
    public void show() {
        for (Object value : columns) {
            System.out.println(value);
        }
    }

    @Override
    public boolean isNumericColumn() {
        for (Object value : columns) {
            if (value != null && !(value instanceof Number)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public long getNullCount() {
        long nullCount = 0;
        for (Object value : columns) {
            if (value == null) {
                nullCount++;
            }
        }
        return nullCount;
    }

    /*
     * Override 아닌 필요한 메소드
     */
    List<Object> getColumns() {
        return columns;
    }

    void setValues(List<Object> column){
        for(int i = 0; i < column.size(); i++){
            this.columns.add(column.get(i));
        }
    }

}