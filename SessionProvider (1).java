package com.allergan.coral.dfc.user.util;

import com.documentum.com.DfClientX;
import com.documentum.com.IDfClientX;
import com.documentum.fc.client.IDfClient;
import com.documentum.fc.client.IDfSessionManager;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.IDfLoginInfo;
import com.documentum.fc.tools.RegistryPasswordUtils;

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
	 * Holds the docbase name.
	 */
	private static String docbaseName = "";

	protected SessionProvider() {
		
	}
	
	/**
	 * This method gets the Docbase Session using DFC calls only for the Default
	 * Docbase and User.
	 * 
	 * @param None
	 * @return dfSession The IDfSession object
	 * @exception DfException
	 *                throws when can not get the session
	 */
	public static IDfSessionManager getDfSessionManager() throws DfException {
		if (SessionProvider.dfSessionManager != null) {
			return dfSessionManager;
		} else {
			SessionProvider.docbaseName = ResourceProperties.getKeyValue("Docbase");
			String userName = ResourceProperties.getKeyValue("SuperUser");
			String password = ResourceProperties.getKeyValue("SUPass");

			dfSessionManager = createSessionManager(SessionProvider.docbaseName, userName, password);

			return SessionProvider.dfSessionManager;
		}
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
	private static IDfSessionManager createSessionManager(String docbase, String user, String pass) throws DfException {
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
		loginInfoObj.setPassword(RegistryPasswordUtils.decrypt(pass));
		loginInfoObj.setDomain(null);

		// bind the Session Manager to the login info
		sMgr.setIdentity(docbase, loginInfoObj);
		return sMgr;
	}

}
