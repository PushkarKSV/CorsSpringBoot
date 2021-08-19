/*
 * (c) 2021 IBM Financial Industry Solutions GmbH, All rights reserved.
 */

package com.ibm.nettrader.brokerage.utilities;

import com.ibm.nettrader.brokerage.custom.exceptions.NormalDataBaseException;
import com.ibm.nettrader.brokerage.custom.exceptions.TechnicalDataBaseException;
import java.io.StringWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import oracle.xdb.XMLType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.w3c.dom.Document;

/**
 * The Class CallStoredProcedure, is used to execute standard stored procedure sp_ftng_svc_runner.
 * requires EntityCode (2nd parameter) and TransactionType (3rd parameter) .
 * - @author 03186D744
 */
@Configuration
public class CallStoredProcedure {
  
  /** The Constant logger. */
  private static final Logger logger = LoggerFactory.getLogger(CallStoredProcedure.class);

  /** The conn. */
  Connection conn = null;
  
  /** The out xml. */
  XMLType outXml = null;

  /** The callable statement. */
  CallableStatement callableStatement = null;
  
  /** The final out xml. */
  String finalOutXml = null;
  
  /** The out xml doc. */
  Document outXmlDoc = null;

  /** The data source. */
  // inject datasource object to get database connection
  @Autowired
  private DataSource dataSource;
  
  /**
   * Instantiates a new call stored procedure.
   *
   * @param dataSource the data source
   */
  public CallStoredProcedure(DataSource dataSource) {
    super();
    this.dataSource = dataSource;
  }

  /**
   * Execute standard sp_ftng_svc_runner stored procedure.
   *
   * @param entity the entity
   * @param txType the tx type
   * @return the XML type
   * @throws SQLException the SQL exception
   */
  public String executeStoredProcedure(String entity, String txType, String payLoad) 
      throws SQLException {
    try {
      conn = dataSource.getConnection();
      callableStatement = conn.prepareCall("{ call sp_ftng_svc_runner (?,?,?,?,?) }");
      // pass input parameters
      callableStatement.setString(1, " ");
      callableStatement.setString(2, entity);
      callableStatement.setString(3, txType);
      callableStatement.setObject(4, XMLType.createXML(conn, payLoad, " "));
      // register OUT parameter
      callableStatement.registerOutParameter(5, oracle.jdbc.OracleTypes.OPAQUE, "SYS.XMLTYPE");
      callableStatement.execute();
      // get result XML
      outXml = (XMLType) callableStatement.getObject(5);
      outXmlDoc = (Document) outXml.getDOM();
      finalOutXml = convertXmlDocToString(outXmlDoc);  
      return finalOutXml;
      
    }  catch (SQLException ex) {
      if (ex.getErrorCode() >= -20999 && ex.getErrorCode() <= -20000) {
        ex.printStackTrace();
        // show oracle(ORA) error code with short description
        throw new NormalDataBaseException(ex.getMessage());
      }  else {
        ex.printStackTrace();
        // show Technical DB error to client
        throw new TechnicalDataBaseException("Technical DB Error occurred");
      } 
    }  finally {
      // close db connection
      conn.close();
    }
  }

  /**
   * fetch the xml.
   *
   * @param outXmlDoc the out xml doc
   * @return the string
   */
  private String convertXmlDocToString(Document outXmlDoc) {
    TransformerFactory tf = TransformerFactory.newInstance();
    Transformer transformer;
    String xmlString = null;
    try {
      transformer = tf.newTransformer();
      StringWriter writer = new StringWriter();
      //transform document to string 
      transformer.transform(new DOMSource(outXmlDoc), new StreamResult(writer));
      xmlString = writer.getBuffer().toString();
      logger.info("Output XML from DB :\n" + xmlString);
    } catch (TransformerException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return xmlString;
  }
}
