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

package eu.nerdz.api;

import java.io.Serializable;
import java.util.Date;


/**
 * A ContentEntry represents an object which have content and can be chronologically determined.
 *
 * @param <E>
 */
public interface ContentEntry<E> extends Serializable {

    /**
     * Returns the content associated with this entry.
     *
     * @return the content associated with this entry
     */
    public abstract E getContent();

    /**
     * Return a precise instant associated with this content.
     *
     * @return a date
     */
    public abstract Date getDate();
}
