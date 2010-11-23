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

/**
 * @author Joern Huxhorn - jhuxhorn at googlemail.com
 */
public interface BlockMethodsAsync {
	/**
	 * Blocks the user specified in the ID parameter as the authenticating user.  Returns the blocked user in the requested format when successful.
	 * <br>This method calls http://api.twitter.com/1/blocks/create%C2%A0
	 *
	 * @param screenName the screen_name of the user to block
	 * @since Twitter4J 2.0.1
     * @see <a href="http://dev.twitter.com/doc/post/blocks/create">POST blocks/create | dev.twitter.com</a>
	 */
	void createBlock(String screenName);

	/**
	 * Blocks the user specified in the ID parameter as the authenticating user.  Returns the blocked user in the requested format when successful.
	 * <br>This method calls http://api.twitter.com/1/blocks/create%C2%A0
	 *
	 * @param userId the screen_name of the user to block
	 * @since Twitter4J 2.1.0
     * @see <a href="http://dev.twitter.com/doc/post/blocks/create">POST blocks/create | dev.twitter.com</a>
	 */
	void createBlock(int userId);

	/**
	 * Un-blocks the user specified in the ID parameter as the authenticating user.  Returns the un-blocked user in the requested format when successful.
	 * <br>This method calls http://api.twitter.com/1/blocks/create%C2%A0
	 *
	 * @param screenName the screen_name of the user to block
	 * @since Twitter4J 2.0.1
     * @see <a href="http://dev.twitter.com/doc/post/blocks/destroy">POST blocks/destroy | dev.twitter.com</a>
	 */
	void destroyBlock(String screenName);

	/**
	 * Un-blocks the user specified in the ID parameter as the authenticating user.  Returns the un-blocked user in the requested format when successful.
	 * <br>This method calls http://api.twitter.com/1/blocks/create%C2%A0
	 *
	 * @param userId the ID of the user to block
	 * @since Twitter4J 2.1.0
     * @see <a href="http://dev.twitter.com/doc/post/blocks/destroy">POST blocks/destroy | dev.twitter.com</a>
	 */
	void destroyBlock(int userId);

	/**
     * Returns if the authenticating user is blocking a target user. Will return the blocked user's object if a block exists, and error with a HTTP 404 response code otherwise.
	 * <br>This method calls http://api.twitter.com/1/blocks/exists/id.xml
	 *
	 * @param screenName The screen_name of the potentially blocked user.
	 * @since Twitter4J 2.0.4
     * @see <a href="http://dev.twitter.com/doc/get/blocks/exists">GET blocks/exists | dev.twitter.com</a>
	 */
	void existsBlock(String screenName);

	/**
     * Returns if the authenticating user is blocking a target user. Will return the blocked user's object if a block exists, and error with a HTTP 404 response code otherwise.
	 * <br>This method calls http://api.twitter.com/1/blocks/exists/id.xml
	 *
	 * @param userId The ID of the potentially blocked user.
	 * @since Twitter4J 2.1.0
     * @see <a href="http://dev.twitter.com/doc/get/blocks/exists">GET blocks/exists | dev.twitter.com</a>
	 */
	void existsBlock(int userId);

	/**
	 * Returns a list of user objects that the authenticating user is blocking.
	 * <br>This method calls http://api.twitter.com/1/blocks/blocking.xml
	 *
	 * @since Twitter4J 2.0.4
     * @see <a href="http://dev.twitter.com/doc/get/blocks/blocking">GET blocks/blocking | dev.twitter.com</a>
	 */
	void getBlockingUsers();

	/**
	 * Returns a list of user objects that the authenticating user is blocking.
	 * <br>This method calls http://api.twitter.com/1/blocks/blocking.xml
	 *
	 * @param page the number of page
	 * @since Twitter4J 2.0.4
     * @see <a href="http://dev.twitter.com/doc/get/blocks/blocking">GET blocks/blocking | dev.twitter.com</a>
	 */
	void getBlockingUsers(int page);

	/**
	 * Returns an array of numeric user ids the authenticating user is blocking.
	 * <br>This method calls http://api.twitter.com/1/blocks/blocking/ids
	 * @since Twitter4J 2.0.4
     * @see <a href="http://dev.twitter.com/doc/get/blocks/blocking/ids">GET blocks/blocking/ids | dev.twitter.com</a>
	 */
	void getBlockingUsersIDs();
}
