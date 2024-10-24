package net.ucanaccess.jdbc;

import net.ucanaccess.test.UcanaccessBaseFileTest;
import org.junit.jupiter.api.Test;

import java.sql.PreparedStatement;
import java.sql.SQLException;

class FloatTest extends UcanaccessBaseFileTest {

    @Test
    void testCreate() throws SQLException {
        init();

        checkQuery("SELECT [row] FROM t_float ORDER BY pk");
        PreparedStatement ps = ucanaccess.prepareStatement("INSERT INTO t_float (row) VALUES(?)");
        ps.setFloat(1, 1.4f);
        ps.execute();
        checkQuery("SELECT [row] FROM t_float ORDER BY pk");
        ps = ucanaccess.prepareStatement("UPDATE t_float SET [row]=?");
        ps.setObject(1, 4.9f);
        ps.execute();
        checkQuery("SELECT [row] FROM t_float ORDER BY pk");
        ps.setDouble(1, 0.0000000000000000000000000000000000000000000000001d);
        ps.execute();
        dumpQueryResult("SELECT * FROM t_float ORDER BY pk");
        checkQuery("SELECT [row] FROM t_float ORDER BY pk");
        ps.setFloat(1, 4.10011001155f);
        ps.execute();
        checkQuery("SELECT [row] FROM t_float ORDER BY pk");
        checkQuery("SELECT COUNT(*) FROM t_float WHERE [row]=4.10011", singleRec(2));
        dumpQueryResult("SELECT * FROM t_float ORDER BY pk");
    }

}
