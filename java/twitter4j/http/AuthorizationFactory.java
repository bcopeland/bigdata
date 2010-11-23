/*
Copyright (c) 2007-2010, Yusuke Yamamoto
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name of the Yusuke Yamamoto nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY Yusuke Yamamoto ``AS IS'' AND ANY
EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL Yusuke Yamamoto BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package twitter4j.http;

import twitter4j.conf.Configuration;

/**
 * A static factory class for Authorization.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.1
 */
public final class AuthorizationFactory {
    public static Authorization getInstance(Configuration conf, boolean supportsOAuth) {
        Authorization auth = null;
        String consumerKey = conf.getOAuthConsumerKey();
        String consumerSecret = conf.getOAuthConsumerSecret();

        if (supportsOAuth && null != consumerKey && null != consumerSecret) {
            OAuthAuthorization oauth;
            oauth = new OAuthAuthorization(conf, consumerKey, consumerSecret);
            String accessToken = conf.getOAuthAccessToken();
            String accessTokenSecret = conf.getOAuthAccessTokenSecret();
            if (null != accessToken && null != accessTokenSecret) {
                oauth.setOAuthAccessToken(new AccessToken(accessToken, accessTokenSecret));
            }
            auth = oauth;
        } else {
            String screenName = conf.getUser();
            String password = conf.getPassword();
            if (null != screenName && null != password) {
                auth = new BasicAuthorization(screenName, password);
            }
        }
        if(null == auth){
            auth = NullAuthorization.getInstance();
        }
        return auth;
    }

    /**
     *
     * @param screenName screen name
     * @param password password
     * @return authorization instance
     * @deprecated The @twitterapi team will be shutting off basic authentication on the Twitter API. All applications, by this date, need to switch to using OAuth. <a href="http://dev.twitter.com/announcements">Read more »</a>
     */
    public static Authorization getBasicAuthorizationInstance(String screenName,
                                                              String password){
        return new BasicAuthorization(screenName, password);
    }
}
