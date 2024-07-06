package net.ucanaccess.jdbc;

import static net.ucanaccess.type.SqlConstants.ORIGINAL_TYPE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.spannm.jackcess.Database;
import io.github.spannm.jackcess.Index.Column;
import io.github.spannm.jackcess.PropertyMap;
import io.github.spannm.jackcess.Table;
import net.ucanaccess.exception.UcanaccessSQLException;
import net.ucanaccess.test.AccessVersionSource;
import net.ucanaccess.test.UcanaccessBaseTest;
import net.ucanaccess.type.AccessVersion;
import org.junit.jupiter.params.ParameterizedTest;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

class CreateTableTest extends UcanaccessBaseTest {

    private void createAsSelect() throws SQLException {
        try (UcanaccessStatement st = ucanaccess.createStatement()) {
            st.executeUpdate("CREATE TABLE tbl_bis as (SELECT baaaa, a, c FROM tbl1) WITH DATA");
            checkQuery("SELECT * FROM tbl_bis ORDER BY baaaa",
                recs(rec("33A", 3, "G"), rec("33B", 111, "G")));

            st.executeUpdate("CREATE TABLE tbl_quadris as (SELECT tbl1.baaaa, tbl_bis.baaaa as xxx FROM tbl1, tbl_bis) WITH DATA");
            dumpQueryResult("SELECT * FROM tbl_quadris ORDER BY baaaa");
        }
    }

    private void createAsSelect2() throws SQLException {
        try (UcanaccessStatement st = ucanaccess.createStatement()) {
            st.executeUpdate("CREATE TABLE tbl_tris as (SELECT baaaa, a, c FROM tbl1) WITH NO DATA");
            st.execute("INSERT INTO tbl_tris SELECT * FROM tbl_bis");
            checkQuery("SELECT * FROM tbl_tris ORDER BY baaaa",
                recs(rec("33A", 3, "G"), rec("33B", 111, "G")));
        }
    }

    private void createPs() throws SQLException {
        try (UcanaccessStatement st = ucanaccess.createStatement()) {
            st.executeUpdate("CREATE \nTABLE BBB (baaaa \nVARCHAR(2) PRIMARY KEY)");
            assertThatThrownBy(() -> st.execute("CREATE TABLE BBB (baaaa TEXT PRIMARY KEY, b TEXT)"))
                .isInstanceOf(SQLException.class)
                .hasMessageContaining("object name already exists");
        }
    }

    private void createSimple() throws SQLException {
        try (UcanaccessStatement st = ucanaccess.createStatement()) {
            executeStatements(st,
                "INSERT INTO tbl1(baaaa, c) VALUES ('33A', 'G')",
                "INSERT INTO tbl1(baaaa, a, c) VALUES ('33B', 111, 'G')");
            checkQuery("SELECT baaaa, a, c FROM tbl1 ORDER BY baaaa",
                recs(rec("33A", 3, "G"), rec("33B", 111, "G")));
        }
    }

    void defaults() throws Exception {
        try (UcanaccessStatement st = ucanaccess.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT D, E FROM tbl1");
            while (rs.next()) {
                assertNotNull(rs.getObject(1));
                assertNotNull(rs.getObject(2));
            }
            Database db = ucanaccess.getDbIO();
            Table tb = db.getTable("tbl1");
            PropertyMap pm = tb.getColumn("d").getProperties();
            assertEquals("now()", pm.getValue(PropertyMap.DEFAULT_VALUE_PROP));
            PropertyMap pm1 = tb.getColumn("a").getProperties();
            assertEquals(true, pm1.getValue(PropertyMap.REQUIRED_PROP));
            tb = db.getTable("TBL");
            pm = tb.getColumn("NUMBER").getProperties();
            assertEquals("-4.6", pm.getValue(PropertyMap.DEFAULT_VALUE_PROP));
            assertEquals(true, pm.getValue(PropertyMap.REQUIRED_PROP));
            pm = tb.getColumn("BLANK").getProperties();
            assertEquals(" ", pm.getValue(PropertyMap.DEFAULT_VALUE_PROP));
        }
    }

