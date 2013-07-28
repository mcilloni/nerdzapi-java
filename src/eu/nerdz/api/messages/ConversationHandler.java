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

package eu.nerdz.api.messages;

import java.io.IOException;
import java.util.List;

import eu.nerdz.api.BadStatusException;
import eu.nerdz.api.ContentException;
import eu.nerdz.api.HttpException;
import eu.nerdz.api.messages.Conversation;
import eu.nerdz.api.messages.Message;

/**
 * This interface is meant to be created by a Messenger, and allows operations on Conversations, like fetching all existing conversations, getting messages from one and deleting them.
 */
public interface ConversationHandler {

    /**
     * Returns a List containing the current conversations list.
     * @return a List containing the current conversations list
     * @throws IOException
     * @throws HttpException
     * @throws ContentException
     */
    public List<Conversation> getConversations() throws IOException, HttpException, ContentException;

    /**
     * Returns a List containing the first <b>ten</b> messages from a given Conversation.
     * @param conversation a conversation previously created by getConversations()
     * @return a List containing the first <b>ten</b> messages from a given Conversation
     * @throws IOException
     * @throws HttpException
     * @throws ContentException
     */
    public List<Message> getMessagesFromConversation(Conversation conversation) throws IOException, HttpException, ContentException;

    /**
     * Returns a List containing howMany messages from a given Conversation starting from the message at the position start.
     * @param conversation a conversation previously created by getConversations()
     * @param start indicates that fetching of messages should begin from the message at this position.
     * @param howMany indicates how many messages should be fetched. Current implementation limits this number to 10; any larger number will be treated as 10.
     * @return a List containing howMany messages from a given Conversation starting from the message at the position start
     * @throws IOException
     * @throws HttpException
     * @throws ContentException
     */
    public List<Message> getMessagesFromConversation(Conversation conversation, int start, int howMany) throws IOException, HttpException, ContentException;

    /**
     * Deletes the given conversation. A consecutive call to getConversations() will not contain it anymore.
     * @param conversation
     * @throws IOException
     * @throws HttpException
     * @throws BadStatusException
     * @throws ContentException
     */
    public void deleteConversation(Conversation conversation) throws IOException, HttpException, BadStatusException, ContentException;

}