package net.ucanaccess.jdbc;

import net.ucanaccess.converters.SQLConverter;
import net.ucanaccess.exception.UcanaccessSQLException;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.sql.Date;
import java.util.*;

public class UcanaccessResultSet implements ResultSet {
    private final ResultSet           wrapped;
    private final UcanaccessStatement wrappedStatement;
    private Set<String>               metadata;
    private final Set<Integer>        updIndexes = new HashSet<>();

    public UcanaccessResultSet(ResultSet _wrapped, UcanaccessStatement _statement) {
        wrapped = _wrapped;
        wrappedStatement = _statement;
    }

    private String checkEscaped(String label) throws SQLException {
        if (label == null) {
            return null;
        }
        if (metadata == null) {
            loadMetadata();
        }
        String lu = label.toUpperCase();
        if (metadata.contains(lu)) {
            return lu;
        }
        String escaped = SQLConverter.preEscapingIdentifier(label);
        String slabel = label.substring(1).toUpperCase();
        if (SQLConverter.isXescaped(slabel) && metadata.contains(slabel)) {
            return slabel;
        }

        if (metadata.contains(escaped.toUpperCase())) {
            return escaped;
        }

        return label;
    }

    private void loadMetadata() throws SQLException {
        metadata = new HashSet<>();
        ResultSetMetaData rsmd = wrapped.getMetaData();
        for (int i = 1; i <= rsmd.getColumnCount(); i++) {
            metadata.add(rsmd.getColumnLabel(i).toUpperCase());
        }

    }