    void setDPK() throws SQLException {
        try (UcanaccessStatement st = ucanaccess.createStatement()) {
            executeStatements(st,
                "CREATE TABLE tbl_dkey(c COUNTER, number NUMERIC(23,5), PRIMARY KEY (c, number))",
                "CREATE TABLE tbl_dunique(c TEXT, number NUMERIC(23,5), UNIQUE (c, number))");
        }

        ucanaccess.setAutoCommit(false);

        try (UcanaccessStatement st = ucanaccess.createStatement()) {
            executeStatements(st,
                "INSERT INTO tbl_dunique VALUES('ddl forces commit', 2.3)",
                "CREATE TABLE tbl_dtrx(c TEXT, number NUMERIC(23,5), UNIQUE(c, number))",
                "INSERT INTO tbl_dtrx VALUES('I''ll be forgotten sob sob', 55555.3)",
                "ALTER TABLE tbl_dtrx ADD CONSTRAINT pk_dtrx PRIMARY KEY (c, number)");
        } catch (Exception _ex) {
            ucanaccess.rollback();
        }

        try (UcanaccessStatement st = ucanaccess.createStatement()) {
            executeStatements(st,
                "INSERT INTO tbl_dtrx VALUES('Hi all', 444.3)",
                "INSERT INTO tbl_dtrx VALUES('Hi all', 4454.3)");
        }

        dumpQueryResult("SELECT * FROM tbl_dtrx");
        dumpQueryResult("SELECT * FROM tbl_dunique");
        ucanaccess.commit();
        checkQuery("SELECT * FROM tbl_dunique");
        checkQuery("SELECT * FROM tbl_dtrx");
    }

    void setTableProperties() throws SQLException {
        try (UcanaccessStatement st = ucanaccess.createStatement()) {
            st.execute("CREATE TABLE tbl(c COUNTER PRIMARY KEY, "
                + "number NUMERIC(23,5) DEFAULT -4.6 NOT NULL, "
                + "txt1 TEXT(23) DEFAULT 'ciao', blank TEXT DEFAULT ' ', "
                + "dt DATE DEFAULT date(), txt2 TEXT(33),"
                + "txt3 TEXT)");
        }
    }

    private void notNullBug() throws SQLException, IOException {
        try (UcanaccessStatement st = ucanaccess.createStatement()) {
            st.execute("CREATE TABLE nnb(c COUNTER PRIMARY KEY, " + "number DECIMAL(23,5) DEFAULT -4.6 NOT NULL, "
                    + "txt1 TEXT(23) NOT NULL, blank TEXT, dt DATE NOT NULL, txt2 TEXT, txt3 TEXT NOT NULL)");

            assertNotNull("nnb", "number", true);
            assertNotNull("nnb", "txt1", true);
            assertNotNull("nnb", "blank", false);
            assertNotNull("nnb", "dt", true);
            assertNotNull("nnb", "txt2", false);
            assertNotNull("nnb", "txt3", true);
        }
    }

