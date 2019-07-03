package org.profile.service;

/**
 * The Interface <code>UserMailService</code> is contains all method declaration
 * related to Mail Services. It is used to obtain all the unread mails from
 * inbox of Gmail, YahooMail and Hotmail. Credential of these Mailing services
 * is necessary. So, username and password is saved in encrypted form. By using
 * POP3 and SMTP, inbox mails are obtained for respective credentials.
 *
 * @author pranav.arya
 */
public interface UserMailService {

    /**
     * This method is used to get no. of unread messages from INBOX for POP3
     * protocol. Gmail and Yahoo supports POP3 protocol. Email-id , password and
     * host name is required to obtain unread mails.
     *
     * @param emailId
     *            Contains the Email-Id
     * @param password
     *            Contains the password
     * @param host
     *            Contains the HOST name
     * @return the unread mails in number as a string
     */
    String getProviderPOP(String emailId, String password, String host);

    /**
     * This method is used to get no. of unread messages from INBOX for imaps
     * protocol. Hotmail supports imaps protocol. Email-id , password and host
     * name is required to obtain unread mails.
     *
     * @param emailId
     *            Contains the Email-Id
     * @param password
     *            Contains the password
     * @param host
     *            Contains the HOST name
     * @return the unread mails in number as a string
     */
    String getProviderIMAP(String emailId, String password, String host);

    /**
     * This method is used to obtain the Unread mail number by getting the
     * credentials of respective loggedIn user. This method calls the other
     * "getProviderPOP" and "getProviderIMAP" method for obtainin the result.
     *
     * @param provider
     *            Contains the providerId i.e. Gmail, Yahoo, Hotmail
     * @param userId
     *            the user id
     * @return an Array of providerId, userId and Email number
     */
    String[] getEmailNumber(String provider, String userId);

    /**
     * Sets the credential. This method is used to set the credential for
     * respective provider
     *
     * @param emailId
     *            Contains the Email-Id
     * @param password
     *            Contains the password
     * @param provider
     *            Contains the providerId i.e. Gmail, Yahoo, Hotmail
     * @param userId
     *            Contains the userId of loggedIn user
     * @return the response flag i.e. "done"
     */
    String setCredential(String emailId, String password, String provider,
            String userId);

    /**
     * Gets the authentication for imap protocol.
     *
     * @param emailId
     *            Contains the Email-Id
     * @param password
     *            Contains the password
     * @param host
     *            Contains the host name
     * @return the success or error flag as string
     */
    String getAuthenticationIMAP(String emailId, String password, String host);

    /**
     * Gets the authentication for POP3 protocol.
     *
     * @param userName
     *            Contains the Email-Id
     * @param password
     *            Contains the password
     * @param host
     *            Contains the host name
     * @return the success or error flag as string
     */
    String getAuthenticationPOP(String userName, String password, String host);
}
