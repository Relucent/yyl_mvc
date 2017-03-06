package yyl.mvc.core.plug.jdbc;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

import org.apache.commons.collections.map.CaseInsensitiveMap;

/**
 * 数据结果集处理类，对ResultSet进行封装，更方便使用<br>
 */
public class ResultSetWrapper /* implements ResultSet */ {

	// ==============================Fields===========================================
	private final ResultSet rs;
	private final ResultSetMetaData md;
	private final CaseInsensitiveMap clmap = new CaseInsensitiveMap();
	private final boolean ignoreError;

	// ==============================Constructors=====================================
	public ResultSetWrapper(ResultSet rs) throws SQLException {
		this(rs, true);
	}

	public ResultSetWrapper(ResultSet rs, boolean ignoreError) throws SQLException {
		this.rs = rs;
		this.md = rs.getMetaData();
		this.ignoreError = ignoreError;
		for (int column = 0, count = md.getColumnCount(); column < count; column++) {
			clmap.put(md.getColumnLabel(column + 1), Boolean.TRUE);
		}
	}

	// ==============================Methods==========================================
	/**
	 * 判断列名标题是否不存在
	 * @param columnLabel 列名标题
	 * @return 列名不存在返回true，存在返回false.
	 */
	private boolean notContainsColumnLabel(String columnLabel) {
		return !clmap.containsKey(columnLabel);
	}

	/**
	 * @param columnLabel
	 * @return String
	 * @throws SQLException
	 * @see java.sql.ResultSet#getString(java.lang.String)
	 */
	public String getString(String columnLabel) throws SQLException {
		if (ignoreError && notContainsColumnLabel(columnLabel)) {
			return null;
		}
		return rs.getString(columnLabel);
	}

