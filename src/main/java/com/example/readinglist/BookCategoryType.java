package com.example.readinglist;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class BookCategoryType implements UserType {
  private static final int[] SQL_TYPES = new int[] { Types.OTHER };

  public int[] sqlTypes() {
    return SQL_TYPES;
  }

  public Class returnedClass() {
    return BookCategories.class;
  }

  public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
    System.out.println("############################ DOING GET #########################");
    Object pgObject = rs.getObject(names[0]);
    try {
      return BookCategories.valueOf(pgObject.toString());
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
    if(value != null) {
      st.setObject(index, value.toString(), Types.OTHER);
    } else {
      st.setObject(index, Types.OTHER);
    }
  }

  // default implementations

  public boolean isMutable() {
    return false;
  }

  public Object assemble(Serializable cached, Object owner) throws HibernateException {
    return cached;
  }

  public Serializable disassemble(Object value) throws HibernateException {
    return (Serializable) value;
  }

  public Object deepCopy(Object value) throws HibernateException {
    return value;
  }

  public boolean equals(Object x, Object y) throws HibernateException {
    return x == y;
  }

  public int hashCode(Object x) throws HibernateException {
    assert (x != null);
    return x.hashCode();
  }

  public Object replace(Object original, Object target, Object owner) throws HibernateException {
    return original;
  }
}
