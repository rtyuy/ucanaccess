package net.ucanaccess.jdbc;

import net.ucanaccess.util.Logger;
import org.hsqldb.error.ErrorCode;

import java.sql.SQLException;

public class UcanaccessSQLException extends SQLException {

    public enum ExceptionMessages {
        CONCURRENT_PROCESS_ACCESS,
        INVALID_CREATE_STATEMENT,
        INVALID_INTERVAL_VALUE,
        INVALID_JACKCESS_OPENER,
        INVALID_MONTH_NUMBER,
        NOT_A_VALID_PASSWORD,
        ONLY_IN_MEMORY_ALLOWED,
        UNPARSABLE_DATE,
        COMPLEX_TYPE_UNSUPPORTED,
        INVALID_PARAMETER,
        INVALID_TYPES_IN_COMBINATION,
        UNSUPPORTED_TYPE,
        STATEMENT_DDL,
        CLOSE_ON_COMPLETION_STATEMENT,
        ACCESS_97,
        PARAMETER_NULL,
        TABLE_DOES_NOT_EXIST,
        DEFAULT_NEEDED
    }

    private static final long   serialVersionUID             = -1432048647665807662L;
    private static final String UCANACCESS_GENERIC_ERROR_STR = String.valueOf(UcanaccessErrorCodes.UCANACCESS_GENERIC_ERROR);

    public UcanaccessSQLException() {
    }

    public UcanaccessSQLException(String _reason, String _sqlState, int _vendorCode, Throwable _cause) {
        super(_reason == null ? null : Logger.getMessage(_reason), _sqlState, _vendorCode, _cause);
    }

    public UcanaccessSQLException(ExceptionMessages _reason) {
        this(Logger.getMessage(_reason.name()), UCANACCESS_GENERIC_ERROR_STR, UcanaccessErrorCodes.UCANACCESS_GENERIC_ERROR, null);
    }

    public UcanaccessSQLException(ExceptionMessages _reason, Object... _args) {
        this(Logger.getMessage(_reason.name(), _args), UCANACCESS_GENERIC_ERROR_STR, UcanaccessErrorCodes.UCANACCESS_GENERIC_ERROR, null);
    }

    public UcanaccessSQLException(String _reason, String _sqlState, int _vendorCode) {
        super(_reason == null ? null : Logger.getMessage(_reason), _sqlState, _vendorCode, null);
    }

    public UcanaccessSQLException(String _reason, String _sqlState, Throwable _cause) {
        super(_reason == null ? null : Logger.getMessage(_reason), _sqlState, UcanaccessErrorCodes.UCANACCESS_GENERIC_ERROR, _cause);
    }

    public UcanaccessSQLException(String _reason, Throwable _cause) {
        super(_reason == null ? null : Logger.getMessage(_reason), UCANACCESS_GENERIC_ERROR_STR, UcanaccessErrorCodes.UCANACCESS_GENERIC_ERROR, _cause);
    }

    public UcanaccessSQLException(String _reason, String _sqlState) {
        super(_reason == null ? null : Logger.getMessage(_reason), _sqlState, UcanaccessErrorCodes.UCANACCESS_GENERIC_ERROR, null);
    }

    public UcanaccessSQLException(Throwable _cause) {
        super(explainCause(_cause),
            _cause instanceof SQLException ? ((SQLException) _cause).getSQLState() : UCANACCESS_GENERIC_ERROR_STR,
            _cause instanceof SQLException ? ((SQLException) _cause).getErrorCode() : UcanaccessErrorCodes.UCANACCESS_GENERIC_ERROR,
            _cause);
    }

    public static String explainCause(Throwable _cause) {
        if (_cause instanceof SQLException) {
            SQLException se = (SQLException) _cause;
            if (se.getErrorCode() == -ErrorCode.X_42562) {
                return _cause.getMessage() + " "
                    + Logger.getMessage(ExceptionMessages.INVALID_TYPES_IN_COMBINATION.name());
            }
        }
        return _cause.getMessage();
    }

    private String versionMessage(String message) {
        if (message != null && message.startsWith("UCAExc:")) {
            return message;
        }
        String version = this.getClass().getPackage().getImplementationVersion();
        version = (version == null) ? "4.x.x " : version + " ";
        version = "UCAExc:::" + version;
        return version + message;
    }

    @Override
    public String getLocalizedMessage() {
        return versionMessage(super.getLocalizedMessage());
    }

    @Override
    public String getMessage() {
        return versionMessage(super.getMessage());
    }

}
