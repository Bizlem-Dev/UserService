package org.profile.service.impl;

import javax.jcr.Node;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.mail.*;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.jcr.api.SlingRepository;
import org.profile.service.CryptoService;
import org.profile.service.UserMailService;

/**
 * The Class <code>UserMailServiceImpl</code> is contains all method
 * implementation related to Mail Services. It is used to obtain all the unread
 * mails from inbox of Gmail, YahooMail and Hotmail. Credential of these Mailing
 * services is necessary. So, username and password is saved in encrypted form.
 * By using POP3 and SMTP, inbox mails are obtained for respective credentials.
 *
 * @author pranav.arya
 */
@Component(configurationFactory = true)
@Service(UserMailService.class)
@Properties({ @Property(name = "userMailService", value = "mailService") })
public class UserMailServiceImpl implements UserMailService {

    /** Object of Folder interface. */
    private Folder inbox;

    /** Object of SlingRepository interface. */
    @Reference
    private SlingRepository repos;

    /** Object of CryptoService interface for encryption and decryption. */
    @Reference
    private CryptoService crypto_Service;

    // Constructor of the calss.
    /*
     * (non-Javadoc)
     *
     * @see org.profile.service.UserMailService#getProviderPOP(java.lang.String,
     * java.lang.String, java.lang.String)
     */
    public String getProviderPOP(String emailId, String password, String host) {
        String result = "";
        java.util.Properties props = System.getProperties();
        props.setProperty("mail.store.protocol", "pop3s");
        try {
            javax.mail.Session session = javax.mail.Session.getDefaultInstance(
                    props, null);
            Store store = session.getStore("pop3s");
            store.connect(host, emailId, password);

            Folder fldr = store.getFolder("INBOX");
            fldr.open(Folder.HOLDS_MESSAGES);
            result = fldr.getUnreadMessageCount() + "";
            fldr.close(true);
            store.close();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.profile.service.UserMailService#getProviderIMAP(java.lang.String,
     * java.lang.String, java.lang.String)
     */
    public String getProviderIMAP(String emailId, String password, String host) {
        String result = "";
        java.util.Properties props = System.getProperties();
        props.setProperty("mail.store.protocol", "imaps");
        try {
            javax.mail.Session session = javax.mail.Session.getDefaultInstance(
                    props, null);
            Store store = session.getStore("imaps");
            store.connect(host, emailId, password);

            inbox = store.getFolder("Inbox");
            result = inbox.getUnreadMessageCount() + "";
            store.close();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.profile.service.UserMailService#getAuthenticationIMAP
     * (java.lang.String, java.lang.String, java.lang.String)
     */
    public String getAuthenticationIMAP(String emailId, String password,
            String host) {
        java.util.Properties props = System.getProperties();
        props.setProperty("mail.store.protocol", "imaps");
        try {
            javax.mail.Session session = javax.mail.Session.getDefaultInstance(
                    props, null);
            Store store = session.getStore("imaps");
            store.connect(host, emailId, password);
            store.close();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
            return "error";
        } catch (MessagingException e) {
            e.printStackTrace();
            return "error";
        }

        return "done";
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.profile.service.UserMailService#getAuthenticationPOP(java.lang.String
     * , java.lang.String, java.lang.String)
     */
    public String getAuthenticationPOP(String userName, String password,
            String host) {

        java.util.Properties props = System.getProperties();
        props.setProperty("mail.store.protocol", "pop3s");

        try {
            javax.mail.Session session = javax.mail.Session.getDefaultInstance(
                    props, null);
            Store store = session.getStore("pop3s");
            store.connect(host, userName, password);

            store.close();

        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }

        return "done";
    }

    /*
     * (non-Javadoc)
     *
     * @see org.profile.service.UserMailService#setCredential(java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String)
     */
    public String setCredential(String emailId, String password,
            String provider, String userId) {

        Node userNode = null, networkNode, providerNode;
        Session session;

        try {

            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));
            if (session.getNode("/content/user").hasNode(userId)) {
                userNode = session.getNode("/content/user/" + userId);
            }
            if (userNode.hasNode("NetworkProvider")) {
                networkNode = userNode.getNode("NetworkProvider");
            } else {
                networkNode = userNode.addNode("NetworkProvider");
            }
            if (networkNode.hasNode(provider)) {
                providerNode = networkNode.getNode(provider);
            } else {
                providerNode = networkNode.addNode(provider);
            }
            providerNode.setProperty("emailId", emailId);
            providerNode.setProperty("password",
                    crypto_Service.encrypt(password));
            providerNode.setProperty("providerId", provider);
            session.save();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "done";
    }

    /*
     * (non-Javadoc)
     *
     * @see org.profile.service.UserMailService#getEmailNumber(java.lang.String,
     * java.lang.String)
     */
    public String[] getEmailNumber(String provider, String userId) {
        Node userNode = null, networkNode, providerNode;
        Session session;
        String[] response = { "", provider, userId };
        try {

            session = repos.login(new SimpleCredentials("admin", "admin"
                    .toCharArray()));
            if (session.getNode("/content/user").hasNode(userId)) {
                userNode = session.getNode("/content/user/" + userId);
            }
            if (userNode.hasNode("NetworkProvider")) {
                networkNode = userNode.getNode("NetworkProvider");
            } else {
                networkNode = userNode.addNode("NetworkProvider");
            }
            if (networkNode.hasNode(provider)) {
                providerNode = networkNode.getNode(provider);
            } else {
                response[0] = "Not Configured";
                return response;
            }
            if (provider.equals("Gmail")) {
                response[0] = getProviderIMAP(
                        providerNode.getProperty("emailId").getString(),
                        crypto_Service.decrypt(providerNode.getProperty(
                                "password").getString()), "imap.gmail.com");
            } else if (provider.equals("Yahoo")) {
                response[0] = getProviderIMAP(
                        providerNode.getProperty("emailId").getString(),
                        crypto_Service.decrypt(providerNode.getProperty(
                                "password").getString()),
                                "imap.mail.yahoo.com");
            } else if (provider.equals("Hotmail")) {
                response[0] = getProviderPOP(providerNode
                        .getProperty("emailId").getString(),
                        crypto_Service.decrypt(providerNode.getProperty(
                                "password").getString()), "pop3.live.com");
            }

            session.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}