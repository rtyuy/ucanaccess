package net.ucanaccess.triggers;

import io.github.spannm.jackcess.Column;
import io.github.spannm.jackcess.DataType;
import io.github.spannm.jackcess.Table;
import io.github.spannm.jackcess.complex.ComplexDataType;
import io.github.spannm.jackcess.impl.ColumnImpl;
import net.ucanaccess.complex.Attachment;
import net.ucanaccess.complex.SingleValue;
import net.ucanaccess.exception.TableNotFoundException;
import net.ucanaccess.exception.UcanaccessRuntimeException;
import net.ucanaccess.jdbc.UcanaccessConnection;
import org.hsqldb.types.JavaObjectData;

import java.sql.SQLException;
import java.util.UUID;

public class TriggerAutoNumber extends TriggerBase {
    private static final String GUID_PATTERN =
            "\\s*[{]?([\\p{XDigit}]{8})-([\\p{XDigit}]{4})-([\\p{XDigit}]{4})-([\\p{XDigit}]{4})-([\\p{XDigit}]{12})[}]?\\s*";

    @Override
    public void fire(int type, String name, String tableName, Object[] oldR, Object[] newR) {
        checkContext();
        UcanaccessConnection conn = UcanaccessConnection.getCtxConnection();
        if (conn.isFeedbackState()) {
            return;
        }
        try {
            Table t = getTable(tableName, conn);
            if (t == null) {
                throw new TableNotFoundException(tableName);
            }
            int i = 0;
            for (Column cli : t.getColumns()) {
                ColumnImpl cl = (ColumnImpl) cli;
                if (cli.getType().equals(DataType.COMPLEX_TYPE) && (newR[i] == null || "".equals(newR[i]))) {
                    if (cli.getComplexInfo().getType().equals(ComplexDataType.ATTACHMENT)) {
                        newR[i] = new JavaObjectData(new Attachment[0]);
                    } else if (cli.getComplexInfo().getType().equals(ComplexDataType.MULTI_VALUE)) {
                        newR[i] = new JavaObjectData(new SingleValue[0]);
                    }
                } else

                if (cl.isAutoNumber()) {
                    if (INSERT_BEFORE_ROW == type) {

                        if (t.isAllowAutoNumberInsert()) {
                            if (cl.getAutoNumberGenerator().getType().equals(DataType.LONG) && newR[i] != null) {
                                AutoNumberManager.bump(cl, (Integer) newR[i]);
                            }
                        } else {
                            if (cl.getAutoNumberGenerator().getType().equals(DataType.GUID)) {

                                if (newR[i] == null) {
                                    newR[i] = "{" + UUID.randomUUID().toString().toUpperCase() + "}";
                                }
                                conn.setGeneratedKey(newR[i]);
                            } else if (cl.getAutoNumberGenerator().getType().equals(DataType.LONG)) {
                                int keyg = AutoNumberManager.getNext(cl);
                                newR[i] = keyg;
                                conn.setGeneratedKey(newR[i]);
                            }
                        }
                    } else if (UPDATE_BEFORE_ROW == type
                            && cl.getAutoNumberGenerator().getType().equals(DataType.LONG)) {
                        if (!oldR[i].equals(newR[i])) {
                            throw new UcanaccessRuntimeException("Cannot update autoincrement column");
                        }
                    } else if (cl.getAutoNumberGenerator().getType().equals(DataType.GUID)) {
                        validateGUID(newR[i]);
                    }
                } else if (DataType.BOOLEAN.equals(cl.getType())) {
                    if (newR[i] == null) {
                        newR[i] = false;
                    }
                }
                ++i;
            }
        } catch (Exception _ex) {
            throw new TriggerException(_ex.getMessage());
        }
    }

    private void validateGUID(Object guid) throws SQLException {
        if (guid instanceof String) {
            String guidS = (String) guid;
            if (guidS.length() != 38 || !guidS.matches(GUID_PATTERN)) {
                throw new SQLException("Invalid guid format " + guidS);
            }
        }
    }
}
