package net.ucanaccess.test.integration;

import net.ucanaccess.test.util.AccessVersion;
import net.ucanaccess.test.util.AccessVersionAllTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;

@RunWith(Parameterized.class)
public class AggregateFunctionsTest extends AccessVersionAllTest {

    public AggregateFunctionsTest(AccessVersion _accessVersion) {
        super(_accessVersion);
        Locale.setDefault(Locale.US);
    }

    @Before
    public void beforeTestCase() throws Exception {
        executeStatements("CREATE TABLE t235 (id INTEGER,descr text(400), num numeric(12,3), date0 datetime) ",
            "INSERT INTO t235 (id,descr,num,date0)  VALUES( 1234,'Show must go off',-1110.55446,#11/22/2003 10:42:58 PM#)",
            "INSERT INTO t235 (id,descr,num,date0)  VALUES( 12344,'Show must go up and down',-113.55446,#11/22/2006 10:42:58 PM#)");
    }

    @After
    public void afterTestCase() throws Exception {
        dropTable("t235");
    }

    @Test
    public void testDCount() throws SQLException, IOException {

        checkQuery("SELECT id, DCount('*','t235','1=1') FROM [t235]", new Object[][] {{1234, 2}, {12344, 2}});
        checkQuery("SELECT id as [WW \"SS], DCount('descr','t235','1=1')from t235",
            new Object[][] {{1234, 2}, {12344, 2}});
        checkQuery("SELECT  DCount('*','t235','1=1') ", 2);

    }

    @Test
    public void testDSum() throws SQLException, IOException {
        checkQuery("SELECT DSum('id','t235','1=1') ", 13578);
    }

    @Test
    public void testDMax() throws SQLException, IOException {
        checkQuery("SELECT  DMax('id','t235') ", 12344);
    }

    @Test
    public void testDMin() throws SQLException, IOException {
        checkQuery("SELECT  DMin('id','t235') ", 1234);
    }

    @Test
    public void testDAvg() throws SQLException, IOException {
        checkQuery("SELECT  DAvg('id','t235') ", 6789);
    }

    @Test
    public void testLast() throws SQLException, IOException {
        checkQuery("SELECT last(descr) FROM t235", "Show must go up and down");
        checkQuery("SELECT last(NUM) FROM t235", -113.5540);
        dumpQueryResult("SELECT last(date0) FROM t235");
    }

    @Test
    public void testFirst() throws SQLException, IOException {
        checkQuery("SELECT first(descr) FROM t235", "Show must go off");
        checkQuery("SELECT first(NUM) FROM t235", -1110.5540);
        dumpQueryResult("SELECT  first(date0) FROM t235");
    }

    @Test
    public void testDLast() throws SQLException, IOException {
        checkQuery("SELECT DLast('descr','t235') ", "Show must go up and down");
    }

    @Test
    public void testDFirst() throws SQLException, IOException {
        checkQuery("SELECT DFirst('descr','t235') ", "Show must go off");
    }

}
