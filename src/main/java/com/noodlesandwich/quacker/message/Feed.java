package com.noodlesandwich.quacker.message;

import com.noodlesandwich.quacker.user.Profile;

public interface Feed {
    void follow(Profile profile);
}
