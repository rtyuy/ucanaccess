package net.ucanaccess.complex;

import com.healthmarketscience.jackcess.complex.ComplexValue;

import java.util.Map;

public class UnsupportedValue extends ComplexBase {

    private static final long   serialVersionUID = 1L;
    private Map<String, Object> values;

    public UnsupportedValue(com.healthmarketscience.jackcess.complex.UnsupportedValue cv) {
        super(cv);
        values = cv.getValues();
    }

    public UnsupportedValue(ComplexValue.Id id, String tableName, String columnName, Map<String, Object> _values) {
        super(id, tableName, columnName);
        values = _values;
    }

    @Override
    public boolean equals(Object _obj) {
        if (this == _obj) {
            return true;
        } else if (_obj == null || getClass() != _obj.getClass()) {
            return false;
        }
        UnsupportedValue other = (UnsupportedValue) _obj;
        if (values == null) {
            if (other.values != null) {
                return false;
            }
        } else if (!values.equals(other.values)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (values == null ? 0 : values.hashCode());
        return result;
    }

    public Map<String, Object> getValues() {
        return values;
    }

    public void setValues(Map<String, Object> _values) {
        values = _values;
    }

}
