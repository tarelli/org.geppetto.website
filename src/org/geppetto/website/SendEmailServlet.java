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

import org.json.JSONObject;

@SuppressWarnings("serial")
public class SendEmailServlet extends HttpServlet {

	private static final String MAIL_TO_US = "  <img src=\"https://raw.github.com/openworm/org.geppetto.frontend/master/src/main/webapp/images/geppetto-logo@2x.png\" alt=\"Geppetto logo\"/>\n"
			+ "\n"
			+ "<h2 style=\"font-family:helvetica;\">Someone is interested in Geppetto!</h2>\n";

	private static final String MAIL_TO_USER = "  <img src=\"https://raw.github.com/openworm/org.geppetto.frontend/master/src/main/webapp/images/geppetto-logo@2x.png\" alt=\"Geppetto logo\"/>\n"
			+ "\n"
			+ "<h2 style=\"font-family:helvetica;\">Thanks for your interest in Geppetto!</h2>\n"
			+ "\n"
			+ "<p  style=\"font-family:helvetica;\">Geppetto, currently in his early access release cycle, is being actively developed by a growing open source community.\n"
			+ "We have taken note of your email in our database and we will use it with diligence only to update you on major progress regarding Geppetto, we will not share your email address with any third party.</p>\n"
			+ "\n"
			+ "<p style=\"font-family:helvetica;\">If you are interested in being up to date with daily progress and issues you can follow our <a href=\"https://github.com/openworm/OpenWorm/wiki/Geppetto---Repositories\">repositories</a> on GitHub or subscribe the high volume <a href=\"https://groups.google.com/forum/#!forum/openworm-discuss\">mailing list</a> of the OpenWorm project where Geppetto discussions take place.</p>\n"
			+ "\n"
			+ "<p style=\"font-family:helvetica;\">If you would like to be contacted for further information or commercial and non commercial enquiries related to Geppetto please send an email to <a href=\"mailto:info@geppetto.org\">info@geppetto.org</a>.</p>\n"
			+ "\n"
			+ "<p style=\"font-family:helvetica;\">Thanks again,</p>\n"
			+ "<p style=\"font-family:helvetica;\"><em>Geppetto team</em></p>";

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		// set response type
		resp.setContentType("application/json");

		try {
			// get data
			String postData = req.getParameter("jsondata");
			JSONObject jObj = new JSONObject(postData);
			String userMail = (String) jObj.get("mail");
			String type = (String) jObj.get("type");
			if (postData == null || postData == "") {
				throw new IllegalArgumentException(
						"bad request - need data in the request");
			}

			sendEmail(new InternetAddress("matteo@geppetto.org", "Geppetto"),
					new InternetAddress("info@geppetto.org"),
					"Someone is interested in Geppetto", MAIL_TO_US
							+ "<p style=\"font-family:helvetica;\">" + postData
							+ "</p>");
			if (type.equals("signup")) {
				sendEmail(
						new InternetAddress("matteo@geppetto.org", "Geppetto"),
						new InternetAddress(userMail),
						"Thanks for your interest in Geppetto!", MAIL_TO_USER);
			}

			JSONObject response=new JSONObject("{'status':'ok'}");
			resp.getWriter().println(response);

		} catch (Exception e) {
			resp.getWriter().println("{\"error\":\"" + e.getMessage() + "\"}");
		} finally {
			resp.getWriter().close();
		}
	}

	public static void sendEmail(InternetAddress from, InternetAddress to,
			String subj, String body) throws MessagingException {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(from);
			msg.addRecipient(Message.RecipientType.TO, to);
			msg.setSubject(subj);
			msg.setContent(body, "text/html; charset=utf-8");
			Transport.send(msg);
		} catch (AddressException e) {
			throw e;
		} catch (MessagingException e) {
			throw e;
		}
	}
}