    private void assertNotNull(String _table, String _column, boolean _expectedNotNull) throws IOException {
        Database db = ucanaccess.getDbIO();
        Table tb = db.getTable(_table);
        PropertyMap pm = tb.getColumn(_column).getProperties();
        assertEquals(_expectedNotNull, pm.getValue(PropertyMap.REQUIRED_PROP));
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @AccessVersionSource(include = "V2010")
    void testCreate(AccessVersion _accessVersion) throws Exception {
        init(_accessVersion);

        executeStatements(
            "CREATE \nTABLE tbl1( baaaa \nTEXT PRIMARY KEY, A LONG DEFAULT 3 NOT NULL, C TEXT(255) NOT NULL, "
                + "d DATETIME DEFAULT now(), e TEXT DEFAULT 'l''aria')");

        createSimple();
        createPs();
        createAsSelect();
        createAsSelect2();
        setTableProperties();
        setDPK();
        defaults();
        notNullBug();

        executeStatements("DROP TABLE tbl1");
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @AccessVersionSource(include = "V2010")
    void testNaming(AccessVersion _accessVersion) throws SQLException {
        init(_accessVersion);

        try (UcanaccessStatement st = ucanaccess.createStatement()) {
            executeStatements(st,
                "CREATE TABLE [ggg kk] ([---bgaaf aa] AUTOINCREMENT PRIMARY KEY, [---bghhaaf b aa] TEXT(222) DEFAULT 'vvv')",
                "CREATE TABLE [ggg kkff] ([---bgaaf() aa] AUTOINCREMENT PRIMARY KEY, [---bghhaaf b aa()] TEXT(222) DEFAULT 'vvv')",
                "CREATE TABLE [wHere12] ([where] AUTOINCREMENT PRIMARY KEY, [---bghhaaf b aa] TEXT(222) DEFAULT 'vvv')",
                "DROP TABLE [ggg kk]",
                "CREATE TABLE [ggg kk] ([---bgaaf aa] AUTOINCREMENT PRIMARY KEY, [---bghhaaf b aa] NUMERIC(22,6) DEFAULT 12.99)",
                "CREATE TABLE kkk  ([---bgaaf aa] AUTOINCREMENT PRIMARY KEY, [---bghhaaf b aa] TEXT(222) DEFAULT 'vvv')",
                "INSERT INTO kkk ([---bgaaf aa],[---bghhaaf b aa]) VALUES(1, '23fff')",
                "CREATE TABLE counter (counter AUTOINCREMENT PRIMARY KEY, [simple] TEXT(222) DEFAULT 'vvv')");
        }
        dumpQueryResult("SELECT * FROM counter");
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @AccessVersionSource(include = "V2010")
    void testCreateWithFK(AccessVersion _accessVersion) throws SQLException, IOException {
        init(_accessVersion);

        try (UcanaccessStatement st = ucanaccess.createStatement()) {
            st.execute("CREATE TABLE Parent(x AUTOINCREMENT PRIMARY KEY, y TEXT(222))");
            st.execute("CREATE TABLE Babe(k LONG, y LONG, PRIMARY KEY(k, y), FOREIGN KEY (y) REFERENCES Parent (x))");
            Database db = ucanaccess.getDbIO();
            Table tb = db.getTable("Babe");
            Table tbr = db.getTable("Parent");
            assertThat(tb.getForeignKeyIndex(tbr).getColumns().stream().map(Column::getName)).contains("y");
            executeStatements(st,
                "CREATE TABLE [1 Parent]( [x 0] long , y long, PRIMARY KEY([x 0],y))",
                "CREATE TABLE [1 Babe]( k LONG , y LONG, [0 z] LONG, PRIMARY KEY(k,y), FOREIGN KEY (y,[0 z] ) REFERENCES [1 Parent] ( [x 0] , y) )");
        }
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @AccessVersionSource(include = "V2010")
    void testPs(AccessVersion _accessVersion) throws SQLException {
        init(_accessVersion);

        for (PreparedStatement ps : List.of(
            ucanaccess.prepareStatement("CREATE TABLE PS0 (PS AUTOINCREMENT PRIMARY KEY)"),
            ucanaccess.prepareStatement("CREATE TABLE PS1 (PS AUTOINCREMENT PRIMARY KEY)", 0),
            ucanaccess.prepareStatement("CREATE TABLE PS2 (PS AUTOINCREMENT PRIMARY KEY)", 0, 0),
            ucanaccess.prepareStatement("CREATE TABLE PS3 (PS AUTOINCREMENT PRIMARY KEY)", 0, 0, 0))) {
            ps.execute();
        }
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @AccessVersionSource(include = "V2010")
    void testPsHyphen(AccessVersion _accessVersion) throws SQLException {
        init(_accessVersion);

        // #9 hyphen in DDL column name confuses PreparedStatement
        try (PreparedStatement prepStmt = ucanaccess.prepareStatement("CREATE TABLE zzzFoo1 ([Req-MTI] TEXT(20))")) {
            prepStmt.executeUpdate();
        }
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @AccessVersionSource(include = "V2010")
    void testCreateHyperlink(AccessVersion _accessVersion) throws SQLException {
        init(_accessVersion);

        try (UcanaccessStatement st = ucanaccess.createStatement()) {
            executeStatements(st,
                "CREATE TABLE urlTest (id LONG PRIMARY KEY, website HYPERLINK)",
                "INSERT INTO urlTest (id, website) VALUES (1, '#http://whatever#')",
                "INSERT INTO urlTest (id, website) VALUES (2, 'example.com#http://example.com#')",
                "INSERT INTO urlTest (id, website) VALUES (3, 'the works#http://burger#with_bacon#and_cheese')",
                "INSERT INTO urlTest (id, website) VALUES (4, 'http://bad_link_no_hash_characters')");
            try (ResultSet rs = ucanaccess.getMetaData().getColumns(null, null, "urlTest", "website")) {
                rs.next();
                assertEquals("HYPERLINK", rs.getString(ORIGINAL_TYPE));
            }

            try (ResultSet rs = st.executeQuery("SELECT website FROM urlTest ORDER BY id")) {
                rs.next();
                assertEquals("http://whatever", rs.getURL(1).toString());
                rs.next();
                assertEquals("http://example.com", rs.getURL(1).toString());
                rs.next();
                assertEquals("http://burger#with_bacon", rs.getURL(1).toString());
                rs.next();
                assertThatThrownBy(() -> rs.getURL(1))
                    .isInstanceOf(UcanaccessSQLException.class)
                    .hasMessageEndingWith("Invalid or unsupported URL format");
            }
        }
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @AccessVersionSource(include = "V2010")
    void tableNameWithUnderscore(AccessVersion _accessVersion) throws SQLException {
        // Ticket #19
        init(_accessVersion);

        executeStatements(
            "CREATE TABLE t01 (id LONG PRIMARY KEY, comments MEMO)",
            "CREATE TABLE t_1 (id LONG PRIMARY KEY)");
    }

}
