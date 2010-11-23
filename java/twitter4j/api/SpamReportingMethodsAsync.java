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
package twitter4j.api;

import twitter4j.TwitterException;

/**
 * @author Joern Huxhorn - jhuxhorn at googlemail.com
 */
/**
 * Not yet supported.
 *
 * http://yusuke.homeip.net/jira/browse/TFJ-223
 */
public interface SpamReportingMethodsAsync {
    /**
     * The user specified in the id is blocked by the authenticated user and reported as a spammer.
     * <br>This method calls http://api.twitter.com/1/report_spam.json
     *
     * @param userId The ID of the user you want to report as a spammer.
     * @throws twitter4j.TwitterException when Twitter service or network is unavailable
     * @see <a href="http://dev.twitter.com/doc/post/report_spam">POST report_spam | dev.twitter.com</a>
     * @since Twitter4J 2.1.0
     */
    void reportSpam(int userId) throws TwitterException;

    /**
     * The user specified in the id is blocked by the authenticated user and reported as a spammer.
     * <br>This method calls http://api.twitter.com/1/report_spam.json
     *
     * @param screenName The screen name of the user you want to report as a spammer.
     * @throws TwitterException when Twitter service or network is unavailable
     * @see <a href="http://dev.twitter.com/doc/post/report_spam">POST report_spam | dev.twitter.com</a>
     * @since Twitter4J 2.1.0
     */
    void reportSpam(String screenName) throws TwitterException;
}
