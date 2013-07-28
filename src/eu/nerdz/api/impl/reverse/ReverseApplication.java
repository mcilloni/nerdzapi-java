/*
 This file is part of NerdzApi-java.

    NerdzApi-java is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    NerdzApi-java is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with NerdzApi-java.  If not, see <http://www.gnu.org/licenses/>.

    (C) 2013 Marco Cilloni <marco.cilloni@yahoo.com>
*/

package eu.nerdz.api.impl.reverse;

import eu.nerdz.api.LoginException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.nerdz.api.Application;
import eu.nerdz.api.HttpException;

/**
 * A Reverse abstract implementation of an Application.
 */
public abstract class  ReverseApplication implements Application {

    /**
     * Represents the domain in which all post/get requests are made.
     */
    protected static String NERDZ_DOMAIN_NAME = "http://beta.nerdz.eu";

    private String userName;
    private String password;

    /**
     * This is the DefaultHttpClient main instance. It will contain login informations, and all requests will pass through it.
     */
    protected DefaultHttpClient httpClient;

    /**
     * the token, required for login.
     */
    protected String token;

    /**the constructor takes care of logging into NERDZ. The cookies gathered through the login process remains in httpClient, allowing for logged in requsts.
     *
     * @param user username, unescaped
     * @param password password
     */
    protected ReverseApplication(String user, String password) throws IOException, HttpException, LoginException {

        this.userName = user;
        this.password = password;
        this.httpClient = new DefaultHttpClient();

        //fetch token.
        {
            String body = this.get();

            // token is hidden in an input tag. It's needed just for login/logout
            int start = body.indexOf("<input type=\"hidden\" value=\"") + 28;
            this.token = body.substring(start, start + 32);
        }

        Map<String,String> form = new HashMap<String, String>(4);
        form.put("setcookie", "on");
        form.put("username", user);
        form.put("password", password);
        form.put("tok", this.token);

        // login
        String responseBody = this.post("/pages/profile/login.json.php", form, null, true);

        //check for a wrong login.
        if( responseBody.contains("error") ) {
            throw new LoginException();
        }
    }

    /**
     * Returns the username.
     * @return a java.lang.String representing the username.
     */
    @Override
    public String getUsername() {
        return this.userName;
    }

    /**
     * Returns the NERDZ ID.
     * @return an int representing the user ID
     */
    @Override
    public int getUserID() {

       for(Cookie cookie : this.httpClient.getCookieStore().getCookies())
           if (cookie.getName().equals("nerdz_id"))
               return Integer.parseInt(cookie.getValue());

        return -1;
    }

    /**
     * Executes a GET request on NERDZ.
     * This version returns the content of NERDZ_DOMAIN_NAME.
     *
     * @return a String containing the contents of NERDZ_DOMAIN_NAME.
     * @throws IOException
     * @throws HttpException
     */
    protected String get() throws IOException, HttpException {
        return this.get("");
    }

    /**
     * Executes a GET request on NERDZ.
     * The given URL is automatically prepended with NERDZ_DOMAIN_NAME, so it should be something like /pages/pm/inbox.html.php.
     *
     * @param url an address beginning with /
     * @return the content of NERDZ_DOMAIN_NAME + url.
     * @throws IOException
     * @throws HttpException
     */
    protected String get(String url) throws IOException, HttpException {
        return this.get(url, false);
    }

    /**
     * Executes a GET request on NERDZ.
     * The given URL is automatically prepended with NERDZ_DOMAIN_NAME, so it should be something like /pages/pm/inbox.html.php.
     *
     * @param url an address beginning with /
     * @param consume if true, the entity associated with the response is consumed
     * @return the content of NERDZ_DOMAIN_NAME + url
     * @throws IOException
     * @throws HttpException
     */
    protected String get(String url, boolean consume) throws IOException, HttpException {

        HttpGet get = new HttpGet(ReverseApplication.NERDZ_DOMAIN_NAME + url);
        ResponseHandler<String> responseHandler = new BasicResponseHandler();

        HttpResponse response = this.httpClient.execute(get);
        StatusLine statusLine = response.getStatusLine();

        int code = statusLine.getStatusCode();
        if (code != 200 )
            throw new HttpException(code, statusLine.getReasonPhrase());

        String body = responseHandler.handleResponse(response);

        if (consume) {
            HttpEntity entity = response.getEntity();
            if (entity != null)
                EntityUtils.consume(entity);
        }

        return body.trim();
    }

    /**
     * Issues a POST request to NERDZ.
     * The given URL is automatically prepended with NERDZ_DOMAIN_NAME, so it should be something like /pages/pm/inbox.html.php.
     * form is urlencoded by post, so it should not be encoded before.
     *
     * @param url an address beginning with /
     * @param form a Map<String,String> that represents a form
     * @return a String containing the response body
     * @throws IOException
     * @throws HttpException
     */
    protected String post(String url, Map<String,String> form) throws IOException, HttpException {
        return this.post(url, form, null, false);
    }

    /**
     * Issues a POST request to NERDZ.
     * The given URL is automatically prepended with NERDZ_DOMAIN_NAME, so it should be something like /pages/pm/inbox.html.php.
     * form is urlencoded by post, so it should not be encoded before.
     *
     * @param url an address beginning with /
     * @param form a Map<String,String> that represents a form
     * @param referer if not null, this string is used as the referer in the response.
     * @return a String containing the response body
     * @throws IOException
     * @throws HttpException
     */
    protected String post(String url, Map<String,String> form, String referer) throws IOException, HttpException {
        return this.post(url, form, referer, false);
    }

    /**
     * Issues a POST request to NERDZ.
     * The given URL is automatically prepended with NERDZ_DOMAIN_NAME, so it should be something like /pages/pm/inbox.html.php.
     * form is urlencoded by post, so it should not be encoded before.
     *
     * @param url an address beginning with /
     * @param form a Map<String,String> that represents a form
     * @param referer if not null, this string is used as the referer in the response.
     * @param consume if true, the entity associated with the response is consumed
     * @return a String containing the response body
     * @throws IOException
     * @throws HttpException
     */
    protected String post(String url, Map<String,String> form, String referer, boolean consume) throws IOException, HttpException {

        HttpPost post = new HttpPost(ReverseApplication.NERDZ_DOMAIN_NAME + url);

        if (referer != null)
            post.addHeader("Referer", referer);

        List<NameValuePair> formEntries = new ArrayList<NameValuePair>(form.size());
        for (Map.Entry<String,String> entry : form.entrySet())
            formEntries.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        post.setEntity(new UrlEncodedFormEntity(formEntries, Charset.forName("UTF-8")));

        HttpResponse response = this.httpClient.execute(post);
        StatusLine statusLine = response.getStatusLine();

        int code = statusLine.getStatusCode();
        if (code != 200 )
            throw new HttpException(code, statusLine.getReasonPhrase());

        ResponseHandler<String> responseHandler = new BasicResponseHandler();

        String body = responseHandler.handleResponse(response);

        if (consume) {
            HttpEntity entity = response.getEntity();
            if (entity != null)
                EntityUtils.consume(entity);
        }


        return body.trim();
    }

}


