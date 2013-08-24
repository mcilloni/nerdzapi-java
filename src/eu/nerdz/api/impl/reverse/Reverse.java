package eu.nerdz.api.impl.reverse;

import java.io.IOException;

import eu.nerdz.api.Application;
import eu.nerdz.api.Application.Features;
import eu.nerdz.api.HttpException;
import eu.nerdz.api.LoginException;
import eu.nerdz.api.Nerdz;
import eu.nerdz.api.UserInfo;
import eu.nerdz.api.WrongUserInfoTypeException;
import eu.nerdz.api.impl.reverse.messages.ReverseMessenger;
import eu.nerdz.api.messages.Messenger;

/**
 * Reverse Nerdz implementation.
 * This instance can be instantiated with eu.nerdz.api.Nerdz.getImplementation("reverse.Reverse");
 */
class Reverse extends Nerdz {

    public Reverse() {}

    @Override
    public UserInfo logAndGetInfo(String userName, String password) throws IOException, HttpException, LoginException {
        return new ReverseMessenger(userName, password).getUserInfo();
    }

    @Override
    public Application newApplication(UserInfo userInfo, Features... instanceFeatures) throws IOException, HttpException, LoginException, WrongUserInfoTypeException {
        if (instanceFeatures.length == 1 && instanceFeatures[0] == Features.MESSENGER) {
            Messenger instance = new ReverseMessenger(userInfo);
            instance.checkValidity();
            return instance;
        } else {
            throw new UnsupportedOperationException("Only MESSENGER is supported by Reverse at the moment. Sorry.");
        }
    }

    @Override
    public Application restoreApplication(UserInfo userInfo, Features... instanceFeatures) throws WrongUserInfoTypeException {
        if (instanceFeatures.length == 1 && instanceFeatures[0] == Features.MESSENGER) {
            return new ReverseMessenger(userInfo);
        } else {
            throw new UnsupportedOperationException("Only MESSENGER is supported by Reverse at the moment. Sorry.");
        }
    }

}