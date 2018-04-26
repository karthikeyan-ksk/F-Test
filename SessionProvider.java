package com.allergan.coral.dfc.util;

import java.util.Map;

import com.documentum.com.DfClientX;
import com.documentum.com.IDfClientX;
import com.documentum.fc.client.IDfClient;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSessionManager;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfLogger;
import com.documentum.fc.common.IDfLoginInfo;
import com.documentum.fc.methodserver.DfMethodArgumentManager;

/**
 * Contains the session provider methods. Session can be obtained in two ways
 * one through only DFC call and another from Session Manager. Also it has the
 * corresponding session release methods.
 * 
 * @author sakthivel_karthikeya
 */
public class SessionProvider {

	/**
	 * Holds the session manager object.
	 */
	private static IDfSessionManager dfSessionManager;

	/**
	 * Holds the session object.
	 */
	private IDfSession dfSession;

	/**
	 * Holds the docbase name.
	 */
	private static String docbaseName = "";

	/**
	 * This method gets the Docbase Session using DFC calls only for the Default
	 * Docbase and User.
	 * 
	 * @param None
	 * @return dfSession The IDfSession object
	 * @exception DfException
	 *                throws when can not get the session
	 */
	public IDfSession getDfSession() throws DfException {
		if (SessionProvider.dfSessionManager != null) {
			dfSession = SessionProvider.dfSessionManager.getSession(SessionProvider.docbaseName);
			return dfSession;
		} else {
			ResourceProperties properties = new ResourceProperties("Application");
			SessionProvider.docbaseName = properties.getKeyValue("Docbase");
			String userName = properties.getKeyValue("SuperUser");
			String password = properties.getKeyValue("SUPass");

			SessionProvider.dfSessionManager = createSessionManager(SessionProvider.docbaseName, userName, password);

			// Attempt to create the session.
			dfSession = SessionProvider.dfSessionManager.getSession(SessionProvider.docbaseName);
			return (dfSession);
		}
	}

	/**
	 * This method releases the Docbase Session obtained through DFC call.
	 */
	public void releaseDfSession() {
		if (dfSession != null) {
			try {
				if (dfSession.isConnected()) {
					SessionProvider.dfSessionManager.release(dfSession);
				}
				dfSession = null;
			} catch (Exception exce) {
				DfLogger.error(this, "in releaseDfSession : Error while releasing Session. " + "Exception ", null, exce);
			}
		}
	}

	/**
	 * This method checks whether the session is connected or not.
	 * 
	 * @param None
	 * @return result boolean : true if connected else false
	 */
	public boolean isConnected() {
		if (dfSession != null) {
			try {
				if (dfSession.isConnected()) {
					return true;
				}
			} catch (Exception exce) {
				DfLogger.error(this, "in isConnected : Error while releasing Session. " + "Exception ", null, exce);
				return false;
			}
		}
		return false;
	}

	/**
	 * Creates the session Manager Object.
	 * 
	 * @param docbase
	 *            name of the docbase.
	 * @param user
	 *            user name.
	 * @param pass
	 *            password.
	 * @return the session manager object.
	 * @throws DfException 
	 */
	private IDfSessionManager createSessionManager(String docbase, String user, String pass) throws DfException {
		// create Client objects
		IDfClientX clientx = new DfClientX();
		IDfClient client;
		IDfSessionManager sMgr = null;
		client = clientx.getLocalClient();

		// create a Session Manager object
		sMgr = client.newSessionManager();

		// create an IDfLoginInfo object named loginInfoObj
		IDfLoginInfo loginInfoObj = clientx.getLoginInfo();
		loginInfoObj.setUser(user);
		loginInfoObj.setPassword(pass);
		loginInfoObj.setDomain(null);

		// bind the Session Manager to the login info
		sMgr.setIdentity(docbase, loginInfoObj);
		return sMgr;
	}

	/**
	 * @param contentsMap
	 * @return
	 * @throws DfException
	 */
	public IDfSession getDfSession(Map contentsMap) throws DfException {
		if (SessionProvider.dfSessionManager != null) {

			dfSession = SessionProvider.dfSessionManager.getSession(SessionProvider.docbaseName);
			return dfSession;
		} else {
				DfMethodArgumentManager dfMethodArgumentManager = new DfMethodArgumentManager(contentsMap);
				String userID = dfMethodArgumentManager.getString("UserName");
				String passWord = dfMethodArgumentManager.getString("PassWord");
				docbaseName = dfMethodArgumentManager.getString("docbase");

				DfLogger.info(this, "UserName: " + userID + "PassWord: " + passWord + "DocBase: " + docbaseName, null, null);
				SessionProvider.dfSessionManager = createSessionManager(SessionProvider.docbaseName, userID, passWord);

				// Attempt to create the session.
				dfSession = SessionProvider.dfSessionManager.getSession(SessionProvider.docbaseName);
			return (dfSession);
		}
	}
}
