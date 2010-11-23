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
public interface FavoriteMethodsAsync {
	/**
	 * Returns the 20 most recent favorite statuses for the authenticating user or user specified by the ID parameter in the requested format.
	 * <br>This method calls http://api.twitter.com/1/favorites
     * @see <a href="http://dev.twitter.com/doc/get/favorites">GET favorites | dev.twitter.com</a>
	 * @since Twitter4J 2.0.1
	 */
	void getFavorites();

	/**
	 * Returns the 20 most recent favorite statuses for the authenticating user or user specified by the ID parameter in the requested format.
	 * <br>This method calls http://api.twitter.com/1/favorites
	 * @param page number of page to retrieve favorites
     * @see <a href="http://dev.twitter.com/doc/get/favorites">GET favorites | dev.twitter.com</a>
	 * @since Twitter4J 2.0.1
	 */
	void getFavorites(int page);

	/**
	 * Returns the 20 most recent favorite statuses for the authenticating user or user specified by the ID parameter in the requested format.
	 * <br>This method calls http://api.twitter.com/1/favorites
	 * @param id the ID or screen name of the user for whom to request a list of favorite statuses
     * @see <a href="http://dev.twitter.com/doc/get/favorites">GET favorites | dev.twitter.com</a>
	 * @since Twitter4J 2.0.1
	 */
	void getFavorites(String id);

	/**
	 * Returns the 20 most recent favorite statuses for the authenticating user or user specified by the ID parameter in the requested format.
	 * <br>This method calls http://api.twitter.com/1/favorites
	 * @param id the ID or screen name of the user for whom to request a list of favorite statuses.
	 * @param page retrieves the 20 next most recent favorite statuses.
     * @see <a href="http://dev.twitter.com/doc/get/favorites">GET favorites | dev.twitter.com</a>
	 * @since Twitter4J 2.0.1
	 */
	void getFavorites(String id,int page);

	/**
	 * Favorites the status specified in the ID parameter as the authenticating user.  Returns the favorite status when successful.
	 * <br>This method calls http://api.twitter.com/1/favorites/create%C2%A0
	 *
	 * @param id       the ID or screen name of the user for whom to request a list of favorite statuses.
	 * @since 1.1.2
     * @see <a href="http://dev.twitter.com/doc/post/favorites/:id/create">POST favorites/:id/create | dev.twitter.com</a>
	 */
	void createFavorite(long id);

	/**
	 * Favorites the status specified in the ID parameter as the authenticating user.  Returns the favorite status when successful.
	 * <br>This method calls http://api.twitter.com/1/favorites/destroy
	 *
	 * @param id       the ID or screen name of the user for whom to request a list of un-favorite statuses.
	 * @since 1.1.2
     * @see <a href="http://dev.twitter.com/doc/post/favorites/destroy">POST favorites/destroy | dev.twitter.com</a>
	 */
	void destroyFavorite(long id);
}
