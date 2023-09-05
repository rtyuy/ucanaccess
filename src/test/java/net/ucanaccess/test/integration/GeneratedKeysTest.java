package net.ucanaccess.test.integration;

import net.ucanaccess.test.util.AccessVersion;
import net.ucanaccess.test.util.AccessVersionAllTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@RunWith(Parameterized.class)
public class GeneratedKeysTest extends AccessVersionAllTest {
    private String tableName = "T_Key";

    public GeneratedKeysTest(AccessVersion _accessVersion) {
        super(_accessVersion);
    }

    @Before
    public void beforeTestCase() throws Exception {
        executeStatements("CREATE TABLE " + tableName + " ( Z COUNTER PRIMARY KEY, B char(4) )");
    }

    @After
    public void afterTestCase() throws Exception {
        dropTable(tableName);
    }

    @Test
    public void testGeneratedKeys() throws SQLException, IOException {

        PreparedStatement ps = ucanaccess.prepareStatement("INSERT INTO " + tableName + " (B) VALUES (?)");
        ps.setString(1, "");
        ps.execute();
        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        assertEquals(1, rs.getInt(1));
        ps.close();
        ps = ucanaccess.prepareStatement("Select @@identity ");
        rs = ps.executeQuery();
        rs.next();
        assertEquals(1, rs.getInt(1));
        Statement st = ucanaccess.createStatement();
        st.execute("INSERT INTO " + tableName + " (B) VALUES ('W')");

        checkQuery("Select @@identity ", 2);
        rs = st.getGeneratedKeys();
        rs.next();
        assertEquals(2, rs.getInt(1));

    }
}