    @Override
    public boolean absolute(int row) throws SQLException {
        try {
            return wrapped.absolute(row);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void afterLast() throws SQLException {
        try {
            wrapped.afterLast();
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void beforeFirst() throws SQLException {
        try {
            wrapped.beforeFirst();
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void cancelRowUpdates() throws SQLException {
        try {
            wrapped.cancelRowUpdates();
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void clearWarnings() throws SQLException {
        try {
            wrapped.clearWarnings();
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void close() throws SQLException {
        try {
            wrapped.close();
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void deleteRow() throws SQLException {
        try {
            new DeleteResultSet(this).execute();
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public int findColumn(String columnLabel) throws SQLException {
        try {
            return wrapped.findColumn(checkEscaped(columnLabel));
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public boolean first() throws SQLException {
        try {
            return wrapped.first();
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public Array getArray(int idx) throws SQLException {
        try {
            return wrapped.getArray(idx);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public Array getArray(String columnLabel) throws SQLException {
        try {
            return wrapped.getArray(checkEscaped(columnLabel));
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public InputStream getAsciiStream(int idx) throws SQLException {
        try {
            return wrapped.getAsciiStream(idx);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public InputStream getAsciiStream(String columnLabel) throws SQLException {
        try {
            return wrapped.getAsciiStream(checkEscaped(columnLabel));
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public BigDecimal getBigDecimal(int idx) throws SQLException {
        try {
            return wrapped.getBigDecimal(idx);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    @Deprecated
    public BigDecimal getBigDecimal(int idx, int arg1) throws SQLException {
        try {
            return wrapped.getBigDecimal(idx, arg1);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
        try {
            return wrapped.getBigDecimal(checkEscaped(columnLabel));
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    @Deprecated
    public BigDecimal getBigDecimal(String columnLabel, int arg1) throws SQLException {
        try {
            return wrapped.getBigDecimal(checkEscaped(columnLabel), arg1);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public InputStream getBinaryStream(int idx) throws SQLException {
        try {
            Object obj = getObject(idx);
            if (obj instanceof Blob) {
                return ((Blob) obj).getBinaryStream();
            }
            return wrapped.getBinaryStream(idx);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public InputStream getBinaryStream(String columnLabel) throws SQLException {
        try {
            Object obj = getObject(columnLabel);
            if (obj instanceof Blob) {
                return ((Blob) obj).getBinaryStream();
            }
            return wrapped.getBinaryStream(checkEscaped(columnLabel));
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public Blob getBlob(int idx) throws SQLException {
        try {
            Blob blb = wrapped.getBlob(idx);
            if (blb != null) {
                blb = new UcanaccessBlob(blb, wrappedStatement.getConnection());
            }
            return blb;
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public Blob getBlob(String columnLabel) throws SQLException {
        try {
            Blob blb = wrapped.getBlob(checkEscaped(columnLabel));
            if (blb != null) {
                blb = new UcanaccessBlob(blb, wrappedStatement.getConnection());
            }
            return blb;
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public boolean getBoolean(int idx) throws SQLException {
        try {
            return wrapped.getBoolean(idx);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public boolean getBoolean(String columnLabel) throws SQLException {
        try {
            return wrapped.getBoolean(checkEscaped(columnLabel));
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public byte getByte(int idx) throws SQLException {
        try {
            return wrapped.getByte(idx);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public byte getByte(String columnLabel) throws SQLException {
        try {
            return wrapped.getByte(checkEscaped(columnLabel));
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public byte[] getBytes(int idx) throws SQLException {
        try {
            return wrapped.getBytes(idx);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public byte[] getBytes(String columnLabel) throws SQLException {
        try {
            return wrapped.getBytes(checkEscaped(columnLabel));
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public Reader getCharacterStream(int idx) throws SQLException {
        try {
            return wrapped.getCharacterStream(idx);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public Reader getCharacterStream(String columnLabel) throws SQLException {
        try {
            return wrapped.getCharacterStream(checkEscaped(columnLabel));
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public Clob getClob(int idx) throws SQLException {
        try {
            return wrapped.getClob(idx);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public Clob getClob(String columnLabel) throws SQLException {
        try {
            return wrapped.getClob(checkEscaped(columnLabel));
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public int getConcurrency() throws SQLException {
        try {
            return wrapped.getConcurrency();
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public String getCursorName() throws SQLException {
        try {
            return wrapped.getCursorName();
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public Date getDate(int idx) throws SQLException {
        try {
            return wrapped.getDate(idx);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public Date getDate(int idx, Calendar arg1) throws SQLException {
        try {
            return wrapped.getDate(idx, arg1);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public Date getDate(String columnLabel) throws SQLException {
        try {
            return wrapped.getDate(checkEscaped(columnLabel));
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public Date getDate(String columnLabel, Calendar arg1) throws SQLException {
        try {
            return wrapped.getDate(checkEscaped(columnLabel), arg1);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public double getDouble(int idx) throws SQLException {
        try {
            return wrapped.getDouble(idx);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public double getDouble(String columnLabel) throws SQLException {
        try {
            return wrapped.getDouble(checkEscaped(columnLabel));
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public int getFetchDirection() throws SQLException {
        try {
            return wrapped.getFetchDirection();
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public int getFetchSize() throws SQLException {
        try {
            return wrapped.getFetchSize();
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public float getFloat(int idx) throws SQLException {
        try {
            return wrapped.getFloat(idx);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public float getFloat(String columnLabel) throws SQLException {
        try {
            return wrapped.getFloat(checkEscaped(columnLabel));
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public int getHoldability() throws SQLException {
        try {
            return wrapped.getHoldability();
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public int getInt(int idx) throws SQLException {
        try {
            return wrapped.getInt(idx);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public int getInt(String columnLabel) throws SQLException {
        try {
            return wrapped.getInt(checkEscaped(columnLabel));
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public long getLong(int idx) throws SQLException {
        try {
            return wrapped.getLong(idx);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public long getLong(String columnLabel) throws SQLException {
        try {
            return wrapped.getLong(checkEscaped(columnLabel));
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        try {
            Map<String, String> hm =
                    wrappedStatement == null ? new HashMap<>() : wrappedStatement.getAliases();
            return new UcanaccessResultSetMetaData(wrapped.getMetaData(), hm, this);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public Reader getNCharacterStream(int idx) throws SQLException {
        try {
            return wrapped.getNCharacterStream(idx);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public Reader getNCharacterStream(String columnLabel) throws SQLException {
        try {
            return wrapped.getNCharacterStream(checkEscaped(columnLabel));
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public NClob getNClob(int idx) throws SQLException {
        try {
            return wrapped.getNClob(idx);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public NClob getNClob(String columnLabel) throws SQLException {
        try {
            return wrapped.getNClob(checkEscaped(columnLabel));
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public String getNString(int idx) throws SQLException {
        try {
            return wrapped.getNString(idx);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public String getNString(String columnLabel) throws SQLException {
        try {
            return wrapped.getNString(checkEscaped(columnLabel));
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public Object getObject(int idx) throws SQLException {
        try {
            Object obj = wrapped.getObject(idx);
            if (obj instanceof Blob) {
                return new UcanaccessBlob((Blob) obj, wrappedStatement.getConnection());
            }
            return obj;
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
        try {
            return wrapped.getObject(columnIndex, type);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public Object getObject(int idx, Map<String, Class<?>> arg1) throws SQLException {
        try {
            Object obj = wrapped.getObject(idx, arg1);
            if (obj instanceof Blob) {
                return new UcanaccessBlob((Blob) obj, wrappedStatement.getConnection());
            }
            return obj;
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public Object getObject(String columnLabel) throws SQLException {
        try {
            Object obj = wrapped.getObject(checkEscaped(columnLabel));
            if (obj instanceof Blob) {
                return new UcanaccessBlob((Blob) obj, wrappedStatement.getConnection());
            }
            return obj;
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public <T> T getObject(String columnLabel, Class<T> type) throws SQLException {
        try {
            return wrapped.getObject(checkEscaped(columnLabel), type);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public Object getObject(String columnLabel, Map<String, Class<?>> arg1) throws SQLException {
        try {
            return wrapped.getObject(checkEscaped(columnLabel), arg1);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public Ref getRef(int idx) throws SQLException {
        try {
            return wrapped.getRef(idx);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public Ref getRef(String columnLabel) throws SQLException {
        try {
            return wrapped.getRef(checkEscaped(columnLabel));
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public int getRow() throws SQLException {
        try {
            return wrapped.getRow();
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public RowId getRowId(int idx) throws SQLException {
        try {
            return wrapped.getRowId(idx);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public RowId getRowId(String columnLabel) throws SQLException {
        try {
            return wrapped.getRowId(checkEscaped(columnLabel));
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public short getShort(int idx) throws SQLException {
        try {
            return wrapped.getShort(idx);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public short getShort(String columnLabel) throws SQLException {
        try {
            return wrapped.getShort(checkEscaped(columnLabel));
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public SQLXML getSQLXML(int idx) throws SQLException {
        try {
            return wrapped.getSQLXML(idx);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public SQLXML getSQLXML(String columnLabel) throws SQLException {
        try {
            return wrapped.getSQLXML(checkEscaped(columnLabel));
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public Statement getStatement() throws SQLException {
        try {
            return wrapped.getStatement();
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public String getString(int idx) throws SQLException {
        try {
            Object obj = getObject(idx);
            if (obj instanceof Number) {
                return obj.toString();
            }
            return wrapped.getString(idx);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public String getString(String columnLabel) throws SQLException {
        try {
            Object obj = getObject(columnLabel);
            if (obj instanceof Number) {
                return obj.toString();
            }
            return wrapped.getString(checkEscaped(columnLabel));
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public Time getTime(int idx) throws SQLException {
        try {
            return wrapped.getTime(idx);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public Time getTime(int idx, Calendar arg1) throws SQLException {
        try {
            return wrapped.getTime(idx, arg1);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public Time getTime(String columnLabel) throws SQLException {
        try {
            return wrapped.getTime(checkEscaped(columnLabel));
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public Time getTime(String columnLabel, Calendar arg1) throws SQLException {
        try {
            return wrapped.getTime(checkEscaped(columnLabel), arg1);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public Timestamp getTimestamp(int idx) throws SQLException {
        try {
            return wrapped.getTimestamp(idx);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public Timestamp getTimestamp(int idx, Calendar arg1) throws SQLException {
        try {
            return wrapped.getTimestamp(idx, arg1);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public Timestamp getTimestamp(String columnLabel) throws SQLException {
        try {
            return wrapped.getTimestamp(checkEscaped(columnLabel));
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public Timestamp getTimestamp(String columnLabel, Calendar arg1) throws SQLException {
        try {
            return wrapped.getTimestamp(checkEscaped(columnLabel), arg1);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public int getType() throws SQLException {
        try {
            return wrapped.getType();
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    @Deprecated
    public InputStream getUnicodeStream(int idx) throws SQLException {
        try {
            return wrapped.getUnicodeStream(idx);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    @Deprecated
    public InputStream getUnicodeStream(String columnLabel) throws SQLException {
        try {
            return wrapped.getUnicodeStream(checkEscaped(columnLabel));
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public URL getURL(int idx) throws SQLException {
        try {
            Object obj = wrapped.getObject(idx);
            return getURL(obj);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    private URL getURL(Object obj) throws SQLException {
        try {
            if (obj instanceof String) {
                String s = (String) obj;
                String[] parts = s.split("#");
                StringBuilder url = new StringBuilder();
                if (parts.length > 1) {
                    url.append(parts[1]);
                    if (parts.length > 2 && !parts[2].isEmpty()) {
                        url.append("#")
                           .append(parts[2]);
                    }
                    return new URL(url.toString());
                }
            }
            throw new SQLException("Invalid or unsupported URL format");
        } catch (Exception _ex) {
            throw new SQLException(_ex);
        }
    }

    @Override
    public URL getURL(String cn) throws SQLException {
        try {
            Object obj = wrapped.getObject(checkEscaped(cn));
            return getURL(obj);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        try {
            return wrapped.getWarnings();
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    public ResultSet getWrapped() {
        return wrapped;
    }

    public UcanaccessStatement getWrappedStatement() {
        return wrappedStatement;
    }

    @Override
    public void insertRow() throws SQLException {
        try {
            for (int i = 1; i <= getMetaData().getColumnCount(); i++) {
                if (!updIndexes.contains(i)) {
                    updateNull(i);
                }
            }
            updIndexes.clear();

            wrappedStatement.getConnection().setCurrentStatement(wrappedStatement);
            new InsertResultSet(this).execute();
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public boolean isAfterLast() throws SQLException {
        try {
            return wrapped.isAfterLast();
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public boolean isBeforeFirst() throws SQLException {
        try {
            return wrapped.isBeforeFirst();
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public boolean isClosed() throws SQLException {
        try {
            return wrapped.isClosed();
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public boolean isFirst() throws SQLException {
        try {
            return wrapped.isFirst();
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public boolean isLast() throws SQLException {
        try {
            return wrapped.isLast();
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        try {
            return wrapped.isWrapperFor(iface);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public boolean last() throws SQLException {
        try {
            return wrapped.last();
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void moveToCurrentRow() throws SQLException {
        try {
            wrapped.moveToCurrentRow();
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void moveToInsertRow() throws SQLException {
        try {
            wrapped.moveToInsertRow();
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public boolean next() throws SQLException {
        try {
            return wrapped.next();
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public boolean previous() throws SQLException {
        try {
            return wrapped.previous();
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void refreshRow() throws SQLException {
        try {
            wrapped.refreshRow();
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public boolean relative(int idx) throws SQLException {
        try {
            return wrapped.relative(idx);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public boolean rowDeleted() throws SQLException {
        try {
            return wrapped.rowDeleted();
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public boolean rowInserted() throws SQLException {
        try {
            return wrapped.rowInserted();
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public boolean rowUpdated() throws SQLException {
        try {
            return wrapped.rowUpdated();
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void setFetchDirection(int idx) throws SQLException {
        try {
            wrapped.setFetchDirection(idx);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void setFetchSize(int idx) throws SQLException {
        try {
            wrapped.setFetchSize(idx);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        try {
            return wrapped.unwrap(iface);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    private void addIndex(int idx) {
        updIndexes.add(idx);
    }

    private void addIndex(String columnLabel) throws SQLException {
        addIndex(findColumn(columnLabel));
    }

    @Override
    public void updateArray(int idx, Array arg1) throws SQLException {
        try {
            addIndex(idx);
            wrapped.updateArray(idx, arg1);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateArray(String columnLabel, Array arg1) throws SQLException {
        try {
            addIndex(columnLabel);
            wrapped.updateArray(checkEscaped(columnLabel), arg1);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateAsciiStream(int idx, InputStream arg1) throws SQLException {
        try {
            addIndex(idx);
            wrapped.updateAsciiStream(idx, arg1);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateAsciiStream(int idx, InputStream arg1, int arg2) throws SQLException {
        try {
            addIndex(idx);
            wrapped.updateAsciiStream(idx, arg1, arg2);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateAsciiStream(int idx, InputStream arg1, long arg2) throws SQLException {
        try {
            addIndex(idx);
            wrapped.updateAsciiStream(idx, arg1, arg2);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateAsciiStream(String columnLabel, InputStream arg1) throws SQLException {
        try {
            addIndex(columnLabel);
            wrapped.updateAsciiStream(checkEscaped(columnLabel), arg1);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateAsciiStream(String columnLabel, InputStream arg1, int arg2) throws SQLException {
        try {
            addIndex(columnLabel);
            wrapped.updateAsciiStream(checkEscaped(columnLabel), arg1, arg2);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateAsciiStream(String columnLabel, InputStream arg1, long arg2) throws SQLException {
        try {
            addIndex(columnLabel);
            wrapped.updateAsciiStream(checkEscaped(columnLabel), arg1, arg2);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateBigDecimal(int idx, BigDecimal arg1) throws SQLException {
        try {
            addIndex(idx);
            wrapped.updateBigDecimal(idx, arg1);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateBigDecimal(String columnLabel, BigDecimal arg1) throws SQLException {
        try {
            addIndex(columnLabel);
            wrapped.updateBigDecimal(checkEscaped(columnLabel), arg1);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateBinaryStream(int idx, InputStream arg1) throws SQLException {
        try {
            addIndex(idx);
            wrapped.updateBinaryStream(idx, arg1);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateBinaryStream(int idx, InputStream arg1, int arg2) throws SQLException {
        try {
            addIndex(idx);
            wrapped.updateBinaryStream(idx, arg1, arg2);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateBinaryStream(int idx, InputStream arg1, long arg2) throws SQLException {
        try {
            addIndex(idx);
            wrapped.updateBinaryStream(idx, arg1, arg2);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateBinaryStream(String columnLabel, InputStream arg1) throws SQLException {
        try {
            addIndex(columnLabel);
            wrapped.updateBinaryStream(checkEscaped(columnLabel), arg1);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateBinaryStream(String columnLabel, InputStream arg1, int arg2) throws SQLException {
        try {
            addIndex(columnLabel);
            wrapped.updateBinaryStream(checkEscaped(columnLabel), arg1, arg2);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateBinaryStream(String columnLabel, InputStream arg1, long arg2) throws SQLException {
        try {
            addIndex(columnLabel);
            wrapped.updateBinaryStream(checkEscaped(columnLabel), arg1, arg2);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateBlob(int idx, Blob arg1) throws SQLException {
        try {
            addIndex(idx);
            wrapped.updateBlob(idx, arg1);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateBlob(int idx, InputStream arg1) throws SQLException {
        try {
            addIndex(idx);
            wrapped.updateBlob(idx, arg1);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateBlob(int idx, InputStream arg1, long arg2) throws SQLException {
        try {
            addIndex(idx);
            wrapped.updateBlob(idx, arg1, arg2);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateBlob(String columnLabel, Blob arg1) throws SQLException {
        try {
            addIndex(columnLabel);
            wrapped.updateBlob(checkEscaped(columnLabel), arg1);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateBlob(String columnLabel, InputStream arg1) throws SQLException {
        try {
            addIndex(columnLabel);
            wrapped.updateBlob(checkEscaped(columnLabel), arg1);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateBlob(String columnLabel, InputStream arg1, long arg2) throws SQLException {
        try {
            addIndex(columnLabel);
            wrapped.updateBlob(checkEscaped(columnLabel), arg1, arg2);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateBoolean(int idx, boolean arg1) throws SQLException {
        try {
            addIndex(idx);
            wrapped.updateBoolean(idx, arg1);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateBoolean(String columnLabel, boolean arg1) throws SQLException {
        try {
            addIndex(columnLabel);
            wrapped.updateBoolean(checkEscaped(columnLabel), arg1);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateByte(int idx, byte arg1) throws SQLException {
        try {
            addIndex(idx);
            wrapped.updateByte(idx, arg1);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateByte(String columnLabel, byte arg1) throws SQLException {
        try {
            addIndex(columnLabel);
            wrapped.updateByte(checkEscaped(columnLabel), arg1);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateBytes(int idx, byte[] arg1) throws SQLException {
        try {
            addIndex(idx);
            wrapped.updateBytes(idx, arg1);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateBytes(String columnLabel, byte[] arg1) throws SQLException {
        try {
            addIndex(columnLabel);
            wrapped.updateBytes(checkEscaped(columnLabel), arg1);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateCharacterStream(int idx, Reader arg1) throws SQLException {
        try {
            addIndex(idx);
            wrapped.updateCharacterStream(idx, arg1);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateCharacterStream(int idx, Reader arg1, int arg2) throws SQLException {
        try {
            addIndex(idx);
            wrapped.updateCharacterStream(idx, arg1, arg2);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateCharacterStream(int idx, Reader arg1, long arg2) throws SQLException {
        try {
            addIndex(idx);
            wrapped.updateCharacterStream(idx, arg1, arg2);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateCharacterStream(String columnLabel, Reader arg1) throws SQLException {
        try {
            addIndex(columnLabel);
            wrapped.updateCharacterStream(checkEscaped(columnLabel), arg1);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateCharacterStream(String columnLabel, Reader arg1, int arg2) throws SQLException {
        try {
            addIndex(columnLabel);
            wrapped.updateCharacterStream(checkEscaped(columnLabel), arg1, arg2);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateCharacterStream(String columnLabel, Reader arg1, long arg2) throws SQLException {
        try {
            wrapped.updateCharacterStream(checkEscaped(columnLabel), arg1, arg2);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateClob(int idx, Clob arg1) throws SQLException {
        try {
            wrapped.updateClob(idx, arg1);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateClob(int idx, Reader arg1) throws SQLException {
        try {
            addIndex(idx);
            wrapped.updateClob(idx, arg1);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateClob(int idx, Reader arg1, long arg2) throws SQLException {
        try {
            addIndex(idx);
            wrapped.updateClob(idx, arg1, arg2);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateClob(String columnLabel, Clob arg1) throws SQLException {
        try {
            wrapped.updateClob(checkEscaped(columnLabel), arg1);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateClob(String columnLabel, Reader arg1) throws SQLException {
        try {
            wrapped.updateClob(checkEscaped(columnLabel), arg1);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateClob(String columnLabel, Reader arg1, long arg2) throws SQLException {
        try {
            wrapped.updateClob(checkEscaped(columnLabel), arg1, arg2);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateDate(int idx, Date arg1) throws SQLException {
        try {
            addIndex(idx);
            wrapped.updateDate(idx, arg1);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateDate(String columnLabel, Date arg1) throws SQLException {
        try {
            addIndex(columnLabel);
            wrapped.updateDate(checkEscaped(columnLabel), arg1);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateDouble(int idx, double arg1) throws SQLException {
        try {
            addIndex(idx);
            wrapped.updateDouble(idx, arg1);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateDouble(String columnLabel, double arg1) throws SQLException {
        try {
            addIndex(columnLabel);
            wrapped.updateDouble(checkEscaped(columnLabel), arg1);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateFloat(int idx, float x) throws SQLException {
        try {
            addIndex(idx);
            wrapped.updateBigDecimal(idx, new BigDecimal(Float.toString(x)));
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateFloat(String columnLabel, float x) throws SQLException {
        try {
            addIndex(columnLabel);
            wrapped.updateBigDecimal(checkEscaped(columnLabel), new BigDecimal(Float.toString(x)));
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateInt(int idx, int arg1) throws SQLException {
        try {
            addIndex(idx);
            wrapped.updateInt(idx, arg1);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateInt(String columnLabel, int arg1) throws SQLException {
        try {
            addIndex(columnLabel);
            wrapped.updateInt(checkEscaped(columnLabel), arg1);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateLong(int idx, long arg1) throws SQLException {
        try {
            addIndex(idx);
            wrapped.updateLong(idx, arg1);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateLong(String columnLabel, long arg1) throws SQLException {
        try {
            addIndex(columnLabel);
            wrapped.updateLong(checkEscaped(columnLabel), arg1);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateNCharacterStream(int idx, Reader arg1) throws SQLException {
        try {
            addIndex(idx);
            wrapped.updateNCharacterStream(idx, arg1);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateNCharacterStream(int idx, Reader arg1, long arg2) throws SQLException {
        try {
            addIndex(idx);
            wrapped.updateNCharacterStream(idx, arg1, arg2);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateNCharacterStream(String columnLabel, Reader arg1) throws SQLException {
        try {
            addIndex(columnLabel);
            wrapped.updateNCharacterStream(checkEscaped(columnLabel), arg1);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateNCharacterStream(String columnLabel, Reader arg1, long arg2) throws SQLException {
        try {
            addIndex(columnLabel);
            wrapped.updateNCharacterStream(checkEscaped(columnLabel), arg1, arg2);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateNClob(int idx, NClob arg1) throws SQLException {
        try {

            addIndex(idx);
            wrapped.updateNClob(idx, arg1);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateNClob(int idx, Reader arg1) throws SQLException {
        try {
            addIndex(idx);
            wrapped.updateNClob(idx, arg1);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateNClob(int idx, Reader arg1, long arg2) throws SQLException {
        try {
            addIndex(idx);
            wrapped.updateNClob(idx, arg1, arg2);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateNClob(String columnLabel, NClob arg1) throws SQLException {
        try {
            addIndex(columnLabel);
            wrapped.updateNClob(checkEscaped(columnLabel), arg1);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateNClob(String columnLabel, Reader arg1) throws SQLException {
        try {
            addIndex(columnLabel);
            wrapped.updateNClob(checkEscaped(columnLabel), arg1);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateNClob(String columnLabel, Reader arg1, long arg2) throws SQLException {
        try {
            addIndex(columnLabel);
            wrapped.updateNClob(checkEscaped(columnLabel), arg1, arg2);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateNString(int idx, String arg1) throws SQLException {
        try {
            addIndex(idx);
            wrapped.updateNString(idx, arg1);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateNString(String columnLabel, String arg1) throws SQLException {
        try {
            addIndex(columnLabel);
            wrapped.updateNString(checkEscaped(columnLabel), arg1);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateNull(int idx) throws SQLException {
        try {
            addIndex(idx);
            wrapped.updateNull(idx);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateNull(String columnLabel) throws SQLException {
        try {
            addIndex(columnLabel);
            wrapped.updateNull(checkEscaped(columnLabel));
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateObject(int idx, Object arg1) throws SQLException {
        try {

            addIndex(idx);
            wrapped.updateObject(idx, arg1);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateObject(int idx, Object arg1, int arg2) throws SQLException {
        try {
            wrapped.updateObject(idx, arg1, arg2);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateObject(String columnLabel, Object arg1) throws SQLException {
        try {
            addIndex(columnLabel);
            wrapped.updateObject(checkEscaped(columnLabel), arg1);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateObject(String columnLabel, Object arg1, int arg2) throws SQLException {
        try {
            addIndex(columnLabel);
            wrapped.updateObject(checkEscaped(columnLabel), arg1, arg2);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateRef(int idx, Ref arg1) throws SQLException {
        try {
            addIndex(idx);
            wrapped.updateRef(idx, arg1);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateRef(String columnLabel, Ref arg1) throws SQLException {
        try {
            addIndex(columnLabel);
            wrapped.updateRef(checkEscaped(columnLabel), arg1);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateRow() throws SQLException {
        try {
            updIndexes.clear();
            new UpdateResultSet(this).execute();
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateRowId(int idx, RowId arg1) throws SQLException {
        try {
            wrapped.updateRowId(idx, arg1);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateRowId(String columnLabel, RowId arg1) throws SQLException {
        try {
            addIndex(columnLabel);
            wrapped.updateRowId(checkEscaped(columnLabel), arg1);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateShort(int idx, short arg1) throws SQLException {
        try {
            addIndex(idx);
            wrapped.updateShort(idx, arg1);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateShort(String columnLabel, short arg1) throws SQLException {
        try {
            addIndex(columnLabel);
            wrapped.updateShort(checkEscaped(columnLabel), arg1);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateSQLXML(int idx, SQLXML arg1) throws SQLException {
        try {
            addIndex(idx);
            wrapped.updateSQLXML(idx, arg1);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateSQLXML(String columnLabel, SQLXML arg1) throws SQLException {
        try {
            addIndex(columnLabel);
            wrapped.updateSQLXML(checkEscaped(columnLabel), arg1);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateString(int idx, String arg1) throws SQLException {
        try {
            addIndex(idx);
            wrapped.updateString(idx, arg1);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateString(String columnLabel, String arg1) throws SQLException {
        try {
            addIndex(columnLabel);
            wrapped.updateString(checkEscaped(columnLabel), arg1);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateTime(int idx, Time arg1) throws SQLException {
        try {
            addIndex(idx);
            wrapped.updateTime(idx, arg1);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateTime(String columnLabel, Time arg1) throws SQLException {
        try {
            addIndex(columnLabel);
            wrapped.updateTime(checkEscaped(columnLabel), arg1);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateTimestamp(int idx, Timestamp arg1) throws SQLException {
        try {
            addIndex(idx);
            wrapped.updateTimestamp(idx, arg1);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public void updateTimestamp(String columnLabel, Timestamp arg1) throws SQLException {
        try {
            addIndex(columnLabel);
            wrapped.updateTimestamp(checkEscaped(columnLabel), arg1);
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }

    @Override
    public boolean wasNull() throws SQLException {
        try {
            return wrapped.wasNull();
        } catch (SQLException _ex) {
            throw new UcanaccessSQLException(_ex);
        }
    }
}