	/**
	 * @param columnLabel
	 * @return BigDecimal
	 * @throws SQLException
	 * @see java.sql.ResultSet#getBigDecimal(java.lang.String)
	 */
	public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
		if (ignoreError && notContainsColumnLabel(columnLabel)) {
			return null;
		}
		return rs.getBigDecimal(columnLabel);
	}

	/**
	 * @param columnLabel
	 * @return boolean
	 * @throws SQLException
	 * @see java.sql.ResultSet#getBoolean(java.lang.String)
	 */
	public boolean getBoolean(String columnLabel) throws SQLException {
		if (ignoreError && notContainsColumnLabel(columnLabel)) {
			return Boolean.FALSE;
		}
		return rs.getBoolean(columnLabel);
	}

	/**
	 * @param columnLabel
	 * @return double
	 * @throws SQLException
	 * @see java.sql.ResultSet#getDouble(java.lang.String)
	 */
	public double getDouble(String columnLabel) throws SQLException {
		if (ignoreError && notContainsColumnLabel(columnLabel)) {
			return 0.0;
		}
		return rs.getDouble(columnLabel);
	}

	/**
	 * @param columnLabel
	 * @return float
	 * @throws SQLException
	 * @see java.sql.ResultSet#getFloat(java.lang.String)
	 */
	public float getFloat(String columnLabel) throws SQLException {
		if (ignoreError && notContainsColumnLabel(columnLabel)) {
			return 0;
		}
		return rs.getFloat(columnLabel);
	}

	/**
	 * @param columnLabel
	 * @return int
	 * @throws SQLException
	 * @see java.sql.ResultSet#getInt(java.lang.String)
	 */
	public int getInt(String columnLabel) throws SQLException {
		if (ignoreError && notContainsColumnLabel(columnLabel)) {
			return 0;
		}
		return rs.getInt(columnLabel);
	}

	/**
	 * @param columnLabel
	 * @return long
	 * @throws SQLException
	 * @see java.sql.ResultSet#getLong(java.lang.String)
	 */
	public long getLong(String columnLabel) throws SQLException {
		if (ignoreError && notContainsColumnLabel(columnLabel)) {
			return 0;
		}
		return rs.getLong(columnLabel);
	}

	/**
	 * @param columnLabel
	 * @return short
	 * @throws SQLException
	 * @see java.sql.ResultSet#getShort(java.lang.String)
	 */
	public short getShort(String columnLabel) throws SQLException {
		if (ignoreError && notContainsColumnLabel(columnLabel)) {
			return 0;
		}
		return rs.getShort(columnLabel);
	}

	/**
	 * @return int
	 * @throws SQLException
	 * @see java.sql.ResultSet#getRow()
	 */
	public int getRow() throws SQLException {
		return rs.getRow();
	}

	/**
	 * @param columnLabel
	 * @return Date
	 * @throws SQLException
	 * @see java.sql.ResultSet#getDate(java.lang.String)
	 */
	public Date getDate(String columnLabel) throws SQLException {
		if (ignoreError && notContainsColumnLabel(columnLabel)) {
			return null;
		}
		return rs.getDate(columnLabel);
	}

	/**
	 * @param columnLabel
	 * @return Time
	 * @throws SQLException
	 * @see java.sql.ResultSet#getTime(java.lang.String)
	 */
	public Time getTime(String columnLabel) throws SQLException {
		if (ignoreError && notContainsColumnLabel(columnLabel)) {
			return null;
		}
		return rs.getTime(columnLabel);
	}

	/**
	 * @param columnLabel
	 * @return Timestamp
	 * @throws SQLException
	 * @see java.sql.ResultSet#getTimestamp(java.lang.String)
	 */
	public Timestamp getTimestamp(String columnLabel) throws SQLException {
		if (ignoreError && notContainsColumnLabel(columnLabel)) {
			return null;
		}
		return rs.getTimestamp(columnLabel);
	}

	/**
	 * @param columnLabel
	 * @return Date
	 * @throws SQLException
	 */
	public java.util.Date getDateTime(String columnLabel) throws SQLException {
		if (ignoreError && notContainsColumnLabel(columnLabel)) {
			return null;
		}
		return new Date(rs.getTimestamp(columnLabel).getTime());
	}

	// ==============================Methods_Proxy====================================
	/**
	 * @return int
	 * @throws SQLException
	 * @see java.sql.ResultSet#getType()
	 */
	public int getType() throws SQLException {
		return rs.getType();
	}

	/**
	 * @throws SQLException
	 * @see java.sql.ResultSet#next()
	 */
	public boolean next() throws SQLException {
		return rs.next();
	}

	/**
	 * @throws SQLException
	 * @see java.sql.ResultSet#close()
	 */
	public void close() throws SQLException {
		rs.close();
	}

	/**
	 * @param row
	 * @return boolean
	 * @throws SQLException
	 * @see java.sql.ResultSet#absolute(int)
	 */
	public boolean absolute(int row) throws SQLException {
		return rs.absolute(row);
	}

	/**
	 * @throws SQLException
	 * @see java.sql.ResultSet#afterLast()
	 */
	public void afterLast() throws SQLException {
		rs.afterLast();
	}

	/**
	 * @throws SQLException
	 * @see java.sql.ResultSet#beforeFirst()
	 */
	public void beforeFirst() throws SQLException {
		rs.beforeFirst();
	}

	/**
	 * @throws SQLException
	 * @see java.sql.ResultSet#cancelRowUpdates()
	 */
	public void cancelRowUpdates() throws SQLException {
		rs.cancelRowUpdates();
	}

	/**
	 * @throws SQLException
	 * @see java.sql.ResultSet#clearWarnings()
	 */
	public void clearWarnings() throws SQLException {
		rs.clearWarnings();
	}

	/**
	 * @throws SQLException
	 * @see java.sql.ResultSet#deleteRow()
	 */
	public void deleteRow() throws SQLException {
		rs.deleteRow();
	}

	/**
	 * @param columnLabel
	 * @return int
	 * @throws SQLException
	 * @see java.sql.ResultSet#findColumn(java.lang.String)
	 */
	public int findColumn(String columnLabel) throws SQLException {
		return rs.findColumn(columnLabel);
	}

	/**
	 * @return boolean
	 * @throws SQLException
	 * @see java.sql.ResultSet#first()
	 */
	public boolean first() throws SQLException {
		return rs.first();
	}

	/**
	 * @param columnIndex
	 * @return array
	 * @throws SQLException
	 * @see java.sql.ResultSet#getArray(int)
	 */
	public Array getArray(int columnIndex) throws SQLException {
		return rs.getArray(columnIndex);
	}

	/**
	 * @param columnLabel
	 * @return array
	 * @throws SQLException
	 * @see java.sql.ResultSet#getArray(java.lang.String)
	 */
	public Array getArray(String columnLabel) throws SQLException {
		return rs.getArray(columnLabel);
	}

	/**
	 * @param columnIndex
	 * @return InputStream
	 * @throws SQLException
	 * @see java.sql.ResultSet#getAsciiStream(int)
	 */
	public InputStream getAsciiStream(int columnIndex) throws SQLException {
		return rs.getAsciiStream(columnIndex);
	}

	/**
	 * @param columnLabel
	 * @return InputStream
	 * @throws SQLException
	 * @see java.sql.ResultSet#getAsciiStream(java.lang.String)
	 */
	public InputStream getAsciiStream(String columnLabel) throws SQLException {
		return rs.getAsciiStream(columnLabel);
	}

	/**
	 * @param columnIndex
	 * @return BigDecimal
	 * @throws SQLException
	 * @see java.sql.ResultSet#getBigDecimal(int)
	 */
	public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
		return rs.getBigDecimal(columnIndex);
	}

	/**
	 * @param columnIndex
	 * @param scale
	 * @return BigDecimal
	 * @throws SQLException
	 * @deprecated
	 * @see java.sql.ResultSet#getBigDecimal(int, int)
	 */
	public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
		return rs.getBigDecimal(columnIndex, scale);
	}

	/**
	 * @param columnLabel
	 * @param scale
	 * @return BigDecimal
	 * @throws SQLException
	 * @deprecated
	 * @see java.sql.ResultSet#getBigDecimal(java.lang.String, int)
	 */
	public BigDecimal getBigDecimal(String columnLabel, int scale) throws SQLException {
		return rs.getBigDecimal(columnLabel, scale);
	}

	/**
	 * @param columnIndex
	 * @return InputStream
	 * @throws SQLException
	 * @see java.sql.ResultSet#getBinaryStream(int)
	 */
	public InputStream getBinaryStream(int columnIndex) throws SQLException {
		return rs.getBinaryStream(columnIndex);
	}

	/**
	 * @param columnLabel
	 * @return InputStream
	 * @throws SQLException
	 * @see java.sql.ResultSet#getBinaryStream(java.lang.String)
	 */
	public InputStream getBinaryStream(String columnLabel) throws SQLException {
		return rs.getBinaryStream(columnLabel);
	}

	/**
	 * @param columnIndex
	 * @return Blob
	 * @throws SQLException
	 * @see java.sql.ResultSet#getBlob(int)
	 */
	public Blob getBlob(int columnIndex) throws SQLException {
		return rs.getBlob(columnIndex);
	}

	/**
	 * @param columnLabel
	 * @return Blob
	 * @throws SQLException
	 * @see java.sql.ResultSet#getBlob(java.lang.String)
	 */
	public Blob getBlob(String columnLabel) throws SQLException {
		return rs.getBlob(columnLabel);
	}

	/**
	 * @param columnIndex
	 * @return boolean
	 * @throws SQLException
	 * @see java.sql.ResultSet#getBoolean(int)
	 */
	public boolean getBoolean(int columnIndex) throws SQLException {
		return rs.getBoolean(columnIndex);
	}

	/**
	 * @param columnIndex
	 * @return byte
	 * @throws SQLException
	 * @see java.sql.ResultSet#getByte(int)
	 */
	public byte getByte(int columnIndex) throws SQLException {
		return rs.getByte(columnIndex);
	}

	/**
	 * @param columnLabel
	 * @return byte
	 * @throws SQLException
	 * @see java.sql.ResultSet#getByte(java.lang.String)
	 */
	public byte getByte(String columnLabel) throws SQLException {
		return rs.getByte(columnLabel);
	}

	/**
	 * @param columnIndex
	 * @return byte[]
	 * @throws SQLException
	 * @see java.sql.ResultSet#getBytes(int)
	 */
	public byte[] getBytes(int columnIndex) throws SQLException {
		return rs.getBytes(columnIndex);
	}

	/**
	 * @param columnLabel
	 * @return byte[]
	 * @throws SQLException
	 * @see java.sql.ResultSet#getBytes(java.lang.String)
	 */
	public byte[] getBytes(String columnLabel) throws SQLException {
		return rs.getBytes(columnLabel);
	}

	/**
	 * @param columnIndex
	 * @return Reader
	 * @throws SQLException
	 * @see java.sql.ResultSet#getCharacterStream(int)
	 */
	public Reader getCharacterStream(int columnIndex) throws SQLException {
		return rs.getCharacterStream(columnIndex);
	}

	/**
	 * @param columnLabel
	 * @return Reader
	 * @throws SQLException
	 * @see java.sql.ResultSet#getCharacterStream(java.lang.String)
	 */
	public Reader getCharacterStream(String columnLabel) throws SQLException {
		return rs.getCharacterStream(columnLabel);
	}

	/**
	 * @param columnIndex
	 * @return Clob
	 * @throws SQLException
	 * @see java.sql.ResultSet#getClob(int)
	 */
	public Clob getClob(int columnIndex) throws SQLException {
		return rs.getClob(columnIndex);
	}

	/**
	 * @param columnLabel
	 * @return Clob
	 * @throws SQLException
	 * @see java.sql.ResultSet#getClob(java.lang.String)
	 */
	public Clob getClob(String columnLabel) throws SQLException {
		return rs.getClob(columnLabel);
	}

	/**
	 * @return int
	 * @throws SQLException
	 * @see java.sql.ResultSet#getConcurrency()
	 */
	public int getConcurrency() throws SQLException {
		return rs.getConcurrency();
	}

	/**
	 * @return String
	 * @throws SQLException
	 * @see java.sql.ResultSet#getCursorName()
	 */
	public String getCursorName() throws SQLException {
		return rs.getCursorName();
	}

	/**
	 * @param columnIndex
	 * @return Date
	 * @throws SQLException
	 * @see java.sql.ResultSet#getDate(int)
	 */
	public Date getDate(int columnIndex) throws SQLException {
		return rs.getDate(columnIndex);
	}

	/**
	 * @param columnIndex
	 * @param cal
	 * @return Date
	 * @throws SQLException
	 * @see java.sql.ResultSet#getDate(int, java.util.Calendar)
	 */
	public Date getDate(int columnIndex, Calendar cal) throws SQLException {
		return rs.getDate(columnIndex, cal);
	}

	/**
	 * @param columnLabel
	 * @param cal
	 * @return Date
	 * @throws SQLException
	 * @see java.sql.ResultSet#getDate(java.lang.String, java.util.Calendar)
	 */
	public Date getDate(String columnLabel, Calendar cal) throws SQLException {
		return rs.getDate(columnLabel, cal);
	}

	/**
	 * @param columnIndex
	 * @return double
	 * @throws SQLException
	 * @see java.sql.ResultSet#getDouble(int)
	 */
	public double getDouble(int columnIndex) throws SQLException {
		return rs.getDouble(columnIndex);
	}

	/**
	 * @return int
	 * @throws SQLException
	 * @see java.sql.ResultSet#getFetchDirection()
	 */
	public int getFetchDirection() throws SQLException {
		return rs.getFetchDirection();
	}

	/**
	 * @return int
	 * @throws SQLException
	 * @see java.sql.ResultSet#getFetchSize()
	 */
	public int getFetchSize() throws SQLException {
		return rs.getFetchSize();
	}

	/**
	 * @param columnIndex
	 * @return float
	 * @throws SQLException
	 * @see java.sql.ResultSet#getFloat(int)
	 */
	public float getFloat(int columnIndex) throws SQLException {
		return rs.getFloat(columnIndex);
	}

	/**
	 * @param columnIndex
	 * @return int
	 * @throws SQLException
	 * @see java.sql.ResultSet#getInt(int)
	 */
	public int getInt(int columnIndex) throws SQLException {
		return rs.getInt(columnIndex);
	}

	/**
	 * @param columnIndex
	 * @return long
	 * @throws SQLException
	 * @see java.sql.ResultSet#getLong(int)
	 */
	public long getLong(int columnIndex) throws SQLException {
		return rs.getLong(columnIndex);
	}

	/**
	 * @return ResultSetMetaData
	 * @throws SQLException
	 * @see java.sql.ResultSet#getMetaData()
	 */
	public ResultSetMetaData getMetaData() throws SQLException {
		return rs.getMetaData();
	}

	/**
	 * @param columnIndex
	 * @return Object
	 * @throws SQLException
	 * @see java.sql.ResultSet#getObject(int)
	 */
	public Object getObject(int columnIndex) throws SQLException {
		return rs.getObject(columnIndex);
	}

	/**
	 * @param columnLabel
	 * @return Object
	 * @throws SQLException
	 * @see java.sql.ResultSet#getObject(java.lang.String)
	 */
	public Object getObject(String columnLabel) throws SQLException {
		return rs.getObject(columnLabel);
	}

	/**
	 * @param columnIndex
	 * @param map
	 * @return Object
	 * @throws SQLException
	 * @see java.sql.ResultSet#getObject(int, java.util.Map)
	 */
	public Object getObject(int columnIndex, Map<String, Class<?>> map) throws SQLException {
		return rs.getObject(columnIndex, map);
	}

	/**
	 * @param columnLabel
	 * @param map
	 * @return Object
	 * @throws SQLException
	 * @see java.sql.ResultSet#getObject(java.lang.String, java.util.Map)
	 */
	public Object getObject(String columnLabel, Map<String, Class<?>> map) throws SQLException {
		return rs.getObject(columnLabel, map);
	}

	/**
	 * @param columnIndex
	 * @return Ref
	 * @throws SQLException
	 * @see java.sql.ResultSet#getRef(int)
	 */
	public Ref getRef(int columnIndex) throws SQLException {
		return rs.getRef(columnIndex);
	}

	/**
	 * @param columnLabel
	 * @return Ref
	 * @throws SQLException
	 * @see java.sql.ResultSet#getRef(java.lang.String)
	 */
	public Ref getRef(String columnLabel) throws SQLException {
		return rs.getRef(columnLabel);
	}

	/**
	 * @param columnIndex
	 * @return short
	 * @throws SQLException
	 * @see java.sql.ResultSet#getShort(int)
	 */
	public short getShort(int columnIndex) throws SQLException {
		return rs.getShort(columnIndex);
	}

	/**
	 * @return Statement
	 * @throws SQLException
	 * @see java.sql.ResultSet#getStatement()
	 */
	public Statement getStatement() throws SQLException {
		return rs.getStatement();
	}

	/**
	 * @param columnIndex
	 * @return String
	 * @throws SQLException
	 * @see java.sql.ResultSet#getString(int)
	 */
	public String getString(int columnIndex) throws SQLException {
		return rs.getString(columnIndex);
	}

	/**
	 * @param columnIndex
	 * @return Time
	 * @throws SQLException
	 * @see java.sql.ResultSet#getTime(int)
	 */
	public Time getTime(int columnIndex) throws SQLException {
		return rs.getTime(columnIndex);
	}

	/**
	 * @param columnIndex
	 * @param cal
	 * @return Time
	 * @throws SQLException
	 * @see java.sql.ResultSet#getTime(int, java.util.Calendar)
	 */
	public Time getTime(int columnIndex, Calendar cal) throws SQLException {
		return rs.getTime(columnIndex, cal);
	}

	/**
	 * @param columnLabel
	 * @param cal
	 * @return Time
	 * @throws SQLException
	 * @see java.sql.ResultSet#getTime(java.lang.String, java.util.Calendar)
	 */
	public Time getTime(String columnLabel, Calendar cal) throws SQLException {
		return rs.getTime(columnLabel, cal);
	}

	/**
	 * @param columnIndex
	 * @return Timestamp
	 * @throws SQLException
	 * @see java.sql.ResultSet#getTimestamp(int)
	 */
	public Timestamp getTimestamp(int columnIndex) throws SQLException {
		return rs.getTimestamp(columnIndex);
	}

	/**
	 * @param columnIndex
	 * @param cal
	 * @return Timestamp
	 * @throws SQLException
	 * @see java.sql.ResultSet#getTimestamp(int, java.util.Calendar)
	 */
	public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
		return rs.getTimestamp(columnIndex, cal);
	}

	/**
	 * @param columnLabel
	 * @param cal
	 * @return Timestamp
	 * @throws SQLException
	 * @see java.sql.ResultSet#getTimestamp(java.lang.String, java.util.Calendar)
	 */
	public Timestamp getTimestamp(String columnLabel, Calendar cal) throws SQLException {
		return rs.getTimestamp(columnLabel, cal);
	}

	/**
	 * @param columnIndex
	 * @return URL
	 * @throws SQLException
	 * @see java.sql.ResultSet#getURL(int)
	 */
	public URL getURL(int columnIndex) throws SQLException {
		return rs.getURL(columnIndex);
	}

	/**
	 * @param columnLabel
	 * @return URL
	 * @throws SQLException
	 * @see java.sql.ResultSet#getURL(java.lang.String)
	 */
	public URL getURL(String columnLabel) throws SQLException {
		return rs.getURL(columnLabel);
	}

	/**
	 * @param columnIndex
	 * @return InputStream
	 * @throws SQLException
	 * @deprecated
	 * @see java.sql.ResultSet#getUnicodeStream(int)
	 */
	public InputStream getUnicodeStream(int columnIndex) throws SQLException {
		return rs.getUnicodeStream(columnIndex);
	}

	/**
	 * @param columnLabel
	 * @return InputStream
	 * @throws SQLException
	 * @deprecated
	 * @see java.sql.ResultSet#getUnicodeStream(java.lang.String)
	 */
	public InputStream getUnicodeStream(String columnLabel) throws SQLException {
		return rs.getUnicodeStream(columnLabel);
	}

	/**
	 * @return SQLWarning
	 * @throws SQLException
	 * @see java.sql.ResultSet#getWarnings()
	 */
	public SQLWarning getWarnings() throws SQLException {
		return rs.getWarnings();
	}

	/**
	 * @throws SQLException
	 * @see java.sql.ResultSet#insertRow()
	 */
	public void insertRow() throws SQLException {
		rs.insertRow();
	}

	/**
	 * @return boolean
	 * @throws SQLException
	 * @see java.sql.ResultSet#isAfterLast()
	 */
	public boolean isAfterLast() throws SQLException {
		return rs.isAfterLast();
	}

	/**
	 * @return boolean
	 * @throws SQLException
	 * @see java.sql.ResultSet#isBeforeFirst()
	 */
	public boolean isBeforeFirst() throws SQLException {
		return rs.isBeforeFirst();
	}

	/**
	 * @return boolean
	 * @throws SQLException
	 * @see java.sql.ResultSet#isFirst()
	 */
	public boolean isFirst() throws SQLException {
		return rs.isFirst();
	}

	/**
	 * @return boolean
	 * @throws SQLException
	 * @see java.sql.ResultSet#isLast()
	 */
	public boolean isLast() throws SQLException {
		return rs.isLast();
	}

	/**
	 * @return boolean
	 * @throws SQLException
	 * @see java.sql.ResultSet#last()
	 */
	public boolean last() throws SQLException {
		return rs.last();
	}

	/**
	 * @throws SQLException
	 * @see java.sql.ResultSet#moveToCurrentRow()
	 */
	public void moveToCurrentRow() throws SQLException {
		rs.moveToCurrentRow();
	}

	/**
	 * @throws SQLException
	 * @see java.sql.ResultSet#moveToInsertRow()
	 */
	public void moveToInsertRow() throws SQLException {
		rs.moveToInsertRow();
	}

	/**
	 * @return boolean
	 * @throws SQLException
	 * @see java.sql.ResultSet#previous()
	 */
	public boolean previous() throws SQLException {
		return rs.previous();
	}

	/**
	 * @throws SQLException
	 * @see java.sql.ResultSet#refreshRow()
	 */
	public void refreshRow() throws SQLException {
		rs.refreshRow();
	}

	/**
	 * @param rows
	 * @return boolean
	 * @throws SQLException
	 * @see java.sql.ResultSet#relative(int)
	 */
	public boolean relative(int rows) throws SQLException {
		return rs.relative(rows);
	}

	/**
	 * @return boolean
	 * @throws SQLException
	 * @see java.sql.ResultSet#rowDeleted()
	 */
	public boolean rowDeleted() throws SQLException {
		return rs.rowDeleted();
	}

	/**
	 * @return boolean
	 * @throws SQLException
	 * @see java.sql.ResultSet#rowInserted()
	 */
	public boolean rowInserted() throws SQLException {
		return rs.rowInserted();
	}

	/**
	 * @return boolean
	 * @throws SQLException
	 * @see java.sql.ResultSet#rowUpdated()
	 */
	public boolean rowUpdated() throws SQLException {
		return rs.rowUpdated();
	}

	/**
	 * @param direction
	 * @throws SQLException
	 * @see java.sql.ResultSet#setFetchDirection(int)
	 */
	public void setFetchDirection(int direction) throws SQLException {
		rs.setFetchDirection(direction);
	}

	/**
	 * @param rows
	 * @throws SQLException
	 * @see java.sql.ResultSet#setFetchSize(int)
	 */
	public void setFetchSize(int rows) throws SQLException {
		rs.setFetchSize(rows);
	}

	/**
	 * @param columnIndex
	 * @param x
	 * @throws SQLException
	 * @see java.sql.ResultSet#updateArray(int, java.sql.Array)
	 */
	public void updateArray(int columnIndex, Array x) throws SQLException {
		rs.updateArray(columnIndex, x);
	}

	/**
	 * @param columnLabel
	 * @param x
	 * @throws SQLException
	 * @see java.sql.ResultSet#updateArray(java.lang.String, java.sql.Array)
	 */
	public void updateArray(String columnLabel, Array x) throws SQLException {
		rs.updateArray(columnLabel, x);
	}

	/**
	 * @param columnIndex
	 * @param x
	 * @param length
	 * @throws SQLException
	 * @see java.sql.ResultSet#updateAsciiStream(int, java.io.InputStream, int)
	 */
	public void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException {
		rs.updateAsciiStream(columnIndex, x, length);
	}

	/**
	 * @param columnLabel
	 * @param x
	 * @param length
	 * @throws SQLException
	 * @see java.sql.ResultSet#updateAsciiStream(java.lang.String, java.io.InputStream, int)
	 */
	public void updateAsciiStream(String columnLabel, InputStream x, int length) throws SQLException {
		rs.updateAsciiStream(columnLabel, x, length);
	}

	/**
	 * @param columnIndex
	 * @param x
	 * @throws SQLException
	 * @see java.sql.ResultSet#updateBigDecimal(int, java.math.BigDecimal)
	 */
	public void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {
		rs.updateBigDecimal(columnIndex, x);
	}

	/**
	 * @param columnLabel
	 * @param x
	 * @throws SQLException
	 * @see java.sql.ResultSet#updateBigDecimal(java.lang.String, java.math.BigDecimal)
	 */
	public void updateBigDecimal(String columnLabel, BigDecimal x) throws SQLException {
		rs.updateBigDecimal(columnLabel, x);
	}

	/**
	 * @param columnIndex
	 * @param x
	 * @param length
	 * @throws SQLException
	 * @see java.sql.ResultSet#updateBinaryStream(int, java.io.InputStream, int)
	 */
	public void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException {
		rs.updateBinaryStream(columnIndex, x, length);
	}

	/**
	 * @param columnLabel
	 * @param x
	 * @param length
	 * @throws SQLException
	 * @see java.sql.ResultSet#updateBinaryStream(java.lang.String, java.io.InputStream, int)
	 */
	public void updateBinaryStream(String columnLabel, InputStream x, int length) throws SQLException {
		rs.updateBinaryStream(columnLabel, x, length);
	}

	/**
	 * @param columnIndex
	 * @param x
	 * @throws SQLException
	 * @see java.sql.ResultSet#updateBlob(int, java.sql.Blob)
	 */
	public void updateBlob(int columnIndex, Blob x) throws SQLException {
		rs.updateBlob(columnIndex, x);
	}

	/**
	 * @param columnLabel
	 * @param x
	 * @throws SQLException
	 * @see java.sql.ResultSet#updateBlob(java.lang.String, java.sql.Blob)
	 */
	public void updateBlob(String columnLabel, Blob x) throws SQLException {
		rs.updateBlob(columnLabel, x);
	}

	/**
	 * @param columnIndex
	 * @param x
	 * @throws SQLException
	 * @see java.sql.ResultSet#updateBoolean(int, boolean)
	 */
	public void updateBoolean(int columnIndex, boolean x) throws SQLException {
		rs.updateBoolean(columnIndex, x);
	}

	/**
	 * @param columnLabel
	 * @param x
	 * @throws SQLException
	 * @see java.sql.ResultSet#updateBoolean(java.lang.String, boolean)
	 */
	public void updateBoolean(String columnLabel, boolean x) throws SQLException {
		rs.updateBoolean(columnLabel, x);
	}

	/**
	 * @param columnIndex
	 * @param x
	 * @throws SQLException
	 * @see java.sql.ResultSet#updateByte(int, byte)
	 */
	public void updateByte(int columnIndex, byte x) throws SQLException {
		rs.updateByte(columnIndex, x);
	}

	/**
	 * @param columnLabel
	 * @param x
	 * @throws SQLException
	 * @see java.sql.ResultSet#updateByte(java.lang.String, byte)
	 */
	public void updateByte(String columnLabel, byte x) throws SQLException {
		rs.updateByte(columnLabel, x);
	}

	/**
	 * @param columnIndex
	 * @param x
	 * @throws SQLException
	 * @see java.sql.ResultSet#updateBytes(int, byte[])
	 */
	public void updateBytes(int columnIndex, byte[] x) throws SQLException {
		rs.updateBytes(columnIndex, x);
	}

	/**
	 * @param columnLabel
	 * @param x
	 * @throws SQLException
	 * @see java.sql.ResultSet#updateBytes(java.lang.String, byte[])
	 */
	public void updateBytes(String columnLabel, byte[] x) throws SQLException {
		rs.updateBytes(columnLabel, x);
	}

	/**
	 * @param columnIndex
	 * @param x
	 * @param length
	 * @throws SQLException
	 * @see java.sql.ResultSet#updateCharacterStream(int, java.io.Reader, int)
	 */
	public void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException {
		rs.updateCharacterStream(columnIndex, x, length);
	}

	/**
	 * @param columnLabel
	 * @param reader
	 * @param length
	 * @throws SQLException
	 * @see java.sql.ResultSet#updateCharacterStream(java.lang.String, java.io.Reader, int)
	 */
	public void updateCharacterStream(String columnLabel, Reader reader, int length) throws SQLException {
		rs.updateCharacterStream(columnLabel, reader, length);
	}

	/**
	 * @param columnIndex
	 * @param x
	 * @throws SQLException
	 * @see java.sql.ResultSet#updateClob(int, java.sql.Clob)
	 */
	public void updateClob(int columnIndex, Clob x) throws SQLException {
		rs.updateClob(columnIndex, x);
	}

	/**
	 * @param columnLabel
	 * @param x
	 * @throws SQLException
	 * @see java.sql.ResultSet#updateClob(java.lang.String, java.sql.Clob)
	 */
	public void updateClob(String columnLabel, Clob x) throws SQLException {
		rs.updateClob(columnLabel, x);
	}

	/**
	 * @param columnIndex
	 * @param x
	 * @throws SQLException
	 * @see java.sql.ResultSet#updateDate(int, java.sql.Date)
	 */
	public void updateDate(int columnIndex, Date x) throws SQLException {
		rs.updateDate(columnIndex, x);
	}

	/**
	 * @param columnLabel
	 * @param x
	 * @throws SQLException
	 * @see java.sql.ResultSet#updateDate(java.lang.String, java.sql.Date)
	 */
	public void updateDate(String columnLabel, Date x) throws SQLException {
		rs.updateDate(columnLabel, x);
	}

	/**
	 * @param columnIndex
	 * @param x
	 * @throws SQLException
	 * @see java.sql.ResultSet#updateDouble(int, double)
	 */
	public void updateDouble(int columnIndex, double x) throws SQLException {
		rs.updateDouble(columnIndex, x);
	}

	/**
	 * @param columnLabel
	 * @param x
	 * @throws SQLException
	 * @see java.sql.ResultSet#updateDouble(java.lang.String, double)
	 */
	public void updateDouble(String columnLabel, double x) throws SQLException {
		rs.updateDouble(columnLabel, x);
	}

	/**
	 * @param columnIndex
	 * @param x
	 * @throws SQLException
	 * @see java.sql.ResultSet#updateFloat(int, float)
	 */
	public void updateFloat(int columnIndex, float x) throws SQLException {
		rs.updateFloat(columnIndex, x);
	}

	/**
	 * @param columnLabel
	 * @param x
	 * @throws SQLException
	 * @see java.sql.ResultSet#updateFloat(java.lang.String, float)
	 */
	public void updateFloat(String columnLabel, float x) throws SQLException {
		rs.updateFloat(columnLabel, x);
	}

	/**
	 * @param columnIndex
	 * @param x
	 * @throws SQLException
	 * @see java.sql.ResultSet#updateInt(int, int)
	 */
	public void updateInt(int columnIndex, int x) throws SQLException {
		rs.updateInt(columnIndex, x);
	}

	/**
	 * @param columnLabel
	 * @param x
	 * @throws SQLException
	 * @see java.sql.ResultSet#updateInt(java.lang.String, int)
	 */
	public void updateInt(String columnLabel, int x) throws SQLException {
		rs.updateInt(columnLabel, x);
	}

	/**
	 * @param columnIndex
	 * @param x
	 * @throws SQLException
	 * @see java.sql.ResultSet#updateLong(int, long)
	 */
	public void updateLong(int columnIndex, long x) throws SQLException {
		rs.updateLong(columnIndex, x);
	}

	/**
	 * @param columnLabel
	 * @param x
	 * @throws SQLException
	 * @see java.sql.ResultSet#updateLong(java.lang.String, long)
	 */
	public void updateLong(String columnLabel, long x) throws SQLException {
		rs.updateLong(columnLabel, x);
	}

	/**
	 * @param columnIndex
	 * @throws SQLException
	 * @see java.sql.ResultSet#updateNull(int)
	 */
	public void updateNull(int columnIndex) throws SQLException {
		rs.updateNull(columnIndex);
	}

	/**
	 * @param columnLabel
	 * @throws SQLException
	 * @see java.sql.ResultSet#updateNull(java.lang.String)
	 */
	public void updateNull(String columnLabel) throws SQLException {
		rs.updateNull(columnLabel);
	}

	/**
	 * @param columnIndex
	 * @param x
	 * @throws SQLException
	 * @see java.sql.ResultSet#updateObject(int, java.lang.Object)
	 */
	public void updateObject(int columnIndex, Object x) throws SQLException {
		rs.updateObject(columnIndex, x);
	}

	/**
	 * @param columnLabel
	 * @param x
	 * @throws SQLException
	 * @see java.sql.ResultSet#updateObject(java.lang.String, java.lang.Object)
	 */
	public void updateObject(String columnLabel, Object x) throws SQLException {
		rs.updateObject(columnLabel, x);
	}

	/**
	 * @param columnIndex
	 * @param x
	 * @param scaleOrLength
	 * @throws SQLException
	 * @see java.sql.ResultSet#updateObject(int, java.lang.Object, int)
	 */
	public void updateObject(int columnIndex, Object x, int scaleOrLength) throws SQLException {
		rs.updateObject(columnIndex, x, scaleOrLength);
	}

	/**
	 * @param columnLabel
	 * @param x
	 * @param scaleOrLength
	 * @throws SQLException
	 * @see java.sql.ResultSet#updateObject(java.lang.String, java.lang.Object, int)
	 */
	public void updateObject(String columnLabel, Object x, int scaleOrLength) throws SQLException {
		rs.updateObject(columnLabel, x, scaleOrLength);
	}

	/**
	 * @param columnIndex
	 * @param x
	 * @throws SQLException
	 * @see java.sql.ResultSet#updateRef(int, java.sql.Ref)
	 */
	public void updateRef(int columnIndex, Ref x) throws SQLException {
		rs.updateRef(columnIndex, x);
	}

	/**
	 * @param columnLabel
	 * @param x
	 * @throws SQLException
	 * @see java.sql.ResultSet#updateRef(java.lang.String, java.sql.Ref)
	 */
	public void updateRef(String columnLabel, Ref x) throws SQLException {
		rs.updateRef(columnLabel, x);
	}

	/**
	 * @throws SQLException
	 * @see java.sql.ResultSet#updateRow()
	 */
	public void updateRow() throws SQLException {
		rs.updateRow();
	}

	/**
	 * @param columnIndex
	 * @param x
	 * @throws SQLException
	 * @see java.sql.ResultSet#updateShort(int, short)
	 */
	public void updateShort(int columnIndex, short x) throws SQLException {
		rs.updateShort(columnIndex, x);
	}

	/**
	 * @param columnLabel
	 * @param x
	 * @throws SQLException
	 * @see java.sql.ResultSet#updateShort(java.lang.String, short)
	 */
	public void updateShort(String columnLabel, short x) throws SQLException {
		rs.updateShort(columnLabel, x);
	}

	/**
	 * @param columnIndex
	 * @param x
	 * @throws SQLException
	 * @see java.sql.ResultSet#updateString(int, java.lang.String)
	 */
	public void updateString(int columnIndex, String x) throws SQLException {
		rs.updateString(columnIndex, x);
	}

	/**
	 * @param columnLabel
	 * @param x
	 * @throws SQLException
	 * @see java.sql.ResultSet#updateString(java.lang.String, java.lang.String)
	 */
	public void updateString(String columnLabel, String x) throws SQLException {
		rs.updateString(columnLabel, x);
	}

	/**
	 * @param columnIndex
	 * @param x
	 * @throws SQLException
	 * @see java.sql.ResultSet#updateTime(int, java.sql.Time)
	 */
	public void updateTime(int columnIndex, Time x) throws SQLException {
		rs.updateTime(columnIndex, x);
	}

	/**
	 * @param columnLabel
	 * @param x
	 * @throws SQLException
	 * @see java.sql.ResultSet#updateTime(java.lang.String, java.sql.Time)
	 */
	public void updateTime(String columnLabel, Time x) throws SQLException {
		rs.updateTime(columnLabel, x);
	}

	/**
	 * @param columnIndex
	 * @param x
	 * @throws SQLException
	 * @see java.sql.ResultSet#updateTimestamp(int, java.sql.Timestamp)
	 */
	public void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {
		rs.updateTimestamp(columnIndex, x);
	}

	/**
	 * @param columnLabel
	 * @param x
	 * @throws SQLException
	 * @see java.sql.ResultSet#updateTimestamp(java.lang.String, java.sql.Timestamp)
	 */
	public void updateTimestamp(String columnLabel, Timestamp x) throws SQLException {
		rs.updateTimestamp(columnLabel, x);
	}

	/**
	 * @return boolean
	 * @throws SQLException
	 * @see java.sql.ResultSet#wasNull()
	 */
	public boolean wasNull() throws SQLException {
		return rs.wasNull();
	}
}
