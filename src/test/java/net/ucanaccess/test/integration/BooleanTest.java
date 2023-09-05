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
import java.text.ParseException;
import java.util.Locale;

@RunWith(Parameterized.class)
public class BooleanTest extends AccessVersionAllTest {

    public BooleanTest(AccessVersion _accessVersion) {
        super(_accessVersion);
        Locale.setDefault(Locale.US);
    }

    @Override
    public String getAccessPath() {
        return "testdbs/bool.accdb";
    }

    @Before
    public void beforeTestCase() throws Exception {
        executeStatements(
            "CREATE TABLE tblMain (ID int NOT NULL PRIMARY KEY,company TEXT NOT NULL, Closed YESNO); ",
            "INSERT INTO tblMain (id,company) VALUES(1,'pippo')",
            "UPDATE tblMain SET closed=yes",
            "INSERT INTO t (pk)values('pippo')");
    }

    @After
    public void afterTestCase() throws Exception {
        dropTable("tblMain");
    }

    @Test
    public void testCreate() throws SQLException, IOException, ParseException {
        dumpQueryResult("SELECT * FROM  tblMain");
        dumpQueryResult("SELECT * FROM  t");
        checkQuery("SELECT * FROM  tblMain");
        checkQuery("SELECT * FROM  t");
    }

}
