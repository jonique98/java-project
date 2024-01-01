# java-project

객체지향 프로그래밍 과제
165 / 200

## 과제 설명

csv 파일을 읽고 구분자(,)를 기준으로 테이블을 생성
데이터베이스 트랜잭션 구현 (ex. SELECT(ROW, COLUMN), UPDATE, DELETE, INSERT, INNERJOIN, OUTERJOIN 등)

**ex) books.csv**

```
id,title,type,author_id,editor_id,translator_id
1,Time to Grow Up!,original,11,21,
2,Your Trip,translated,15,22,32
3,Lovely Love,original,14,24,
4,Dream Your Life,original,11,24,
5,Oranges,translated,12,25,31
6,Your Happy Life,translated,15,22,33
7,Applied AI,translated,13,23,34
8,My Last Book,original,11,28,
```
<img src = https://github.com/jonique98/javascript-christmas-6-jonique98/assets/104954561/720b2bbc-7d46-48d9-bae3-5b64aff90361 />

## 구조

```
+ rsc --- 리소스 파일
  └ authors.csv
  └ books.csv
  └ editors.csv
  └ translators.csv

+ src
  + database
    └ Column.java ------------- 컬럼 단위로 데이터를 관리한느 메소드를 정의한 interface
	└ ColumnImpl.java ----------- Column.java를 구현한 클래스
	└ Database.java ------------- DDL 위주 명령어를 구현한 class (ex. CREATE TABLE)
	└ Joinable.java ------------- JOIN 관련 메소드가 정의된 interface
	└ JoinableImpl.java ---------- Joinable.java를 구현한 클래스
	└ Table.java ------------------ DML 위주 명령어를 구현한 class
  + test
    └ Test.java ------------------ 테스트 코드
```

## TEST 결과

**Database.showtables() : 데이터베이스의 테이블 목록 출력**\

**Table.show() : 테이블의 데이터 출력**

**Table.describe() : 테이블 메타 데이터 출력**

<img width="707" alt="스크린샷 2024-01-01 16 50 26" src="https://github.com/jonique98/java-project/assets/104954561/9f74ddec-258e-4cd3-8d9c-14d021c3040c">

**Table.head() : 테이블의 0-5번 줄(행) 데이터**

**Table.tail() : 테이블의 마지막 5번 줄(행) 데이터**

<img width="714" alt="스크린샷 2024-01-01 16 52 01" src="https://github.com/jonique98/java-project/assets/104954561/9493ecf6-6752-4e16-8b60-4b1e9be13bf5">

**Table.selectRows(beginIndex, endIndex) : 테이블의 beginIndex부터 endIndex까지의 데이터로 구성된 테이블 반환**

**Table.selectRowsAt(...index) : 테이블의 index에 해당하는 데이터로만 구성된 테이블 반환**

 **Table.selectColumns(beginIndex, endIndex) : 테이블의 beginIndex부터 endIndex까지의 컬럼으로 구성된 테이블 반환**

**Table.selectColumnsAt(...index) : 테이블의 index에 해당하는 컬럼으로만 구성된 테이블 반환**

<img width="728" alt="스크린샷 2024-01-01 16 52 29" src="https://github.com/jonique98/java-project/assets/104954561/aac1e0f5-cc84-481a-b9f9-6952571e8bb7">

**Table.sort(byINdexOfColumn, isAscending, isNullFirst) : 테이블의 byIndexOfColumn 컬럼을 기준으로 정렬**

<img width="927" alt="스크린샷 2024-01-01 16 52 44" src="https://github.com/jonique98/java-project/assets/104954561/cd354e82-253d-43b1-a344-5b3dcfb97ac6">

**Table.crossJoin(rightTable) : 테이블과 rightTable의 모든 데이터를 조합한 테이블 반환(rightTable이 오른쪽)**

<img width="1440" alt="스크린샷 2024-01-01 16 53 35" src="https://github.com/jonique98/java-project/assets/104954561/9c421799-ec6b-48a5-90cd-6bd8f913a0a8">

**Table innerJoin(Table rightTable, List<JoinColumn> joinColumns): 두번째 인자 columns르 기준으로 두 테이블을 조인**

<img width="1422" alt="스크린샷 2024-01-01 16 53 44" src="https://github.com/jonique98/java-project/assets/104954561/c3f416a7-1834-4cbb-a650-529ab72c9e1c">

**랜던한 row, column 인덱스를 selectedRowsAt, selectedColumnsAt을 통해 선택한 후 출력을 확인하고**

**setValue 메소드를 통해 column의 head값을 변경**

<img width="716" alt="스크린샷 2024-01-01 16 56 03" src="https://github.com/jonique98/java-project/assets/104954561/02f1119b-e799-4933-9937-08db4d994a09">

