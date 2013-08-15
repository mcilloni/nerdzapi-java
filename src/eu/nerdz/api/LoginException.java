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

    (C) 2013 Marco Cilloni <marco.cilloni@yahoo.com>, AlexZ
*/

package eu.nerdz.api;

/**
 * Represents a wrong login.
 */
@SuppressWarnings("serial")
public class LoginException extends Exception {

    private static final String DEFAULT_MSG = "wrong combination of username and password during the login session";

    public LoginException(){
        this(LoginException.DEFAULT_MSG);
    }

    public LoginException(String errMsg){
        super(errMsg);
    }


}
