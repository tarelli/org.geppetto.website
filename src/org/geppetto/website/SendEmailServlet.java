package org.geppetto.website;
import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.restlet.ext.json.JsonRepresentation;

import com.google.appengine.labs.repackaged.org.json.JSONObject;


@SuppressWarnings("serial")
public class SendEmailServlet extends HttpServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		// set response type
		resp.setContentType("application/json");

		try {
			// get data
			String postData =  req.getParameter("jsondata");
			
			if(postData == null || postData == "")
			{
				throw new IllegalArgumentException("bad request - need data in the request");
			}
						

			sendEmail(new InternetAddress("matteo@geppetto.org"),new InternetAddress("info@geppetto.org"),"Someone is interested in Geppetto", postData+"\n P.S. You guys rock!");
			
			resp.getWriter().println("OK");
			
		} catch (Exception e) {
			// TODO: do some logging
			resp.getWriter().println("{\"error\":\"" + e.getMessage() + "\"}");
		}
	}
	
	public static void sendEmail(InternetAddress from, InternetAddress to, String subj, String body) throws MessagingException
	{
		Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(from);
            msg.addRecipient(Message.RecipientType.TO, to);
            msg.setSubject(subj);
            msg.setText(body);
            Transport.send(msg);
        } catch (AddressException e) {
            throw e;
        } catch (MessagingException e) {
            throw e;
        }
	}
}